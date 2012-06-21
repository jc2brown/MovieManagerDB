package ca.jc2brown.generic.model;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.apache.log4j.Logger;

public class MethodInvokation<T extends ModelEntity> {
	private static Logger log = Logger.getLogger( MethodInvokation.class.getName() );
	
	// String token indicators for tailored method calls
	protected static final String SELF = "$SELF$";
	protected static final String ARGS = "$ARGS$";
	protected static final String COL = "$COLL$";

	// Display full class names in log output
	public static boolean logFullyQualified = false;
	
	// Display full class names in log output
	public static boolean debugFullyQualified = true;

	
	private Class<?> clazz;
	private Field field;
	private Method method;
	private boolean self;
	private boolean args; 
	private boolean collection; 
	
	public MethodInvokation(Class<?> clazz, Field field, String toStringName) throws NoSuchMethodException {
		this.clazz = clazz;
		this.field = field;
		
    	boolean wasAccessible = field.isAccessible();
    	field.setAccessible(true);

		// Determine whether to act on the entity or the field value
		self = ! toStringName.contains(SELF);
		toStringName = toStringName.replace(SELF, "");
		args = toStringName.contains(ARGS);
		toStringName = toStringName.replace(ARGS, "");
		collection = toStringName.contains(COL);
		toStringName = toStringName.replace(COL, "");
		Class<?> targetClass = null;
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
				if ( args ) {
					if ( collection ) {
						log.debug("Scanning for method " + getLogClassName(targetClass) + "." + toStringName + "(Collection)" );
						this.method = targetClass.getDeclaredMethod(toStringName, Collection.class);
					} else {
						log.debug("Scanning for method " + getLogClassName(targetClass) + "." + toStringName + "(Object)" );
						this.method = targetClass.getDeclaredMethod(toStringName, Object.class);
					}
				} else {
					log.debug("Scanning for method " + getLogClassName(targetClass) + "." + toStringName + "()" );
					this.method = targetClass.getDeclaredMethod(toStringName);
				}
		    	field.setAccessible(wasAccessible);
				return;
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// Hack: using NoSuchMethodException as control flow to climb the object hierarchy
				// Easier than searching targetClass.getDeclaredMethods each time
				targetClass = targetClass.getSuperclass();
				
			}
		}
		log.fatal("No method found for " + clazz.getName() + "." + toStringName + "()");
    	field.setAccessible(wasAccessible);
    	throw new NoSuchMethodException();
	}
	
	@SuppressWarnings("unchecked")
	public <U> String invoke(T mappedEntity) {
		U object = null;
		String str = null;
		
		if ( mappedEntity.isCalled() ) {
			return "";
		}
		mappedEntity.setCalled(true);
		
    	boolean wasFieldAccessible = field.isAccessible();
    	field.setAccessible(true);
    	
		try {
			object = (U) field.get(mappedEntity);
			if ( object == null ) {
		    	field.setAccessible(wasFieldAccessible);
				mappedEntity.setCalled(false);
				return "null";
			}
	    	boolean wasMethodAccessible = method.isAccessible();
	    	method.setAccessible(true);
    		if ( ! self ) {
    			log.debug("invoking " + method.getName() + " on other object " + getDebugClassName(object.getClass()));
    			str = (String) method.invoke(object);
    		} else {
    			if ( args ) {
	    			log.debug("invoking " + method.getName() + " on self " + getDebugClassName(mappedEntity.getClass()) + " cast to " + getDebugClassName(clazz));
	    			method.setAccessible(true);
	    			str = (String) method.invoke(clazz.cast(mappedEntity), field.get(mappedEntity));
    			} else {
        			log.debug("invoking " + method.getName() + " on self " + getDebugClassName(mappedEntity.getClass()) + " cast to " + getDebugClassName(clazz));
        			str = (String) method.invoke(clazz.cast(mappedEntity));
    			}
    		}
    		method.setAccessible(wasMethodAccessible);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
    	field.setAccessible(wasFieldAccessible);
		if ( str == null ) {
			str = "null";
		}
		mappedEntity.setCalled(false);
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