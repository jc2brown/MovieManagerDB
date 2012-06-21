package ca.jc2brown.mmdb.model;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ca.jc2brown.generic.model.ModelField;


@Entity
@Table(name="MovieFiles")
public class MovieFile extends BaseEntity {
	/*
	@Column(name="FileId")
	public Long getId() {
		return super.getId();
	}
*/
	@ModelField
	private Movie movie;
	@ModelField(rep=true)
	private String filename; 
	@ModelField
	private String origin;
	@ModelField
	private Long sequence;

	@ManyToOne(optional=true, cascade=CascadeType.ALL,  fetch=FetchType.EAGER)
	@JoinColumn(name="MovieId")
	public Movie getMovie() {
		return movie;
	}
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Long getSequence() {
		return sequence;
	}
	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}
	
	
	public MovieFile() {
		super();
	}
}