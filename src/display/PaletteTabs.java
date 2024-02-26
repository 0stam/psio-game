package display;

import IO.IOManager;
import enums.EditableTile;
import enums.EditorMode;
import enums.Layer;
import event.display.*;
import event.editor.EnemySelectedEvent;
import event.editor.SavePathEvent;
import gamemanager.GameManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PaletteTabs {
	private JTabbedPane tabs;

	private TilePalette tilePalette;
	private ConnectionsPalette connectionsPalette;
	private PathEditPalette pathEditPalette;
	private MessagesPalette messagesPalette;
	private ToolPalette toolPalette;
	//trzeba to przemyslec, na razie zrobie tak
	public PaletteTabs (ToolPalette toolPalette) {
		tabs = new JTabbedPane();
		this.toolPalette = toolPalette;

		tilePalette = new TilePalette(EditableTile.values());
		connectionsPalette = new ConnectionsPalette();
		pathEditPalette = new PathEditPalette();
		messagesPalette = new MessagesPalette();

		tabs.addTab("", new ImageIcon("src/graphics/tool_tiles.png"), tilePalette, "Tiles");
		tabs.addTab("", new ImageIcon("src/graphics/tool_connect.png"), connectionsPalette, "Connections");
		tabs.addTab("", new ImageIcon("src/graphics/tool_path.png"), pathEditPalette, "Pathedit");
		tabs.addTab("", new ImageIcon("src/graphics/tool_message.png"), messagesPalette, "Messages");

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
			EditorInputHandler inputHandler = (EditorInputHandler) IOManager.getInstance().getInputHandler();
			switch (selectedTabTip)
			{
				//FIXME: zrobmy cos z tym pozniej bo to obrzydliwe
				case "Tiles":
				{
					//zrobic cos madrzejszego
					inputHandler.onEvent(new ConnectableTileSelectedEvent(null));
					GameManager.getInstance().onEvent(new SavePathEvent());
					resetState(EditorMode.ADD);
					GameManager.getInstance().onEvent(new PalettePressedEvent(EditableTile.EMPTY));
					//pathEditPalette.getArrows().selectOne(pathEditPalette.getArrows().buttons.get(0));
					IOManager.getInstance().drawEditor();
					break;
				}
				case "Connections":
				{
					//zrobic cos madrzejszego pozniej
					inputHandler.onEvent(new ConnectableTileSelectedEvent(null));
					GameManager.getInstance().onEvent(new SavePathEvent());
					resetState(EditorMode.CONNECT);
					IOManager.getInstance().drawEditor();
					break;
				}
				case "Pathedit":
				{
					resetState(EditorMode.PATHEDIT);
					//zrobic cos madrzejszego pozniej
					inputHandler.onEvent(new ConnectableTileSelectedEvent(null));
					pathEditPalette.getTree().clearSelection();
					GameManager.getInstance().onEvent(new PalettePressedEvent(enums.Arrow.ARROW_UP));
					//tilePalette.selectOne(tilePalette.buttons.get(0));
					IOManager.getInstance().drawEditor();
					break;
				}
				case "Messages":
				{
					resetState(EditorMode.MESSAGES);
					//zrobic cos madrzejszego pozniej
					inputHandler.onEvent(new ConnectableTileSelectedEvent(null));
					messagesPalette.getTree().clearSelection();
					GameManager.getInstance().onEvent(new PalettePressedEvent(EditableTile.EMPTY));
					//tilePalette.selectOne(tilePalette.buttons.get(0));
					IOManager.getInstance().drawEditor();
					break;
				}
				default:
				{
					System.out.println("Ustawiono tryb na default - prawdopodobnie nieporzadane\nPaletteTabs line 63");
					inputHandler.onEvent(new ModeChangedEvent(EditorMode.DEFAULT));
					break;
				}
			}
			toolPalette.selectOne(toolPalette.buttons.get(0));
		}
	}

	public void resetState (EditorMode m)
	{
		EditorInputHandler inputHandler = (EditorInputHandler) IOManager.getInstance().getInputHandler();

		inputHandler.onEvent(new ChangeLayerEvent(Layer.BOTH));
		inputHandler.onEvent(new ModeChangedEvent(m));
		GameManager.getInstance().onEvent(new EditorChangeEvent());
	}


}
