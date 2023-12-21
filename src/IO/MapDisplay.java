package IO;

import map.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Hashtable;

import static enums.Graphics.*;

public class MapDisplay extends JPanel {
	private Hashtable<enums.Graphics, String> fileNames;

	private BufferedImage[][] images;
	private BufferedImage[][] images2;
	private JPanel container = new JPanel();
	public void setupMap (Map map) {
		if (this.fileNames == null) {
			setupFileNames();
		}

		this.images = new BufferedImage[map.getWidth()][map.getHeight()];
		this.images2 = new BufferedImage[map.getWidth()][map.getHeight()];

		for (int i = 0; i < map.getWidth(); i++) {
			for (int j = 0; j < map.getHeight(); j++) {
				try {
					if (map.getBottomLayer(i, j) != null) {
						images[i][j] = ImageIO.read(new File(this.fileNames.get(map.getBottomLayer(i, j).getGraphicsID())));
					} else {
						images[i][j] = ImageIO.read(new File(this.fileNames.get(DEFAULT)));
					}

					if (map.getUpperLayer(i, j) != null) {
						images2[i][j] = ImageIO.read(new File(this.fileNames.get(map.getUpperLayer(i,j).getGraphicsID())));
					}
				} catch (IOException ignored) {

				}
			}
		}
		container.add(this);
		container.setBorder(BorderFactory.createEmptyBorder());
	}

	@Override
	protected void paintComponent(java.awt.Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		float scaleX = (float) container.getWidth() / (32.0f * images.length);
		float scaleY = (float) container.getHeight() / (32.0f * images[0].length);

		float scale = Math.min(scaleX, scaleY);

		for (int i = 0;i < images.length;i++) {
			for (int j = 0;j < images[0].length;j++) {
				g.drawImage(images[i][j].getScaledInstance((int) (32 * scale), (int) (32 * scale), Image.SCALE_AREA_AVERAGING), i * ((int) (32 * scale)), j * ((int) (32 * scale)), this);
				if (this.images2[i][j] != null) {
					g.drawImage(images2[i][j].getScaledInstance((int) (32 * scale), (int) (32 * scale), Image.SCALE_AREA_AVERAGING), i * ((int) (32 * scale)), j * ((int) (32 * scale)), this);
				}
			}
		}
		this.setPreferredSize(new Dimension((int) (32 * scale * images.length), (int) (32 * scale * images[0].length)));
		this.revalidate();
	}

	private void setupFileNames ()	{
		this.fileNames = new Hashtable<>();

		this.fileNames.put(FLOOR, "src/graphics/floor.png");
		this.fileNames.put(WALL, "src/graphics/wall.png");
		this.fileNames.put(BOX, "src/graphics/box.png");
		switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
			case Calendar.MONDAY -> this.fileNames.put(PLAYER, "src/graphics/player_mon.png");
			case Calendar.TUESDAY -> this.fileNames.put(PLAYER, "src/graphics/player_tue.png");
			case Calendar.WEDNESDAY -> this.fileNames.put(PLAYER, "src/graphics/player_wed.png");
			case Calendar.THURSDAY -> this.fileNames.put(PLAYER, "src/graphics/player_thu.png");
			case Calendar.FRIDAY -> this.fileNames.put(PLAYER, "src/graphics/player_fri.png");
			default -> this.fileNames.put(PLAYER, "src/graphics/player_wek.png");
		}
		this.fileNames.put(ENEMY, "src/graphics/enemy.png");
		this.fileNames.put(BUTTON_PRESSED, "src/graphics/button_pressed.png");
		this.fileNames.put(BUTTON_RELEASED, "src/graphics/button_released.png");
		this.fileNames.put(DOOR_OPEN, "src/graphics/door_open.png");
		this.fileNames.put(DOOR_CLOSED, "src/graphics/door_closed.png");
		this.fileNames.put(GOAL, "src/graphics/goal.png");
		this.fileNames.put(DEFAULT, "src/graphics/default.png");
	}

	public JPanel getContainer() {
		return container;
	}

	public void setContainer(JPanel container) {
		this.container = container;
		return;
	}
}
