package tile;

import enterablestrategy.Empty;
import enterablestrategy.Oneway;
import enums.Direction;
import enums.Graphics;

public class OnewayFloor extends Tile {
	private Direction direction;

	public OnewayFloor (int x, int y, Direction direction) {
		super(x, y);
		this.direction = direction;
		this.setEnterableStrategy(new Oneway(direction));
		switch (direction) {
			case UP -> this.setGraphicsID(Graphics.ONEWAY_UP);
			case DOWN -> this.setGraphicsID(Graphics.ONEWAY_DOWN);
			case LEFT -> this.setGraphicsID(Graphics.ONEWAY_LEFT);
			case RIGHT -> this.setGraphicsID(Graphics.ONEWAY_RIGHT);
			default -> this.setGraphicsID(Graphics.ONEWAY_UP);
		}
	}

	public Direction getDirection() {
		return direction;
	}
}