package IO;

import javax.swing.*;

import display.*;
import event.EventObserver;
import gamemanager.GameManager;

import java.awt.*;

public class GraphicIO implements IOStrategy {
	private JFrame window;
	private EditorDisplay editorDisplay;
	private GameMapDisplay gameDisplay;
	private MenuDisplay menuDisplay;
	private EventObserver inputHandler;

	private boolean versionWarningShown = false;

	private void createWindow () {
		try {
			// Set cross-platform L&F
			UIManager.setLookAndFeel(
					UIManager.getCrossPlatformLookAndFeelClassName());
		}
		catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
			   IllegalAccessException e) {

		}

		window = new JFrame("Nanogrid");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.setUndecorated(true);
		window.setVisible(true);
		window.setIconImage(new ImageIcon("src/graphics/icon.png").getImage());
		//i tak nie ma sensu robic mniejszego
		window.setMinimumSize(new Dimension(1280, 720));
	}

	public void drawGame () {
		if (window == null) {
			createWindow();
		}

		if (gameDisplay == null) {
			Dimension currentSize = window.getSize();
			window.getContentPane().removeAll();

			editorDisplay = null;
			inputHandler = new GameEventHandler();
			menuDisplay = null;

			gameDisplay = new GameMapDisplay();
			gameDisplay.setMinimumSize(new Dimension(32 * gameDisplay.getW()-1, 32 * gameDisplay.getH()));
			gameDisplay.setupMap(GameManager.getInstance().getMap());

			window.getContentPane().add(gameDisplay.getContainer(), BorderLayout.CENTER);
			window.pack();
			window.setSize(currentSize);
		} else {
			gameDisplay.setupMap(GameManager.getInstance().getMap());
			gameDisplay.repaint();
		}
	}

	public void drawEditor () {
		if (window == null) {
			createWindow();
		}

		if (editorDisplay == null) {
			Dimension currentSize = window.getSize();
			window.getContentPane().removeAll();

			gameDisplay = null;
			menuDisplay = null;

			inputHandler = new EditorInputHandler();

			editorDisplay = new EditorDisplay();
			editorDisplay.setMinimumSize(new Dimension(32 * GameManager.getInstance().getMap().getWidth()-1, 32 * GameManager.getInstance().getMap().getHeight()));

			window.getContentPane().add(editorDisplay, BorderLayout.CENTER);
			window.pack();
			window.setSize(currentSize);
		} else {
			// FIXME: add proper editor refresh method
			editorDisplay.refresh();
			editorDisplay.repaint();
		}
	}

	public void drawMenu() {
		if (window == null) {
			createWindow();
		}

		if (menuDisplay == null) {
			Dimension currentSize = window.getSize();
			window.getContentPane().removeAll();

			gameDisplay = null;
			editorDisplay = null;
			inputHandler = null;

			menuDisplay = new MenuDisplay();
			menuDisplay.setMinimumSize(new Dimension(300, 400));

			window.getContentPane().add(menuDisplay, BorderLayout.CENTER);
			window.pack();
			window.setSize(currentSize);
		} else {
			menuDisplay.repaint();
		}

		showVersionWarning();
	}

	private static int getJavaVersion() {
		String version = System.getProperty("java.version");
		if(version.startsWith("1.")) {
			version = version.substring(2, 3);
		} else {
			int dot = version.indexOf(".");
			if(dot != -1) { version = version.substring(0, dot); }
		} return Integer.parseInt(version);
	}

	private void showVersionWarning() {
		if (!versionWarningShown && getJavaVersion() < 21) {
			JOptionPane.showMessageDialog(null, "It looks like you are using an old Java " +
					"version. If you encounter any bugs, especially graphical artifacts, consider upgrading to at least Java 21.",
					"Old Java version", JOptionPane.WARNING_MESSAGE);
		}

		versionWarningShown = true;
	}

	public JFrame getWindow() {
		return window;
	}

	public void setWindow(JFrame window) {
		window = window;
	}

	public EditorDisplay getEditorDisplay() {
		return editorDisplay;
	}

	public void setEditorDisplay(EditorDisplay editorDisplay) {
		editorDisplay = editorDisplay;
	}

	public EventObserver getInputHandler() {
		return inputHandler;
	}

	public void setInputHandler(EventObserver inputHandler) {
		this.inputHandler = inputHandler;
	}
}