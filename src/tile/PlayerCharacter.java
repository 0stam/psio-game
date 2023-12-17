package tile;

import enterablestrategy.Solid;
import enums.Graphics;
import turnstrategy.PlayerMovement;

public class PlayerCharacter extends ActionTile{
    public PlayerCharacter(int x, int y) {
        super(x, y, 100);
        setGraphicsID(Graphics.PLAYER);
        setEnterableStrategy(new Solid());
        setTurnStrategy(new PlayerMovement());
    }
}
