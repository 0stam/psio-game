package tile;

import enterablestrategy.Solid;
import enums.Graphics;

public class Wall extends Tile {
	public Wall (int x, int y) {
		super(x, y);
		this.setEnterableStrategy(new Solid());
		this.setGraphicsID(Graphics.WALL);
	}
}