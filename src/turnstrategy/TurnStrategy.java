package turnstrategy;
import tile.*;
import enums.Direction;

public interface TurnStrategy {
	public void onTurn(Direction direction);
}
