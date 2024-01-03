package display;

import editor.Editor;

import javax.swing.*;
import java.awt.*;


public class EditorDisplay extends JPanel {
	private EditorMapDisplay editorDisplay;
	private PaletteTabs paletteTabs;
	private ToolPalette toolPalette;
	public EditorDisplay ()
	{
		editorDisplay = new EditorMapDisplay();
		paletteTabs = new PaletteTabs();
		toolPalette = new ToolPalette();

		this.setLayout(new BorderLayout());
		this.add(editorDisplay, BorderLayout.CENTER);
		this.add(paletteTabs.getTabs(), BorderLayout.SOUTH);
		this.add(toolPalette, BorderLayout.EAST);
	}
}
