package tile;

import enterablestrategy.Solid;
import enums.Graphics;
import turnstrategy.Idle;
import turnstrategy.StateMachine;
import turnstrategy.TurnStrategy;

import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;

public class SmartEnemy extends ActionTile<TurnStrategy> {
	private ArrayList<PathTile> pathTileList;
	public SmartEnemy(int x, int y) {
		super(x, y, 1);
		setGraphicsID(Graphics.SMART);
		setEnterableStrategy(new Solid());
		setTurnStrategy(new StateMachine(pathTileList));
	}

	public ArrayList<PathTile> getPathTileList() {
		return pathTileList;
	}

	public void setPathTileList(ArrayList<PathTile> pathTileList) {
		this.pathTileList = pathTileList;
		setTurnStrategy(new StateMachine(pathTileList));
	}
	@Override
	public String toString()
	{
		return "Smart enemy (" + super.getX() + ", " + super.getY() + ")";
	}
}
