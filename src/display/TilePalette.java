package display;

import display.GraphicsHashtable;
import event.EditorEvent;
import event.PalettePressedEvent;
import event.TilePressedEvent;
import gamemanager.GameManager;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Hashtable;

import static enums.Graphics.*;

public class TilePalette extends AbstractPalette {
	private enums.Graphics[] placeableTiles = {EMPTY, FLOOR, WALL, BUTTON_RELEASED, DOOR_CLOSED, ENEMY, BOX, PLAYER, GOAL};

	public TilePalette () {
		buttons = new ArrayList<>(placeableTiles.length);

		this.setLayout(new GridLayout(1, placeableTiles.length, 0, 0));

		for (int i = 0; i < placeableTiles.length; i++) {
			buttons.add(new ImageButton(GraphicsHashtable.getInstance().getImages().get(placeableTiles[i]), placeableTiles[i].toString()));
			buttons.get(i).addMouseListener(new TileListener(placeableTiles[i]));
			this.add(buttons.get(i));
		}
		buttons.get(0).setSelected(true);
		selected = buttons.get(0);
	}

	@Override
	protected void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);
		
		float scale = (float) this.getWidth() / (32.0f * buttons.toArray().length);

		for (ImageButton i : buttons) {
			i.setScale(scale / 2);
		}

		this.setPreferredSize(new Dimension(0, (int) (32.0 * (scale - 1))));
		this.revalidate();
	}
	public class TileListener extends MouseInputAdapter {
		private enums.Graphics changeGraphic;

		TileListener(enums.Graphics changeGraphic) {
			this.changeGraphic = changeGraphic;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			TilePalette.this.selectOne((ImageButton) e.getSource());
			TilePalette.this.repaint();
			//EditorDisplay.setMode(EditorModes.ADD);
			GameManager.getInstance().onEvent(new PalettePressedEvent(changeGraphic));
		}
	}
}