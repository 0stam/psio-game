package tile;

import enterablestrategy.Solid;
import enums.Graphics;
import turnstrategy.PlayerMovement;

public class PlayerCharacter extends ActionTile<PlayerMovement>{
    public PlayerCharacter(int x, int y) {
        super(x, y, 100);
        setGraphicsID(Graphics.PLAYER);
        setEnterableStrategy(new Solid());
        setTurnStrategy(new PlayerMovement());
    }
}
