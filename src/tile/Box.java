package tile;

import enterablestrategy.Dummy;
import enums.Graphics;

public class Box extends Tile {
	public Box (int x, int y) {
		super(x, y);
		this.setEnterableStrategy(new Dummy());
		this.setGraphicsID(Graphics.BOX);
	}
}