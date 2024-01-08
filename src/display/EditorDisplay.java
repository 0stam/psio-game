package display;

import event.EscapeEvent;
import gamemanager.GameManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


public class EditorDisplay extends JPanel {
	private EditorMapDisplay editorDisplay;
	private PaletteTabs paletteTabs;
	private ToolPalette toolPalette;
	//private static enums.EditorModes mode = enums.EditorModes.ADD;
	public EditorDisplay() {
		editorDisplay = new EditorMapDisplay();
		paletteTabs = new PaletteTabs();
		toolPalette = new ToolPalette();

		this.setLayout(new BorderLayout());
		this.add(editorDisplay, BorderLayout.CENTER);
		this.add(paletteTabs.getTabs(), BorderLayout.SOUTH);
		this.add(toolPalette, BorderLayout.EAST);

		createKeyBinding();
	}

	public void createKeyBinding() {
		InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = getActionMap();

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
		actionMap.put("escape", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameManager.getInstance().onEvent(new EscapeEvent());
			}
		});
	}

	/*
	public static EditorModes getMode() {
		return mode;
	}

	public static void setMode(EditorModes mode) {
		EditorDisplay.mode = mode;
	}
	*/
	public void refresh() {
		editorDisplay.refresh();
		paletteTabs.refresh();
	}

	public EditorMapDisplay getEditorMapDisplay() {
		return editorDisplay;
	}
}
