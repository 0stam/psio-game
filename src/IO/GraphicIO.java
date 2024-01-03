package IO;

import javax.swing.*;

import display.EditorMapDisplay;
import display.GameMapDisplay;
import display.TilePalette;
import enums.Direction;
import event.MoveEvent;
import gamemanager.GameManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GraphicIO implements IOStrategy, KeyListener {
	private static JFrame window;
	private static EditorMapDisplay editorDisplay;
	private static GameMapDisplay gameDisplay;

	public void drawGame () {

		if (gameDisplay == null) {
			//gameDisplay = new gameDisplayDisplay();
			//gameDisplay.setPreferredSize(new Dimension(32 * GameManager.getInstance().getgameDisplay().getWidth()-1, 32 * GameManager.getInstance().getgameDisplay().getHeight()));


			gameDisplay = new GameMapDisplay();
			gameDisplay.setPreferredSize(new Dimension(32 * gameDisplay.getW()-1, 32 * gameDisplay.getH()));
			gameDisplay.setupMap(GameManager.getInstance().getMap());
		} else {
			gameDisplay.setupMap(GameManager.getInstance().getMap());
			gameDisplay.repaint();
		}

		if (window == null) {
			window = new JFrame("Nasza cudowna gra");
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.addKeyListener(this);

			window.getContentPane().add(gameDisplay.getContainer(), BorderLayout.CENTER);
			window.pack();

			window.setVisible(true);
		}
	}

	public void drawEditor () {
		if (editorDisplay == null) {
			editorDisplay = new EditorMapDisplay();
			editorDisplay.setPreferredSize(new Dimension(32 * GameManager.getInstance().getMap().getWidth()-1, 32 * GameManager.getInstance().getMap().getHeight()));
		} else {
			editorDisplay.refreshMap();
		}

		if (window == null) {
			window = new JFrame("Nasza cudowna gra");
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.addKeyListener(this);
			window.getContentPane().add(editorDisplay, BorderLayout.CENTER);
			window.getContentPane().add(new TilePalette(), BorderLayout.SOUTH);
			window.pack();

			window.setVisible(true);
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
}