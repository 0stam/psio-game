package map;

import IO.GraphicsEnum;

public class Goal extends Tile {
	public Goal () {
		super();

		this.setGraphicsId(GraphicsEnum.GOAL);
	}

	public Goal (int x, int y) {
		super(x, y);

		this.setGraphicsId(GraphicsEnum.GOAL);
	}
}