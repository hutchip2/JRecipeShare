package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Formatter;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import Logic.Connectdatabase;
import Logic.Main;
import net.miginfocom.swing.MigLayout;

/**
 * Class for the newUser frame.
 * @author Paul Hutchinson
 *
 */
public class newUser extends JFrame implements ActionListener {
	private static final long serialVersionUID = -8167291489082889287L;
	JTextField userAccount 	= new JTextField();
	JTextField firstname 	= new JTextField();
	JTextField lastname 	= new JTextField();
	JPasswordField userPassword = new JPasswordField();
	JPasswordField reEnter 		= new JPasswordField();
	JTextField email 		= new JTextField();
	JTextField description 	= new JTextField();
	Cursor WAIT_CURSOR 		= Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
	Cursor DEFAULT_CURSOR 	= Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	RoundButton create = new RoundButton(new ImageIcon("images/other/add_to_database.png"));
	RoundButton cancel = new RoundButton(new ImageIcon("images/other/block.png"));

	/**
	 * Constructor for the new user frame.
	 */
	public newUser() {
		create.setRolloverEnabled(true);
		create.setRolloverIcon(new ImageIcon("images/other/rolloveradd_to_database.png"));
		create.setPressedIcon(new ImageIcon("images/other/selectedadd_to_database.png"));
		create.setToolTipText("Create Account...");
		create.addActionListener(this);
		cancel.setRolloverEnabled(true);
		cancel.setRolloverIcon(new ImageIcon("images/other/rolloverblock.png"));
		cancel.setPressedIcon(new ImageIcon("images/other/selectedblock.png"));
		cancel.setToolTipText("Cancel");
		cancel.addActionListener(this);
		setTitle("New User");
		setBackground(Color.WHITE);
		setVisible(true);
		setSize(new Dimension(1024, 768));
		setLayout(new BorderLayout());
		Main.centerFrame(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPanel = new JPanel(new MigLayout());
		mainPanel.setBackground(Color.WHITE);
		// setup fields
		JLabel Account = new JLabel("Enter desired username: *");
		JLabel Firstname = new JLabel("First name: ");
		JLabel Lastname = new JLabel("Last name: ");
		JLabel Password = new JLabel("Enter desired password: *");
		JLabel Reenter = new JLabel("Re-enter desired password: *");
		JLabel Email = new JLabel("Enter email address: *");
		JLabel Description = new JLabel("Self description: ");
		// set sizes
		userAccount.setPreferredSize(new Dimension(200, 20));
		userPassword.setPreferredSize(new Dimension(200, 20));
		reEnter.setPreferredSize(new Dimension(200, 20));
		email.setPreferredSize(new Dimension(200, 20));
		description.setPreferredSize(new Dimension(200, 20));
		firstname.setPreferredSize(new Dimension(200,20));
		lastname.setPreferredSize(new Dimension(200,20));
		// add fields to main panel		
		mainPanel.add(Account);
		mainPanel.add(userAccount, "wrap");
		mainPanel.add(Firstname);
		mainPanel.add(firstname, "wrap");
		mainPanel.add(Lastname);
		mainPanel.add(lastname, "wrap");
		mainPanel.add(Password);
		mainPanel.add(userPassword, "wrap");
		mainPanel.add(Reenter);
		mainPanel.add(reEnter, "wrap");
		mainPanel.add(Email);
		mainPanel.add(email, "wrap");
		mainPanel.add(Description);
		mainPanel.add(description, "wrap");
		mainPanel.add(create);
		mainPanel.add(cancel);
		JLabel bannerLabel = new JLabel();
		ImageIcon bannerIcon = new ImageIcon("images/banners/bannerBlank.png");
		bannerLabel.setIcon(bannerIcon);
		add(bannerLabel, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
	}

	/**
	 * Action listener for the newUser frame.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancel)	{
			setCursor(WAIT_CURSOR);
			new Login();
			dispose();
			setCursor(DEFAULT_CURSOR);
		}
		if (e.getSource() == create) {
			if ( this.validateFields() ) {
				if (this.validatePasswords()) {
					// Good to insert
					String usr = userAccount.getText().trim();
					String fname = firstname.getText().trim();
					String lname = lastname.getText().trim();
					String pswd = new String(userPassword.getPassword()).trim();
					String useremail = email.getText().trim();
					String descript = description.getText().trim();
					Connectdatabase connectdb = new Connectdatabase();			
					
					String sql = "INSERT INTO recipe.user (login, email, fname, lname, description, passwd, avatar_content)" +
					" VALUES ( ?, ?, ?, ?, ?, ?, ?)";
					
					int s = -1;
					
					try {
						
						FileInputStream fis = new FileInputStream("images/other/avatar.png");
						
						PreparedStatement stmnt = (PreparedStatement) connectdb.connect.prepareStatement(sql);
						stmnt.setString(1, usr);
						stmnt.setString(2, useremail);
						stmnt.setString(3, fname);
						stmnt.setString(4, lname);
						stmnt.setString(5, descript);
						stmnt.setString(6, this.getMD5HashVal(pswd));
						
						File myImage = new File( "images/other/avatar.jpg" );
					
						stmnt.setBinaryStream(7, (InputStream)fis, (int)(myImage.length()));
		
					
						s = stmnt.executeUpdate();
						
					} catch (SQLException e1) {
						e1.printStackTrace();
					} catch (FileNotFoundException e2) {
						e2.printStackTrace();
					}
					
					if (s > 0) {
						dispose();
						new Login();//I am not sure we need a new login rather a new home
						JOptionPane.showMessageDialog(this, "User created, please login.");
						
					} else	{
						JOptionPane.showMessageDialog(this, "error can't create user");
					}
				} else {
					JOptionPane.showMessageDialog(this, "passwords do not match");
				}		
			} else {
				JOptionPane.showMessageDialog(this, "please fill out all required fields marked with a *");
			}	
		}
	}

	/**
	 * Takes a string and returns its MD5 hash, to compare passwords. 
	 * @param strToBeEncrypted - String to by encrypted
	 * @return String of MD5 hash
	 */
	private String getMD5HashVal(String strToBeEncrypted) {  
		String encryptedString = null;  
		byte[] bytesToBeEncrypted;  
		try {  
			// convert string to bytes using a encoding scheme  
			bytesToBeEncrypted = strToBeEncrypted.getBytes("UTF-8");  
			MessageDigest md = MessageDigest.getInstance("MD5");  
			byte[] theDigest = md.digest(bytesToBeEncrypted); 
			// convert each byte to a hexadecimal digit
			Formatter formatter = new Formatter();  
			for (byte b : theDigest) {
				formatter.format("%02x", b);
			}  
			encryptedString = formatter.toString().toLowerCase();  
		} catch (UnsupportedEncodingException e) {  
			e.printStackTrace();  
		} catch (NoSuchAlgorithmException e) {  
			e.printStackTrace();  
		}
		return encryptedString;  
	}  

	/**
	 * Validates fields for create user. Use before sending insert statement to mysql. 
	 * @return - True: all required fields are filled out. False: some fields missing information.
	 */
	private boolean validateFields() {
		boolean valid = true;
		if ( this.userAccount.getText() == null || this.userAccount.getText() == "") {
			valid = false;
		} else if (this.email.getText() == null || this.email.getText() == "") {
			valid = false;
		} else if (this.userPassword.getPassword().length == 0) {
			valid = false;
		} else if (this.reEnter.getPassword().length == 0 ) {
			valid = false;
		}
		return valid;
	}

	/**
	 * Validates passwords for create user.
	 * @return true if validated, false otherwise.
	 */
	private boolean validatePasswords() {
		String password = new String(userPassword.getPassword()).trim();
		return password.equals(new String(reEnter.getPassword()).trim());
	}
}
