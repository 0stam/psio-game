package enterablestrategy;
import tile.*;

public interface EnterableStrategy {
	public Boolean isEnterable();
	public void onEntered(String direction,Tile tile);
	public void onExited(String direction, Tile tile);
}
