package ca.jc2brown.mmdb.model;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ca.jc2brown.generic.model.ModelField;


@Entity
@Table(name="MovieGenres")
public class MovieGenre extends BaseEntity {

	@ModelField
	private Movie movie;
	@ModelField(rep=true)
	private Genre genre; 


	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="MovieId")
	public Movie getMovie() {
		return movie;
	}
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="GenreId")
	public Genre getGenre() {
		return genre;
	}
	public void setGenre(Genre genre) {
		this.genre = genre;
	}
	

	
	public MovieGenre() {
		super();
	}
}