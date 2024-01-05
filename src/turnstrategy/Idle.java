package turnstrategy;

import enums.Direction;
import tile.ActionTile;
import java.io.Serializable;

public class Idle implements TurnStrategy, Serializable{
    @Override
    public void onTurn(Direction direction, ActionTile tile) {}
}
