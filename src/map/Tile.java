package map;

import IO.GraphicsEnum;

public abstract class Tile {
	private int x;
	private int y;
	private GraphicsEnum graphicsId;

	public Tile () {
		this.x = 0;
		this.y = 0;

		this.graphicsId = GraphicsEnum.DEFAULT;
	}

	public Tile (int x, int y) {
		this.x = x;
		this.y = y;
		this.graphicsId = GraphicsEnum.DEFAULT;
	}

	public int getX() {
		return x;
	}

	public Tile setX(int x) {
		this.x = x;
		return this;
	}

	public int getY() {
		return y;
	}

	public Tile setY(int y) {
		this.y = y;
		return this;
	}

	public GraphicsEnum getGraphicsId() {
		return graphicsId;
	}

	public Tile setGraphicsId(GraphicsEnum graphicsId) {
		this.graphicsId = graphicsId;
		return this;
	}
}
