package display;

import IO.IOManager;
import enums.EditorModes;
import enums.Layer;
import gamemanager.GameManager;
import map.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Hashtable;

import static enums.Graphics.*;

public class ToolPalette extends JPanel {
	private ImageButton[] buttons;
	private EditorMapDisplay mapDisplay;

	public ToolPalette(EditorMapDisplay mapDisplay) {
		this.mapDisplay = mapDisplay;

		buttons = new ImageButton[5];

		try {
			buttons[0] = new ImageButton(ImageIO.read(new File("src/graphics/tool_both.png")), "Both", new Point(0, 0));
			buttons[0].addMouseListener(new layerListener(Layer.BOTH));
			selectOne(buttons[0]);

			buttons[1] = new ImageButton(ImageIO.read(new File("src/graphics/tool_front.png")), "Front", new Point(0, 1));
			buttons[1].addMouseListener(new layerListener(Layer.UPPER));

			buttons[2] = new ImageButton(ImageIO.read(new File("src/graphics/tool_bottom.png")), "Bottom", new Point(0, 2));
			buttons[2].addMouseListener(new layerListener(Layer.BOTTOM));

			buttons[3] = new ImageButton(ImageIO.read(new File("src/graphics/tool_save.png")), "Save", new Point(0, 3));
			buttons[4] = new ImageButton(ImageIO.read(new File("src/graphics/tool_load.png")), "Load", new Point(0, 4));
		} catch (IOException ignored) {

		}

		this.setLayout(new GridLayout(buttons.length, 1, 0, 0));

		for (int i = 0;i < buttons.length;i++) {
			this.add(buttons[i].getContainer());
		}
	}
	public void unselectAll() {
		for (ImageButton i : buttons)
		{
			i.setSelected(false);
		}
	}
	public void selectOne(ImageButton selected) {
		selected.setSelected(true);
	}

	@Override
	protected void paintComponent(java.awt.Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		float scale = (float) this.getHeight() / (32.0f * buttons.length);

		for (int i = 0;i < buttons.length;i++) {
			((ImageButton)buttons[i]).setScale(scale / 2);
		}

		this.setPreferredSize(new Dimension((int) (32 * scale), (int) (32 * scale * buttons.length)));
		this.revalidate();
	}

	public class layerListener extends MouseInputAdapter {
		private enums.Layer changeLayer;

		layerListener(enums.Layer changeLayer) {
			this.changeLayer = changeLayer;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			ToolPalette.this.unselectAll();
			ToolPalette.this.selectOne((ImageButton) e.getSource());
			EditorMapDisplay.setLayer(changeLayer);
			mapDisplay.refreshMap();
		}
	}
}