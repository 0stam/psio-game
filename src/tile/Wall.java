package tile;

import enums.Graphics;

public class Wall extends Tile {
	public Wall (int x, int y) {
		super(x, y);

		this.setGraphicsID(Graphics.WALL);
	}
}