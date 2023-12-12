package map;

import IO.GraphicsEnum;

public class Floor extends Tile {
	public Floor () {
		super();

		this.setGraphicsId(GraphicsEnum.FLOOR);
	}

	public Floor (int x, int y) {
		super(x, y);

		this.setGraphicsId(GraphicsEnum.FLOOR);
	}
}