package ca.jc2brown.generic.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;


public abstract class ModelEntity {
	
	private static Logger log = Logger.getLogger( ModelEntity.class.getName() );
	
	private static Map<Class<?>,Map<String, MethodInvokation<? extends ModelEntity>>> toStringClassMap = new HashMap<Class<?>,Map<String, MethodInvokation<? extends ModelEntity>>>();
	
	private static Map<Class<?>,Boolean> toStringCalledMap = new HashMap<Class<?>,Boolean>();
	
	
	@SuppressWarnings("unchecked")
	public <T extends ModelEntity> Map<String,String> toMap() {
		log.debug(getClass().getName() + ".toMap()");
		Map<String,String> map = new HashMap<String,String>();
		Map<String, MethodInvokation<? extends ModelEntity>> toStringFieldMap = toStringClassMap.get(getClass());
		for ( Entry<String, MethodInvokation<? extends ModelEntity>> entry : toStringFieldMap.entrySet() ) {
			String fieldName = entry.getKey();
			MethodInvokation<T> methodInvokation = (MethodInvokation<T>) entry.getValue();
			String str = methodInvokation.invoke((T)this);
			log.debug(fieldName + ":" + methodInvokation + " produced " + str);
			map.put( fieldName, str);
		}
		log.debug(getClass().getName() + ".toMap() produced " + map);
		return map;
	}
	
	public String toString() {
		log.debug(getClass().getName() + ".toString()");
		StringBuffer sb = new StringBuffer();
		sb.append(getClass().getName() + '@' + Integer.toHexString(hashCode()));
		sb.append("\n  ");
		sb.append(toLine());
		log.debug(getClass().getName() + ".toString() produced " + sb.toString());
		return sb.toString();
	}
	
	public String toLine() {
		return "{" + mapToString(toMap()) + "}";
	}
	
	public static String mapToString( Map<String,String> map) {
		StringBuffer sb = new StringBuffer();
		for ( Entry<String,String> entry : map.entrySet() ) {
			sb.append(entry.getKey());
			sb.append("=");
			sb.append("\"");
			sb.append(entry.getValue());
			sb.append("\" ");
		}
		return sb.toString();
	}

	public static Map<String,MethodInvokation<? extends ModelEntity>> getMap(Class<?> clazz) {
		return toStringClassMap.get(clazz);
	}
	
	protected static <T> String toCollectionString(Collection<T> c) {
		return toCommaList(c);
	}
	
	protected static <T extends ModelEntity> String toEntityCollectionString(Collection<T> c) {
		return toCommaList(c);
	}	
	
	private static <T> String toShortString(T item) {
		String rep = null;
		if ( item instanceof ModelEntity ) {
			rep = ((ModelEntity)item).toMap().get("representative");
		}
		if ( rep == null ) {
			rep = item.toString();
		}
		return rep;
	}
	
	
	private static <T> String toCommaList(Collection<T> items) {
		StringBuffer sb = new StringBuffer();
		for ( T item : items ) {
			if ( sb.length() > 0 ) {
				sb.append(", ");
			}
			sb.append( toShortString(item) );
		}
		if ( sb.length() == 0 ) {
			sb.append("{}");
		}
		return sb.toString();
	}
	
	
	protected boolean isCalled() {
		return toStringCalledMap.get(getClass());
	}
	protected boolean setCalled(boolean value) {
		return toStringCalledMap.put(getClass(), value);
	}
	
	protected static <E> Set<E> add(Set<E> collection, E object) {
		return (Set<E>)add((Collection<E>)collection, object, HashSet.class);
	}
	
	protected static <E> List<E> add(List<E> collection, E object) {
		return (List<E>)add((Collection<E>)collection, object, ArrayList.class);
	}
	
	@SuppressWarnings("unchecked")
	private static <E> Collection<E> add(Collection<E> collection, E object, Class<?> clazz) {
		if ( collection == null ) {
			try {
				collection = (Collection<E>) clazz.newInstance();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		((Collection<E>)collection).add(object);
		return collection;
	}	

}