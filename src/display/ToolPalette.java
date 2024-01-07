package display;

import IO.IOManager;
//import enums.EditorModes;
import enums.Layer;
import enums.Loader;
import event.LevelLoadedEvent;
import event.LevelSavedEvent;
import gamemanager.GameManager;
import levelloader.LevelLoader;
import levelloader.LevelNotLoaded;
import levelloader.LevelNotSaved;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

import static enums.Graphics.*;

public class ToolPalette extends AbstractPalette {
	public ToolPalette() {
		buttons = new ArrayList<>();
		try {
			buttons.add(new ImageButton(ImageIO.read(new File("src/graphics/tool_both.png")), "Both"));
			buttons.get(0).addMouseListener(new layerListener(Layer.BOTH));
			selected = buttons.get(0);
			buttons.get(0).setSelected(true);

			buttons.add(new ImageButton(ImageIO.read(new File("src/graphics/tool_front.png")), "Front"));
			buttons.get(1).addMouseListener(new layerListener(Layer.UPPER));

			buttons.add(new ImageButton(ImageIO.read(new File("src/graphics/tool_bottom.png")), "Bottom"));
			buttons.get(2).addMouseListener(new layerListener(Layer.BOTTOM));

			buttons.add(new ImageButton(ImageIO.read(new File("src/graphics/tool_save.png")), "Save"));
			buttons.get(3).addMouseListener(new loaderListener(Loader.SAVE));

			buttons.add(new ImageButton(ImageIO.read(new File("src/graphics/tool_load.png")), "Load"));
			buttons.get(4).addMouseListener(new loaderListener(Loader.LOAD));
		} catch (IOException ignored) {

		}

		this.setLayout(new GridLayout(buttons.toArray().length, 1, 0, 0));

		for (ImageButton i : buttons) {
			this.add(i);
		}
	}

	@Override
	protected void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);
		float scale = (float) this.getHeight() / (32.0f * buttons.toArray().length);

		for (ImageButton i : buttons) {
			i.setScale(scale / 2);
		}

		this.setPreferredSize(new Dimension((int) (32 * scale), 0));
		this.revalidate();
	}

	public class layerListener extends MouseInputAdapter {
		private enums.Layer changeLayer;

		layerListener(enums.Layer changeLayer) {
			this.changeLayer = changeLayer;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			ToolPalette.this.selectOne((ImageButton) e.getSource());
			if (changeLayer != GameManager.getInstance().getEditor().getLayer()) {
				GameManager.getInstance().getEditor().setLayer(changeLayer);
				IOManager.getInstance().drawEditor();
			}

		}
	}
	public class loaderListener extends MouseInputAdapter {
		private enums.Loader changeLayer;

		loaderListener(enums.Loader changeLayer) {
			this.changeLayer = changeLayer;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			ToolPalette.this.selectOne((ImageButton) e.getSource());
			if (changeLayer == Loader.LOAD) {
				LevelLoadedEvent levelLoadedEvent = new LevelLoadedEvent("testsave");
				GameManager.getInstance().onEvent(levelLoadedEvent);
				IOManager.getInstance().drawEditor();
			}
			else if (changeLayer == Loader.SAVE)
			{
				LevelSavedEvent levelSavedEvent = new LevelSavedEvent("testsave");
				GameManager.getInstance().onEvent(levelSavedEvent);
			}

		}
	}
}
