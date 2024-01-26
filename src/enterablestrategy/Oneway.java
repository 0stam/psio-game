package enterablestrategy;
import tile.*;
import enums.Direction;
import java.io.Serializable;

public class Oneway implements EnterableStrategy, Serializable{
	private Direction block;

	public Oneway (Direction direction) {
		block = direction;
	}

	public boolean isEnterable(Direction direction, Tile tile){
		return (direction != block);
	}

	public void onEntered(Direction direction, Tile tile){
		return;
	}

	public void onExited(Direction direction, Tile tile){
		return;
	}
}