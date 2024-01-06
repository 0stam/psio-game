package turnstrategy;
import enums.Direction;
import gamemanager.GameManager;
import tile.ActionTile;
import java.io.Serializable;

public class PlayerMovement implements TurnStrategy, Serializable{
    @Override
    public void onTurn(Direction direction, ActionTile player)
    {
        GameManager.getInstance().getMap().move(player.getX(), player.getY(), direction);
    }

}
