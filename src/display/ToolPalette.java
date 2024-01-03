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

public class ToolPalette extends JPanel {
	private BufferedImage[] images;
	private ImageButton[] buttons;

	public ToolPalette() {
		images = new BufferedImage[5];
		buttons = new ImageButton[images.length];

		try {
			images[0] = ImageIO.read(new File("src/graphics/tool_both.png"));
			images[1] = ImageIO.read(new File("src/graphics/tool_front.png"));
			images[2] = ImageIO.read(new File("src/graphics/tool_bottom.png"));
			images[3] = ImageIO.read(new File("src/graphics/tool_save.png"));
			images[4] = ImageIO.read(new File("src/graphics/tool_load.png"));
		} catch (IOException ignored) {

		}

		this.setLayout(new GridLayout(images.length, 1, 0, 0));

		for (int i = 0;i < images.length;i++) {
			buttons[i] = new ImageButton(images[i], "", new Point(0, i));
			this.add(buttons[i].getContainer());
		}
	}

	@Override
	protected void paintComponent(java.awt.Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		float scale = (float) this.getHeight() / (32.0f * images.length);

		for (int i = 0;i < buttons.length;i++) {
			((ImageButton)buttons[i]).setScale(scale / 2);
		}

		this.setPreferredSize(new Dimension((int) (32 * scale), (int) (32 * scale * images.length)));
		this.revalidate();
	}
}