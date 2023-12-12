package enterablestrategy;
import tile.*;
import enums.Direction;

public interface EnterableStrategy {
	public Boolean isEnterable();
	public void onEntered(Direction direction,Tile tile);
	public void onExited(Direction direction, Tile tile);
}
