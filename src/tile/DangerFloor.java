package tile;

import enterablestrategy.Empty;
import enums.Graphics;

public class DangerFloor extends Tile {
	public DangerFloor (int x, int y) {
		super(x, y);
		this.setEnterableStrategy(new Empty());
		this.setGraphicsID(Graphics.DANGER);
	}
}