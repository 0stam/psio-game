package tile;

import enterablestrategy.Dummy;
import enterablestrategy.LevelExit;
import enums.Graphics;

public class Goal extends Tile {
	public Goal (int x, int y) {
		super(x, y);
		this.setEnterableStrategy(new LevelExit());
		this.setGraphicsID(Graphics.GOAL);
	}
}