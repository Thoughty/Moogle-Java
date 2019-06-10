import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class MainController {
	//////////////////////////////
	// DO NOT MODIFY THIS CLASS //
	//////////////////////////////
	public static int DISPLAY = 20;
	
	public static void main(String[] args) {
		String mainUsage = "Loading movies and ratings: load <movie_filename> <rating_filename>\n"
				+ "Ex. java -jar MainController.jar load movies.csv ratings.csv\n\n"
				
				+ "Searching movies by title (exact matching): search <movie_filename> <rating_filename> -e <title>\n"
				+ "Ex. java -jar MainController.jar search movies.csv ratings.csv -e \"stand up guys\" \n"
				
				+ "Searching movies by title (approximate matching): search <movie_filename> <rating_filename> -a <title>\n"
				+ "Ex. java -jar MainController.jar search movies.csv ratings.csv -a \"stand\" \n"

				+ "Searching movies by tag: search <movie_filename> <rating_filename> -t <tag>\n"
				+ "Ex. java -jar MainController.jar search movies.csv ratings.csv -t Comedy \n"
				
				+ "Searching movies by year: search <movie_filename> <rating_filename> -y <year>\n"
				+ "Ex. java -jar MainController.jar search movies.csv ratings.csv -y 2013 \n"
				
				+ "Advance search movie: search+ <movie_filename> <rating_filename> [-a] [<title>] [-t] [<tag>] [-y] [<year>]\n"
				+ "Ex. java -jar MainController.jar search+ movies.csv ratings.csv -a \"all\" -t Sci-Fi \n"
				+ "Ex. java -jar MainController.jar search+ movies.csv ratings.csv -a \"er\" -t Drama -y 2013\n"
				+ "Ex. java -jar MainController.jar search+ movies.csv ratings.csv -a \"er\" -y 2013\n"
				
				+ "";
				
		boolean printMainUsage = false;
		
		SimpleMovieSearchEngine s = new SimpleMovieSearchEngine();
		Map<Integer, Movie> movies = null;
		Map<Integer, Rating> ratings = null;
		List<Movie> result = null;
		
		if(args.length < 3) {
			System.out.println("Error: See Usage");
			printMainUsage = true;
		} else {
			String command = args[0];
			String movieFileName = args[1];
			String ratingFileName = args[2];
			
			switch (command){
				case "load":
					if(args.length != 3) {
						System.out.println("Error: See Usage");
						printMainUsage = true;
					} else {
						s.loadData(movieFileName, ratingFileName);
						movies = s.getAllMovies();
						
						int numRating = 0;
						for(Integer key: movies.keySet()){
							ratings = movies.get(key).getRating();
							numRating += ratings.size();
						}
						System.out.println("*****************************************");
						System.out.println("Total number of movies: " + movies.size());
						System.out.println("Total number of ratings: " + numRating);
					}
					break;
				case "search":
					if(args.length != 5) {
						System.out.println("Error: See Usage");
						printMainUsage = true;
					} else {
						s.loadData(movieFileName, ratingFileName);
						String searchBy = args[3];
						String keyword = args[4].replaceAll("\"", "");;
						
						if(searchBy.equalsIgnoreCase("-e")) {
							result = s.searchByTitle(keyword, true);
							printResult("Keyword -> title (exact) = " + keyword, result);
						} else if(searchBy.equalsIgnoreCase("-a")) {
							result = s.searchByTitle(keyword, false);
							printResult("Keyword -> title (approximate) = " + keyword, result);
						} else if(searchBy.equalsIgnoreCase("-t")) {
							result = s.searchByTag(keyword.replaceAll("\"", ""));
							printResult("Keyword -> tag = " + keyword, result);
						} else if(searchBy.equalsIgnoreCase("-y")) {
							try {
								result = s.searchByYear(Integer.parseInt(keyword));
								printResult("Keyword -> year = " + keyword, result);
							} catch(Exception e) {
								e.printStackTrace();
								System.out.println("Error: See Usage");
								printMainUsage = true;
							}
						} else {
							System.out.println("Error: See Usage");
							printMainUsage = true;
						}
						
					} 
					break;
				case "search+":	
					if(args.length < 5 || args.length%2 == 0) {	// the total number of parameters should >= 5 and must be odd number 
						System.out.println("Error: See Usage");
						printMainUsage = true;
					} else {
						s.loadData(movieFileName, ratingFileName);
						
						int numOfCriteria = (args.length - 3)/2;
						String title = null, tag = null;
						int year = -1;
						for(int i = 0; i < numOfCriteria; i++) {
							if(args[3 + (i*2)].equalsIgnoreCase("-a")) {
								title = args[4 + (i*2)].replaceAll("\"", "");
							} else if(args[3 + (i*2)].equalsIgnoreCase("-t")) {
								tag = args[4 + (i*2)];
							} else if(args[3 + (i*2)].equalsIgnoreCase("-y")) {
								try {
									year = Integer.parseInt(args[4 + (i*2)]);
								}  catch(Exception e) {
									e.printStackTrace();
									System.out.println("Error: See Usage");
									printMainUsage = true;
								}
							}
						}
						String msg = "Keywords ->";
						if(title != null)
							msg += " ,title = " + title;
						if(tag != null)
							msg += " ,tag = " + tag;
						if(year != -1)
							msg += " ,year = " + year;
						result = s.advanceSearch(title, tag, year);
						printResult(msg, result);
					} 
					break;
				default:
					System.out.println("Error: See Usage");
					printMainUsage = true;
					break;
			}
		}
		
		if(printMainUsage) {
			// print the main message of how to use the program
			System.out.println(mainUsage); 
			// end program
		} else {
			
			// If the initial state is completed, the system will prompt the user and ask for the next step
			String nextUsage = "What do you want to do next?\n"
							+ "type 't' to sort the result by title in ascending order,\n"
							+ "type 'T' to sort the result by title in descending order,\n"
							+ "type 'r' to sort the result by movie's average rating in ascending order,\n"
							+ "type 'R' to sort the result by movie's average rating in descending order,\n"
							+ "type 's' follow by the search criteria (-e/-a/-t/-y) and keyword to search movies again with normal searh criteria,\n"
							+ "type 'S' follow by the search criteria and list of keywords to search movies again with advanced search criteria\n"
							+ "type 'q' to quite the program.";
			System.out.println(nextUsage);
			
			Scanner scan = new Scanner(System.in);
			boolean done = false;
			while(!done) {
				String opt = scan.nextLine();
				List<Movie> sorted = null;
				
				switch(opt.charAt(0)) {
					case 't':
						if(result == null) {
							System.out.println("Error: Please search movies before sorting the results");
						} else {
							sorted = s.sortByTitle(result, true);
							printResult("Sorted by title in ascending order", sorted);
						}	
						break;
					case 'T':
						if(result == null) {
							System.out.println("Error: Please search movies before sorting the results");
						} else {
							System.out.println(result.size());
							sorted = s.sortByTitle(result, false);
							printResult("Sorted by title in descending order", sorted);
						}
						break;
					case 'r':
						if(result == null) {
							System.out.println("Error: Please search movies before sorting the results");
						} else {
							sorted = s.sortByRating(result, true);
							printResult("Sorted by rating in ascending order", sorted);
						}
						break;
					case 'R':
						if(result == null) {
							System.out.println("Error: Please search movies before sorting the results");
						} else {
							sorted = s.sortByRating(result, false);
							printResult("Sorted by rating in descending order", sorted);
						}
						break;
					case 's':
						String[] criteria = opt.split(" ");
						if(criteria.length != 3) {
							System.out.println("Error:");
							printSearchUsage();
						} else {
							String searchBy = criteria[1];
							String keyword = criteria[2].replaceAll("\"", "");
							
							if(searchBy.equalsIgnoreCase("-e")) {
								result = s.searchByTitle(keyword, true);
								printResult("Keyword -> title (exact) = " + keyword, result);
							} else if(searchBy.equalsIgnoreCase("-a")) {
								result = s.searchByTitle(keyword, false);
								printResult("Keyword -> title (approximate) = " + keyword, result);
							} else if(searchBy.equalsIgnoreCase("-t")) {
								result = s.searchByTag(keyword.replaceAll("\"", ""));
								printResult("Keyword -> tag = " + keyword, result);
							} else if(searchBy.equalsIgnoreCase("-y")) {
								try {
									result = s.searchByYear(Integer.parseInt(keyword));
									printResult("Keyword -> year = " + keyword, result);
								} catch(Exception e) {
									e.printStackTrace();
									System.out.println("Error: See Usage");
									printSearchUsage();
								}
							} else {
								System.out.println("Error: See Usage");
								printSearchUsage();
							}
							
						} 
						break;
					case 'S':	
						String[] keys = opt.split(" ");
						if(keys.length < 3 || keys.length%2 == 0) {	// the total number of parameters should >= 3 and must be odd number 
							System.out.println("Error: See Usage");
							printSearchUsage();
						} else {
							int numOfCriteria = (keys.length - 1)/2;
							String title = null, tag = null;
							int year = -1;
							for(int i = 0; i < numOfCriteria; i++) {
								if(keys[1 + (i*2)].equalsIgnoreCase("-a")) {
									title = keys[2 + (i*2)].replaceAll("\"", "");
								} else if(keys[1 + (i*2)].equalsIgnoreCase("-t")) {
									tag = keys[2 + (i*2)];
								} else if(keys[1 + (i*2)].equalsIgnoreCase("-y")) {
									try {
										year = Integer.parseInt(keys[2 + (i*2)]);
									}  catch(Exception e) {
										e.printStackTrace();
										System.out.println("Error: See Usage");
										printSearchUsage();
									}
								}
							}
							String msg = "Keywords ->";
							if(title != null)
								msg += " ,title = " + title;
							if(tag != null)
								msg += " ,tag = " + tag;
							if(year != -1)
								msg += " ,year = " + year;
							result = s.advanceSearch(title, tag, year);
							printResult(msg, result);
						} 
						break;
					case 'q':
						System.out.println("Thank you for using Moogle. Bye!");
						done = true;
						break;
					default:
						System.out.println("Error: Incorrec option, please see usage");
						break;
				}
				if(!done)
					System.out.println(nextUsage);
			}
		}
	}
	public static void printResult(String msg, List<Movie> result) {
		System.out.println("********************* RESULTS ********************");
		// at most 20 movies will be displayed
		for(int i = 0; i < result.size() && i < DISPLAY; i++) {
			System.out.println(result.get(i));
		}
		System.out.println(msg);
		System.out.println(result.size() + " movie(s) found");
	}
	
	public static void printSearchUsage() {
		String searchUsage = "Search by title (exact matching): s -e <title> -> ex. s -e \"stand up guys\" \n"
							+ "Search by title (approximate matching): s -a <title> -> ex. s -a \"stand\" \n"
							+ "Search movies by tag: s -t <tag> -> ex. s -t Comedy \n"
							+ "Search movies by year: s -y <year> -> ex. s -y 2013 \n"
							
							+ "Advanced search movie: S [-a] [<title>] [-t] [<tag>] [-y] [<year>] -> \n"
							+ "ex. S -a \"all\" -t Sci-Fi \n"
							+ "ex. S -a \"er\" -t Drama -y 2013\n"
							+ "ex. S -a \"er\" -y 2013\n";
		
		System.out.println(searchUsage);
		
	}
}
