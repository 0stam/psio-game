package tile;

import enterablestrategy.Solid;
import enums.Graphics;
import turnstrategy.Idle;

public class SmartEnemy extends ActionTile {
	public SmartEnemy(int x, int y) {
		super(x, y, 1);
		setGraphicsID(Graphics.SMART);
		setEnterableStrategy(new Solid());
		setTurnStrategy(new Idle());
	}
}
