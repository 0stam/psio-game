package display;

import display.GraphicsHashtable;
import enums.EditorModes;
import event.EditorEvent;
import event.PalettePressedEvent;
import event.TilePressedEvent;
import gamemanager.GameManager;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import static enums.Graphics.*;

public class TilePalette extends JPanel {
	private Hashtable<enums.Graphics, BufferedImage> fileNames;

	private enums.Graphics[] placeableTiles = {EMPTY, FLOOR, WALL, BUTTON_RELEASED, DOOR_CLOSED, ENEMY, BOX, PLAYER, GOAL};
	private ImageButton[] buttons;

	public TilePalette () {
		fileNames = GraphicsHashtable.getInstance().getImages();
		buttons = new ImageButton[placeableTiles.length];

		this.setLayout(new GridLayout(1, placeableTiles.length, 0, 0));

		for (int i = 0; i < placeableTiles.length; i++) {
			buttons[i] = new ImageButton(this.fileNames.get(placeableTiles[i]), placeableTiles[i].toString(), new Point(0, i));
			buttons[i].addMouseListener(new TileListener(placeableTiles[i]));
			this.add(buttons[i].getContainer());
		}

		selectOne(buttons[0]);
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
		this.setPreferredSize(new Dimension(0, (int) (32.0 * (scale - 1))));
		this.revalidate();
	}
	public void unselectAll()
	{
		for (ImageButton i : buttons)
		{
			i.setSelected(false);
		}
	}
	public void selectOne(ImageButton selected)
	{
		selected.setSelected(true);
	}
	public class TileListener extends MouseInputAdapter {
		private enums.Graphics changeGraphic;

		TileListener(enums.Graphics changeGraphic) {
			this.changeGraphic = changeGraphic;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			TilePalette.this.unselectAll();
			TilePalette.this.selectOne((ImageButton) e.getSource());
			EditorDisplay.setMode(EditorModes.ADD);
			GameManager.getInstance().onEvent(new PalettePressedEvent(changeGraphic));
		}
	}
}