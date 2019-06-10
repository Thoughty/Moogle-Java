// Name:Warat Phat-in
// Student ID: 6188035
// Section: 2

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SimpleMovieSearchEngine implements BaseMovieSearchEngine {
	public Map<Integer, Movie> movies;
	
	@Override
	public Map<Integer, Movie> loadMovies(String movieFilename) {
		Map<Integer, Movie> MovieLoader = new HashMap<Integer, Movie>();
		BufferedReader Reading = null;
		int MovieId,year;
		if(movieFilename == null)
		{
			return MovieLoader;
		}
		try {
			Reading = new BufferedReader(new FileReader(movieFilename));
			String line;
			while((line = Reading.readLine())!=null)
			{
				Pattern Pat1 = Pattern.compile("([0-9]+),([\\S ]+)\\s\\(([0-9]+)\\),(.*)"); // Pattern for File
				Pattern Pat2 = Pattern.compile("([0-9]+),\"([\\S ]+)\\s\\(([0-9]+)\\)\",(.*)"); // Pattern for another File with different pattern
				Matcher Mat1 = Pat1.matcher(line);
				Matcher Mat2 = Pat2.matcher(line);
				Movie LoadingMovie = null;
				if(Mat1.matches()) // if File Matches with Pattern 1
				{
					String[] TagSplit = Mat1.group(4).split("\\|"); //Split The tag when encounter the " | " sign.
					MovieId = Integer.parseInt(Mat1.group(1));
					year = Integer.parseInt(Mat1.group(3));
					LoadingMovie = new Movie(MovieId,Mat1.group(2) ,year);
					for(String tag : TagSplit)
					{	
						LoadingMovie.addTag(tag);
					}
					MovieLoader.put(MovieId, LoadingMovie);
				}
				else if(Mat2.matches())// if File Matches with Pattern 2
				{
					String[] TagSplit = Mat2.group(4).split("\\|");//Split The tag when encounter the " | " sign.
					MovieId = Integer.parseInt(Mat2.group(1));
					year = Integer.parseInt(Mat2.group(3));
					LoadingMovie = new Movie(MovieId, Mat2.group(2), year);
					for(String tag : TagSplit)
					{
						LoadingMovie.addTag(tag);
					}
					MovieLoader.put(MovieId, LoadingMovie);
				}
			}
			
		}
		catch(FileNotFoundException e){ // Print Result if file not found in the inserted directory 
			e.printStackTrace();
		}
		catch(IOException e) // Print Result if Error is occur
		{
			e.printStackTrace();
		}
		
		finally { // After reading all data in the file 
			try
            {
                Reading.close(); // Close the Bufferreader
            }
            catch(IOException ie) //  Print Error result 
            {
                System.out.println("Error occured while closing the BufferedReader");
                ie.printStackTrace();
            }
		}
		// YOUR CODE GOES HERE
		
		return MovieLoader;
	}

	@Override
	public void loadRating(String ratingFilename) {

		// YOUR CODE GOES HERE
		BufferedReader RatingReading = null;
		int UserId,MovieId;
		double rating;
		long timestamp;
		Movie movie;
		if(ratingFilename == null)
		{
			return ;
		}
		try {
			RatingReading = new BufferedReader(new FileReader(ratingFilename));
			String line;
			while((line = RatingReading.readLine())!= null)
			{
				Pattern Pat1 = Pattern.compile("(\\d+),(\\d+),(\\d+.\\d+),(\\d+)"); // Pattern to matches with the rating file
				Matcher Mat1 = Pat1.matcher(line);
				if(Mat1.matches())
				{
					// Convert String to matches the implement variable
					UserId = Integer.parseInt(Mat1.group(1));
					MovieId = Integer.parseInt(Mat1.group(2));
					rating = Double.parseDouble(Mat1.group(3));
					timestamp = Long.parseLong(Mat1.group(4));
					movie = movies.get(MovieId);
					if(movie != null)
					{
						if(!movie.getRating().containsKey(UserId)) // if this user have never rate this movie before
						{
							movie.addRating(new User(UserId), movie, rating, timestamp);
						}
						else
						{
							if(movie.getRating().get(UserId).timestamp < timestamp) // Update their rating score if it's exits
							{
								movie.getRating().replace(UserId, new Rating(new User(UserId),movie, rating, timestamp));
							}
						}
					}
				}
			}
			for(Integer i : movies.keySet()) { // calculate all the rating score from all the User that rate this movie
				movies.get(i).calMeanRating();
			}
		}
		
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		finally
        {
            try
            {
                RatingReading.close();
            }
            catch(IOException ie)
            {
                System.out.println("Error occured while closing the BufferedReader");
                ie.printStackTrace();
            }
        }
		
	}

	@Override
	public void loadData(String movieFilename, String ratingFilename) {
	
		// YOUR CODE GOES HERE
			movies = loadMovies(movieFilename);  // LoadData From 2 methods
			loadRating(ratingFilename);
			
			
			PrintWriter Writer;
			int RatingNum = 0;
			Map<Integer, Rating> ratings;
			
			try 
			{
				Writer = new PrintWriter("test.txt","UTF-8");
				for(Integer key : movies.keySet())
				{
					Writer.println(movies.get(key).toString());
					ratings = movies.get(key).getRating();
					RatingNum += ratings.size();
					for(Integer UserID : ratings.keySet())
					{
						Writer.println(" "+ratings.get(UserID).toString());
					}
					Writer.println("************************************");
					Writer.println("Total number of movies: " + movies.size());
					Writer.println("Total number of ratings: " + RatingNum);
					Writer.close();
				}
			}
			 catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			} 
			catch (UnsupportedEncodingException e) 
			{
				e.printStackTrace();
			}
	}

	@Override
	public Map<Integer, Movie> getAllMovies() {

		// YOUR CODE GOES HERE
		
		return movies;
	}

	@Override
	public List<Movie> searchByTitle(String title, boolean exactMatch) {

		// YOUR CODE GOES HERE
		List<Movie> ListOfMovie = new ArrayList<Movie>();
		Iterator<HashMap.Entry<Integer,Movie>> iterator = movies.entrySet().iterator();
		Movie MovieTempName;
		String MovieTempTitle;
		
		if(exactMatch) // if the name that insert is correct order and correct letter
		{
			while(iterator.hasNext())
			{
				MovieTempName = iterator.next().getValue();
				MovieTempTitle = MovieTempName.getTitle();
				if(MovieTempTitle.equalsIgnoreCase(title)) 
				{
					ListOfMovie.add(MovieTempName);
				}
				
			}
		}
		else
		{
			while(iterator.hasNext())
			{
				MovieTempName = iterator.next().getValue();
				MovieTempTitle = MovieTempName.getTitle();
				if(MovieTempTitle.toLowerCase().contains(title.toLowerCase()))
				{
					ListOfMovie.add(MovieTempName);
				}
			}
		}
		return ListOfMovie;
	}

	@Override
	public List<Movie> searchByTag(String tag) {

		// YOUR CODE GOES HERE
		List<Movie> ListOfMovie = new ArrayList<Movie>();
		Iterator<HashMap.Entry<Integer,Movie>> iterator = movies.entrySet().iterator();
		Movie MovieTempName;
		Set<String> MovieTempTag;
		
		while(iterator.hasNext())
		{
			MovieTempName = iterator.next().getValue();
			MovieTempTag = MovieTempName.getTags();
			if(MovieTempTag.contains(tag))
			{
				ListOfMovie.add(MovieTempName);
			}
		}
		return ListOfMovie;
	}

	@Override
	public List<Movie>searchByYear(int year) {

		// YOUR CODE GOES HERE
		List<Movie> ListOfMovie = new ArrayList<Movie>();
		Iterator<HashMap.Entry<Integer,Movie>> iterator = movies.entrySet().iterator();
		Movie MovieTempName;
		int MovieYear;
		
		while(iterator.hasNext())
		{
			MovieTempName = iterator.next().getValue();
			MovieYear = MovieTempName.getYear();
			if(MovieYear == year)
			{
				ListOfMovie.add(MovieTempName);
			}
		}
		return ListOfMovie;
	}

	public List<Movie> FilteredByTag (List<Movie> listMovie,String tag) // This method is used in "adcanceSearch" to qualified the movie
	{
		List<Movie> toReturn = listMovie;
		for(Iterator<Movie> i = toReturn.iterator(); i.hasNext();)
		{
			Movie temp = i.next();
			if(!temp.getTags().contains(tag))
			{
				i.remove(); //Remove the movie if it's not the same tag
			}
		}
		return toReturn;
	}
	public List<Movie> FilteredByYear (List<Movie> listMovie,int year) // This method is used in "adcanceSearch" to qualified the movie
	{
		List<Movie> toReturn = listMovie;
		for(Iterator<Movie> i = toReturn.iterator(); i.hasNext();)
		{
			Movie temp = i.next();
			if(temp.getYear() != year)
			{
				i.remove(); // Remove the movie if it's not the same year
			}
		}
		return toReturn;
	}
	
	
	@Override
	public List<Movie> advanceSearch(String title, String tag, int year) {
		
		// YOUR CODE GOES HERE
		List<Movie> ListOfMovie = new ArrayList<Movie>();
		
		if(title != null)
		{
			ListOfMovie =searchByTitle(title, false);
			if(tag!=null)
			{
				ListOfMovie = FilteredByTag(ListOfMovie, tag); 
			}
			if(year!= -1)
			{
				ListOfMovie = FilteredByYear(ListOfMovie, year);
			}
		}
		else
		{
			if(tag != null)
			{
				ListOfMovie = searchByTag(tag);
				if(year != -1)
				{
					ListOfMovie = FilteredByYear(ListOfMovie, year);
				}
			}
			else
			{
				ListOfMovie = searchByYear(year);
			}
		}
		return ListOfMovie;
	}

	@Override
	public List<Movie> sortByTitle(List<Movie> unsortedMovies, boolean asc) {

		// YOUR CODE GOES HERE
		List<Movie> ListOfMovie = unsortedMovies;
		ListOfMovie.sort(Comparator.comparing(Movie::getTitle));
		if(!asc)
		{
			Collections.reverse(ListOfMovie);
		}
		return ListOfMovie;
	}

	@Override
	public List<Movie> sortByRating(List<Movie> unsortedMovies, boolean asc) {

		// YOUR CODE GOES HERE
		List<Movie> ListOfMovie = unsortedMovies;
		ListOfMovie.sort(Comparator.comparing(Movie::getMeanRating));
		if(!asc)
		{
			Collections.reverse(ListOfMovie);
		}
		return ListOfMovie;	}

	
}
