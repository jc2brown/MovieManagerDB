package ca.jc2brown.mmdb.model.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/*
 * Fields with this annotation will be included in calls to (? extends @MappedClass).toString()
 * Output is of the form:
 * fully.qualified.class.Name@0xHASHCODE
 *   {field1="field1 value" field2="field2 value" etc...}
 *   
 * If the field's default toString method is undesirable, 
 * pass the name of an alternative method with the same signature as toString, 
 * which is located in the field's declaring class and reads the field (this means no static methods).
 * 
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MappedField {
	public String value() default "toString";
	
}