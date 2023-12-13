package tile;

import enums.Graphics;

public class Floor extends Tile {
	public Floor (int x, int y) {
		super(x, y);

		this.setGraphicsID(Graphics.FLOOR);
	}
}