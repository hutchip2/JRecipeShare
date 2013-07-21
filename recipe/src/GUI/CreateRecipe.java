package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import Logic.Connectdatabase;
import Logic.Ingredient;
import Logic.Recipe;
import Logic.User;
import Logic.Main;
import com.mysql.jdbc.PreparedStatement;

/**
 * Class for creating a recipe frame.
 * @author Paul Hutchinson
 * @author Talamihg
 */
public class CreateRecipe extends JFrame implements ActionListener, MouseListener {
	private static final long serialVersionUID = -7193212923564525423L;
	RoundButton saveButton	= new RoundButton(new ImageIcon("images/other/add_to_database.png"));
	JTextField RecipeName 	= new JTextField("<Recipe Name>");
	JPanel Ingredients 		= new JPanel();
	JTextArea Steps 		= new JTextArea("<preparation>");
	JTextArea Tags 			= new JTextArea("<tags: enter one per line>");
	RoundButton Home 		= new RoundButton(new ImageIcon("images/other/Home1.png"));
	RoundButton Identity 	= new RoundButton(new ImageIcon("images/other/Identity.png"));
	RoundButton Friends 	= new RoundButton(new ImageIcon("images/other/Friends.png"));
	RoundButton AddRecipe 	= new RoundButton(new ImageIcon("images/other/AddRecipe.png"));
	RoundButton Logout 		= new RoundButton(new ImageIcon("images/other/Logout.png"));
	JPanel back = new JPanel(new BorderLayout());
	JPanel east = new JPanel(new MigLayout());
	JPanel west = new JPanel(new MigLayout());
	JLabel recipePicture;
	ImageIcon  recipeImage;
	String recipeImagePath = null;
	ArrayList<Recipe> userRecipes = new ArrayList<Recipe>();
	User thisUser;
	IngredientBox ingredientBox = new IngredientBox();
	Cursor WAIT_CURSOR 		= Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
	Cursor DEFAULT_CURSOR 	= Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

	/**
	 * Constructor for creating a recipe page.
	 */
	public CreateRecipe(User newUser) {
		this.thisUser = newUser; 
		// if the user is null, kick them back out to the login page
		if (thisUser == null) {
			dispose();
			new Login();
		}
		setIconImage(new ImageIcon("images/other/recipetoolbar.png").getImage());
		saveButton.setRolloverEnabled(true);
		saveButton.setRolloverIcon(new ImageIcon("images/other/rolloveradd_to_database.png"));
		saveButton.setPressedIcon(new ImageIcon("images/other/selectedadd_to_database.png"));
		saveButton.setToolTipText("Save Recipe...");
		saveButton.addActionListener(this);
		buildWestPanel();
		buildEastPanel();
		//setup frame
		setTitle("Create a recipe...");
		setBackground(Color.WHITE);
		setVisible(true);
		setSize(new Dimension(1024,768));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		Main.centerFrame(this);
		recipePicture.addMouseListener(this);
		add(back);
	}

	/**
	 * Builds the west panel on the createRecipe frame.
	 */
	public void buildWestPanel() {
		JLabel title		= new JLabel("Create a new recipe!");
		recipePicture 		= new JLabel();
		Ingredients 		= this.getIngredientsBox();
		JLabel preparation 	= new JLabel("Preparation: ");
		JLabel tags 		= new JLabel("Tags: ");
		title.setFont(new Font("Candara", Font.PLAIN, 30));
		//set sizes of text fields
		RecipeName.setPreferredSize(new Dimension(400, 50));
		Steps.setPreferredSize(new Dimension(400,100));
		Tags.setPreferredSize(new Dimension(400,100));
		Steps.setWrapStyleWord(true);
		Tags.setWrapStyleWord(true);
		recipeImage = (recipeImage == null) ? new ImageIcon("images/other/recipe.png") : recipeImage;
		recipePicture.setIcon(recipeImage);
		west.setAlignmentY(LEFT_ALIGNMENT);
		RecipeName.setAlignmentY(LEFT_ALIGNMENT);
		//add items to panel
		west.add(title,			"wrap");
		west.add(RecipeName, 	"wrap");
		west.add(recipePicture,	"wrap");
		west.add(Ingredients, 	"wrap");
		west.add(preparation, 	"wrap");
		west.add(Steps,			"wrap");
		west.add(tags, 			"wrap");
		west.add(Tags, 			"wrap");
		west.add(saveButton);
		back.add(west, BorderLayout.WEST);
	}

	/**
	 * Protected helper method, for editing selected ingredients. 
	 * - going to be used by RecipeBox for editing ingredients. 
	 * @return - JPanel
	 */
	protected JPanel getIngredientsBox() {
		return ingredientBox.box;
	}

	/**
	 * Builds the east panel on the create recipe frame.
	 */
	public void buildEastPanel() {
		ImageIcon avatar = (thisUser.ProfilePic == null) ? new ImageIcon("images/other/avatar.png") : thisUser.ProfilePic;
		JLabel Avatar = new JLabel();
		Avatar.setIcon(avatar);	
		east.add(Avatar, "wrap");
		back.add(east, BorderLayout.EAST);
		JPanel bannerPanel = Main.buildBanner(Home, Identity, Friends, AddRecipe, Logout);		
		back.add(bannerPanel, BorderLayout.NORTH);		
		Home.addActionListener(this);
		Identity.addActionListener(this);
		Friends.addActionListener(this);
		AddRecipe.addActionListener(this);
		Logout.addActionListener(this);
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == Home || e.getSource() == Identity || e.getSource() == Friends 
				|| e.getSource() == Logout)	{
			setCursor(WAIT_CURSOR);
			if (e.getSource() == Home)	{
				dispose();
				new Home(thisUser);
			}
			if (e.getSource() == Identity)	{
				dispose();
				new Identity(thisUser, null);
			}
			if (e.getSource() == Friends)	{
				dispose();
				new Friends(thisUser);
			}
			if (e.getSource() == Logout)	{
				dispose();
				new Login();
			}
			setCursor(DEFAULT_CURSOR);
		}
		if (e.getSource() == saveButton)	{
			// format tags
			String[] newTags = Tags.getText().split("\n");
			String myTags = "";
			for (int i=0; i < newTags.length-2; i++) {
				myTags += newTags[i];
				myTags += ", ";
			}
			myTags = newTags[newTags.length-1];
			
			
			Boolean didIt = this.saveRecipe(RecipeName.getText().trim(), 
					thisUser.UserName,
					Steps.getText(),
					myTags,
					recipeImagePath,
					10.0
				);
			if (didIt) {				
				for (Ingredient ingred : ingredientBox.ingredients) {					
					Boolean savedIngred = this.saveIngredients(RecipeName.getText().trim(),
							thisUser.UserName,
							ingred.name,
							ingred.measure,
							ingred.type,
							ingred.nutritionValue);
					
					if (!savedIngred) {
						JOptionPane.showMessageDialog(null, "can't have duplicate ingredients, " + ingred.name);
					}					
				}
				
				JOptionPane.showMessageDialog(null, "Added your recipe!, " + RecipeName.getText().trim());

				dispose();
				new CreateRecipe(thisUser);
			}
			
			
		}
	}

	/**
	 * Connects to the database to save the new recipe.
	 * Helper method
	 * @param recipe
	 */
	private Boolean saveRecipe(String name, String creator, String steps, String tags, String picturePath, Double totalCal)	{
		Connectdatabase connectdb = new Connectdatabase();
		String sql = "INSERT INTO recipe.recipe (name, userLogin, postDate, stepsList, picture_content, totalCalories, tags, Vis) " +
		"VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement psmnt;
		try {
			psmnt = (PreparedStatement) connectdb.connect.prepareStatement(sql);
			psmnt.setString(1, name);		
			psmnt.setString(2, creator);
			Timestamp date = new Timestamp(new Date().getTime());
			psmnt.setTimestamp(3, date);
			psmnt.setString(4, steps);
			File image = new File(picturePath);		
			FileInputStream fis = new FileInputStream(image);		
			psmnt.setBinaryStream(5, (InputStream)fis, (int)(image.length()));
			psmnt.setDouble(6, totalCal);
			psmnt.setString(7, tags);
			psmnt.setInt(8, 0);
			int s = psmnt.executeUpdate();
			if(s > 0) {
				System.out.println("Uploaded successfully !");
			} else {
				System.out.println("unsucessfull to upload image.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Problem added your recipe, " + name);
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Problem added your recipe, " + name);
			return false;
		}		
		return true;		
	}
	
	/**
	 * Helper method to add ingredients, the recipe must already be in the database, so the 
	 * 
	 * @param recipeName
	 * @param userLogin
	 * @param ingredientName
	 * @param measure
	 * @param type
	 * @param nutrition
	 * @return - true if it did it
	 */
	private boolean saveIngredients(String recipeName, String userLogin, String ingredientName, Double measure, String type, Double nutrition) {
		
		Connectdatabase connectdb = new Connectdatabase();
		String sql = "INSERT INTO recipe.ingredientList (recipeName, userLogin, inredientName, measureValue, measureType, nutritionValue) " +
		"VALUES (?, ?, ?, ?, ?, ?);";
		PreparedStatement psmnt;
		try {
			psmnt = (PreparedStatement) connectdb.connect.prepareStatement(sql);
			psmnt.setString(1, recipeName);		
			psmnt.setString(2, userLogin);
			psmnt.setString(3, ingredientName);
			psmnt.setDouble(4, measure);		
			psmnt.setString(5, type);
			psmnt.setDouble(6, nutrition);
			int s = psmnt.executeUpdate();
			if(s > 0) {
				System.out.println("Uploaded successfully !");
				return true;
			} else {
				System.out.println("unsucessfull to upload image.");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Problem added your ingredient, " + ingredientName);
			return false;
		}
	}

	/**
	 * Listener for mouseClicked.
	 */
	public void mouseClicked(MouseEvent e) {
		// Set the Recipe picture for saving
		if (e.getSource() == recipePicture) {
			JFileChooser chooser = new JFileChooser();
			int option = chooser.showOpenDialog(this);
			if (option == 0) {
				File file = chooser.getSelectedFile();
				try {
					ImageIcon icon = new ImageIcon(file.getCanonicalPath());
					recipeImage.setImage(Main.resizeRecipe(icon.getImage()));						
					west.revalidate();
					recipeImagePath = file.getCanonicalPath();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch	(NullPointerException e1)	{
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * Listener for mouseEntered.
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * Listener for mouseExited.
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Listener for mousePressed.
	 */
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * Listener for mouseReleased.
	 */
	public void mouseReleased(MouseEvent e) {
	}
}
