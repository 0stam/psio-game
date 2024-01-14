package map;

import tile.PathTile;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public class RawPath implements Serializable {
	private Point enemyPos;

	private ArrayList<PathTile> path;
	RawPath() {}
	RawPath(Point enemyPos, ArrayList<PathTile> path)
	{
		this.enemyPos = enemyPos;
		this.path = path;
	}
	//gettery i settery
}
