package turnstrategy;

import enums.Direction;
import tile.ActionTile;

public class Idle implements TurnStrategy{
    @Override
    public void onTurn(Direction direction, ActionTile tile) {}
}
