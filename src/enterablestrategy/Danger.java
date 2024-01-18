package enterablestrategy;
import tile.*;
import enums.Direction;
import java.io.Serializable;

public class Danger implements EnterableStrategy, Serializable{
	public boolean isEnterable(Direction direction, Tile tile){
		return (tile instanceof PlayerCharacter);
	}

	public void onEntered(Direction direction, Tile tile){
		return;
	}

	public void onExited(Direction direction, Tile tile){
		return;
	}
}