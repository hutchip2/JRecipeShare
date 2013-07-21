package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Formatter;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
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
 * Class for the Login frame.
 * @author Paul Hutchinson
 *
 */
public class Login extends JFrame implements ActionListener, KeyListener {
	private static final long serialVersionUID = 472771024844552451L;
	JPanel outer = new JPanel(new BorderLayout());
	JPanel panel = new JPanel(new MigLayout());
	RoundButton login = new RoundButton(new ImageIcon("images/other/next.png"));
	RoundButton cancel = new RoundButton(new ImageIcon("images/other/block.png"));
	RoundButton newUser = new RoundButton(new ImageIcon("images/other/add_user.png"));
	JLabel Username = new JLabel("Username");
	JLabel Password = new JLabel("Password");
	JTextField username = new JTextField();
	JPasswordField password = new JPasswordField();
	JCheckBox remember = new JCheckBox();
	Cursor WAIT_CURSOR =
		Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
	Cursor DEFAULT_CURSOR =
		Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

	/**
	 * Constructor for the Login frame.
	 */
	@SuppressWarnings("unchecked")
	public Login()	{
		Username.setForeground(new Color(143, 198, 23));
		Password.setForeground(new Color(0, 139, 213));
		login.setRolloverEnabled(true);
		login.setRolloverIcon(new ImageIcon("images/other/rollovernext.png"));
		login.setPressedIcon(new ImageIcon("images/other/selectednext.png"));
		login.setToolTipText("Login");
		cancel.setRolloverEnabled(true);
		cancel.setRolloverIcon(new ImageIcon("images/other/rolloverblock.png"));
		cancel.setPressedIcon(new ImageIcon("images/other/selectedblock.png"));
		cancel.setToolTipText("Cancel");
		newUser.setRolloverEnabled(true);
		newUser.setRolloverIcon(new ImageIcon("images/other/rolloveradd_user.png"));
		newUser.setPressedIcon(new ImageIcon("images/other/selectedadd_user.png"));
		newUser.setToolTipText("New User...");
		remember.setToolTipText("Save Your Login...");
		setIconImage(new ImageIcon("images/other/recipetoolbar.png").getImage());
		username.setPreferredSize(new Dimension(200, 20));
		password.setPreferredSize(new Dimension(200, 20));
		username.addKeyListener(this);
		password.addKeyListener(this);
		buildMainPanel();
		login.addActionListener(this);
		cancel.addActionListener(this);
		newUser.addActionListener(this);
		JLabel bannerLabel = new JLabel();
		ImageIcon bannerIcon = new ImageIcon("images/banners/bannerBlank.png");
		bannerLabel.setIcon(bannerIcon);
		outer.add(bannerLabel, BorderLayout.NORTH);
		outer.add(panel, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Login");
		add(outer);
		setVisible(true);
		setSize(new Dimension(1024,768));
		Main.centerFrame(this);
		login.requestFocusInWindow();
		if (new File("data.recipe").exists())	{
			ArrayList<String> data = new ArrayList<String>();
			FileInputStream fis;
			File file = new File("data.recipe");
			try {
				fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis); 
				data = (ArrayList<String>) ois.readObject(); 
				ois.close(); 
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			if (data.get(0).equals("checked"))	{
				remember.setSelected(true);
				username.setText(data.get(1));
				password.setText(data.get(2));
			}
		}
	}

	/**
	 * Builds the main panel for the login frame.
	 */
	public void buildMainPanel()	{
		panel.setName("Login");
		panel.setBackground(Color.WHITE);
		panel.add(Username);
		panel.add(username, "wrap");
		panel.add(Password);
		panel.add(password);
		JLabel rememberme = new JLabel("Remember me: ");
		panel.add(rememberme);
		rememberme.setForeground(new Color(0, 139, 213));
		panel.add(remember, "wrap");
		panel.add(login);
		panel.add(cancel);
		panel.add(newUser);
		panel.addKeyListener(this);
		remember.setBackground(Color.WHITE);
	}

	/**
	 * Action performed method for the components.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == login)	{
			doLogin();
		}
		if (e.getSource() == cancel)	{
			System.exit(0);
		}
		if (e.getSource() == newUser)	{
			dispose();
			new newUser();
		}
	}

	/**
	 * Logs into the database using the user's credentials.
	 */
	@SuppressWarnings("deprecation")
	public void doLogin()	{
		setCursor(WAIT_CURSOR);		 
		String usrname = username.getText().trim();
		String pswd = password.getText().trim();
		if (remember.isSelected())	{
			ArrayList<String> data = new ArrayList<String>();
			data.add("checked");
			data.add(username.getText());
			data.add(password.getText());
			File file = new File("data.recipe");	
			FileOutputStream fos;
			@SuppressWarnings("unused")
			DataOutputStream dos;
			try {
				fos = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(fos); 
				oos.writeObject(data); 
				oos.flush(); 
				oos.close(); 
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} 
		} else	{
			File file = new File("data.recipe");
			file.delete();
		}
		if (this.validateUser(usrname, pswd)) {	
			dispose();
			new Home(Main.getUser(usrname));
		} else {
			setCursor(DEFAULT_CURSOR);
			JOptionPane.showMessageDialog(this, "Sorry invalid username or password");
		}				
	}

	
	/**
	 * Validates the user, given a username and password. 
	 * @param userName - String of username.
	 * @param passwd - String of password. Can be plain text, as this will compare the MD5 hash
	 * @return - boolean of whether or not the user was valid. 
	 */
	protected boolean validateUser (String userName, String passwd) {
		boolean valid = false;
		String sqlvalidate = "select passwd from user where login = '"+userName+"'";
		Connectdatabase connectdb = new Connectdatabase();
		if (connectdb.connect != null) {
			ResultSet result = connectdb.executeselect(sqlvalidate);
			try {
				while ( result.next() ) {
					String passwdResult = result.getString(1);
					if (passwdResult.compareTo(this.getMD5HashVal(passwd)) == 0) {
						valid = true;						
					}					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
		connectdb.closeconnection();
		return valid;
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
	 * Key listener for keyPressed.
	 */
	public void keyPressed(KeyEvent e) {
	}

	/**
	 * Key listener for keyReleased.
	 */
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 10)	{
			if (e.getSource() == username || e.getSource() == password)	{
				doLogin();
			}
		}
	}

	/**
	 * Key listener for keyTyped.
	 */
	public void keyTyped(KeyEvent e) {
	}  
}
