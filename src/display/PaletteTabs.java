package display;

import javax.swing.*;

public class PaletteTabs {
	JTabbedPane tabs;

	public PaletteTabs () {
		tabs = new JTabbedPane();
		tabs.addTab("", new ImageIcon("src/graphics/tool_tiles.png"), new TilePalette(), "Tiles");
		tabs.addTab("", new ImageIcon("src/graphics/tool_connect.png"), new ConnectionsPalette(), "Connections");
	}

	public JTabbedPane getTabs() {
		return tabs;
	}
}
