import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;


public class MoogleTester {
	
	public static void main(String[] args){
		SampleSearch();
	}
	
	public static void SampleSearch(){
		String movieFileName = "data-micro/movies.csv";
		String ratingFileName = "data-micro/ratings.csv";

		SimpleMovieSearchEngine s = new SimpleMovieSearchEngine();
		
		s.loadData(movieFileName, ratingFileName);
		Map<Integer, Movie> movies = s.getAllMovies();
		Map<Integer, Rating> ratings;
		int numRatings = 0;
	
		for(Integer key: movies.keySet()){
			System.out.println(movies.get(key).toString());
			ratings = movies.get(key).getRating();
			numRatings += ratings.size();
			for(Integer uid: ratings.keySet()){
				System.out.println( "   " + ratings.get(uid).toString());
			}
		}
		System.out.println("************************************");
		System.out.println("Total number of movies: " + movies.size());
		System.out.println("Total number of ratings: " + numRatings);
		
		String title = "stand up guys";
		List<Movie> foundMovies = s.searchByTitle(title, true);
		printResults("Search by exact title = " + title, foundMovies, -1);
		
		title = "and";
		foundMovies = s.searchByTitle(title, false);
		printResults("Search by approximate title = " + title, foundMovies, -1);
		
		String tag = "Comedy";
		foundMovies = s.searchByTag(tag);
		printResults("Search by tag = " + tag, foundMovies, -1);
		
		int year = 2013;
		foundMovies = s.searchByYear(year);
		printResults("Search by year = " + year, foundMovies, -1);
		
		List<Movie> sortedMovies = s.sortByTitle(foundMovies, true);
		printResults("Sorted by title in ascending order", sortedMovies, 5);
		
		sortedMovies = s.sortByRating(foundMovies, false);
		printResults("Sorted by rating in descending order", sortedMovies, 5);
		
		year = 2012;
		foundMovies = s.advanceSearch(null, tag, year);
		printResults("Advance search by approximate tag = " + tag + " and year = " + year, foundMovies, -1);
		
		foundMovies = s.advanceSearch(title, tag, -1);
		printResults("Advance search by approximate title = " + title + " and tag = " + tag, foundMovies, -1);
		
		year = 2016;
		foundMovies = s.advanceSearch(title, tag, year);
		printResults("Advance search by approximate title = " + title + ", tag = " + tag + ", and year = " + year, foundMovies, -1);
		
	}
	
	public static void printResults(String msg, List<Movie> results, int topK){
		
		System.out.println("********************* RESULTS ********************");
		if(topK == -1 || topK > results.size())
			topK = results.size();
		int i = 0;
		for(Movie m: results){
			if(i >= topK)
				break;
			System.out.println(m);
			i++;
		}
		//System.out.println("---------------------------------------------------");
		System.out.println(msg);
		System.out.println(results.size() + " movies found!");
		
	}
}
