package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.*;

import Logic.Connectdatabase;
import Logic.SearchDatabase;
import Logic.User;
import Logic.Main;

import net.miginfocom.swing.MigLayout;

/**
 * Class for the Friends frame.
 * @author Paul Hutchinson
 *
 */
public class Friends extends JFrame implements ActionListener, MouseListener {
	private static final long serialVersionUID = 3156245691207250865L;
	JPanel outer 		  = new JPanel(new BorderLayout());
	JPanel mainPanel 	  = new JPanel(new MigLayout());
	JLabel friendsbanner  = new JLabel();
	JPanel south 	 	  = new JPanel(new MigLayout());
	RoundButton Home 	  = new RoundButton(new ImageIcon("images/other/Home1.png"));
	RoundButton Identity  = new RoundButton(new ImageIcon("images/other/Identity.png"));
	RoundButton Friends   = new RoundButton(new ImageIcon("images/other/Friends.png"));
	RoundButton AddRecipe = new RoundButton(new ImageIcon("images/other/AddRecipe.png"));
	RoundButton Logout 	  = new RoundButton(new ImageIcon("images/other/Logout.png"));
	JButton searchinDB 	  = new JButton("search in database");
	JPanel RsIconPanel	  = null;
	JPanel resutPanel 	  = new JPanel(new MigLayout("wrap 6"));
	private JTextField searchField = new JTextField();		
	HashMap<String, ImageIcon> followers = new HashMap<String, ImageIcon>();
	HashMap<ImageIcon,User> hashAll 	 = new HashMap<ImageIcon,User>();
	HashMap<String, ImageIcon> following = new HashMap<String, ImageIcon>();
	ArrayList<User> searchResult 		 = new ArrayList<User>();
	User thisUser;

	Cursor WAIT_CURSOR    = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
	Cursor DEFAULT_CURSOR = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

	/**
	 * Constructor for the Friends frame.
	 * @param user
	 */
	public Friends(User user) {
		setIconImage(new ImageIcon("images/other/recipetoolbar.png").getImage());
		thisUser = user;
		JScrollPane pane = new JScrollPane(mainPanel);
		outer.add(pane);
		south.setBackground(Color.WHITE);
		buildSouthPanel();
		mainPanel.add(south, BorderLayout.SOUTH);
		mainPanel.setBackground(Color.WHITE);
		// setup frame
		setTitle("Friends");
		setBackground(Color.WHITE);
		setVisible(true);
		setSize(new Dimension(1024, 768));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		add(outer, BorderLayout.CENTER);
		JPanel bannerPanel = Main.buildBanner(Home, Identity, Friends, AddRecipe, Logout);
		add(bannerPanel, BorderLayout.NORTH);
		Main.centerFrame(this);
		Home.addActionListener(this);
		Identity.addActionListener(this);
		Friends.addActionListener(this);
		AddRecipe.addActionListener(this);
		Logout.addActionListener(this);
		searchinDB.addActionListener(this);
		JPanel trial = new JPanel(new MigLayout());
		searchinDB.setPreferredSize(new Dimension(50,10));
		trial.add(searchField, "width 1024, dock north");
		trial.add(searchinDB, "wrap");
		trial.setOpaque(false);
		mainPanel.add(trial,BorderLayout.NORTH);
		resutPanel.setOpaque(false);
		mainPanel.add(resutPanel,BorderLayout.NORTH);
	}

	/**
	 * Builds the south panel - user's own recipes.
	 */
	private void buildSouthPanel() {
		Font labelFont = new Font("Candara", Font.PLAIN, 30);
		JScrollPane following = this.getFollowing();
		following.setBackground(Color.WHITE);
		JLabel Following = new JLabel("Following");
		Following.setFont(labelFont);
		south.add(Following, "cell 0 0");
		south.add(following, "cell 0 1, width pref:325, height pref:330");
		JScrollPane myFollowers = this.getFollowers();
		myFollowers.setBackground(Color.WHITE);
		JLabel Followers = new JLabel("Followers");
		Followers.setFont(labelFont);
		south.add(Followers, "cell 2 0");
		south.add(myFollowers, "cell 2 1, width pref:325, height pref:330,  wrap");		
		JScrollPane myFriends = this.getFriends();
		myFriends.setBackground(Color.WHITE);
		JLabel Friends = new JLabel("Friends");
		Friends.setFont(labelFont);
		south.add(Friends, "cell 1 0");
		south.add(myFriends, "cell 1 1, width pref:325, height pref:330");
	}

	/**
	 * Creates a grid of who's following you.
	 * @return - JScrollPanel of the grid
	 */
	private JScrollPane getFollowers()	{
		JPanel followingPanel = new JPanel(new MigLayout("wrap 3"));
		JScrollPane scrollPanel = new JScrollPane(followingPanel);
		scrollPanel.setBackground(Color.WHITE);
		String sql = "SELECT F.userLogin, U.avatar_content FROM friend F, user U WHERE friendLogin =  '"+thisUser.UserName+"' AND F.userLogin = U.login;";
		Connectdatabase connectdb = new Connectdatabase();
		if(connectdb.connect!=null)	{
			ResultSet result = connectdb.executeselect(sql);
			try 	{
				while(result.next())	{
					String name = result.getString("userLogin") != null ? result.getString("userLogin") : "" ;
					InputStream avatarIcon = result.getBinaryStream("avatar_content");				
					Image myPic = ImageIO.read(avatarIcon);
					ImageIcon icon = new ImageIcon(Main.resizeAvatarIcon(myPic));				
					followingPanel.add(this.iconHolder( icon, name));				
					followers.put(new String(result.getString("userLogin")), icon);
				}
			} 
			catch (SQLException e)	{
				e.printStackTrace();
			} catch (IOException e ) {
				e.printStackTrace();
			}
			connectdb.closeconnection();
		}
		return scrollPanel;
	}

	/**
	 * Creates a grid of who your friends are.
	 * @return a JScrollPane with components on it
	 */
	private JScrollPane getFriends() {
		HashMap<String, ImageIcon> friends = new HashMap<String, ImageIcon>();
		for (String key : following.keySet()) {
			if (followers.containsKey(key)) {
				friends.put(key, followers.get(key));
			}			
		}	
		JPanel friendPanel = new JPanel(new MigLayout("wrap 3"));
		JScrollPane scrollPanel = new JScrollPane(friendPanel);
		scrollPanel.setBackground(Color.WHITE);
		for (String key : friends.keySet()) {
			friendPanel.add(this.iconHolder(friends.get(key), key));
		}
		return scrollPanel;
	}

	/**
	 * Creates a grid of who you are following.
	 * @return - JScrollPanel of the grid
	 */
	private JScrollPane getFollowing()	{
		JPanel friendPanel = new JPanel(new MigLayout("wrap 3"));
		JScrollPane scrollPanel = new JScrollPane(friendPanel);
		scrollPanel.setBackground(Color.WHITE);
		String sql = "select F.friendLogin, U.avatar_content from friend F, user U where F.userLogin='"+this.thisUser.UserName+"' and F.friendLogin=U.login";
		Connectdatabase connectdb = new Connectdatabase();
		if(connectdb.connect!=null)	{
			ResultSet result = connectdb.executeselect(sql);
			try 	{
				while(result.next()) {
					String name = result.getString("friendLogin") != null ? result.getString("friendLogin") : "" ;
					InputStream avatarIcon = result.getBinaryStream("avatar_content");				
					Image myPic = ImageIO.read(avatarIcon);
					ImageIcon icon = new ImageIcon(Main.resizeAvatarIcon(myPic));					
					friendPanel.add(this.iconHolder( icon, name));
					following.put(new String(result.getString("friendLogin")), icon);
				}
			} 			
			catch (SQLException e)	{
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			connectdb.closeconnection();
		}
		return scrollPanel;
	}

	/**
	 * Returns a JPanel of a friend icon given their name and picture.
	 * @param pictureUrl
	 * @param name
	 * @return JPanel
	 */
	private JPanel iconHolder(ImageIcon friendIcon, String name) {		
		JPanel iconHolder = new JPanel(new MigLayout());
		iconHolder.setPreferredSize(new Dimension(90, 90));
		iconHolder.setBackground(Color.WHITE);
		iconHolder.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel iconLabel = new JLabel();
		JLabel followerName = new JLabel();			
		if(friendIcon == null) {
			friendIcon = new ImageIcon("images/other/avatar.png");
		}		
		iconLabel.setIcon(friendIcon);	
		iconLabel.setToolTipText(name);
		followerName.setText(name);
		iconHolder.add(iconLabel, 	 "cell 0 0, width 80!, height 50!");
		iconHolder.add(followerName, "cell 0 1, width 60!, height 10!");
		iconLabel.setName(name);
		iconLabel.addMouseListener(this);
		
		return iconHolder;
	}

	/**
	 * Action listener for the Friends frame.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == Home || e.getSource() == Identity
				||e.getSource() == AddRecipe || e.getSource() == Logout||e.getSource()==searchinDB)	{
			setCursor(WAIT_CURSOR);
			if (e.getSource() == Home)	{
				dispose();
				new Home(thisUser);
			}
			if (e.getSource() == Identity)	{
				dispose();
				new Identity(thisUser, null);
			}
			if (e.getSource() == AddRecipe)	{
				dispose();
				new CreateRecipe(thisUser);
			}
			if (e.getSource() == Logout)	{
				dispose();
				new Login();
			}
			
			if(e.getSource()==searchinDB) {
				
				resutPanel.removeAll();
				resutPanel.revalidate();
				
				if(!(searchField.getText().trim()==""))	{
					
					String searchPerson = searchField.getText().trim();
					SearchDatabase SDB = new SearchDatabase(searchPerson,"user");
					searchResult = SDB.finduser(searchPerson.trim());
					
				if (!searchResult.isEmpty()) {
					
					for(User usr: searchResult)	{
						
						RsIconPanel = iconHolder(usr.ProfilePic, usr.UserName);
						hashAll.put(usr.ProfilePic, usr);

						resutPanel.add(RsIconPanel);
						mainPanel.revalidate();
					}
				}
				setCursor(DEFAULT_CURSOR);
			}
			setCursor(DEFAULT_CURSOR);
			}
		}
	}

	
	/**
	 * Mouse listener for friends frame
	 */
	public void mouseClicked(MouseEvent e)
	{
		System.out.println(e.getComponent().getClass().getName());

		if(e.getComponent().getClass().getName() == "javax.swing.JLabel")
		{
			System.out.println( e.getComponent().getName() + "'s icon pressed");
			this.dispose();
			new Identity(thisUser, Main.getUser(e.getComponent().getName()));
		}

	}

	public void mouseEntered(MouseEvent e) {		
	}
	public void mouseExited(MouseEvent e) {		
	}
	public void mousePressed(MouseEvent e) {		
	}
	public void mouseReleased(MouseEvent e) {		
	}
}
