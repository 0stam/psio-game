package map;

import enums.EditableTile;
import enums.EditorGraphics;
import enums.Graphics;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class NewRawMap implements Serializable {
	private static final long serialVersionUID = -6277955962762571597L;
	private int width;
	private int height;

	private EditableTile[][] bottomLayer;
	private EditableTile[][] topLayer;

	private ArrayList<RawConnection> connections;
	private ArrayList<RawPath> paths;

	private ArrayList<RawMessage> messages;

	public NewRawMap(int width, int height) {
		this.width = width;
		this.height = height;

		bottomLayer = new EditableTile[this.width][this.height];
		topLayer = new EditableTile[this.width][this.height];

		connections = new ArrayList<>();
		paths = new ArrayList<>();
		messages = new ArrayList<>();
	}

	public EditableTile getBottom (int x, int y) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			return bottomLayer[x][y];
		} else {
			return null;
		}
	}

	public void setBottom (int x, int y, EditableTile tile) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			bottomLayer[x][y] = tile;
		}
	}

	public EditableTile getTop (int x, int y) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			return topLayer[x][y];
		} else {
			return null;
		}
	}

	public void setTop (int x, int y, EditableTile tile) {
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

	public ArrayList<RawConnection> getConnections () {
		return connections;
	}

	public void addConnection (RawConnection connection) {
		connections.add(connection);
	}

	public ArrayList<RawPath> getPaths () {
		return paths;
	}

	public void addPath (RawPath path) {
		paths.add(path);
	}

	public ArrayList<RawMessage> getMessages () {
		return messages;
	}

	public void addMessage (RawMessage message) {
		messages.add(message);
	}
}
