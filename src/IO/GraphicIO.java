package IO;

import javax.swing.*;
import javax.swing.border.Border;

import enums.Direction;
import event.InputEvent;
import event.MoveEvent;
import gamemanager.GameManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GraphicIO implements IOStrategy, KeyListener {
	private static JFrame window;
	private static MapDisplay map;

	public void drawGame () {
		if (map == null) {
			map = new MapDisplay();
			map.setPreferredSize(new Dimension(32 * GameManager.getInstance().getMap().getWidth()-1, 32 * GameManager.getInstance().getMap().getHeight()));
			map.setupMap(GameManager.getInstance().getMap());
		} else {
			map.setupMap(GameManager.getInstance().getMap());
			map.repaint();
		}

		if (window == null) {
			window = new JFrame("Nasza cudowna gra");
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.addKeyListener(this);

			window.getContentPane().add(map.getContainer(), BorderLayout.CENTER);
			window.pack();

			window.setVisible(true);
		}
	}

	public void drawEditor () {
		/*if (map == null) {
			map = new MapDisplay();
			map.setPreferredSize(new Dimension(32 * GameManager.getInstance().getMap().getWidth(), 32 * GameManager.getInstance().getMap().getHeight()));
			map.setupMap(GameManager.getInstance().getMap());
		} else {
			map.setupMap(GameManager.getInstance().getMap());
			map.repaint();
		}

		if (window == null) {
			window = new JFrame("Nasza cudowna gra");
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.addKeyListener(this);
			window.getContentPane().add(map, BorderLayout.CENTER);
			window.pack();

			window.setVisible(true);
		}*/
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
}