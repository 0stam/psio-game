package tile;
import enterablestrategy.*;
import turnstrategy.*;
import enums.Direction;

public class ActionTile extends Tile implements Comparable<ActionTile> {
	private TurnStrategy turnStrategy;
	private int priority;
	
	//konstruktor
	
	public ActionTile(int x, int y, int priority)
	{
		super(x,y);
		this.priority = priority;
	}
	
	//gettery settery

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	//metody wywołujące metody z interfejsu
	
	public void onTurn(Direction direction)
	{
		//turnStrategy.onTurn(direction);
	}
	
	//compare
	
	public int compareTo(ActionTile tile2)
	{
		Integer temp = this.getPriority();
		return temp.compareTo(tile2.getPriority());
	}
}
