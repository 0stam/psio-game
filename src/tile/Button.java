package tile;

import enums.Graphics;
import java.util.Random;

public class Button extends Tile {
	public Button (int x, int y) {
		super(x, y);

		Random generator = new Random();
		if (generator.nextInt() % 2 == 0) {
			this.setGraphicsID(Graphics.BUTTON_PRESSED);
		} else {
			this.setGraphicsID(Graphics.BUTTON_RELEASED);
		}
	}
}