package tile;
import enterablestrategy.*;
import turnstrategy.*;
import enums.Direction;

public class ActionTile<T extends TurnStrategy> extends Tile implements Comparable<ActionTile> {
	private T turnStrategy;
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

	public T getTurnStrategy() {
		return turnStrategy;
	}

	public void setTurnStrategy(T turnStrategy) {
		this.turnStrategy = turnStrategy;
	}

	//metody wywołujące metody z interfejsu
	
	public void onTurn(Direction direction)
	{
		turnStrategy.onTurn(direction, this);
	}
	
	//compare
	
	public int compareTo(ActionTile tile2)
	{
		Integer temp = this.getPriority();
		return temp.compareTo(tile2.getPriority());
	}
}
