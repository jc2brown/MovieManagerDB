package ca.jc2brown.mmdb.model.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

public class MethodInvokation<T extends MappedEntity> {
	private static Logger log = Logger.getLogger( MethodInvokation.class.getName() );
	
	private Class<?> clazz;
	private Field field;
	private Method fieldToString;
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
			log.debug("self-acting method, target=" + targetClass.getCanonicalName());
		} else {
			targetClass = field.getType();
			log.debug("non-self method, target=" + targetClass.getCanonicalName());
		}
		
		try {
			this.fieldToString = targetClass.getDeclaredMethod(toStringName);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public <U> String invoke(T mappedEntity) {
		U object = null;
		String str = null;
		try {
			object = (U) field.get(mappedEntity);
			if ( object == null ) {
				log.error("Could not retrieve value from field");
				return null;
			}
    		if ( ! self ) {
    			log.debug("invoking " + fieldToString.getName() + " on other object " + object.getClass().getCanonicalName());
    			str = (String) fieldToString.invoke(object);
    		} else {
    			log.debug("invoking " + fieldToString.getName() + " on self " + mappedEntity.getClass().getCanonicalName() + " cast to " + clazz.getCanonicalName());
    			str = (String) fieldToString.invoke(clazz.cast(mappedEntity));
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
	
	
	public String toString() {
		String preamble = "MethodInvokation for " + clazz.getSimpleName() + "." + field.getName() + ": ";
		if ( fieldToString.getName().equals("toString") ) {
			return preamble + field.getName() + "." + fieldToString.getName() + "()";
		} else {
			return preamble + field.getDeclaringClass().getSimpleName() + "." + fieldToString.getName() + "(" + field.getName() + ")";
		}
	}
}