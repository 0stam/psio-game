package display;

import IO.IOManager;
import enums.EditableTile;
import enums.EditorMode;
import enums.Layer;
import event.PalettePressedEvent;
import event.SavePathEvent;
import gamemanager.GameManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.tools.Tool;

public class PaletteTabs {
	private JTabbedPane tabs;

	private TilePalette tilePalette;
	private ConnectionsPalette connectionsPalette;
	private PathEditPalette pathEditPalette;
	private ToolPalette toolPalette;
	//trzeba to przemyslec, na razie zrobie tak
	public PaletteTabs (ToolPalette toolPalette) {
		tabs = new JTabbedPane();
		this.toolPalette = toolPalette;

		tilePalette = new TilePalette(EditableTile.values());
		connectionsPalette = new ConnectionsPalette();
		pathEditPalette = new PathEditPalette();

		tabs.addTab("", new ImageIcon("src/graphics/tool_tiles.png"), tilePalette, "Tiles");
		tabs.addTab("", new ImageIcon("src/graphics/tool_connect.png"), connectionsPalette, "Connections");
		tabs.addTab("", new ImageIcon("src/graphics/tool_path.png"), pathEditPalette, "Pathedit");

		tabs.addChangeListener(new ModeListener());
	}

	public void refresh() {
		connectionsPalette.refresh();
	}

	public JTabbedPane getTabs() {
		return tabs;
	}

	public ToolPalette getToolPalette() {
		return toolPalette;
	}

	public class ModeListener implements ChangeListener
	{
		@Override
		public void stateChanged(ChangeEvent e) {
			String selectedTabTip = tabs.getToolTipTextAt(tabs.getSelectedIndex());
			switch (selectedTabTip)
			{
				//FIXME: zrobmy cos z tym pozniej bo to obrzydliwe
				case "Tiles":
				{
					GameManager.getInstance().getEditor().setMode(EditorMode.PREADD);
					GameManager.getInstance().getEditor().setLayer(Layer.BOTH);
					//zrobic cos madrzejszego
					GameManager.getInstance().onEvent(new SavePathEvent());
					GameManager.getInstance().getEditor().setCurrentEnemy(null);
					GameManager.getInstance().onEvent(new PalettePressedEvent(EditableTile.EMPTY));
					pathEditPalette.getArrows().selectOne(pathEditPalette.getArrows().buttons.get(0));
					IOManager.getInstance().drawEditor();
					break;
				}
				case "Connections":
				{
					//zrobic cos madrzejszego pozniej
					GameManager.getInstance().onEvent(new SavePathEvent());
					GameManager.getInstance().getEditor().setCurrentEnemy(null);
					GameManager.getInstance().getEditor().setMode(EditorMode.CONNECT);
					IOManager.getInstance().drawEditor();
					break;
				}
				case "Pathedit":
				{
					GameManager.getInstance().getEditor().setMode(EditorMode.PREPATHEDIT);
					GameManager.getInstance().getEditor().setLayer(Layer.BOTH);
					//zrobic cos madrzejszego pozniej
					pathEditPalette.getTree().clearSelection();
					GameManager.getInstance().onEvent(new PalettePressedEvent(enums.Arrow.ARROW_UP));
					tilePalette.selectOne(tilePalette.buttons.get(0));
					IOManager.getInstance().drawEditor();
					break;
				}
				default:
				{
					System.out.println("Ustawiono tryb na default - prawdopodobnie nieporzadane\nPaletteTabs line 63");
					GameManager.getInstance().getEditor().setMode(EditorMode.DEFAULT);
					break;
				}
			}
			toolPalette.selectOne(toolPalette.buttons.get(0));
		}
	}
}
