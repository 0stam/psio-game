package display;

import map.Map;
import tile.PlayerCharacter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import static enums.Graphics.*;

public class GameMapDisplay extends JPanel {
	private BufferedImage[][] images;
	private BufferedImage[][] images2;
	private JPanel container = new JPanel();
	private enums.Graphics[][] prevMapBottom;
	private enums.Graphics[][] prevMapFront;
	private final int width = 15;
	private final int height = 15;
	private int playerX = -1;
	private int playerY = -1;

	public GameMapDisplay () {
		prevMapBottom = new enums.Graphics[width][height];
		prevMapFront = new enums.Graphics[width][height];
		this.images = new BufferedImage[width][height];
		this.images2 = new BufferedImage[width][height];
	}

	public void setupMap (Map map) {
		playerSearch (map);

		int x = 0;
		int y = 0;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				x = i + playerX - width / 2;
				y = j + playerY - height / 2;

				if (x < 0 || y < 0 || x >= map.getWidth() || y >= map.getHeight()) {
					images[i][j] = GraphicsHashtable.images.get(WALL);
					prevMapBottom[i][j] = WALL;
					images2[i][j] = null;
					prevMapFront[i][j] = null;
				} else {
					if ((map.getBottomLayer(x, y).getGraphicsID() != ((prevMapBottom[i][j] == null) ? DEFAULT : prevMapBottom[i][j])) || images[i][j] == null) {
						if (map.getBottomLayer(x, y) != null) {
							images[i][j] = GraphicsHashtable.images.get(map.getBottomLayer(x, y).getGraphicsID());
							prevMapBottom[i][j] = map.getBottomLayer(x, y).getGraphicsID();
						} else {
							images[i][j] = GraphicsHashtable.images.get(DEFAULT);
							prevMapBottom[i][j] = DEFAULT;
						}
					}

					if ((images2[i][j] == null) || (((map.getUpperLayer(x, y) == null) ? DEFAULT : map.getUpperLayer(x, y).getGraphicsID()) != prevMapFront[i][j])) {
						if (map.getUpperLayer(x, y) != null) {
							images2[i][j] = GraphicsHashtable.images.get(map.getUpperLayer(x, y).getGraphicsID());
							prevMapFront[i][j] = map.getUpperLayer(x, y).getGraphicsID();
						} else {
							images2[i][j] = null;
							prevMapFront[i][j] = null;
						}
					}
				}
			}
		}

		container.add(this);
		container.setBorder(BorderFactory.createEmptyBorder());
	}

	private void playerSearch(Map map) {
		boolean found = false;

		if (playerX != -1 && playerY != -1) {
			if ((map.getUpperLayer(playerX, playerY) instanceof PlayerCharacter) || (map.getBottomLayer(playerX, playerY) instanceof PlayerCharacter)) {
				found = true;
			}

			if (playerX - 1 >= 0) {
				if ((map.getUpperLayer(playerX - 1, playerY) instanceof PlayerCharacter) || (map.getBottomLayer(playerX - 1, playerY) instanceof PlayerCharacter)) {
					playerX--;
					found = true;
				}
			}

			if (playerX + 1 < map.getWidth()) {
				if ((map.getUpperLayer(playerX + 1, playerY) instanceof PlayerCharacter) || (map.getBottomLayer(playerX + 1, playerY) instanceof PlayerCharacter)) {
					playerX++;
					found = true;
				}
			}

			if (playerY - 1 >= 0) {
				if ((map.getUpperLayer(playerX, playerY - 1) instanceof PlayerCharacter) || (map.getBottomLayer(playerX, playerY - 1) instanceof PlayerCharacter)) {
					playerY--;
					found = true;
				}
			}

			if (playerY + 1 < map.getHeight()) {
				if ((map.getUpperLayer(playerX, playerY + 1) instanceof PlayerCharacter) || (map.getBottomLayer(playerX, playerY + 1) instanceof PlayerCharacter)) {
					playerY++;
					found = true;
				}
			}
		}

		if (playerX == -1 || playerY == -1 || !found) {
			for (int i = 0; i < map.getWidth();i++) {
				for (int j = 0;j < map.getHeight();j++) {
					if ((map.getUpperLayer(i, j) instanceof PlayerCharacter) || (map.getBottomLayer(i, j) instanceof PlayerCharacter)) {
						playerX = i;
						playerY = j;
					}
				}
			}
		}
	}

	@Override
	protected void paintComponent(java.awt.Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		float scaleX = (float) container.getWidth() / (32.0f * width);
		float scaleY = (float) container.getHeight() / (32.0f * height);

		float scale = Math.min(scaleX, scaleY);

		for (int i = 0;i < width;i++) {
			for (int j = 0;j < height;j++) {
				g.drawImage(images[i][j].getScaledInstance((int) (32 * scale), (int) (32 * scale), Image.SCALE_AREA_AVERAGING), i * ((int) (32 * scale)), j * ((int) (32 * scale)), this);
				if (this.images2[i][j] != null) {
					g.drawImage(images2[i][j].getScaledInstance((int) (32 * scale), (int) (32 * scale), Image.SCALE_AREA_AVERAGING), i * ((int) (32 * scale)), j * ((int) (32 * scale)), this);
				}
			}
		}
		this.setPreferredSize(new Dimension((int) (32 * scale * width), (int) (32 * scale * height)));
		this.revalidate();
	}

	public JPanel getContainer() {
		return container;
	}

	public void setContainer(JPanel container) {
		this.container = container;
		return;
	}

	public int getW() {
		return width;
	}

	public int getH() {
		return height;
	}
}