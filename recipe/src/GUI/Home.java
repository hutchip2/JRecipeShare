package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import Logic.Connectdatabase;
import Logic.Recipe;
import Logic.User;
import Logic.Main;
import net.miginfocom.swing.MigLayout;

/**
 * Class for the Home page frame.
 * @author Paul Hutchinson
 * @author Talamihg
 */
public class Home extends JFrame implements ActionListener, KeyListener, MouseListener, Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4443260478362126069L;
	RoundButton Home 		= new RoundButton(new ImageIcon("images/other/Home1.png"));
	RoundButton Identity 	= new RoundButton(new ImageIcon("images/other/Identity.png"));
	RoundButton Friends 	= new RoundButton(new ImageIcon("images/other/Friends.png"));
	RoundButton AddRecipe 	= new RoundButton(new ImageIcon("images/other/AddRecipe.png"));
	RoundButton Logout 		= new RoundButton(new ImageIcon("images/other/Logout.png"));
	JPanel outer 			= new JPanel(new BorderLayout());
	JPanel mainPanel 		= new JPanel(new MigLayout());
	JScrollPane pane 		= new JScrollPane(mainPanel);
	ArrayList<RecipeBox> recipeBoxes = new ArrayList<RecipeBox>();
	ArrayList<Recipe> friendsRecipies = new ArrayList<Recipe>();
	ArrayList<Recipe> copy;	
	Cursor WAIT_CURSOR 		= Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
	Cursor DEFAULT_CURSOR 	= Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	private JTextField searchField = new JTextField();
	Highlighter.HighlightPainter painter;
	User thisUser;
	JTextField curTitle = null;
	Boolean containsLast = false;
	ArrayList<Integer> searchIndexesToRemove = new ArrayList<Integer>();
	int StartIndex;
	int indexesSize = 0;
	ArrayList<Integer> startIndexes = new ArrayList<Integer>();
	ArrayList<String> indexTitles = new ArrayList<String>();
	JPanel firstPanel;
	Boolean isFirst = false;

	/**
	 * Constructor for the home frame
	 */
	public Home(User user) {
		setCursor(WAIT_CURSOR);
		this.thisUser = user;
		// Kick back out to the login screen if the given user is null
		if (thisUser == null) {
			dispose();
			new Login();
		}
		painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
		setIconImage(new ImageIcon("images/other/recipetoolbar.png").getImage());
		this.setupUI();
		// Multithreaded adding recipes
		Thread t = new Thread(this, "Boxes");
		t.start();		
	}

	/**
	 * Helper method for setting up the user interface.
	 */
	private void setupUI() {
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pane.getVerticalScrollBar().setUnitIncrement(30);
		outer.add(pane);
		setTitle("Home");
		setBackground(Color.WHITE);
		setVisible(true);
		setSize(new Dimension(1024, 768));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		add(outer, BorderLayout.WEST);
		JPanel bannerPanel = Main.buildBanner(Home, Identity, Friends, AddRecipe, Logout);
		add(bannerPanel, BorderLayout.NORTH);
		Home.addActionListener(this);
		Identity.addActionListener(this);
		Friends.addActionListener(this);
		AddRecipe.addActionListener(this);
		Logout.addActionListener(this);
		outer.setPreferredSize(new Dimension(1010, 768));
		outer.add(searchField, BorderLayout.NORTH);
		searchField.addKeyListener(this);
		searchField.addMouseListener(this);
		searchField.setText("[Search by recipe name]");
		searchField.selectAll();
		// center frame
		Main.centerFrame(this);
	}

	/**
	 * Action listener for the Home frame.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == Identity || e.getSource() == Friends 
				||e.getSource() == AddRecipe || e.getSource() == Logout || e.getSource() == Home)	{
			setCursor(WAIT_CURSOR);
			if (e.getSource() == Home) {
				mainPanel.removeAll();
				friendsRecipies.clear();
				mainPanel.revalidate();
				Thread t = new Thread(this, "Boxes");
				t.start();				
			}			
			if (e.getSource() == Identity)	{
				dispose();
				new Identity(thisUser, null);
			}
			if (e.getSource() == Friends)	{
				dispose();
				new Friends(thisUser);
			}
			if (e.getSource() == AddRecipe)	{
				dispose();
				new CreateRecipe(thisUser);
			}
			if (e.getSource() == Logout)	{
				dispose();
				new Login();
			}
			setCursor(DEFAULT_CURSOR);
		}
	}

	/**
	 * Key listener for the Home frame.
	 */
	public void keyPressed(KeyEvent e) {
		//Listen to searchField and implement highlighter.
		if (e.getSource() == searchField)	{
			if (searchField.getText().isEmpty())	{
				for (int i = 0; i < mainPanel.getComponentCount(); i++)	{
					JPanel currentPanel = (JPanel) mainPanel.getComponent(i);
					JTextField currentTitle = (JTextField) currentPanel.getComponent(0);
					currentTitle.getHighlighter().removeAllHighlights();
				}
				firstPanel = mainPanel;
				pane.getViewport().setViewPosition(firstPanel.getLocation());
				firstPanel.scrollRectToVisible(new Rectangle(0, 0, firstPanel.getWidth(), firstPanel.getHeight()));
			}
			if ( e.getKeyCode() == 8 )	{	//remove all highlights if search is empty
				for (int i = 0; i < searchIndexesToRemove.size(); i++)	{
					JPanel currentPanel = (JPanel) mainPanel.getComponent(searchIndexesToRemove.get(i));
					JTextField currentTitle = (JTextField) currentPanel.getComponent(0);
					currentTitle.getHighlighter().removeAllHighlights();
				}
				if (e.getKeyCode() == 8 && containsLast==true)	{
					try {
						for (int i = 0; i < searchIndexesToRemove.size(); i++)	{
							JPanel currentPanel = (JPanel) mainPanel.getComponent(searchIndexesToRemove.get(i));
							JTextField currentTitle = (JTextField) currentPanel.getComponent(0);
							int startIndex = currentTitle.getText().toLowerCase().indexOf(searchField.getText().toLowerCase());
							int endIndex = startIndex + searchField.getText().length()-1;
							currentTitle.getHighlighter().addHighlight(startIndex, endIndex, painter);
						}
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}
				mainPanel.revalidate();
			} else	{	//if backspace wasn't pressed...
				searchIndexesToRemove = new ArrayList<Integer>();
				for (int i = 0; i < friendsRecipies.size(); i++)	{	// for every recipe on the page
					JPanel currentPanel = (JPanel) mainPanel.getComponent(i);
					JTextField currentTitle = (JTextField) currentPanel.getComponent(0);
					curTitle = currentTitle;
					// if the search field is contained in one of the JTextFields (recipe titles)
					if (friendsRecipies.get(i).name.toLowerCase().contains(searchField.getText().toLowerCase()))	{
						containsLast = true;
						if (searchIndexesToRemove.isEmpty()) { //if this is the first index found, scroll to it
							firstPanel = currentPanel;
							pane.getViewport().setViewPosition(firstPanel.getLocation());
							firstPanel.scrollRectToVisible(new Rectangle(0, 0, firstPanel.getWidth(), firstPanel.getHeight()));
						}
						searchIndexesToRemove.add(i);	//adds the index on the mainPanel of the JTextField containing search
						//assign panel and title
						currentPanel = (JPanel) mainPanel.getComponent(i);
						currentTitle = (JTextField) currentPanel.getComponent(0);
						@SuppressWarnings("unused")
						int oldIndex = StartIndex;
						StartIndex = currentTitle.getText().toLowerCase().indexOf(searchField.getText().toLowerCase());
						int endIndex = StartIndex + searchField.getText().length()+1;
						// if startIndexes and indexTitles don't contain the current index and title...
						if (!startIndexes.contains(StartIndex) && !indexTitles.contains(currentTitle.getText())){
							startIndexes.add(StartIndex);
							indexTitles.add(currentTitle.getText());
						}
						// remove the highlights in the container
						currentTitle.getHighlighter().removeAllHighlights();
						try {
							// highlight recipe title
							currentTitle.getHighlighter().addHighlight(StartIndex, endIndex, painter);
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
						indexesSize = startIndexes.size();
					} else	{
						//if the current field no longer contains the searchField content, then remove highlight and remove from lists
						if (!currentTitle.getText().toLowerCase().contains(searchField.getText().toLowerCase()))	{
							currentTitle.getHighlighter().removeAllHighlights();
						}
					}
				}
			}
		}
		mainPanel.revalidate();
	}

	/**
	 * Key listener for keyReleased.
	 */
	public void keyReleased(KeyEvent e) {
	}

	/**
	 * Key listener for keyTyped.
	 */
	public void keyTyped(KeyEvent arg0) {
	}

	/**
	 * Mouse listener for mouseClicked.
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == searchField){
			searchField.selectAll();
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

	/**
	 * Run method for the addRecipe thread.
	 */
	public void run() {
		this.addRecipies();
	}

	/**
	 * Helper method for adding recipes to the page from the database.
	 */
	private void addRecipies() {
		Connectdatabase connectdb = new Connectdatabase();
		String sql = "SELECT R.* FROM recipe R, friend F WHERE F.friendLogin = R.userLogin AND F.userLogin = '"+thisUser.UserName+"' ORDER BY F.friendLogin ASC LIMIT 0, 10 ";
		if (connectdb.connect != null) {
			ResultSet result = connectdb.executeselect(sql);	
			try {
				while ( result.next() ) {					
					// Filter the results
					InputStream is = result.getBinaryStream("picture_content");
					Image thisPic = ImageIO.read(is);
					ImageIcon recipePic = new ImageIcon(Main.resizeRecipe(thisPic));				
					friendsRecipies.add( new Recipe(result.getString("name"),
							result.getString("userLogin"),
							result.getTimestamp("postDate"),
							result.getString("stepsList"),
							recipePic,
							result.getDouble("totalCalories"),
							result.getString("tags")							
					) );
				}				
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		copy = friendsRecipies;
		int everyOther = 0;
		for ( Recipe thisRecipe : friendsRecipies ) {
			RecipeBox box = new RecipeBox(thisRecipe,thisUser);
			box.setRecipe(thisRecipe);
			box.run();	/*	builds the box in a separate thread	*/
			JPanel thisBox = box.thisBox;
			recipeBoxes.add(box);
			thisBox.setName(thisRecipe.name);
			if (everyOther % 2 == 0) {
				thisBox.setBackground(new Color(245, 245, 245));
			}
			mainPanel.add(thisBox, "width 900, wrap");
			System.out.println("Adding new box to main: " + thisRecipe.name);
			// add title and count to array
			mainPanel.getComponentCount();
			everyOther++;
			mainPanel.revalidate();
		}
		setCursor(DEFAULT_CURSOR);
	}
}
