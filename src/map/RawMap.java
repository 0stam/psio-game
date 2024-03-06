package map;

import enums.Graphics;

import java.awt.*;
import java.io.Serializable;
import java.util.List;
import java.util.Hashtable;

public class RawMap implements Serializable {
	private int width;
	private int height;
	private Graphics[][] bottomLayer;
	private Graphics[][] topLayer;

	private Hashtable<Point, List<Point>> connections;

	public RawMap(int width, int height) {
		this.width = width;
		this.height = height;

		bottomLayer = new Graphics[this.width][this.height];
		topLayer = new Graphics[this.width][this.height];

		connections = new Hashtable();
	}

	public Graphics getBottom (int x, int y) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			return bottomLayer[x][y];
		} else {
			return null;
		}
	}

	public void setBottom (int x, int y, Graphics tile) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			bottomLayer[x][y] = tile;
		}
	}

	public Graphics getTop (int x, int y) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			return topLayer[x][y];
		} else {
			return null;
		}
	}

	public void setTop (int x, int y, Graphics tile) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			topLayer[x][y] = tile;
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Hashtable<Point, List<Point>> getConnections () {
		return connections;
	}

	public void addConnection (Point button, List<Point> doors) {
		connections.put(button, doors);
	}
}
