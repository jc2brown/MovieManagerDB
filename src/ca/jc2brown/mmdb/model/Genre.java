package ca.jc2brown.mmdb.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import ca.jc2brown.generic.model.ModelField;

@Entity
@Table(name="Genres", uniqueConstraints={@UniqueConstraint(columnNames={"genre"})})
public class Genre extends BaseEntity {

	@ModelField(rep=true)
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
	
	
	public Genre(String genre) {
		super();
		this.genre = genre;
	}
}