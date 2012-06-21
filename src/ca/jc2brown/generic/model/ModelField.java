package ca.jc2brown.generic.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/*
 * Fields with this annotation will be included in calls to (? extends @ModelEntity).toString()
 * Output is of the form:
 * fully.qualified.class.Name@0xHASHCODE
 *   {field1="field1 value" field2="field2 value" etc...}
 *   
 * If the field's default toString method is undesirable, 
 * pass the name of an alternative method with the same signature as toString.
 * The method must be in the field's declaring class or a superclass, and may not be static.
 * 
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ModelField {
	public String method() default MethodInvokation.SELF + "toString";
	public boolean rep() default false;
	
}