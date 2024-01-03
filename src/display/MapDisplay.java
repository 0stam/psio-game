package display;

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
	private BufferedImage[][] images;
	private BufferedImage[][] images2;
	private JPanel container = new JPanel();
	private enums.Graphics[][] prevMapBottom;
	private enums.Graphics[][] prevMapFront;

	//dla latwosci dostepu zdjecia o tych "defaultowych" wymiarach
	public static final int imgWidth = 15;
	public static final int imgHeight = 15;
	public void setupMap (Map map) {

		this.images = new BufferedImage[map.getWidth()][map.getHeight()];
		this.images2 = new BufferedImage[map.getWidth()][map.getHeight()];

		for (int i = 0; i < map.getWidth(); i++) {
			for (int j = 0; j < map.getHeight(); j++) {
				if (map.getBottomLayer(i, j) != null) {
					images[i][j] = GraphicsHashtable.getInstance().getImage(map.getBottomLayer(i, j).getGraphicsID());
				} else {
					images[i][j] = GraphicsHashtable.getInstance().getImage(DEFAULT);
				}
				if (map.getUpperLayer(i, j) != null) {
					images2[i][j] = GraphicsHashtable.getInstance().getImage(map.getUpperLayer(i,j).getGraphicsID());
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

	public JPanel getContainer() {
		return container;
	}

	public void setContainer(JPanel container) {
		this.container = container;
	}
}
