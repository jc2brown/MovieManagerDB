package ca.jc2brown.mmdb.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ca.jc2brown.generic.dao.GenericDao;
import ca.jc2brown.generic.model.ModelField;


@Entity
@Table(name="Movies")
public class Movie extends BaseEntity {

	@ModelField
	private Set<MovieGenre> movieGenres;
	@ModelField
	private List<MovieFile> movieFiles;
	@ModelField
	private String plot;
	@ModelField
	private Rating rating;
	@ModelField
	private String reception;
	@ModelField
	private Date released;
	@ModelField
	private Long runtime;
	@ModelField(rep=true)
	private String title;
	@ModelField
	private Long year;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="MovieId")
	public Set<MovieGenre> getMovieGenres() {
		return movieGenres;
	}
	public void setMovieGenres(Set<MovieGenre> movieGenres) {
		this.movieGenres = movieGenres;
	}
	
	@OneToMany (cascade=CascadeType.ALL, mappedBy="movie", fetch=FetchType.EAGER)
	@Column(name="id")
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

	@ManyToOne(cascade=CascadeType.ALL)
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
	
	
	public void addGenre(String genre) {
		MovieGenre movieGenre = new MovieGenre();
		movieGenre.setMovie(this);
		movieGenre.setGenre(GenericDao.smakePersistent(new Genre(genre)));
		movieGenres = add(movieGenres, movieGenre);
	}
	
	
	public void addMovieFile(MovieFile movieFile) {
		movieFile.setMovie(this);
		movieFiles = add(movieFiles, movieFile);
	}
	

	
	
}