package tile;

import enterablestrategy.Dummy;
import enums.Graphics;

public class Goal extends Tile {
	public Goal (int x, int y) {
		super(x, y);
		this.setEnterableStrategy(new Dummy());
		this.setGraphicsID(Graphics.GOAL);
	}
}