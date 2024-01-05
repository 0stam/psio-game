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
	private float scale = 1;
	private JPanel container;
	private boolean selected;
	private String name;

	public ImageButton(BufferedImage image, String name) {
		this.selected = false;
		this.name = name;


		this.setBorder(BorderFactory.createEmptyBorder());
		this.setBackground(Color.white);
		this.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints(1, 1, 3, 3, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		container = new JPanel()
		{
			@Override
			public void paintComponent(Graphics g)
			{
				this.setPreferredSize(new Dimension((int)(32.0 * ImageButton.this.scale), (int) (32.0 * ImageButton.this.scale)));
				g.drawImage(image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_AREA_AVERAGING), 0, 0, this);
				this.revalidate();
			}
		};
		this.add(container, constraints);
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), name));
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
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
			this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.red, Color.red), this.name));
		} else {
			this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), this.name));
		}
	}
}
