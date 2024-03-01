package tile;
//import javax.lang.model.element.ModuleElement.Directive;

import enterablestrategy.EnterableStrategy;
import enums.Direction;
import enums.Graphics;
import java.io.Serializable;

public abstract class Tile implements Serializable {
	private int x;
	private int y;
	private Graphics graphicsID;
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

	public Graphics getGraphicsID() {return graphicsID;}

	public void setGraphicsID(Graphics graphicsID) {this.graphicsID = graphicsID;}

	public EnterableStrategy getEnterableStrategy() {
		return enterableStrategy;
	}

	public void setEnterableStrategy(EnterableStrategy enterableStrategy) {
		this.enterableStrategy = enterableStrategy;
	}
	//metody wywołujące metody z interfejsów
	
	public boolean isEnterable(Direction direction, Tile tile)
	{
		return enterableStrategy.isEnterable(direction, tile);
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
