package tile;

import enterablestrategy.Solid;
import enums.Graphics;
import turnstrategy.PlayerMovement;

public class MimicEnemy extends ActionTile {
	public MimicEnemy(int x, int y) {
		super(x, y, 1);
		setGraphicsID(Graphics.MIMIC);
		setEnterableStrategy(new Solid());
		setTurnStrategy(new PlayerMovement());
	}
}
