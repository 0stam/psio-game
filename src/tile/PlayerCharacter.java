package tile;

import enums.Graphics;
import turnstrategy.PlayerMovement;

public class PlayerCharacter extends ActionTile{
    public PlayerCharacter(int x, int y, int priority) {
        super(x, y, priority);
        this.setGraphicsID(Graphics.PLAYER);
        this.setTurnStrategy(new PlayerMovement());
    }
    public void updateCoords(int x, int y)
    {
        setX(x);
        setY(y);
    }
}
