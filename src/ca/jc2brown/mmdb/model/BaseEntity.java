package ca.jc2brown.mmdb.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {
	
	private long id;
	
	public void setId(long id) {
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	
	public BaseEntity() {
		super();
	}	
	
	@Override
	public String toString() {
		return super.toString() + "\nid=" + id;
	}
}