package tile;

import enums.Graphics;

public class Box extends Tile {
	public Box (int x, int y) {
		super(x, y);

		this.setGraphicsID(Graphics.BOX);
	}
}