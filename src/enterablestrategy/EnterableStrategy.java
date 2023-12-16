package enterablestrategy;
import tile.*;
import enums.Direction;

public interface EnterableStrategy {
	// tile to tile wchodzacy, nie ten na ktory cos wchodzi
	public boolean isEnterable(Direction direction, Tile tile);
	public void onEntered(Direction direction,Tile tile);
	public void onExited(Direction direction, Tile tile);
}
