package tile;

import enterablestrategy.Empty;
import enums.Graphics;

public class Floor extends Tile {
	public Floor (int x, int y) {
		super(x, y);
		this.setEnterableStrategy(new Empty());
		this.setGraphicsID(Graphics.FLOOR);
	}
}