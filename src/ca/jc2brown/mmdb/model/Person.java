package ca.jc2brown.mmdb.model;


import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import ca.jc2brown.generic.model.ModelField;

@MappedSuperclass
public abstract class Person extends BaseEntity {
	
	@ModelField
	private String lastName;
	@ModelField
	private String firstName;
	@ModelField(rep=true)
	private String fullName;
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getLastName() {
		return lastName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getFirstName() {
		return firstName;
	}

	@Column(unique=true)
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getFullName() {
		return fullName;
	}
	
	
	public Person() {
		super();
	}
}