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
			case UP:
				this.setGraphicsID(Graphics.ONEWAY_UP);
				break;
			case DOWN:
				this.setGraphicsID(Graphics.ONEWAY_DOWN);
				break;
			case LEFT:
				this.setGraphicsID(Graphics.ONEWAY_LEFT);
				break;
			case RIGHT:
				this.setGraphicsID(Graphics.ONEWAY_RIGHT);
				break;
			default:
				this.setGraphicsID(Graphics.ONEWAY_UP);
				break;
		}
	}

	public Direction getDirection() {
		return direction;
	}
}