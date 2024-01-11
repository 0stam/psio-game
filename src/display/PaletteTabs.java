package display;

import editor.Editor;
import enums.EditorMode;
import gamemanager.GameManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaletteTabs {
	private JTabbedPane tabs;

	private TilePalette tilePalette;
	private ConnectionsPalette connectionsPalette;

	public PaletteTabs () {
		tabs = new JTabbedPane();

		tilePalette = new TilePalette();
		connectionsPalette = new ConnectionsPalette();

		tabs.addTab("", new ImageIcon("src/graphics/tool_tiles.png"), tilePalette, "Tiles");
		tabs.addTab("", new ImageIcon("src/graphics/tool_connect.png"), connectionsPalette, "Connections");
		tabs.addTab("", new ImageIcon("src/graphics/tool_path.png"), new JPanel(), "Pathedit");

		tabs.addChangeListener(new ModeListener());
	}

	public void refresh() {
		connectionsPalette.refresh();
	}

	public JTabbedPane getTabs() {
		return tabs;
	}
	public class ModeListener implements ChangeListener
	{
		@Override
		public void stateChanged(ChangeEvent e) {
			String selectedTabTip = tabs.getToolTipTextAt(tabs.getSelectedIndex());
			switch (selectedTabTip)
			{
				case "Tiles":
				{
					GameManager.getInstance().getEditor().setMode(EditorMode.PREADD);
					break;
				}
				case "Connections":
				{
					GameManager.getInstance().getEditor().setMode(EditorMode.CONNECT);
					break;
				}
				case "Pathedit":
				{
					GameManager.getInstance().getEditor().setMode(EditorMode.PATHEDIT);
					break;
				}
				default:
				{
					System.out.println("Ustawiono tryb na default - prawdopodobnie nieporzadane\nPaletteTabs line 63");
					GameManager.getInstance().getEditor().setMode(EditorMode.DEFAULT);
					break;
				}
			}
		}
	}
}
