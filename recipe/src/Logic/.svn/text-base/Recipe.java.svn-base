package Logic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import javax.swing.ImageIcon;


/**
 * Class for a Recipe object.
 * @author Paul Hutchinson
 * @author Talamihg
 *
 */
public class Recipe {
	public String name;
	public String creator;
	public Timestamp postDate;
	public String steps;
	public ImageIcon picture;
	double calories;
	public int rating;
	public ArrayList<String> ingrediants;
	String[] tags;


	/**
	 * Constructor for a recipe object, takes following parameters:
	 * @param name
	 * @param creator
	 * @param postDate
	 * @param steps
	 * @param picture
	 * @param calories
	 * @param tags
	 */
	public Recipe(String name, String creator, Timestamp postDate, String steps, ImageIcon picture, double calories, String tags) {
		this.name 			= name;
		this.creator 		= creator;				
		this.postDate 		= postDate;
		this.steps 			= steps;
		this.picture 		= picture;
		this.calories 		= calories;
		this.tags 			= tags.split(", ");
		this.rating 		= this.getRating(name, creator);
		this.ingrediants 	= this.getIngredients(name, creator);
	}

	/*
	private ArrayList<String> retreiveRecipeInfo() {
		ArrayList<String> al = new ArrayList<String>();
		Connectdatabase connectdb = new Connectdatabase();
		String sql = "SELECT R.* FROM recipe R, friend F WHERE F.friendLogin = R.userLogin AND F.userLogin = """
		if (connectdb.connect != null) {
			ResultSet result = connectdb.executeselect(sql);
			try {
				while ( result.next() ) {
					// Create the user
					username = result.getString(1);
					email = result.getString(2);
					fname = result.getString(3);
					lname = result.getString(4);
					description = result.getString(5);
					picture = result.getString(7);
				}
			} catch () {

			}
		}
		return al;
	}				
	 */


	/**
	 * Queries the database for ingredients.
	 * @param name - name of the recipe
	 * @param creator - person who made the recipe
	 * @return ArrayList of the ingredients
	 */
	private ArrayList<String> getIngredients(String name, String creator) { 
		Connectdatabase connectdb = new Connectdatabase();
		String sql 	= "SELECT inredientName, measureValue, measureType FROM  ingredientList " +
		"WHERE recipeName = '"+name+"' AND  userLogin = '"+creator+"';";
		ArrayList<String> rtnArray = new ArrayList<String>();		
		if (connectdb.connect != null) {
			ResultSet result = connectdb.executeselect(sql);
			try {
				while ( result.next() ) {
					String newIngredient = Double.toString(result.getDouble("measureValue"));
					newIngredient += " " + result.getString("measureType");
					newIngredient += " " +result.getString("inredientName");
					rtnArray.add(newIngredient);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		connectdb.closeconnection();
		return rtnArray;
	}			


	/**
	 * Queries the database for a recipe rating.
	 * @param name - name of the recipe
	 * @param creator - person who made the recipe
	 * @return average - int of the avg rating
	 */
	private int getRating(String name, String creator) {
		Connectdatabase connectdb = new Connectdatabase();
		String sql 	= "SELECT AVG( R.number ) as rating FROM rating R " +
		"WHERE recipeName = '"+name+"' " +
		"AND recipeCreator = '"+creator+"';";
		int ratingCount = 0;
		int ratingSum = 0;
		if (connectdb.connect != null) {
			try {
				ResultSet result = connectdb.executeselect(sql);
				while ( result.next() ) {
					ratingCount++;
					ratingSum += result.getInt("rating");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
		connectdb.closeconnection();
		return (ratingSum / ratingCount);
	}
}
