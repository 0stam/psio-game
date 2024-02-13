package tile;

import enterablestrategy.Solid;
import enums.Graphics;
import turnstrategy.PlayerMovement;

import java.io.Serializable;

public class MimicEnemy extends ActionTile<PlayerMovement> implements Serializable {
	public MimicEnemy(int x, int y) {
		super(x, y, 2);
		setGraphicsID(Graphics.MIMIC);
		setEnterableStrategy(new Solid());
		setTurnStrategy(new PlayerMovement());
	}
}
