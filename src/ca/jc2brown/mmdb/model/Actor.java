package ca.jc2brown.mmdb.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="People")
public class Actor extends Person {
	
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

	@Override
	public String toString() {
		return super.toString() + "/n" + movies.toString();
	}
	
	
}