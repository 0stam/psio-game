package tile;

import enterablestrategy.Pushable;
import enums.Graphics;

public class Box extends Tile {
	public Box (int x, int y) {
		super(x, y);
		this.setEnterableStrategy(new Pushable());
		this.setGraphicsID(Graphics.BOX);
	}
}