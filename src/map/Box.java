package map;

import IO.GraphicsEnum;

public class Box extends Tile {
	public Box () {
		super();

		this.setGraphicsId(GraphicsEnum.BOX);
	}

	public Box (int x, int y) {
		super(x, y);

		this.setGraphicsId(GraphicsEnum.BOX);
	}
}