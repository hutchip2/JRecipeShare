package Logic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import GUI.RecipeBox;
import net.miginfocom.swing.MigLayout;

/**
 * Constructor for the AddComment frame.
 * @author Paul Hutchinson
 *
 */
public class AddComment extends JFrame implements ActionListener, MouseListener	{
	private static final long serialVersionUID = 7544749523086958824L;
	private User Commentator =null;
	private String rCreator,rName =null;
	private JLabel LName;
	private JTextField TxtName;
	private JTextArea TxtComment;
	private JButton save,discard;
	private int ratevalue;
	private JLabel[] ratinglabel;
	private JPanel mainpanel;
	private boolean [] rate;
	private ImageIcon emptystar, fullstar;
	private RecipeBox activeParentPanel;

	/**
	 * Adds a comment to a recipe.
	 * @param recipe
	 * @param Commentator
	 * @param activeParent
	 */
	public AddComment(Recipe recipe, User Commentator, RecipeBox activeParent)	{
		this.rCreator=recipe.creator;
		this.rName= recipe.name;
		this.Commentator = Commentator;
		activeParentPanel = activeParent;
	}

	/**
	 * Gets the comment panel.
	 * @return the JPanel containing the comments.
	 */
	public JPanel getCommentPane() 	{		
		return buildInterface();
	}

	/**
	 * Action performed method for the Add Comment frame.
	 */
	public void actionPerformed(ActionEvent evt)	{
		if(evt.getSource()==discard)	{
			TxtComment.removeAll();
		}
		else if(evt.getSource()==save)	{
			executeCommentSave();
		}
	}

	/**
	 * Builds the interface for the add comment frame.
	 * @return the panel containing the components
	 */
	private JPanel buildInterface()	{
		LName = new JLabel("Comment on: ");
		TxtName = new JTextField(rName);
		TxtName.setEditable(false);
		TxtComment = new JTextArea();
		TxtComment.setPreferredSize(new Dimension(500,100));
		TxtComment.setEditable(true);		
		TxtComment.setLineWrap(true);				
		TxtComment.setBackground(new Color(230,230,230));	
		save 	= new JButton("save");
		discard = new JButton("discard");
		save.addActionListener(this);
		discard.addActionListener(this);
		mainpanel = new JPanel(new MigLayout());
		mainpanel.setBackground(Color.white);
		JPanel west = new JPanel(new MigLayout());
		JPanel east = new JPanel(new MigLayout());
		west.add(LName,		"cell 0 0");
		west.add(TxtName,	"cell 0 1");
		mainpanel.add(west, "dock west");
		mainpanel.add(TxtComment,	"dock center");
		east.add(ratingicons(),	"cell 2 3");
		east.add(save, 			"cell 2 0");
		east.add(discard,		"cell 2 1");
		mainpanel.add(east, "dock east");
		return mainpanel;
	}

	/**
	 * Creates a panel containing the rating.
	 * @return a JPanel with the components on it
	 */
	private JPanel ratingicons()	{
		ratinglabel = new JLabel[5];
		rate 		= new boolean[5];
		fullstar 	= new ImageIcon("images/other/rating.png");
		emptystar	= new ImageIcon("images/other/ratingEmpty.png");
		JPanel icons= new JPanel(); 
		for(int i=0;i<5;i++)	{
			ratinglabel[i] = new JLabel();
			rate[i] = false;
			ratinglabel[i].setIcon(emptystar);
			icons.add(ratinglabel[i], "cell ");
			ratinglabel[i].addMouseListener(this);
		}
		return icons;
	}

	/**
	 * Saves a comment added to a recipe.
	 */
	private void executeCommentSave() {
		if(TxtComment.getText().trim()=="") {
			JOptionPane.showMessageDialog(this, "comment should not be empty");
		} else {
			String sqlcomment = "insert into comment values ("+"'" +Commentator.UserName+"','"+TxtComment.getText()+
			"','"+rName+"','"+rCreator+"',DATE_SUB(NOW(), INTERVAL 5 HOUR));";
			Connectdatabase connection = new Connectdatabase();
			if(connection.executeInsert(sqlcomment)) {
				activeParentPanel.refreshComments();
				TxtComment.removeAll();
				activeParentPanel.showHideCommentsBox();
			} else {
				JOptionPane.showMessageDialog(this, "unable to perform operation");
			}
		}
	}

	/**
	 * Saves the rating on a recipe.
	 */
	private void executeRatingSave() {
		String sqlrating  = "insert into rating values ("+ratevalue+",'"+Commentator.UserName+"','"+rName+"','"+rCreator+"');";
		Connectdatabase connectdb = new Connectdatabase();
		if (connectdb.executeInsert(sqlrating) == true) {
			activeParentPanel.refreshRating();
		} else {
			JOptionPane.showMessageDialog(this, "you've already rated this recipe");
		}			
	}

	/**
	 * Changes the labels.
	 * @param i
	 */
	private void changelabels(int i)	{
		if(rate[i]==false)	{
			for(int inc=0; inc<=i; inc++)	{
				ratinglabel[inc].setIcon(fullstar);
				rate[inc]=true;
			}
			for(int inc=i+1; inc<=4; inc++)	{
				ratinglabel[inc].setIcon(emptystar);
				rate[inc]=false;
			}
		}
		else	{
			for(int inc=i+1; inc<=4; inc++)	{
				ratinglabel[inc].setIcon(emptystar);
				rate[inc]=false;
			}
		}
		repaint();
	}

	/**
	 * Listener for mouse clicks.
	 */
	public void mouseClicked(MouseEvent e) {
		if(e.getSource()==ratinglabel[0])	{
			changelabels(0);
			ratevalue=1;
		}
		if(e.getSource()==ratinglabel[1])	{
			changelabels(1);
			ratevalue=2;
		}
		if(e.getSource()==ratinglabel[2])	{
			changelabels(2);
			ratevalue=3;
		}
		if(e.getSource()==ratinglabel[3])	{
			changelabels(3);
			ratevalue=4;
		}
		if(e.getSource()==ratinglabel[4])	{
			changelabels(4);
			ratevalue=5;
		}
		executeRatingSave();
		activeParentPanel.showHideCommentsBox();
	}

	/**
	 * Listener for mouse entered.
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * Listener for mouse exited.
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Listener for mouse pressed.
	 */
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * Listener for mouse released.
	 */
	public void mouseReleased(MouseEvent e) {
	}
}
