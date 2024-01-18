package tile;

import enterablestrategy.Danger;
import enterablestrategy.Empty;
import enums.Graphics;

public class DangerFloor extends Tile {
	public DangerFloor (int x, int y) {
		super(x, y);
		this.setEnterableStrategy(new Danger());
		this.setGraphicsID(Graphics.DANGER);
	}
}