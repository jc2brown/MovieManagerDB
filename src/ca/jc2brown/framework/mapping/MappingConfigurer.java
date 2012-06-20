package ca.jc2brown.framework.mapping;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import ca.jc2brown.framework.mapping.MappedEntity;
import ca.jc2brown.mmdb.MediaManagerDB;

// This class has a single public method, configure, which scans the given package for subclasses of MappedEntity.
// 
// The parse method scans each candidate class for fields with @MappedField, and adds an entry in the map for each.
// Each map entry consists of the field's name and a MethodInvokation object which can be used to generate a string value of that field
// Each map is added to a master map that associates each field:method map with the class containing those fields
//
// The result is a mechanism that facilitates automatic - albeit inflexible - toString and toMap generation.

public class MappingConfigurer {
	
	private static Logger log = Logger.getLogger( MappingConfigurer.class.getName() );

	
	// Scan the package, parse the classes and set the class map on MappedEntity.
	public static void configure(String basePackage) {
		log.debug("Configuring package " + basePackage);
		Map<Class<?>,Map<String, MethodInvokation<? extends MappedEntity>>> toStringClassMap = new HashMap<Class<?>,Map<String, MethodInvokation<? extends MappedEntity>>>();
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AssignableTypeFilter(MappedEntity.class));
		
		// This is pretty essential stuff. We won't try to recover from any problems here.
		try {
			// Scan each subclass of MappedEntity
			for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
				String className = bd.getBeanClassName();
				log.debug("Configuring class " + className);
				Class<?> clazz = Class.forName(className);
				Map<String,MethodInvokation<? extends MappedEntity>> toStringFieldMap = parse(clazz);
				log.debug("Parsed field map " + toStringFieldMap);
				toStringClassMap.put(clazz, toStringFieldMap);
			}			
			// We don't want to expose the map at all to subclasses of MappedEntity, so set
			// the private map with reflection instead of a regular accessor
			Field toStringMapField = MappedEntity.class.getDeclaredField("toStringClassMap");
			toStringMapField.setAccessible(true);
			toStringMapField.set(null, toStringClassMap);
			log.debug("Class map generated for the following keys:\n" + toStringClassMap.keySet());
			
		} catch (IllegalArgumentException e) {
			bail(e);
		} catch (SecurityException e) {
			bail(e);
		} catch (IllegalAccessException e) {
			bail(e);
		} catch (NoSuchFieldException e) {
			bail(e);
		} catch (ClassNotFoundException e) {
			bail(e);
		}
	}
	
	// Scans a class for @MappedField-annotated fields and produces a map of fieldName:toString_method entries
	private static <T extends MappedEntity> Map<String, MethodInvokation<? extends MappedEntity>> parse(Class<?> clazz) {
    	Map<String, MethodInvokation<? extends MappedEntity>> toStringFieldMap = new HashMap<String, MethodInvokation<? extends MappedEntity>>();
    	// Are we at the top of the object hieracrchy?
    	if ( clazz.equals(MappedEntity.class) ) {
    		return toStringFieldMap;
    	}
    	// Scan all fields: public, protected, and private
    	log.debug("Parsing " + clazz.getCanonicalName());
        Field[] fields = clazz.getDeclaredFields();
	    for (Field field : fields ) {
	    	String fieldName = field.getName();
	    	field.setAccessible(true);
	    	MappedField a = field.getAnnotation(MappedField.class);
	    	if ( a != null ) {
	            String toStringMethodName = a.value();
	            log.debug("Found MappedField " + fieldName + " using method " + toStringMethodName);
	            MethodInvokation<T> methodInvokation = new MethodInvokation<T>(clazz, field, toStringMethodName);
	            toStringFieldMap.put(field.getName(), methodInvokation);
	        } else {
	        	log.debug("Ignoring field " + fieldName);
	        }
	    }
	    // Recursive scan toward java.lang.Object (and then null)
	    toStringFieldMap.putAll( parse(clazz.getSuperclass()) );
	    
	    // Results for this class
	    return toStringFieldMap;
    }
	

	
		
	private static void bail(Exception e) {
		log.fatal(e);
		MediaManagerDB.bail(e.getMessage());
	}
	
}