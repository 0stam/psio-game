package map;

import IO.GraphicsEnum;

public class Wall extends Tile {
	public Wall () {
		super();

		this.setGraphicsId(GraphicsEnum.WALL);
	}

	public Wall (int x, int y) {
		super(x, y);

		this.setGraphicsId(GraphicsEnum.WALL);
	}
}