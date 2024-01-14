package display;

import enums.EditorGraphics;
import event.PalettePressedEvent;
import gamemanager.GameManager;

import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class TilePalette extends AbstractPalette {
	public TilePalette (EditorGraphics[] values) {
		buttons = new ArrayList<>(values.length);

		this.setLayout(new GridLayout(1, values.length, 0, 0));

		for (int i = 0; i < values.length; i++) {
			EditorGraphics tile = values[i];
			buttons.add(new ImageButton(GraphicsHashtable.getInstance().getImages().get(tile.getGraphics()), tile.getName()));
			buttons.get(i).addMouseListener(new TileListener(tile));
			this.add(buttons.get(i));
		}
		buttons.get(0).setSelected(true);
		selected = buttons.get(0);
	}

	@Override
	protected void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);
		
		float scaleX = (float) this.getWidth() / (32.0f * buttons.toArray().length);
		float scaleY = (float) this.getHeight() / (32.0f);

		float scale = Math.min(scaleX, scaleY);

		for (ImageButton i : buttons) {
			i.setScale(scale / 2);
		}

		//this.setPreferredSize(new Dimension(0, (int) (32.0 * (scale - 1))));
		this.revalidate();
	}
	public class TileListener extends MouseInputAdapter {
		private EditorGraphics changeGraphic;

		TileListener(EditorGraphics changeGraphic) {
			this.changeGraphic = changeGraphic;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			TilePalette.this.selectOne((ImageButton) e.getSource());
			TilePalette.this.repaint();
			GameManager.getInstance().onEvent(new PalettePressedEvent(changeGraphic));
		}
	}
}