package ca.jc2brown.mmdb.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.log4j.Logger;

import ca.jc2brown.mmdb.model.mapping.MappedEntity;
import ca.jc2brown.mmdb.model.mapping.MappedField;

@MappedSuperclass
public abstract class BaseEntity extends MappedEntity {
	private static Logger log = Logger.getLogger( BaseEntity.class.getName() );
	
	
	@MappedField
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
		id = null;
	}
}

