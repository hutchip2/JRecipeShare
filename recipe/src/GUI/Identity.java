package GUI;

import java.awt.BorderLayout;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import Logic.Connectdatabase;
import Logic.Recipe;
import Logic.User;
import Logic.Main;
import com.mysql.jdbc.PreparedStatement;
import net.miginfocom.swing.MigLayout;

/**
 * Creates the Identity frame
 * @author Paul Hutchinson
 *
 */
public class Identity extends JFrame implements ActionListener, Runnable, MouseListener	 {

	/**
	 * Components
	 */
	private static final long serialVersionUID = -7001833788057310606L;
	JPanel outer 	 = new JPanel(new BorderLayout());
	JPanel mainPanel = new JPanel(new BorderLayout());
	JPanel west 	 = new JPanel(new MigLayout());
	JPanel east 	 = new JPanel(new MigLayout());
	JPanel south 	 = new JPanel(new MigLayout());

	// build jtextfields
	JLabel username  = new JLabel();
	JTextField fname = new JTextField();
	JTextField lname = new JTextField();
	JTextField email = new JTextField();
	JTextArea selfDescription = new JTextArea();
	RoundButton Home = new RoundButton(new ImageIcon("images/other/Home1.png"));
	RoundButton Identity  	= new RoundButton(new ImageIcon("images/other/Identity.png"));
	RoundButton Friends   	= new RoundButton(new ImageIcon("images/other/Friends.png"));
	RoundButton AddRecipe 	= new RoundButton(new ImageIcon("images/other/AddRecipe.png"));
	RoundButton Logout 	  	= new RoundButton(new ImageIcon("images/other/Logout.png"));	
	RoundButton saveProfile = new RoundButton(new ImageIcon("images/other/add_to_database.png"));
	JButton addAsFriend;
	JLabel profilePic;
	//	boolean buttondisabled=false;
	private User thisUser;	/* this will always be the current user */
	private User displayedUser = null;	/* this user will be where we pull */

	Cursor WAIT_CURSOR 	  = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
	Cursor DEFAULT_CURSOR = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

	/**
	 * Constructor for Identity frame
	 */
	public Identity(User user, User displayedUser) {

		this.thisUser = user;
		
		if (displayedUser != null) {
			this.displayedUser = displayedUser;
		} else {
			this.displayedUser = this.thisUser;
		}
		
		if ( ! (this.displayedUser.UserName.equals(this.thisUser.UserName))) {
			disableComponents();
		}

		// Push back to the login screen if the user is null!
		if (this.thisUser == null) {
			dispose();
			new Login();
		}		

		setCursor(WAIT_CURSOR);

		setIconImage(new ImageIcon("images/other/recipetoolbar.png").getImage());

		if (this.displayedUser.UserName.equals(this.thisUser.UserName)) {
			saveProfile.setRolloverEnabled(true);
			saveProfile.setRolloverIcon(new ImageIcon("images/other/rolloveradd_to_database.png"));
			saveProfile.setPressedIcon(new ImageIcon("images/other/selectedadd_to_database.png"));
			saveProfile.setToolTipText("Save Changes...");
			saveProfile.addMouseListener(this);
		}


		this.setupUI();
		//mainPanel.add(searchField, BorderLayout.NORTH);

	}


	/**
	 * Helper method - Setting up graphics
	 */
	private void setupUI() {

		JScrollPane scrollPane = new JScrollPane(mainPanel);

		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		outer.add(scrollPane);

		//setup frame
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Identity");

		//set title for identity panel
		outer.setName("Identity");

		//set backgrounds
		mainPanel.setBackground(Color.WHITE);
		west.setBackground(Color.WHITE);
		east.setBackground(Color.WHITE);
		south.setBackground(Color.WHITE);

		//build panels
		buildWestPanel();
		buildEastPanel();

		//add panels to main panel
		mainPanel.add(west, BorderLayout.WEST);
		mainPanel.add(east, BorderLayout.EAST);
		mainPanel.add(south, BorderLayout.SOUTH);

		//add main panel to jframe
		add(outer, BorderLayout.CENTER);

		//setup jframe
		setVisible(true);
		setSize(new Dimension(1024,768));
		Main.centerFrame(this);

		Home.addActionListener(this);
		Identity.addActionListener(this);
		Friends.addActionListener(this);
		AddRecipe.addActionListener(this);
		Logout.addActionListener(this);

		JPanel bannerPanel = Main.buildBanner(Home, Identity, Friends, AddRecipe, Logout);

		//add banner to jpanel
		add(bannerPanel, BorderLayout.NORTH);		


		// Multithreaded adding recipes
		Thread t = new Thread(this, "Recipies");

		t.start();	
	}


	/**
	 * Builds the east panel
	 */
	private void buildEastPanel() {

		profilePic = new JLabel();
		if (this.displayedUser.UserName.equals(this.thisUser.UserName)) {
			profilePic.addMouseListener(this);
		}

		// Set profile picture
		ImageIcon icon = (displayedUser.ProfilePic == null) ? new ImageIcon("images/other/prof.png") : displayedUser.ProfilePic;


		//  Jlabels n' things 
		JLabel rating = new JLabel();
		ImageIcon Rating = new ImageIcon("images/other/rating0(large).png");
		rating.setIcon(Rating);
		JLabel profilepic = new JLabel("Profile Picture");

		east.add(profilepic, 	"span,wrap");
		profilePic.setIcon(icon);
		east.add(profilePic, 	"span,wrap");
		east.add(rating, 		"wrap");
		
		if ( ! (this.displayedUser.UserName.equals(this.thisUser.UserName))) {
			addAsFriend = new JButton("+ Follow");
			addAsFriend.setToolTipText("Add " + this.displayedUser.FirstName + " as a friend");
			addAsFriend.addActionListener(this);
			east.add(addAsFriend, "wrap");
		}
		

	}


	/**
	 * Builds the west panel
	 */
	private void buildWestPanel() {

		//create a count variable
		int count = getUserRecipeCount();

		//set fields
		username.setText(displayedUser.UserName);
		fname.setText(displayedUser.FirstName);
		lname.setText(displayedUser.LastName);
		email.setText(displayedUser.Email);
		selfDescription.setText(displayedUser.Description);

		//set sizes
		username.setPreferredSize(new Dimension(200, 30));
		fname.setPreferredSize(new Dimension(200, 30));
		lname.setPreferredSize(new Dimension(200, 30));
		email.setPreferredSize(new Dimension(200, 30));
		selfDescription.setPreferredSize(new Dimension(200, 200));
		selfDescription.setLineWrap(true);
		selfDescription.setWrapStyleWord(true);
		selfDescription.setBorder(email.getBorder());
		selfDescription.setBackground(email.getBackground());
		
		// Backgrounds & Borders
		if ( ! (this.displayedUser.UserName.equals(this.thisUser.UserName))) {
			username.setOpaque(false);
			fname.setOpaque(false);
			lname.setOpaque(false);
			email.setOpaque(false);
			selfDescription.setOpaque(false);
			
			username.setBorder(null);
			fname.setBorder(null);
			lname.setBorder(null);
			email.setBorder(null);
			selfDescription.setBorder(null);
			
		}
		
		// build jlabels
		JLabel Username = new JLabel("Username: ");
		JLabel Firstname = new JLabel("First name: ");
		JLabel Lastname = new JLabel("Last name: ");
		JLabel Email = new JLabel("E-mail: ");
		JLabel description = new JLabel("Self Description: ");
		JLabel RecipeCount = new JLabel("You have posted "+count+" recipies!");

		// JLabel sizes
		Font labelFont = new Font("Dialog", Font.PLAIN, 14);
		Username.setFont(labelFont);
		Firstname.setFont(labelFont);
		Lastname.setFont(labelFont);
		Email.setFont(labelFont);
		description.setFont(labelFont);


		west.add(Username);
		west.add(username, 		"wrap");
		west.add(Firstname);
		west.add(fname, 		"wrap");
		west.add(Lastname);
		west.add(lname, 		"wrap");
		west.add(Email, 		"gap unrelated");
		west.add(email, 		"wrap");
		west.add(description);
		west.add(selfDescription, "span");
		west.add(RecipeCount,	"wrap");
		if (this.displayedUser.UserName.equals(this.thisUser.UserName)) {
			west.add(saveProfile);
			saveProfile.addActionListener(this);
		}
	}

	/**
	 * Builds the south panel
	 */
	private void buildSouthPanel() {

		JLabel MyRecipes = new JLabel();
		if (this.displayedUser.UserName.equals(this.thisUser.UserName)) {
			 MyRecipes.setText("My Recipes");
		} else {
			 MyRecipes.setText(displayedUser.FirstName + "'s Recipes");
		}
		MyRecipes.setFont(new Font("Candara", Font.PLAIN, 36));
		south.add(MyRecipes, "span, wrap");
		// 
		Connectdatabase connectdb = new Connectdatabase();
		String sql = "SELECT R.* FROM recipe R WHERE R.userLogin = '"+displayedUser.UserName+"'";
		if (connectdb.connect != null) {
			ArrayList<Recipe> recipes = new ArrayList<Recipe>();
			ResultSet result = connectdb.executeselect(sql);
			try {
				while ( result.next() ) {	
					InputStream is = result.getBinaryStream("picture_content");
					Image thisPic = ImageIO.read(is);
					ImageIcon recipePic = new ImageIcon(Main.resizeRecipe(thisPic));
					recipes.add( new Recipe(result.getString("name"),
							result.getString("userLogin"),
							result.getTimestamp("postDate"),
							result.getString("stepsList"),
							recipePic,
							result.getDouble("totalCalories"),
							result.getString("tags")							
							)  );
				}				
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (Recipe recipe : recipes) {
				RecipeBox box = new RecipeBox(recipe, thisUser);
				box.setRecipe(recipe);
				box.run();	/*	builds the box in a separate thread	*/
				JPanel thisBox = box.thisBox;
				//mainPanel.add(thisBox, "width 900, wrap");
				// add title and count to array
				mainPanel.getComponentCount();
				south.add(thisBox, "width 900:900:1024, wrap");
				mainPanel.revalidate();
			}		
		}
		setCursor(DEFAULT_CURSOR);
	}

	/**
	 * Queries the DB to see how many recipes he/she posted
	 * @return - int
	 */
	private int getUserRecipeCount() {
		int rtnVal = 0;
		String sql = "SELECT count(distinct R.name) as count FROM recipe R WHERE R.userLogin = '"+displayedUser.UserName+"'";
		Connectdatabase connectdb = new Connectdatabase();
		if (connectdb.connect != null) {
			ResultSet result = connectdb.executeselect(sql);
			try {
				while ( result.next() ) {
					rtnVal = result.getInt("count");					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			connectdb.closeconnection();		
		}
		return rtnVal;
	}

	/**
	 * Save fields 
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == Home || e.getSource() == Friends 
				||e.getSource() == AddRecipe || e.getSource() == Logout || e.getSource() == Identity) {

			setCursor(WAIT_CURSOR);

			if (e.getSource() == Home)	{
				dispose();
				new Home(thisUser);
			}
			if (e.getSource() == Identity)	{
				if (this.thisUser.UserName.equals(this.displayedUser.UserName)) {
					south.removeAll();
					south.revalidate();
					Thread t = new Thread(this, "Boxes");
					t.start();
				} else {
					dispose();
					new Identity(thisUser, null);
				}
			}
			if (e.getSource() == Friends) {
				dispose();
				new Friends(thisUser);
			}
			if (e.getSource() == AddRecipe)	{
				dispose();
				new CreateRecipe(thisUser);
			}
			if (e.getSource() == Logout) {
				dispose();
				new Login();
			}
			setCursor(DEFAULT_CURSOR);
		}
		
		if (e.getSource() == addAsFriend) {
			if (this.addFriend(this.displayedUser.UserName)) {
				JOptionPane.showMessageDialog(null, "Friended!");
			}
		}

	}
	
	/**
	 * Will add as friend 
	 * @param friendUsername
	 * @return
	 */
	private boolean addFriend(String friendUsername) {
		
		String sql = "INSERT INTO friend (userLogin,userEmail,friendLogin) VALUES ('"+this.thisUser.UserName+"', '"+
				this.thisUser.Email+"', '"+this.displayedUser.UserName+"');";
		
		Connectdatabase connectdb = new Connectdatabase();
		
		if (connectdb.connect != null) {
			return connectdb.executeInsert(sql);
		}
		
		return false;
		
	}
	
	/**
	 * Saves the user's profile
	 */
	private void saveProfile() {
		
		String Email 	 = (email.getText().trim().equalsIgnoreCase(thisUser.Email)) ? null : email.getText().toLowerCase().trim();
		String FirstName = (fname.getText().equals(thisUser.FirstName)) ? null : fname.getText();
		String LastName  = (lname.getText().equals(thisUser.LastName)) ? null : lname.getText();
		String SelfDesc  = (selfDescription.getText().equals(thisUser.Description)) ? null : selfDescription.getText();
		String sqlUpdate = "UPDATE recipe.user ";
		String sqlSet	 = "SET ";
		String sqlWhere  = "WHERE user.login = '"+thisUser.UserName+"' AND user.email = '"+thisUser.Email+"';";
		Boolean set 	 = false;
		if (Email != null) {
			sqlSet += "email = '"+Email+"' ";
			set = true;
		}
		if (FirstName != null) {
			if (Email != null) { sqlSet += ", ";}
			sqlSet += "fname = '"+FirstName+"' ";
			set = true;
		}
		if (LastName != null) {
			if (Email != null || FirstName != null) { sqlSet += ", ";}
			sqlSet += "lname = '"+LastName+"' ";
			set = true;
		}
		if (SelfDesc != null) {
			if (Email != null || FirstName != null || LastName != null) { sqlSet += ", ";}
			sqlSet += "description = '"+SelfDesc+"' ";
			set = true;
		}
		if (set) {
			String finalSql = sqlUpdate + sqlSet + sqlWhere;
			Connectdatabase connectdb = new Connectdatabase();
			if (connectdb.executeInsert(finalSql)) {
				JOptionPane.showMessageDialog(null, "Saved!");

				if (Email != null) {
					thisUser.Email = Email;
				}
				if (FirstName != null) {
					thisUser.FirstName = FirstName;
				}
				if (LastName != null) {
					thisUser.LastName = LastName;
				}
				if (SelfDesc != null) {
					thisUser.Description = SelfDesc;
				}
			} else {
				JOptionPane.showMessageDialog(null, "Error saving");
			}				
		}	
	}
	

	/**
	 * Mouse listener for banner
	 */
	public void mouseClicked(MouseEvent e) {

		if (e.getSource() == saveProfile) {
			this.saveProfile();
		}
		
		if (e.getSource() == profilePic) {

			JFileChooser chooser = new JFileChooser();
			int option = chooser.showOpenDialog(this);

			if (option == 0) {

				try {

					File myImage = chooser.getSelectedFile();

					Image img = ImageIO.read(myImage);

					img = Main.resizeProfile(img);				

					BufferedImage resized = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);

					Graphics2D g2 = resized.createGraphics();

					g2.drawImage(img, null, null);

					ImageIO.write(resized, "jpg", new File("resizedImg.jpg"));

					myImage = new File( "resizedImg.jpg" );

					/**
					 * JDBC to save the user's new profile picture
					 */

					Connectdatabase connectdb = new Connectdatabase();

					String sql = "UPDATE recipe.user SET avatar_content = ? " +
							"WHERE login = ? AND  email = ?";

					PreparedStatement psmnt = (PreparedStatement) connectdb.connect.prepareStatement(sql);

					FileInputStream fis = new FileInputStream("resizedImg.jpg");

					psmnt.setBinaryStream(1, (InputStream)fis, (int)(myImage.length()));

					psmnt.setString(2, thisUser.UserName);

					psmnt.setString(3, thisUser.Email);

					int s = psmnt.executeUpdate();
					if(s>0) {
						System.out.println("Uploaded successfully !");
						thisUser.ProfilePic = new ImageIcon(img);
						profilePic.setIcon(thisUser.ProfilePic);
						outer.revalidate();
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

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	private void disableComponents()
	{
		fname.setEditable(false) ;
		lname .setEditable(false);
		email .setEditable(false);
		selfDescription .setEditable(false);
	}
	public void run() {
		this.buildSouthPanel();
	}	

}
