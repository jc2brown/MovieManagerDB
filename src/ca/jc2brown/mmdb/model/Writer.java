package ca.jc2brown.mmdb.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="Writer")
public class Writer extends Person {
	
	private Set<Movie> movies;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name	= "MovieWriters", 
			joinColumns 		= { @JoinColumn(name = "WriterId") }, 
			inverseJoinColumns 	= { @JoinColumn(name = "MovieId") })
	public Set<Movie> getMovies() {
		return movies;
	}
	public void setMovies(Set<Movie> movies) {
		this.movies = movies;
	}
	
	
	public Writer() {
		super();
	}
	
}