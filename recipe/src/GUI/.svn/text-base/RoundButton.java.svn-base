package GUI;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

/**
 * Class for a custom JButton.
 * @author Paul Hutchinson
 *
 */
public class RoundButton extends JButton {
	private static final long serialVersionUID = -22476090290214683L;

	/**
	 * Constructor for a RoundButton, excepts an ImageIcon to use.
	 * @param icon
	 */
	public RoundButton(ImageIcon icon) {
		setIcon(icon);
		Dimension size = getPreferredSize();
		size.width = size.height = Math.max(size.width, 
				size.height);
		setPreferredSize(size);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setFocusable(false);
	}
	Shape shape;

	/**
	 * Checks bounds.
	 */
	public boolean contains(int x, int y) {
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
		}
		return shape.contains(x, y);
	}
}