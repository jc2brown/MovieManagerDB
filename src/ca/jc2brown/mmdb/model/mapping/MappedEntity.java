package ca.jc2brown.mmdb.model.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import ca.jc2brown.mmdb.utils.Utils;

@MappedClass
public abstract class MappedEntity {
	
	private static Logger log = Logger.getLogger( MappedEntity.class.getName() );
	
	private static Map<Class<?>,Map<String, MethodInvokation<? extends MappedEntity>>> toStringClassMap = new HashMap<Class<?>,Map<String, MethodInvokation<? extends MappedEntity>>>();

	@SuppressWarnings("unchecked")
	public <T extends MappedEntity> Map<String,String> toMap() {
		log.debug(getClass().getName() + ".toMap()");
		Map<String,String> map = new HashMap<String,String>();
		Map<String, MethodInvokation<? extends MappedEntity>> toStringFieldMap = toStringClassMap.get(getClass());
		for ( Entry<String, MethodInvokation<? extends MappedEntity>> entry : toStringFieldMap.entrySet() ) {
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
		return "{" + Utils.mapToString(toMap()) + "}";
	}

	/* 
	// Probably best to restrict access as much as possible
	protected static void setToStringClassMap(Map<Class<?>, Map<String, MethodInvokation<? extends MappedEntity>>> toStringClassMap) {
		MappedEntity.toStringClassMap = toStringClassMap;
	}
	*/
}