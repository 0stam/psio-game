package display;

import editor.Editor;
import enums.EditorModes;

import javax.swing.*;
import java.awt.*;


public class EditorDisplay extends JPanel {
	private EditorMapDisplay editorDisplay;
	private PaletteTabs paletteTabs;
	private ToolPalette toolPalette;
	private static enums.EditorModes mode = enums.EditorModes.SELECT;
	public EditorDisplay() {
		editorDisplay = new EditorMapDisplay();
		paletteTabs = new PaletteTabs();
		toolPalette = new ToolPalette(editorDisplay);

		this.setLayout(new BorderLayout());
		this.add(editorDisplay, BorderLayout.CENTER);
		this.add(paletteTabs.getTabs(), BorderLayout.SOUTH);
		this.add(toolPalette, BorderLayout.EAST);
	}

	public static EditorModes getMode() {
		return mode;
	}

	public static void setMode(EditorModes mode) {
		EditorDisplay.mode = mode;
	}

}
