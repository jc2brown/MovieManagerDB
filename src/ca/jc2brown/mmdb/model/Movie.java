package ca.jc2brown.mmdb.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ca.jc2brown.mmdb.model.mapping.MappedField;


@Entity
@Table(name="Movies")
public class Movie extends BaseEntity {
	
	private Set<Genre> genres;
	private List<MovieFile> movieFiles;
	private String plot;
	private Rating rating;
	private String reception;
	private Date released;
	private Long runtime;
	@MappedField
	private String title;
	private Long year;
	
	
	@ManyToMany
	@JoinTable(name	= "MovieGenres", 
	joinColumns 		= { @JoinColumn(name = "GenreId") }, 
	inverseJoinColumns 	= { @JoinColumn(name = "MovieId") })
	public Set<Genre> getGenres() {
		return genres;
	}
	public void setGenres(Set<Genre> genres) {
		this.genres = genres;
	}
	
	@OneToMany
	public List<MovieFile> getMovieFiles() {
		return movieFiles;
	}
	public void setMovieFiles(List<MovieFile> movieFiles) {
		this.movieFiles = movieFiles;
	}

	public String getPlot() {
		return plot;
	}
	public void setPlot(String plot) {
		this.plot = plot;
	}

	@ManyToOne
	@JoinColumn(name="RatingId")
	public Rating getRating() {
		return rating;
	}
	public void setRating(Rating rating) {
		this.rating = rating;
	}

	public String getReception() {
		return reception;
	}
	public void setReception(String reception) {
		this.reception = reception;
	}

	public Date getReleased() {
		return released;
	}
	public void setReleased(Date released) {
		this.released = released;
	}

	public Long getRuntime() {
		return runtime;
	}
	public void setRuntime(Long runtime) {
		this.runtime = runtime;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public Long getYear() {
		return year;
	}
	public void setYear(Long year) {
		this.year = year;
	}

	
	public Movie() {
		super();
	}	
	

}