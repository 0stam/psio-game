package tile;

import enterablestrategy.Dummy;
import enums.Graphics;

public class Wall extends Tile {
	public Wall (int x, int y) {
		super(x, y);
		this.setEnterableStrategy(new Dummy());
		this.setGraphicsID(Graphics.WALL);
	}
}