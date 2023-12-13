package enterablestrategy;
import tile.*;
import enums.Direction;

public interface EnterableStrategy {
	public boolean isEnterable(Direction direction, Tile tile);
	public void onEntered(Direction direction,Tile tile);
	public void onExited(Direction direction, Tile tile);
}
