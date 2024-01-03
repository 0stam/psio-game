package display;

import display.GraphicsHashtable;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import static enums.Graphics.*;

public class TilePalette extends JPanel {
	private Hashtable<enums.Graphics, BufferedImage> fileNames;

	private enums.Graphics[] placeableTiles = {FLOOR, WALL, BUTTON_RELEASED, DOOR_CLOSED, ENEMY, BOX, PLAYER, GOAL};
	private ImageButton[] buttons;

	public TilePalette () {
		fileNames = GraphicsHashtable.getInstance().getImages();
		buttons = new ImageButton[placeableTiles.length];

		this.setLayout(new GridLayout(1, placeableTiles.length, 0, 0));

		for (int i = 0; i < placeableTiles.length; i++) {
			buttons[i] = new ImageButton(this.fileNames.get(placeableTiles[i]), new Point(0, i));
			this.add(buttons[i].getContainer());
		}
	}

	@Override
	protected void paintComponent(java.awt.Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		float scale = (float) this.getWidth() / (32.0f * placeableTiles.length);

		for (int i = 0;i < buttons.length;i++) {
			buttons[i].setScale(scale / 2);
		}
		//border layout sam nam dopasuje szerokosc
		this.setPreferredSize(new Dimension(0, (int) (32 * scale)));
		this.revalidate();
	}

}