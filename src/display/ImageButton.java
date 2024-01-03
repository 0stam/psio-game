package display;

import event.TilePressedEvent;
import gamemanager.GameManager;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static enums.Graphics.*;

public class ImageButton extends JPanel {
	private BufferedImage image;
	private Point coords;
	private float scale = 1;
	private JPanel container;
	private GridBagConstraints constraints;

	public ImageButton(BufferedImage image, Point coords)
	{
		this.image = image;
		this.coords = coords;
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.addMouseListener(new EventListener());
		container = new JPanel();
		container.setBackground(Color.white);
		container.setLayout(new GridBagLayout());
		constraints = new GridBagConstraints(1, 1, 3, 3, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		container.add(this, constraints);
		container.setBorder(BorderFactory.createLineBorder(Color.red));
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	@Override
	public void paintComponent(java.awt.Graphics g)
	{
		this.setPreferredSize(new Dimension((int)(32.0 * scale ), (int) (32.0 * scale)));
		this.revalidate();
		g.drawImage(this.image.getScaledInstance((int) (32.0 * scale), (int) (32.0 * scale), Image.SCALE_AREA_AVERAGING), 0, 0, this);
	}

	public class EventListener extends MouseInputAdapter
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			System.out.println("Kliknieto na panel o kordach: "+coords.x+" "+coords.y + " ; przygotowuje event");
			TilePressedEvent editorEvent = new TilePressedEvent(coords.x, coords.y, EditorMapDisplay.getLayer());
			GameManager.getInstance().onEvent(editorEvent);
		}
	}

	public JPanel getContainer() {
		return container;
	}
}