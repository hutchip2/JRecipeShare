package Logic;

import javax.swing.ImageIcon;

/**
 * Creates a user object
 * @author Paul Hutchinson
 *
 */
public class User {
	public String UserName;
	public String FirstName;
	public String LastName;
	public String Email;
	public String Description;
	public ImageIcon ProfilePic;

	/**
	 * Creates a user object with the following parameters:
	 * @param username
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param description
	 * @param profilePic 
	 */
	public User(String username, String firstname, String lastname, String email, String description, ImageIcon profilePic) {
		UserName = username;
		FirstName = firstname;
		LastName = lastname;
		Email = email;
		Description = description;
		ProfilePic = profilePic;		
	}

	/**
	 * Queries the database for how many recipes this user has. 
	 * @return number of recipes
	 */
	public int RecepieCount() {	
		int count = 0;
		return count;
	}
}
