package IO;

import javax.imageio.ImageIO;
import javax.swing.*;

import enums.Graphics;
import gamemanager.GameManager;
import map.Map;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GraphicIO extends JPanel implements IOStrategy {
	private static JFrame window;

	private GameManager gameManager;
	private Map map;

	private BufferedImage[][] images;
	private BufferedImage[][] images2;

	public void draw () {
		this.gameManager = GameManager.getInstance();
		this.map = gameManager.getMap();

		this.images = new BufferedImage[this.map.getWidth()][this.map.getHeight()];
		this.images2 = new BufferedImage[this.map.getWidth()][this.map.getHeight()];

		for (int i = 0; i < map.getWidth(); i++) {
			for (int j = 0; j < map.getHeight(); j++) {
				try {
					switch (this.map.getBottomLayer(i, j).getGraphicsID()) {
						case Graphics.FLOOR:
							images[i][j] = ImageIO.read(new File("src\\graphics\\floor.png"));
							break;
						case Graphics.WALL:
							images[i][j] = ImageIO.read(new File("src\\graphics\\wall.png"));
							break;
						case Graphics.BOX:
							images[i][j] = ImageIO.read(new File("src\\graphics\\box.png"));
							break;
						case Graphics.PLAYER:
							images[i][j] = ImageIO.read(new File("src\\graphics\\player.png"));
							break;
						case Graphics.ENEMY:
							images[i][j] = ImageIO.read(new File("src\\graphics\\enemy.png"));
							break;
						case Graphics.BUTTON_PRESSED:
							images[i][j] = ImageIO.read(new File("src\\graphics\\button_pressed.png"));
							break;
						case Graphics.BUTTON_RELEASED:
							images[i][j] = ImageIO.read(new File("src\\graphics\\button_released.png"));
							break;
						case Graphics.DOOR_OPEN:
							images[i][j] = ImageIO.read(new File("src\\graphics\\door_open.png"));
							break;
						case Graphics.DOOR_CLOSED:
							images[i][j] = ImageIO.read(new File("src\\graphics\\door_closed.png"));
							break;
						case Graphics.GOAL:
							images[i][j] = ImageIO.read(new File("src\\graphics\\goal.png"));
							break;
						default:
							images[i][j] = ImageIO.read(new File("src\\graphics\\floor.png"));
							break;
					}
				} catch (IOException ignored) {

				}
			}
		}

		for (int i = 0; i < map.getWidth(); i++) {
			for (int j = 0; j < map.getHeight(); j++) {
				if (map.getUpperLayer(i, j) != null) {
					try {
						switch (this.map.getUpperLayer(i, j).getGraphicsID()) {
							case Graphics.FLOOR:
								images2[i][j] = ImageIO.read(new File("src\\graphics\\floor.png"));
								break;
							case Graphics.WALL:
								images2[i][j] = ImageIO.read(new File("src\\graphics\\wall.png"));
								break;
							case Graphics.BOX:
								images2[i][j] = ImageIO.read(new File("src\\graphics\\box.png"));
								break;
							case Graphics.PLAYER:
								images2[i][j] = ImageIO.read(new File("src\\graphics\\player.png"));
								break;
							case Graphics.ENEMY:
								images2[i][j] = ImageIO.read(new File("src\\graphics\\enemy.png"));
								break;
							case Graphics.BUTTON_PRESSED:
								images2[i][j] = ImageIO.read(new File("src\\graphics\\button_pressed.png"));
								break;
							case Graphics.BUTTON_RELEASED:
								images2[i][j] = ImageIO.read(new File("src\\graphics\\button_released.png"));
								break;
							case Graphics.DOOR_OPEN:
								images2[i][j] = ImageIO.read(new File("src\\graphics\\door_open.png"));
								break;
							case Graphics.DOOR_CLOSED:
								images2[i][j] = ImageIO.read(new File("src\\graphics\\door_closed.png"));
								break;
							case Graphics.GOAL:
								images2[i][j] = ImageIO.read(new File("src\\graphics\\goal.png"));
								break;
							default:
								images2[i][j] = ImageIO.read(new File("src\\graphics\\floor.png"));
								break;
						}
					} catch (IOException ignored) {

					}
				}
			}
		}

		if (window == null) {
			window = new JFrame("Nasza cudowna gra");
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setVisible(true);
		}

		this.setPreferredSize(new Dimension(32 * this.map.getWidth(), 32 * this.map.getHeight()));
		window.getContentPane().add(this, BorderLayout.CENTER);
		window.pack();
	}

	@Override
	protected void paintComponent(java.awt.Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		for (int i = 0;i < this.map.getWidth();i++) {
			for (int j = 0;j < this.map.getHeight();j++) {
				g.drawImage(images[i][j], i * 32, j * 32, this);
				if (this.images2[i][j] != null) {
					g.drawImage(images2[i][j], i * 32, j * 32, this);
				}
			}
		}
	}
}