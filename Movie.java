// Name: Warat Phat-in
// Student ID: 6188035
// Section: 2

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Movie {
	private int mid;
	private String title;
	private int year;
	private Set<String> tags;
	private Map<Integer, Rating> ratings;	//mapping userID -> rating
	private Double avgRating;
	//additional
	
	public Movie(int _mid, String _title, int _year){
		// YOUR CODE GOES HERE
		mid = _mid;
		title = _title;
		year = _year;
		ratings = new HashMap<Integer, Rating>();
		tags = new HashSet<String>();
		
	}
	
	public int getID() {
		
		// YOUR CODE GOES HERE
		return mid;
	}
	public String getTitle(){
		
		// YOUR CODE GOES HERE
		return title;
	}
	
	public Set<String> getTags() {
		
		// YOUR CODE GOES HERE
		return this.tags;
	}
	
	public void addTag(String tag){
		
		// YOUR CODE GOES HERE
		tags.add(tag);
	}
	
	public int getYear(){
		
		// YOUR CODE GOES HERE
		return year;
	}
	
	public String toString()
	{
		return "[mid: "+mid+":"+title+" ("+year+") "+tags+"] -> avg rating: " + avgRating;
	}
	
	
	public double calMeanRating(){
		
		// YOUR CODE GOES HERE
		avgRating = 0.0;
		for(Integer Mean : ratings.keySet())
		{
			avgRating += ratings.get(Mean).rating;
		}
		avgRating = avgRating/ratings.size();
		return avgRating;
	}
	
	public Double getMeanRating(){
		
		// YOUR CODE GOES HERE
		return avgRating;
	}
	
	public void addRating(User user, Movie movie, double rating, long timestamp) {
		// YOUR CODE GOES HERE
		Rating Adding = new Rating(user, movie, rating,timestamp);
		ratings.put(user.uid, Adding);
		
	}
	
	public Map<Integer, Rating> getRating(){
		
		// YOUR CODE GOES HERE
		
		return ratings;
	}
	
}
