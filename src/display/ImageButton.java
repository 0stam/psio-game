package display;

import event.TilePressedEvent;
import gamemanager.GameManager;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
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
	private boolean selected;
	private String name;

	public ImageButton(BufferedImage image, String name, Point coords) {
		this.image = image;
		this.coords = coords;
		this.selected = false;
		this.name = name;

		container = new JPanel();
		container.setBorder(BorderFactory.createEmptyBorder());
		container.setBackground(Color.white);
		container.setLayout(new GridBagLayout());
		constraints = new GridBagConstraints(1, 1, 3, 3, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		container.add(this, constraints);
		container.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), name));
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	@Override
	public void paintComponent(java.awt.Graphics g) {
		this.setPreferredSize(new Dimension((int)(32.0 * scale ), (int) (32.0 * scale)));
		this.revalidate();
		g.drawImage(this.image.getScaledInstance((int) (32.0 * scale), (int) (32.0 * scale), Image.SCALE_AREA_AVERAGING), 0, 0, this);
	}

	public class EventListener extends MouseInputAdapter {
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

	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;

		if (selected) {
			container.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.red, Color.red), this.name));
		} else {
			container.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), this.name));
		}
	}
}
