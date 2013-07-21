package GUI;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Class for the system tray of the recipe application.
 * @author Paul Hutchinson
 *
 */
public class SysTray extends JPanel implements ActionListener {
	private static final long serialVersionUID = -1820734607730588346L;
	public SysTray()	{
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}
		final PopupMenu popup = new PopupMenu();
		Image img = new ImageIcon("images/other/recipetray.png").getImage();
		TrayIcon trayIcon = new TrayIcon(img, "RecipeExperts");
		SystemTray tray = SystemTray.getSystemTray();
		MenuItem exitItem = new MenuItem("Exit");
		popup.add(exitItem);  
		exitItem.addActionListener(this);        
		trayIcon.setPopupMenu(popup);       
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
		}
	}

	/**
	 * Action listener for the system tray.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Exit"))	{
			System.exit(0);
		}
	}
}
