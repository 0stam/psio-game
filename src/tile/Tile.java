package tile;
import javax.lang.model.element.ModuleElement.Directive;

import enterablestrategy.EnterableStrategy;
import enums.Direction;
public abstract class Tile {
	private int x;
	private int y;
	private String graphicsID;
	private EnterableStrategy enterableStrategy;
	
	//konstruktor, by w dziedziczących można było używać super(x,y);
	
	public Tile(int x, int y)
	{
		this.x=x;
		this.y=y;
	}
	
	//gettery i settery
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getGraphicsID() {
		return graphicsID;
	}

	public void setGraphicsID(String graphicsID) {
		this.graphicsID = graphicsID;
	}
	
	//metody wywołujące metody z interfejsów
	
	public Boolean isEnterable()
	{
		return enterableStrategy.isEnterable();
	}
	public void onEntered(Direction direction, Tile tile)
	{
		enterableStrategy.onEntered(direction, tile);
	}
	public void onExited(Direction direction, Tile tile)
	{
		enterableStrategy.onExited(direction,tile);
	}
}
