package IO;

import javax.swing.*;

import display.*;
import enums.Direction;
import event.MoveEvent;
import gamemanager.GameManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GraphicIO implements IOStrategy {
	private static JFrame window;
	private static EditorDisplay editorDisplay;
	private static GameMapDisplay gameDisplay;
	private static MenuDisplay menuDisplay;

	private void createWindow () {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		}
		catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
			   IllegalAccessException e) {

		}

		window = new JFrame("Nasza cudowna gra");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		window.setIconImage(new ImageIcon("src/graphics/icon.png").getImage());
		//i tak nie ma sensu robic mniejszego
		window.setMinimumSize(new Dimension(300, 400));
	}

	public void drawGame () {
		if (window == null) {
			createWindow();
		}

		if (gameDisplay == null) {
			Dimension currentSize = window.getSize();
			window.getContentPane().removeAll();

			editorDisplay = null;
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

			editorDisplay = new EditorDisplay();
			editorDisplay.setMinimumSize(new Dimension(32 * GameManager.getInstance().getMap().getWidth()-1, 32 * GameManager.getInstance().getMap().getHeight()));

			window.getContentPane().add(editorDisplay, BorderLayout.CENTER);
			window.pack();
			window.setSize(currentSize);
		} else {
			// FIXME: add proper editor refresh method
			editorDisplay.getEditorMapDisplay().refreshMap();
			editorDisplay.repaint();
		}
	}

	//gotowy placeholder
	public void drawMenu() {
		if (window == null) {
			createWindow();
		}

		if (menuDisplay == null) {
			Dimension currentSize = window.getSize();
			window.getContentPane().removeAll();

			gameDisplay = null;
			editorDisplay = null;

			menuDisplay = new MenuDisplay();
			menuDisplay.setMinimumSize(new Dimension(300, 400));

			window.getContentPane().add(menuDisplay, BorderLayout.CENTER);
			window.pack();
			window.setSize(currentSize);
		} else {
			menuDisplay.repaint();
		}
	}

	public static JFrame getWindow() {
		return window;
	}

	public static void setWindow(JFrame window) {
		GraphicIO.window = window;
	}

	public static EditorDisplay getEditorDisplay() {
		return editorDisplay;
	}

	public static void setEditorDisplay(EditorDisplay editorDisplay) {
		GraphicIO.editorDisplay = editorDisplay;
	}
}