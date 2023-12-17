package tile;

import enterablestrategy.Solid;
import enums.Graphics;
import turnstrategy.PlayerFollower;

public class ChasingEnemy extends ActionTile {
    public ChasingEnemy(int x, int y, Tile player) {
        super(x, y, 1);
        setGraphicsID(Graphics.ENEMY);
        setEnterableStrategy(new Solid());
        setTurnStrategy(new PlayerFollower(player));
    }
}
