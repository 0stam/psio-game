package display;

import IO.IOManager;
//import enums.EditorModes;
import enums.Layer;
import event.LevelLoadedEvent;
import event.LevelSavedEvent;
import gamemanager.GameManager;
import levelloader.LevelLoader;
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
	private EditorDisplay parent;

	public ToolPalette(EditorDisplay parent) {
		this.parent = parent;

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

			buttons.add(new ImageButton(ImageIO.read(new File("src/graphics/tool_resize.png")), "Resize"));
			buttons.get(3).addMouseListener(new resizeListener());

			buttons.add(new ImageButton(ImageIO.read(new File("src/graphics/tool_zoomin.png")), "Zoom in"));
			buttons.get(4).addMouseListener(new zoomListener(parent, 1));

			buttons.add(new ImageButton(ImageIO.read(new File("src/graphics/tool_zoomout.png")), "Zoom out"));
			buttons.get(5).addMouseListener(new zoomListener(parent, -1));

			buttons.add(new ImageButton(ImageIO.read(new File("src/graphics/tool_save.png")), "Save"));
			buttons.get(6).addMouseListener(new saveListener(this));

			buttons.add(new ImageButton(ImageIO.read(new File("src/graphics/tool_load.png")), "Load"));
			buttons.get(7).addMouseListener(new loadListener(this));
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
		public void mousePressed(MouseEvent e) {
			ToolPalette.this.selectOne((ImageButton) e.getSource());
			if (changeLayer != GameManager.getInstance().getEditor().getLayer()) {
				GameManager.getInstance().getEditor().setLayer(changeLayer);
				IOManager.getInstance().drawEditor();
			}

		}
	}

	public class resizeListener extends MouseInputAdapter {
		ResizeWindow resizeWindow;

		@Override
		public void mousePressed(MouseEvent e) {
			resizeWindow = new ResizeWindow();
		}
	}

	public class zoomListener extends MouseInputAdapter {
		private EditorDisplay parent;
		private int zoom;

		public zoomListener (EditorDisplay parent, int zoom) {
			this.parent = parent;
			this.zoom = zoom;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (zoom > 0) {
				parent.getEditorMapDisplay().setScale(parent.getEditorMapDisplay().getScale() * 1.1f);
			} else {
				parent.getEditorMapDisplay().setScale(parent.getEditorMapDisplay().getScale() / 1.1f);
			}
			parent.getEditorMapDisplay().repaint();
		}
	}

	public class loadListener extends MouseInputAdapter {
		JFileChooser fc;
		private ToolPalette parent;

		public loadListener (ToolPalette parent) {
			this.parent = parent;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			fc = new JFileChooser(new File(System.getProperty("user.home") + LevelLoader.userLevelsPath));

			int returnVal = fc.showOpenDialog(this.parent);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				GameManager.getInstance().getEditor().onEvent(new LevelLoadedEvent(file.getPath()));
			}
		}
	}

	public class saveListener extends MouseInputAdapter {
		JFileChooser fc;
		private ToolPalette parent;

		public saveListener (ToolPalette parent) {
			this.parent = parent;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			fc = new JFileChooser(new File(System.getProperty("user.home") + LevelLoader.userLevelsPath));

			int returnVal = fc.showSaveDialog(this.parent);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				GameManager.getInstance().getEditor().onEvent(new LevelSavedEvent(file.getPath()));
			}
		}
	}
}