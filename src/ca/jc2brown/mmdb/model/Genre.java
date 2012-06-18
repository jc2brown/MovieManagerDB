package ca.jc2brown.mmdb.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="Genres")
public class Genre extends BaseEntity {

	private String genre;
	
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	
	public Genre() {
		super();
	}




}