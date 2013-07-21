package Logic;

import java.awt.Image;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

import GUI.RecipeBox;

/**
 * Class for searching the database.
 * @author
 *
 */
public class SearchDatabase {
	String tag = null;
	String keyword = null;
	String username,recipeUsername, fname, lname, email, description,picture;
	JPanel panel;
	ImageIcon img;
	ArrayList <Recipe> searchRecipies = null;
	ArrayList<User> searchUserList = null;
	ArrayList<RecipeBox> recipeBoxes = null;

	/**
	 * Constructor for the SearchDatabase class.
	 * @param word
	 * @param tags
	 */
	public SearchDatabase(String word, String tags)	{
		keyword = word;
		tag = tags;
		searchRecipies = new ArrayList<Recipe>();
		searchUserList = new ArrayList<User>();
	}
	/*public ArrayList<RecipeBox> searchforIngredient(String ingredientname)
	{
		Connectdatabase connect = new Connectdatabase();
		ArrayList<String> resultList = new ArrayList<String>();
		String searchIngredient = "select recipeName from ingredient where ingredientName= '"+keyword+"'";
		ResultSet result = connect.executeselect(searchIngredient);
		if(result!=null)
		{
			try
			{
				while(result.next())
				{
					resultList.add(result.getString("recipeName"));
				}
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			connect.closeconnection();
		}
	}*/

	/**
	 * Searches the database for a recipe.
	 * @return RecipeBoxes that match the queried recipe.
	 */
	public ArrayList<Recipe> searchforRecipe(String keyword) {
		
		ArrayList<Recipe> recipeBoxes = new ArrayList<Recipe>();

		Connectdatabase connect = new Connectdatabase();
		panel = new JPanel();
		String searchRecipe = "select * from recipe where name LIKE '%"+keyword+"%';";
		ResultSet result = connect.executeselect(searchRecipe);
		try	{
			while(result.next()) {

				recipeUsername = result.getString("userLogin");
//				Recipe(String name, String creator, Timestamp postDate, String steps, ImageIcon picture, double calories, String tags)
			}				
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		connect.closeconnection();


		
		return recipeBoxes;	
	}

	/**
	 * Finds a user in the database.
	 * @param usrtag string that is either a username, a first name, a last name or email;
	 * @return User
	 */
	public ArrayList<User> finduser(String usrtag) {		
		if ((!usrtag.equalsIgnoreCase("")) && usrtag != null) {		
			Connectdatabase connect = new Connectdatabase();
			String SqlUser = "SELECT * FROM user WHERE login LIKE '%"+ usrtag +"%' or fname LIKE '%"+usrtag+"%' or lname LIKE '%"+usrtag+
			"%' or email LIKE '%" +usrtag+"%';";
			try	{
				ResultSet  usrResult = connect.executeselect(SqlUser);
				if(usrResult!=null)	{
					while(usrResult.next())	{
						username = usrResult.getString(1);
						email = usrResult.getString(2);
						fname = usrResult.getString(3);
						lname = usrResult.getString(4);
						description = usrResult.getString(5);
						picture = usrResult.getString(7);
						InputStream avatarIcon = usrResult.getBinaryStream("avatar_content");
						Image myPic = ImageIO.read(avatarIcon);
						ImageIcon icon = new ImageIcon(Main.resizeAvatarIcon(myPic));
						
						User newUser = new User(username, fname, lname, email, description, icon);
						searchUserList.add(newUser);
					}
				}
			} catch (Exception e)	{
				e.printStackTrace();
			} 			
			connect.closeconnection();
		}
		return searchUserList;
	}
}
