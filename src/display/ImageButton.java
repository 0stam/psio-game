package display;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageButton extends JPanel {
	private float scale = 1;
	private boolean selected;
	private String name;
	private BufferedImage image;

	public ImageButton(BufferedImage image, String name) {
		this.selected = false;
		this.name = name;
		this.image = image;

		this.setBorder(BorderFactory.createEmptyBorder());
		this.setBackground(Color.white);

		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), name));
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(image, (int) ((this.getWidth() - 32 * scale) / 2), (int) ((this.getHeight() - 32 * scale) / 2), (int) (32 * scale), (int) (32 * scale),this);
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
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
