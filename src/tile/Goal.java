package tile;

import enums.Graphics;

public class Goal extends Tile {
	public Goal (int x, int y) {
		super(x, y);

		this.setGraphicsID(Graphics.GOAL);
	}
}