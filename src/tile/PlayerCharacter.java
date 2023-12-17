package tile;

import enums.Graphics;
import turnstrategy.PlayerMovement;

public class PlayerCharacter extends ActionTile{
    public PlayerCharacter(int x, int y) {
        super(x, y, 100);
        this.setGraphicsID(Graphics.PLAYER);
        this.setTurnStrategy(new PlayerMovement());
    }
}
