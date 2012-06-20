package ca.jc2brown.mmdb.model.mapping;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import ca.jc2brown.mmdb.MediaManagerDB;
import ca.jc2brown.mmdb.model.mapping.MappedEntity;

// This class has a single public method, configure, which scans the given package for classes annotated with @MappedClass.
// Since @MappedClass is @Inherited, this effectively gets all subclasses too
// 
// The parse method scans each @MappedClass for fields with @MappedField, and adds an entry in the map for each.
// Each map entry consists of the field's name and a MethodInvokation object which can be used to generate a string value of that field
// Each map is added to a master map that associates each field:method map with the class containing those fields
//
// The result is a mechanism that facilitates automatic - albeit inflexible - toString and toMap generation.

public class MappingConfigurer {
	
	private static Logger log = Logger.getLogger( MappingConfigurer.class.getName() );
	
	public static void configure(String basePackage) {
		log.debug("Configuring package: " + basePackage);
		Map<Class<?>,Map<String, MethodInvokation<? extends MappedEntity>>> toStringClassMap = new HashMap<Class<?>,Map<String, MethodInvokation<? extends MappedEntity>>>();
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(MappedClass.class));
		try {
			for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
				String className = bd.getBeanClassName();
				log.debug("configuring bean: " + className);
				Class<?> clazz = Class.forName(className);
				Map<String,MethodInvokation<? extends MappedEntity>> toStringSubMap = parse(clazz);
				log.debug("Parsed toStringSubMap: " + toStringSubMap);
				toStringClassMap.put(clazz, toStringSubMap);
			}
			
			// We don't want to expose the map at all to subclasses of MappedEntity, so set
			// the private map with reflection instead of a regular accessor
			Field toStringMapField = MappedEntity.class.getDeclaredField("toStringClassMap");
			toStringMapField.setAccessible(true);
			toStringMapField.set(null, toStringClassMap);
			
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
	
	private static <T extends MappedEntity> Map<String, MethodInvokation<? extends MappedEntity>> parse(Class<?> clazz) {
	    	Map<String, MethodInvokation<? extends MappedEntity>> map = new HashMap<String, MethodInvokation<? extends MappedEntity>>();
	    	if ( clazz == null ) {
	    		return map;
	    	}
	    	log.debug("Parsing " + clazz.getCanonicalName());
	        Field[] fields = clazz.getDeclaredFields();
		    for (Field field : fields ) {
		    	field.setAccessible(true);
		    	MappedField a = field.getAnnotation(MappedField.class);
		    	String toStringMethodName = "";
		    	if ( a != null ) {
		    		toStringMethodName = a.value();
		            log.debug("found TableField " + field.getName());
		            MethodInvokation<T> methodInvokation = new MethodInvokation<T>(clazz, field, toStringMethodName);
		            map.put(field.getName(), methodInvokation);
		        } else {
		        	log.debug("non-table field " + field.getName());
		        }
		    }
		    Map<String, MethodInvokation<? extends MappedEntity>> submap = parse(clazz.getSuperclass());
		    map.putAll(submap);
		    return map;
	    }
		
		private static void bail(Exception e) {
			log.fatal(e);
			MediaManagerDB.bail(e.getMessage());
		}
		
	}