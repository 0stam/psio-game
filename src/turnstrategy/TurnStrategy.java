package turnstrategy;
import tile.*;
import enums.Direction;

public interface TurnStrategy {
	void onTurn(Direction direction, ActionTile owner);
}
