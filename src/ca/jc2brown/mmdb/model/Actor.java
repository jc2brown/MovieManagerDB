package ca.jc2brown.mmdb.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import ca.jc2brown.framework.mapping.MappedField;

@Entity
@Table(name="People")
public class Actor extends Person {
	
	@MappedField("moviesToString")
	private Set<Movie> movies;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name	= "MovieActors", 
			joinColumns 		= { @JoinColumn(name = "Actors") }, 
			inverseJoinColumns 	= { @JoinColumn(name = "Movies") })
	public Set<Movie> getMovies() {
		return movies;
	}
	public void setMovies(Set<Movie> movies) {
		this.movies = movies;
	}
	
	
	public Actor() {
		super();
	}
	
	public String moviesToString() {
		StringBuffer sb = new StringBuffer();
		for ( Movie movie : movies ) {
			sb.append(movie.toLine());
		}
		return sb.toString();
	}
}