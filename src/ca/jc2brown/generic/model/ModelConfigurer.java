package ca.jc2brown.generic.model;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import ca.jc2brown.generic.model.ModelEntity;

// This class has a single public method, configure, which scans the given package for subclasses of ModelEntity.
// 
// The parse method scans each candidate class for fields with @ModelField, and adds an entry in the map for each.
// Each map entry consists of the field's name and a MethodInvokation object which can be used to generate a string value of that field
// Each map is added to a master map that associates each field:method map with the class containing those fields
//
// The result is a mechanism that facilitates automatic - albeit inflexible - toString and toMap generation.

// Note that the the call to configure will *temporarily* change private fields to public as it scans.

public class ModelConfigurer {
	
	private static Logger log = Logger.getLogger( ModelConfigurer.class.getName() );

	
	public static Set<Class<? extends ModelEntity>> ModelEntityes; 
	
	// Scan the package, parse the classes and set the class map on ModelEntity.
	@SuppressWarnings("unchecked")
	public static void configure(String basePackage) throws NoSuchMethodException, 
															ClassNotFoundException, 
															SecurityException, 
															NoSuchFieldException,
															IllegalArgumentException, 
															IllegalAccessException {
		log.debug("Configuring package " + basePackage);
		Map<Class<? extends ModelEntity>,Map<String, MethodInvokation<? extends ModelEntity>>> toStringClassMap = new HashMap<Class<? extends ModelEntity>,Map<String, MethodInvokation<? extends ModelEntity>>>();
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AssignableTypeFilter(ModelEntity.class));
		
		// Scan each subclass of ModelEntity
		for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
			String className = bd.getBeanClassName();
			log.debug("Configuring class " + className);
			Class<? extends ModelEntity> clazz = (Class<? extends ModelEntity>) Class.forName(className);
			Map<String,MethodInvokation<? extends ModelEntity>> toStringFieldMap = parse(clazz);
			//log.debug("Parsed field map " + toStringFieldMap);
			toStringClassMap.put(clazz, toStringFieldMap);
		}
		ModelEntityes = toStringClassMap.keySet();
		
		// We don't want to expose the map at all to subclasses of ModelEntity, so set
		// the private map with reflection instead of a regular accessor
		Field toStringClassMapField = ModelEntity.class.getDeclaredField("toStringClassMap");
		toStringClassMapField.setAccessible(true);
		toStringClassMapField.set(null, toStringClassMap);
		toStringClassMapField.setAccessible(false);
		
		Map<Class<? extends ModelEntity>,Boolean> toStringCalledMap = new HashMap<Class<? extends ModelEntity>,Boolean>(ModelEntityes.size());
		for ( Class<? extends ModelEntity> clazz : ModelEntityes ) {
			toStringCalledMap.put(clazz, false);
		}
		Field toStringCalledMapField = ModelEntity.class.getDeclaredField("toStringCalledMap");
		toStringCalledMapField.setAccessible(true);
		toStringCalledMapField.set(null, toStringCalledMap);
		toStringCalledMapField.setAccessible(false);
		
		log.debug("Class map generated for the following keys:\n" + ModelEntityes);
		return;
	}
	
	// Scans a class for @ModelField-annotated fields and produces a map of fieldName:toString_method entries
	@SuppressWarnings("unchecked")
	private static <T extends ModelEntity> Map<String, MethodInvokation<? extends ModelEntity>> parse(Class<?> clazz) throws NoSuchMethodException {
    	Map<String, MethodInvokation<? extends ModelEntity>> toStringFieldMap = new HashMap<String, MethodInvokation<? extends ModelEntity>>();
    	// Are we at the top of the object hieracrchy?
    	if ( clazz.equals(ModelEntity.class) ) {
    		return toStringFieldMap;
    	}
    	// Scan all fields: public, protected, and private
    	log.debug("Parsing " + clazz.getCanonicalName());
        Field[] fields = clazz.getDeclaredFields();
	    for (Field field : fields ) {
	    	String fieldName = field.getName();
	    	boolean wasAccessible = field.isAccessible();
	    	field.setAccessible(true);
	    	ModelField a = field.getAnnotation(ModelField.class);
	    	if ( a != null ) {
	            String toStringMethodName = a.method();
	            // Test for collections
	            if ( Collection.class.isAssignableFrom( field.getType() ) ) {
					Class<T> persistentClass = (Class<T>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
	            	// Test for entity collections
	            	if ( ModelEntity.class.isAssignableFrom(persistentClass) ) {
		            	toStringMethodName = MethodInvokation.ARGS + MethodInvokation.COL + "toEntityCollectionString";
	            	} else {
	            		toStringMethodName = MethodInvokation.ARGS + MethodInvokation.COL + "toCollectionString";
	            	}
	            } else {
	            	if ( ModelEntity.class.isAssignableFrom( field.getType() ) ) {
	            		toStringMethodName = MethodInvokation.ARGS + "toShortString";
	            	}
	            }
	            log.debug("Found ModelField " + fieldName + " using method " + toStringMethodName);
	            MethodInvokation<T> methodInvokation = new MethodInvokation<T>(clazz, field, toStringMethodName);
	            toStringFieldMap.put(field.getName(), methodInvokation);
	            if ( a.rep() ) {
		            toStringFieldMap.put("representative", methodInvokation);
	            }
	        } else {
	        	log.debug("Ignoring field " + fieldName);
	        }
	    	field.setAccessible(wasAccessible);
	    }
	    // Recursive scan toward java.lang.Object (and then null)
	    toStringFieldMap.putAll( parse(clazz.getSuperclass()) );
	    
	    // Results for this class
	    return toStringFieldMap;
    }
	
}