package ca.jc2brown.mmdb.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="Ratings")
public class Rating extends BaseEntity {

	private String rating;
	private Rating preferredRating;
	
	
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}

	@ManyToOne
	@JoinColumn(name="preferredRatingId")
	public Rating getPreferredRating() {
		return preferredRating;
	}
	public void setPreferredRating(Rating preferredRating) {
		this.preferredRating = preferredRating;
	}
	
	
	public Rating() {
		super();
	}
}