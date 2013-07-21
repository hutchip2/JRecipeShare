package GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import net.miginfocom.swing.MigLayout;
import Logic.AddComment;
import Logic.Connectdatabase;
import Logic.Recipe;
import Logic.User;
import Logic.Main;

import com.mysql.jdbc.PreparedStatement;

/**
 * Public class for creating recipe "boxes".
 * Use by creating a new object and call buildBox, by passing that method a recipe object
 *  - Used on homepage
 *  - Used on identity page
 * @author talamihg
 *
 */
public class RecipeBox extends JFrame implements ActionListener, Runnable, MouseListener {	
	private static final long serialVersionUID = -5085661527035756630L;
	private JPanel 	recipeBox;	
	Highlighter 	highlight;
	JButton 		commentButton;
	JButton 		editButton;
	JButton 		saveButton;
	JButton			sharingButton;
	JPanel 			thisBox;
	JLabel 			CreatorName;
	JTextField 		RecipeName;
	JTextArea		Comments;
	JTextArea 		Ingredients;
	JTextArea 		Steps;
	JPanel			AddComment;
	JLabel 			Rating;
	JLabel 			RecipePic;
	private Recipe 	thisrecipe;
	private User 	thisUser;
	private Boolean isEditable = false;
	private Boolean editing = false;
	Cursor WAIT_CURSOR 		= Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
	Cursor DEFAULT_CURSOR 	= Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

	/**
	 * Constructor for a RecipeBox.
	 * @param recipe
	 */
	public RecipeBox() {
		highlight = new DefaultHighlighter();
	}

	/**
	 * Overloading the recipeBox constructor.
	 */
	public RecipeBox(Recipe rcp, User usr)	{
		this();
		thisrecipe  = rcp;
		thisUser 	= usr;
		if (thisrecipe.creator.equals(thisUser.UserName)) {
			isEditable = true;
		}
	}

	/**
	 * Sets a recipe.
	 * @param recipe to set
	 */
	public void setRecipe(Recipe recipe) {
		this.thisrecipe = recipe;
	}

	/**
	 * Constructs a defined box for the scroll pane.
	 */
	private JPanel buildBox(Recipe recipe) {
		recipeBox = new JPanel(new MigLayout());
		recipeBox.setBackground(Color.WHITE);
		// Build Components
		RecipeName 		 = this.getRecipeName(recipe);
		RecipePic 		 = this.getRecipePic(recipe);
		CreatorName  	 = new JLabel("Posted By: "+ recipe.creator);
		Ingredients 	 = this.getIngredients(recipe);
		Steps 			 = this.getSteps(recipe);
		JLabel PostDate	 = this.getPostDate(recipe);
		CreatorName.setPreferredSize(new Dimension(200, 10));
		PostDate.setPreferredSize(new Dimension(200, 10));
		Comments 		 = this.getComments(recipe);
		JLabel Avatar 	 = this.getCreatorsAvatar(recipe);
		Rating 	 		 = this.getRating(recipe);
		commentButton    = new JButton("comment");
		AddComment 		 = this.buildCommentBox(recipe);
		if (thisrecipe.creator.equals(thisUser.UserName)) {
			editButton = new JButton("edit");
			saveButton = new JButton("save");
			sharingButton = new JButton("sharing");
			sharingButton.addActionListener(this);
			editButton.addActionListener(this);
			if (editing) {
				saveButton.addActionListener(this);
			}
		}
		// Install highlighters
		highlight.install(Steps);
		highlight.install(Ingredients);
		highlight.install(Comments);
		highlight.install(RecipeName);
		commentButton.addActionListener(this);		
		RecipeName.setFont(new Font("Candara", Font.PLAIN, 30));
		// build divider
		JLabel Divider = new JLabel();
		ImageIcon blackLine = new ImageIcon("images/other/blackLine.png");
		Divider.setIcon(blackLine);	
		RecipePic.setToolTipText(recipe.name);
		recipeBox.add(RecipeName, 	"cell 0 0, width 500:700:800");
		recipeBox.add(RecipePic, 	"cell 1 0");
		JPanel whoami = new JPanel(new MigLayout());
		whoami.setOpaque(false);
		whoami.add(Avatar, 			 "dock west, wrap");
		whoami.add(CreatorName, 	 "wrap, pad 0 5 0 10");	/* padding: top left bottom right */
		whoami.add(PostDate, 		 "pad 0 5 0 10");		
		recipeBox.add(whoami, 		 "cell 1 1, wrap");		
		recipeBox.add(Ingredients, 	 "cell 0 3, width 500:900:1024, span, wrap");
		recipeBox.add(Steps, 		 "cell 0 4, width 500:900:1024, span, wrap");
		recipeBox.add(Rating, 		 "cell 0 5, span, wrap");
		recipeBox.add(Comments, 	 "cell 0 6, width 500:900:1024, span, wrap");
		recipeBox.add(commentButton, "cell 0 7, left, span, wrap");
		if (isEditable) {
			recipeBox.add(editButton, "cell 0 7, right");
			recipeBox.add(sharingButton, "cell 0 7, right");
		}
		recipeBox.add(Divider, 		 "cell 0 9, span, wrap");
		return recipeBox;
	}

	/**
	 * Helper method for getting a recipe's picture.
	 * @param recipe
	 * @return JLabel containing the recipe picture.
	 */
	private JLabel getRecipePic(Recipe recipe) {
		JLabel RecipePic = new JLabel();
		ImageIcon picRecipe = (recipe.picture == null) ? new ImageIcon("images/other/prof.png") : recipe.picture;
		RecipePic.setIcon(picRecipe);
		RecipePic.setOpaque(false);
		return RecipePic;
	}

	/**
	 * Helper method for getting a recipe's name.
	 * @param recipe
	 * @return JLabel
	 */
	private JTextField getRecipeName(Recipe recipe) {
		JTextField RecipeName = new JTextField(recipe.name);
		RecipeName.setFont(new Font("Dialog",Font.PLAIN, 30));
		RecipeName.setBackground(Color.WHITE);
		RecipeName.setEditable(false);
		RecipeName.setBorder(null);
		RecipeName.setOpaque(false);
		return RecipeName;
	}

	/**
	 * Helper method for getting a recipe's ingredients.
	 * @param recipe
	 * @return JTextArea
	 */
	private JTextArea getIngredients(Recipe recipe) {
		String ingreds = "Ingrediants:\n     ";
		for (String ingred : recipe.ingrediants) {
			ingreds += ingred;
			ingreds += "\n     ";
		}
		JTextArea Ingredients = new JTextArea(ingreds);
		Ingredients.setEditable(false);
		Ingredients.setLineWrap(true);
		RecipeName.setBackground(new Color(202, 225, 255));		
		Ingredients.setOpaque(false);		
		return Ingredients;
	}	

	/**
	 * Helper method for getting a recipe's directions.
	 * @return JTextArea
	 */
	private JTextArea getSteps(Recipe recipe) {
		JTextArea Steps = new JTextArea(recipe.steps + "\n\n");	
		Steps.setLineWrap(true);
		Steps.setWrapStyleWord(true);
		RecipeName.setBackground(new Color(202, 225, 255));
		Steps.setOpaque(false);
		Steps.setEditable(false);
		return Steps;
	}

	/**
	 * Helper method for getting a recipe's post date.
	 * @param recipe
	 * @return JLabel of the post date.
	 */
	private JLabel getPostDate(Recipe recipe) {
		Calendar cal = new GregorianCalendar();
		Timestamp postDate = recipe.postDate;
		cal.setTimeInMillis(postDate.getTime());
		String date = cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR) + 
		" " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + " " + ( (cal.get(Calendar.AM_PM)==0) ? "AM" : "PM")
		+ "\n\n";

		JLabel rtnDate = new JLabel(date);
		RecipeName.setBackground(new Color(202, 225, 255));
		rtnDate.setOpaque(false);
		return rtnDate;
	}

	/**
	 * Helper method for getting a recipe's comments.
	 * @param recipe
	 * @return JTextArea of the recipe's comments.
	 */
	private JTextArea getComments(Recipe recipe) {
		Connectdatabase connectdb = new Connectdatabase();
		String sql = "SELECT userLogin, comment, postDate FROM comment WHERE " +
		"recipeName = '"+recipe.name+"' AND recipeCreator = '"+recipe.creator+"';";
		String com = "Comments: \n\n     "; 		
		if (connectdb.connect != null) {
			ResultSet result = connectdb.executeselect(sql);
			try {
				Calendar cal = new GregorianCalendar();	
				while ( result.next() ) {
					Timestamp postDate = result.getTimestamp("postDate");
					cal.setTimeInMillis(postDate.getTime());
					cal.get(Calendar.MONTH);					
					com += result.getString("userLogin") + ": " +  cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH) + 
					"/" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) +
					" " + ( (cal.get(Calendar.AM_PM)==0) ? "AM" : "PM");
					com += "\n          ";
					com += result.getString("comment");
					com += "\n\n     ";
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		JTextArea Comments = new JTextArea(com);
		Comments.setEditable(false);
		Comments.setLineWrap(true);
		Comments.setWrapStyleWord(true);			
		Comments.setOpaque(false);		
		return Comments;
	}

	/**
	 * Connects to the database to retrieve the avatar of the recipe creator.
	 * Might be good if these methods could be called asynchronously!
	 * @param creator - String of the recipe creator's name
	 * @return ImageIcon of the person's avatar
	 */
	private JLabel getCreatorsAvatar(Recipe recipe) {
		Image avatar = null;
		Connectdatabase connectdb = new Connectdatabase();
		String sql = "SELECT avatar_content FROM user WHERE login = '"+recipe.creator+"';";
		if (connectdb.connect != null) {
			ResultSet result = connectdb.executeselect(sql);
			try {				
				while ( result.next() ) {
					InputStream blob = result.getBinaryStream("avatar_content");
					avatar = ImageIO.read(blob);
					avatar = Main.resizeAvatarIcon(avatar);										
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			connectdb.closeconnection();			
		}
		if (avatar ==  null) {
			try {
				avatar = ImageIO.read(new File ("images/other/avatar.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		JLabel rtnLabel = new JLabel();
		rtnLabel.setIcon(new ImageIcon(avatar));
		rtnLabel.setToolTipText(recipe.creator);
		rtnLabel.setOpaque(false);
		return rtnLabel;
	}	

	/**
	 * Helper method for getting recipe's rating.
	 * @param recipe
	 * @return JLabel containing the rating.
	 */
	private JLabel getRating(Recipe recipe) {
		int thisRating = recipe.rating;
		thisRating = (thisRating > 5) ? thisRating = 5 : thisRating;
		thisRating = (thisRating < 0) ? thisRating = 0 : thisRating;
		String ratingFile	 = "images/other/rating" + thisRating + ".png";
		ImageIcon ratingIcon = new ImageIcon(ratingFile);
		JLabel Rating = new JLabel();
		Rating.setIcon(ratingIcon);
		Rating.setToolTipText(Integer.toString(thisRating));
		Rating.setOpaque(false);
		return Rating;
	}

	/**
	 * Helper method for adding a comment box.
	 * @param recipe
	 * @return JPanel of the comment box.
	 */
	private JPanel buildCommentBox(Recipe recipe) {
		AddComment commentBox = new AddComment(recipe, thisUser, this);	
		return commentBox.getCommentPane();
	}

	/**
	 * Refreshes the comments box.
	 */
	public void refreshComments() {	
		recipeBox.remove(Comments);
		Comments = null;
		recipeBox.revalidate();	
		Comments = this.getComments(thisrecipe);
		recipeBox.add(Comments, 	"cell 0 6, width 500:900:1024, span, wrap");		
		recipeBox.revalidate();
	}

	/**
	 * Refreshes the rating stars.
	 */
	public void refreshRating() {
		recipeBox.remove(Rating);
		recipeBox.revalidate();
		Rating = this.getRating(thisrecipe);
		recipeBox.add(Rating, 		"cell 0 5, span, wrap");
		recipeBox.revalidate();
	}

	/**
	 * Execute the action of a JButton.
	 * @param fkButton
	 */
	private void executeAction(JButton fkButton)	{
		this.showHideCommentsBox();
	}

	/**
	 * Shows or hides the comment box.
	 */
	public void showHideCommentsBox() {
		if (recipeBox.isAncestorOf(AddComment)) {
			recipeBox.remove(AddComment);
		} else {
			recipeBox.add(AddComment, "cell 0 8, span, wrap");
		}
		recipeBox.revalidate();
	}

	/**
	 * Set whether or not you want to edit the fields in RecipeBox.
	 */
	private void setEditable() {
		Boolean toSet = !editing;
		RecipeName.setEditable(toSet);
		Steps.setEditable(toSet);
		Color ec;
		if (toSet) {
			ec = new Color(202, 225, 255);			
			Color brd = new Color(53, 230, 59);
			RecipeName.setBorder(BorderFactory.createLineBorder (brd));
			Steps.setBorder(BorderFactory.createLineBorder (brd));
			RecipePic.setBorder(BorderFactory.createLineBorder (brd));
			saveButton.addActionListener(this);
			RecipePic.addMouseListener(this);
			recipeBox.add(saveButton, "cell 0 7, right");
		} else {
			ec = Color.WHITE;
			RecipeName.setBorder(null);
			Steps.setBorder(null);
			RecipePic.setBorder(null);
			saveButton.removeActionListener(this);
			RecipePic.removeMouseListener(this);
			recipeBox.remove(saveButton);
		}
		RecipeName.setOpaque(toSet);
		Steps.setOpaque(toSet);
		RecipeName.setBackground(ec);
		Steps.setBackground(ec);
		editing = !editing;
		recipeBox.revalidate();
		System.out.println("Set editable: " + editing);
	}

	/**
	 * Save the user's changes to his recipe.
	 */
	private void saveChanges() {
		Connectdatabase connectdb = new Connectdatabase();
		String sql = "UPDATE recipe.recipe SET name = ?, stepsList = ? WHERE userLogin = ? AND name = ?";
		try {
			PreparedStatement prsmnt = (PreparedStatement) connectdb.connect.prepareStatement(sql);		
			prsmnt.setString(1, RecipeName.getText().trim());
			prsmnt.setString(2, Steps.getText());
			prsmnt.setString(3, thisrecipe.creator);
			prsmnt.setString(4, thisrecipe.name);
			int s = prsmnt.executeUpdate();
			if (s > 0) {
				System.out.println("Changes saved!");
			} else  {
				System.out.println("Changes not saved");
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Action listener for the RecipeBox.
	 */
	public void actionPerformed(ActionEvent evt)	{
		if (evt.getSource() == commentButton)	{
			JButton fakebutton = (JButton) evt.getSource();
			executeAction(fakebutton);
		}
		if (evt.getSource() == editButton)	{
			this.setEditable();
		}
		if (evt.getSource() == saveButton)	{
			this.saveChanges();
		}
		if (evt.getSource() == sharingButton)	{
			Object[] possibleValues = { "Global", "Friends Only", "Private" };
			Object selectedValue = JOptionPane.showInputDialog(null,
			"Choose a sharing setting for this recipe: ", "Input",
			JOptionPane.INFORMATION_MESSAGE, null,
			possibleValues, possibleValues[0]);
			
			if (selectedValue == null)	{
				selectedValue = new String("");
			}
			if (selectedValue.toString().equals("Global"))	{
				// set visible to everyone
			}
			if (selectedValue.toString().equals("Friends Only"))	{
				// set visible to friends only
			}
			if (selectedValue.toString().equals("Private"))	{
				// set visible only to the poster
			}
		}
	}

	/**
	 * Run method for the thread.
	 */
	public void run() {
		System.out.println("Filling in box: " + thisrecipe.name);
		thisBox = this.buildBox(thisrecipe);
	}

	/**
	 * Mouse listener for mouseClicked.
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == RecipePic) {
			JFileChooser chooser = new JFileChooser();
			int option = chooser.showOpenDialog(this);
			if (option == 0) {
				try {
					File myImage = chooser.getSelectedFile();
					Image img = ImageIO.read(myImage);
					img = Main.resizeRecipe(img);				
					BufferedImage resized = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
					Graphics2D g2 = resized.createGraphics();
					g2.drawImage(img, null, null);				
					ImageIO.write(resized, "jpg", new File("resizedImg.jpg"));
					myImage = new File( "resizedImg.jpg" );
					Connectdatabase connectdb = new Connectdatabase();
					String sql = "UPDATE recipe.recipe SET picture_content = ? " +
					"WHERE userLogin = ? AND  name = ?";
					PreparedStatement psmnt = (PreparedStatement) connectdb.connect.prepareStatement(sql);
					FileInputStream fis = new FileInputStream("resizedImg.jpg");
					psmnt.setBinaryStream(1, (InputStream)fis, (int)(myImage.length()));
					psmnt.setString(2, thisrecipe.creator);
					psmnt.setString(3, thisrecipe.name);
					int s = psmnt.executeUpdate();
					if(s > 0) {
						System.out.println("Uploaded successfully !");
						ImageIcon newImg = new ImageIcon(img);
						thisrecipe.picture = newImg;
						RecipePic.setIcon(newImg);
						recipeBox.revalidate();
					} else {
						System.out.println("unsucessfull to upload image.");
					}
				} catch (SQLException ea) {
					ea.printStackTrace();
				} catch (IOException ee) {
					ee.printStackTrace();
				}
			}
		}		
	}

	/**
	 * Mouse listener for mouseEntered.
	 */
	public void mouseEntered(MouseEvent arg0) {	
	}

	/**
	 * Mouse listener for mouseExited.
	 */
	public void mouseExited(MouseEvent arg0) {	
	}

	/**
	 * Mouse listener for mousePressed.
	 */
	public void mousePressed(MouseEvent arg0) {	
	}

	/**
	 * Mouse listener for mouseReleased.
	 */
	public void mouseReleased(MouseEvent arg0) {	
	}	
}
