package map;

import tile.PathTile;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public class RawPath implements Serializable {
	private Point enemyPos;

	private ArrayList<PathTile> path;

	RawPath() {
		enemyPos = null;
		path = new ArrayList<>();
	}
	RawPath(Point enemyPos, ArrayList<PathTile> path)
	{
		this.enemyPos = enemyPos;
		this.path = path;
	}
	//gettery i settery


	public Point getEnemyPos() {
		return enemyPos;
	}

	public void setEnemyPos(Point enemyPos) {
		this.enemyPos = enemyPos;
	}

	public ArrayList<PathTile> getPath() {
		return path;
	}

	public void addPath (PathTile tile) {
		path.add(tile);
	}
}
