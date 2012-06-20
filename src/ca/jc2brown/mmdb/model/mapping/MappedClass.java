package ca.jc2brown.mmdb.model.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/* 
 * Classes decorated with this annotation will be scanned by the MappingConfigurator
 * for fields with the MappedField annotation when it's configure method is called.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MappedClass {
	
}