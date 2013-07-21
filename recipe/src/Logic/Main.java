package Logic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import GUI.Login;
import GUI.RoundButton;
import GUI.SysTray;

/**
 * Main method for the recipe program
 * @author Paul Hutchinson
 *
 */
public class Main {

	/**
	 * Initial launch for the program
	 * @param args
	 */	
	public static void main(String args[])	{
		new Login();
		new SysTray();
	}

	/**
	 * Builds the banner of navigation buttons at the top of each frame.
	 * @param Home
	 * @param Identity
	 * @param Friends
	 * @param AddRecipe
	 * @param Logout
	 * @return the completed jpanel
	 */
	public static JPanel buildBanner(RoundButton Home, RoundButton Identity, RoundButton Friends, RoundButton AddRecipe, RoundButton Logout){
		JPanel bannerPanel  = new JPanel(new BorderLayout());
		JPanel bannerWest	= new JPanel();
		JPanel bannerEast	= new JPanel();
		bannerPanel.add(bannerWest, BorderLayout.WEST);
		bannerPanel.add(bannerEast, BorderLayout.EAST);
		Home.setRolloverEnabled(true);
		Home.setRolloverIcon(new ImageIcon("images/other/Home1Rollover.png"));
		Home.setPressedIcon(new ImageIcon("images/other/Home1Selected.png"));
		Home.setToolTipText("Home");
		Identity.setRolloverEnabled(true);
		Identity.setRolloverIcon(new ImageIcon("images/other/IdentityRollover.png"));
		Identity.setPressedIcon(new ImageIcon("images/other/IdentitySelected.png"));
		Identity.setToolTipText("Edit Profile");
		Friends.setRolloverEnabled(true);
		Friends.setRolloverIcon(new ImageIcon("images/other/FriendsRollover.png"));
		Friends.setPressedIcon(new ImageIcon("images/other/FriendsSelected.png"));
		Friends.setToolTipText("Friends");
		AddRecipe.setRolloverEnabled(true);
		AddRecipe.setRolloverIcon(new ImageIcon("images/other/AddRecipeRollover.png"));
		AddRecipe.setPressedIcon(new ImageIcon("images/other/AddRecipeSelected.png"));
		AddRecipe.setToolTipText("Add Recipe");
		Logout.setRolloverEnabled(true);
		Logout.setRolloverIcon(new ImageIcon("images/other/LogoutRollover.png"));
		Logout.setPressedIcon(new ImageIcon("images/other/LogoutSelected.png"));
		Logout.setToolTipText("Logout");
		JLabel homebanner 	= new JLabel();
		bannerEast.add(homebanner);
		bannerEast.setBackground(Color.WHITE);
		ImageIcon HomeBanner = new ImageIcon("images/other/bannerBlankHalf.png");
		homebanner.setIcon(HomeBanner);
		bannerWest.setBackground(Color.WHITE);
		bannerPanel.setBackground(Color.WHITE);
		bannerWest.add(Home);
		bannerWest.add(Identity);
		bannerWest.add(Friends);
		bannerWest.add(AddRecipe);
		bannerWest.add(Logout);
		return bannerPanel;
	}

	/**
	 * Centers the frame, used in other classes
	 * @param frame
	 */
	public static void centerFrame(JFrame frame) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		int x = (screenSize.width - frame.getWidth()) / 2;
		int y = (screenSize.height - frame.getHeight()) / 2;
		frame.setLocation(x, y);
	}

	/**
	 * Runs a profile image through the scaleImage method.
	 * @param image
	 * @return the resized profile image
	 */
	public static Image resizeProfile(Image image) {
		return scaleImage(image, 200, 300);
	}

	/**
	 * Runs a recipe image through the scaleImage method.
	 * @param image
	 * @return the resized recipe image
	 */
	public static Image resizeRecipe(Image image) {		
		return scaleImage(image, 200,200);
	}

	/**
	 * Runs an avatar image through the scaleImage method.
	 * @param image
	 * @return the resized avatar image
	 */
	public static Image resizeAvatarIcon(Image image) {
		return scaleImage(image, 50, 50);		
	}
	
	/**
	 * Returns a User object, looks them up in the database. 
	 * @param usrname - String of the unique username
	 * @return - User object
	 */
	public static User getUser(String usrname) {
		User newUser = null;
		String username 	= "";
		String email 		= "";
		String fname 		= "";
		String lname 		= "";
		String description 	= "";
		ImageIcon img 		= null;
		String sql = "SELECT * FROM user WHERE login = '"+usrname+"'";
		Connectdatabase connectdb = new Connectdatabase();
		if (connectdb.connect != null) {
			ResultSet result = connectdb.executeselect(sql);
			try {
				while ( result.next() ) {
					username = result.getString(1);
					email = result.getString(2);
					fname = result.getString(3);
					lname = result.getString(4);
					description = result.getString(5);
					InputStream binaryStream = result.getBinaryStream(7);
					Image myPic = ImageIO.read(binaryStream);
					myPic = Main.resizeProfile(myPic);	
					img = new ImageIcon(myPic);			
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		newUser = new User(username, fname, lname, email, description, img);
		connectdb.closeconnection();
		return newUser;
	}

	/**
	 *
	 * @param image The image to be scaled
	 * @param imageType Target image type, e.g. TYPE_INT_RGB
	 * @param newWidth The required width
	 * @param newHeight The required width
	 *
	 * @return The scaled image
	 */
	private static Image scaleImage(Image image, int newWidth, int newHeight) {
		if (image == null) {
			image = Toolkit.getDefaultToolkit().getImage("images/other/avatar.png");
		}
		// Make sure the aspect ratio is maintained, so the image is not distorted
		double thumbRatio 	= (double) newWidth / (double) newHeight;
		int imageWidth 		= image.getWidth(null);
		int imageHeight 	= image.getHeight(null);
		double aspectRatio 	= (double) imageWidth / (double) imageHeight;
		if (thumbRatio < aspectRatio) {
			newHeight = (int) (newWidth / aspectRatio);
		} else {
			newWidth = (int) (newHeight * aspectRatio);
		}
		Image newImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
		return newImage;
	}
}