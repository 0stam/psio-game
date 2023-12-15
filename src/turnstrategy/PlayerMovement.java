package turnstrategy;
import enums.Direction;
import gamemanager.GameManager;
import tile.ActionTile;

public class PlayerMovement implements TurnStrategy{
    @Override
    public void onTurn(Direction direction, ActionTile player)
    {
        GameManager.getInstance().getMap().move(player.getX(), player.getY(), direction);
    }

}
