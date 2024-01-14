package display;

import IO.IOManager;
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
	private JSplitPane vSplitPane;
	private JSplitPane hSplitPane;
	private JScrollPane mapScrollPane;

	public EditorDisplay() {
		vSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
		hSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);

		editorDisplay = new EditorMapDisplay();
		toolPalette = new ToolPalette(this);
		paletteTabs = new PaletteTabs(toolPalette);

		mapScrollPane = new JScrollPane(editorDisplay);
		mapScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		mapScrollPane.getHorizontalScrollBar().setUnitIncrement(16);

		this.setLayout(new BorderLayout());
		this.add(vSplitPane, BorderLayout.CENTER);
		hSplitPane.setLeftComponent(mapScrollPane);
		hSplitPane.setRightComponent(toolPalette);
		vSplitPane.setTopComponent(hSplitPane);
		vSplitPane.setBottomComponent(paletteTabs.getTabs());

		vSplitPane.setResizeWeight(0.9f);
		hSplitPane.setResizeWeight(0.9f);

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

	public void refresh() {
		editorDisplay.refresh();
		paletteTabs.refresh();
	}

	public EditorMapDisplay getEditorMapDisplay() {
		return editorDisplay;
	}
}
