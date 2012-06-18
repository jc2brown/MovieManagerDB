package ca.jc2brown.mmdb.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Person extends BaseEntity {
	
	private String lastName;
	private String firstName;
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
	
	@Override
	public String toString() {
		return super.toString() + 
						"\nfullName=" + fullName +
						"\nfirstName=" + firstName +
						"\nlastName=" + lastName;
	}
	
	
}