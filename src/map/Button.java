package map;

import IO.GraphicsEnum;
import java.util.Random;

public class Button extends Tile {
	public Button () {
		super();

		Random generator = new Random();
		if (generator.nextInt() % 2 == 0) {
			this.setGraphicsId(GraphicsEnum.BUTTON_PRESSED);
		} else {
			this.setGraphicsId(GraphicsEnum.BUTTON_RELEASED);
		}
	}

	public Button (int x, int y) {
		super(x, y);

		Random generator = new Random();
		if (generator.nextInt() % 2 == 0) {
			this.setGraphicsId(GraphicsEnum.BUTTON_PRESSED);
		} else {
			this.setGraphicsId(GraphicsEnum.BUTTON_RELEASED);
		}
	}
}