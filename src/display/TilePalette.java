package display;

import display.GraphicsHashtable;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import static enums.Graphics.*;

public class TilePalette extends JPanel {
	private Hashtable<enums.Graphics, BufferedImage> fileNames;

	private BufferedImage[] images;

	public TilePalette () {
		this.fileNames = GraphicsHashtable.getInstance().getImages();

		images = new BufferedImage[values().length];

		for (int i = 0; i < images.length; i++) {
			images[i] = this.fileNames.get(enums.Graphics.values()[i]);
		}
	}

	@Override
	protected void paintComponent(java.awt.Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		float scale = (float) this.getWidth() / (32.0f * images.length);

		int tileSize = (int)(16 * scale);

		for (int i = 0;i < images.length;i++) {
			g.drawImage(images[i].getScaledInstance(tileSize, tileSize, Image.SCALE_AREA_AVERAGING), (int) ((i + 0.5f) * ((int) (32 * scale)) - (0.5f * tileSize)), (int) (0.5f * (32 * scale - tileSize)), this);
		}
		//border layout sam nam dopasuje szerokosc
		this.setPreferredSize(new Dimension(0, (int) (32 * scale)));
		this.revalidate();
	}

}