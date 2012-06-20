package ca.jc2brown.framework.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

public class MethodInvokation<T extends MappedEntity> {
	private static Logger log = Logger.getLogger( MethodInvokation.class.getName() );

	// Display full class names in log output
	public static boolean logFullyQualified = false;
	
	// Display full class names in log output
	public static boolean debugFullyQualified = true;
	
	private Class<?> clazz;
	private Field field;
	private Method method;
	private boolean self;
	
	public MethodInvokation(Class<?> clazz, Field field, String toStringName) {
		this.clazz = clazz;
		this.field = field;
		
		// toString comes from the class of the field type.
		// Others come from the class of the Entity
		Class<?> targetClass = null;
		self = ! toStringName.equals("toString");
		if ( self ) {
			targetClass = this.clazz;
			log.debug("Entity method, targetClass=" + getDebugClassName(targetClass));
		} else {
			targetClass = field.getType();
			log.debug("Field method, targetClass=" + getDebugClassName(targetClass));
		}
		
		// Check the entire hierarchy for the specified method
		while ( targetClass != null ) {
			try {
				this.method = targetClass.getDeclaredMethod(toStringName);
				return;
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				targetClass = targetClass.getSuperclass();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <U> String invoke(T mappedEntity) {
		U object = null;
		String str = null;
		try {
			object = (U) field.get(mappedEntity);
			if ( object == null ) {
				return null;
			}
    		if ( ! self ) {
    			log.debug("invoking " + method.getName() + " on other object " + getDebugClassName(object.getClass()));
    			str = (String) method.invoke(object);
    		} else {
    			log.debug("invoking " + method.getName() + " on self " + getDebugClassName(mappedEntity.getClass()) + " cast to " + getDebugClassName(clazz));
    			str = (String) method.invoke(clazz.cast(mappedEntity));
    		}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	private static String getLogClassName(Class<?> clazz) {
		if ( logFullyQualified ) {
			return clazz.getName();
		} 
		return clazz.getSimpleName();
	}
	
	private static String getDebugClassName(Class<?> clazz) {
		if ( debugFullyQualified ) {
			return clazz.getName();
		} 
		return clazz.getSimpleName();
	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{MethodInvokation ");
		if ( self ) {
			sb.append( getLogClassName(field.getDeclaringClass()) );			
		} else {
			sb.append( getLogClassName(field.getType()) );
		}
		sb.append(".");
		sb.append(method.getName());
		sb.append("() acts on ");
		sb.append( getLogClassName(clazz) );
		sb.append(".");
		sb.append(field.getName());
		sb.append("}");
		return sb.toString();
	}
}