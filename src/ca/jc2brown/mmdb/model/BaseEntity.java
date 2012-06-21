package ca.jc2brown.mmdb.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.log4j.Logger;

import ca.jc2brown.generic.model.ModelEntity;
import ca.jc2brown.generic.model.ModelField;

@MappedSuperclass
public abstract class BaseEntity extends ModelEntity {
	
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( BaseEntity.class.getName() );
	
	
	@ModelField
	private Long id;
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public BaseEntity() {
		super();
	}
	

	public String[] compProps = { "id" };
	

	public String toIdString() {
		return id.toString();
	}
}

