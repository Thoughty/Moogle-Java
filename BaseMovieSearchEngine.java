import java.util.List;
import java.util.Map;


public interface BaseMovieSearchEngine {

	//////////////////////////////
	// DO NOT MODIFY THIS CLASS //
	//////////////////////////////

	/**
	 * Parse movieFilename and stores the movie information in a Map data structure
	 * that maps movie's id (mid) to the corresponding Movie object. If the input is null, 
	 * simply return null.
	 * Each line in the file "movieFilename" has one of the following formats:
	 * 
	 * 1. The movie title does not contain ','
	 * 
	 * <mid>,<title> (<year>),<tag_1>|<tag_2>|<tag_3>|...|<tag_n>
	 * For example: 2,Jumanji (1995),Adventure|Children|Fantasy
	 * 
	 * 2. The movie title contains ','
	 * <mid>,"<title> (<year>)",<tag_1>|<tag_2>|<tag_3>|...|<tag_n>
	 * For example: 11,"American President, The (1995)",Comedy|Drama|Romance
	 * 
	 * The information of each movie must be store in a Movie object.
	 * 
	 * Return a Map<Integer, Movie> object that is a mapping from 'mid' -> the corresponding Movie object.
	 * 
	 * Hint: You may find it easier to parse each line using regular expressions,
	 * and each tag set using String.split()
	 * 
	 * Note: the first line of the movie file contains the header information. Ignore it.
	 * 
	 * If movieFilename is null, simply return an empty map;
	 * 
	 */
	public Map<Integer,Movie> loadMovies(String movieFilename);
	
	
	
	/**
	 * Parse the file "ratingFilename" which contains the information about movie ratings. 
	 * Each line in the file contains information about which user rates what movie, and when.
	 * 
	 * Each line in the file has the following format:
	 * 
	 * <uid>,<mid>,<rating>,<timestamp>
	 * For example: 1,256,0.5,1217895764
	 * 
	 * The information in each line must be stored in a Rating object.
	 * 
	 * Additionally, ratings associated with a movie should be stored in the "ratings" object in the corresponding Movie object.
	 * 
	 * If a user rate the same movie multiple times, the "ratings" object will only keep the latest rating (larger timestamp). 
	 * 
	 * For example, suppose in the file contains these following two lines in the file
	 * 				1,16,4.0,1217897793
	 * 				1,16,3.0,1217898888
	 * This means that the user 1 rates the movie 16 twice.
	 * Therefore, the ratings object of movie 16 for the user 1 will be 3.0 instead of 4.0
	 * 
	 * Hint: In this case, String.split() may be less messier to use.
	 * 
	 * Note: the first line of the movie file contains the header information. Ignore it.
	 * 
	 * You may assume that this method is always called after loadMovies()
	 * 
	 * If userFilename is null, simply do nothing
	 * 
	 */
	public void loadRating(String ratingFilename);
	
	
	/**
	 * loadData() is called right after the search engine is instantiated. It loads the movies from "movieFilename"
	 * and users and ratings information from "ratingFilename", and store these pieces of information in the
	 * search engine's internal memory. 
	 * 
	 * Specifically, this method makes calls to loadMovies(String movieFilename) and loadRating(String ratingFilename) 
	 * respectively to parse and extract information from those input files.
	 * 
	 * @param movieFilename
	 * @param ratingFilename
	 */
	public  void loadData(String movieFilename, String ratingFilename);
	
	/**
	 * Return the reference to the Map<Integer, Movie> object, which stores the loaded movies and their ratings.
	 * 
	 * If the movies/ratings have not been loaded, there is 0 movie in the data, return an empty Map.
	 * 
	 */
	public Map<Integer, Movie> getAllMovies();

	
	/**
	 * Search for movies whose title matches with the provided title
	 * The boolean 'exactMatch' determine whether the exact match is required or not.
	 * If exactMatch is false, the approximate match is allowed e.g., 
	 * the keyword "balls" matches with the movie's title "Spaceballs".  
	 * 
	 * Note that the matching result is case insensitive
	 * 
	 * Return a List<Movie> object that is a list of Movie object. 
	 */
	public List<Movie> searchByTitle(String title, boolean exactMatch);
	
	/**
	 * Search for movies whose tag matches with the provided tag
	 * 
	 * Note that the matching result is case insensitive
	 * 
	 * Return a List<Movie> object that is a list of Movie object. 
	 */
	public List<Movie> searchByTag(String tag);
	
	/**
	 * Search for movies whose year matches with the provided year
	 * 
	 * Note that the matching result is case insensitive
	 * 
	 * Return a List<Movie> object that is a list of Movie object. 
	 */
	public List<Movie> searchByYear(int year);
	
	/**
	 * Search for movies based on a combination of criteria including "title", "tag" and "year"
	 * For the title, the search will find both exact match and approximate match with case-insensitive. 
	 * At least one input parameter is required. 
	 * If the title or tag is not used in the search criteria, the null value will be provided as an input.
	 * If the year is not used,  the number "-1" will be provided as an input.
	 * 
	 * e.g.,  	advanceSearch("balls", null, 2012);		// only title and year are used as search criteria
	 * 			advanceSearch("balls", "Comedy", -1);	// only title and tag are used as search criteria
	 * 			advanceSearch("balls", "Comedy", 2012);	// all search criteria are used.
	 * 
	 * Return a List<Movie> object that is a list of Movie object. 
	 */
	
	public List<Movie> advanceSearch(String title, String tag, int year);
	
	/**
	 * Sort Movie object in the 'unsortedMovies' map by the movie's title.
	 * 
	 * If the boolean 'asc' is true, the output will be sorted in ascending order (a -> z)
	 * Otherwise, the output will be sorted in descending order (z -> a)
	 * 
	 * Return a sorted List<Movie> object that is a list o + (i/ Movie object.  
	 */
	public List<Movie> sortByTitle(List<Movie> unsortedMovies, boolean asc);
	
	
	/**
	 * Sort Movie object in the 'unsortedMovies' map by the movie's average rating.
	 * 
	 * If the boolean 'asc' is true, the output will be sorted in ascending order (low -> high)
	 * Otherwise, the output will be sorted in descending order (high -> low)
	 * 
	 * Return a sorted List<Movie> object that is a list of Movie object.   
	 */
	public List<Movie> sortByRating(List<Movie> unsortedMovies, boolean asc);
	
	
}
