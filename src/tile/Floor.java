package tile;

import enterablestrategy.Dummy;
import enums.Graphics;

public class Floor extends Tile {
	public Floor (int x, int y) {
		super(x, y);
		this.setEnterableStrategy(new Dummy());
		this.setGraphicsID(Graphics.FLOOR);
	}
}