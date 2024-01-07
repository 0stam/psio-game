package IO;

import javax.swing.*;

import display.*;
import enums.Direction;
import event.MoveEvent;
import gamemanager.GameManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GraphicIO implements IOStrategy, KeyListener{
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
		window.addKeyListener(this);
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
			window.getContentPane().removeAll();

			editorDisplay = null;
			//menuDisplay = null;

			gameDisplay = new GameMapDisplay();
			gameDisplay.setPreferredSize(new Dimension(32 * gameDisplay.getW()-1, 32 * gameDisplay.getH()));
			gameDisplay.setupMap(GameManager.getInstance().getMap());

			window.getContentPane().add(gameDisplay.getContainer(), BorderLayout.CENTER);
			window.pack();
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
			window.getContentPane().removeAll();

			gameDisplay = null;
			//menuDisplay = null;

			editorDisplay = new EditorDisplay();
			editorDisplay.setPreferredSize(new Dimension(32 * GameManager.getInstance().getMap().getWidth()-1, 32 * GameManager.getInstance().getMap().getHeight()));

			window.getContentPane().add(editorDisplay, BorderLayout.CENTER);
			window.pack();
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
			window.getContentPane().removeAll();

			gameDisplay = null;
			editorDisplay = null;

			menuDisplay = new MenuDisplay();
			menuDisplay.setPreferredSize(new Dimension(300, 400));

			window.getContentPane().add(menuDisplay, BorderLayout.CENTER);
			window.pack();
		} else {
			menuDisplay.repaint();
		}
	}







	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Direction direction = switch (e.getKeyCode()) {
			case 'w', 'W', KeyEvent.VK_UP -> Direction.UP;
			case 'a', 'A', KeyEvent.VK_LEFT -> Direction.LEFT;
			case 's', 'S', KeyEvent.VK_DOWN -> Direction.DOWN;
			case 'd', 'D', KeyEvent.VK_RIGHT -> Direction.RIGHT;
			default -> Direction.DEFAULT;
		};

		if (!GameManager.getInstance().getLevelCompleted() && direction != Direction.DEFAULT) {
			GameManager.getInstance().onEvent(new MoveEvent(direction));
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
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