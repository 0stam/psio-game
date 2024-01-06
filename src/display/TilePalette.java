package display;

import event.PalettePressedEvent;
import gamemanager.GameManager;
import enums.EditableTile;

import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class TilePalette extends AbstractPalette {
	public TilePalette () {
		buttons = new ArrayList<>(EditableTile.values().length);

		this.setLayout(new GridLayout(1, EditableTile.values().length, 0, 0));

		for (int i = 0; i < EditableTile.values().length; i++) {
			EditableTile editableTile = EditableTile.values()[i];
			buttons.add(new ImageButton(GraphicsHashtable.getInstance().getImages().get(editableTile.graphics), editableTile.name));
			buttons.get(i).addMouseListener(new TileListener(editableTile));
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
		private EditableTile changeGraphic;

		TileListener(EditableTile changeGraphic) {
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