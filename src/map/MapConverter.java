package map;

import enums.Graphics;
import event.EventObserver;
import tile.*;
import tile.Button;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static enums.Graphics.*;

public class MapConverter {
	public static RawMap saveConvert (Map map) {
		Hashtable<String, Graphics>graphicsConvert = new Hashtable<>();

		graphicsConvert.put("Box", BOX);
		graphicsConvert.put("Button", BUTTON_RELEASED);
		graphicsConvert.put("ChasingEnemy", ENEMY);
		graphicsConvert.put("MimicEnemy", MIMIC);
		graphicsConvert.put("Door", DOOR_CLOSED);
		graphicsConvert.put("Floor", FLOOR);
		graphicsConvert.put("Goal", GOAL);
		graphicsConvert.put("PlayerCharacter", PLAYER);
		graphicsConvert.put("Wall", WALL);

		RawMap result = new RawMap(map.getWidth(), map.getHeight());

		for (int i = 0;i < map.getWidth();i++) {
			for (int j = 0;j < map.getHeight();j++) {
				if (map.getBottomLayer(i, j) != null) {
					result.setBottom(i, j, graphicsConvert.get(map.getBottomLayer(i, j).getClass().getSimpleName()));

					if (result.getBottom(i, j) == BUTTON_RELEASED) {
						List<EventObserver> buttonObservers = ((Button)map.getBottomLayer(i, j)).getObservers();
						List<Point> observerPositions = new ArrayList<>();

						for (EventObserver observer : buttonObservers) {
							if (observer instanceof Tile) {
								observerPositions.add(new Point(((Tile) observer).getX(), ((Tile) observer).getY()));
							}
						}

						result.addConnection(new Point(i, j), observerPositions);
					}
				} else {
					result.setBottom(i, j, EMPTY);
				}

				if (map.getUpperLayer(i, j) != null) {
					result.setTop(i, j, graphicsConvert.get(map.getUpperLayer(i, j).getClass().getSimpleName()));
				} else {
					result.setTop(i, j, EMPTY);
				}
			}
		}

		return result;
	}

	public static Map loadConvert (RawMap map) {
		Map result = new Map(map.getWidth(), map.getHeight());

		Point playerPos = new Point();

		for (int i = 0;i < map.getWidth();i++) {
			for (int j = 0;j < map.getHeight();j++) {
				switch (map.getBottom(i, j)) {
					case BOX -> result.setBottomLayer(i, j, new Box(i, j));
					case BUTTON_RELEASED -> result.setBottomLayer(i, j, new Button(i, j));
					case ENEMY -> result.setBottomLayer(i, j, new ChasingEnemy(i, j, null));
					case MIMIC -> result.setBottomLayer(i, j, new MimicEnemy(i, j));
					case DOOR_CLOSED -> result.setBottomLayer(i, j, new Door(i, j));
					case FLOOR -> result.setBottomLayer(i, j, new Floor(i, j));
					case GOAL -> result.setBottomLayer(i, j, new Goal(i, j));
					case PLAYER -> {
						result.setBottomLayer(i, j, new PlayerCharacter(i, j));
						playerPos = new Point(i, j);
					}
					case WALL -> result.setBottomLayer(i, j, new Wall(i, j));
				}

				switch (map.getTop(i, j)) {
					case BOX -> result.setUpperLayer(i, j, new Box(i, j));
					case BUTTON_RELEASED -> result.setUpperLayer(i, j, new Button(i, j));
					case ENEMY -> result.setUpperLayer(i, j, new ChasingEnemy(i, j, null));
					case MIMIC -> result.setUpperLayer(i, j, new MimicEnemy(i, j));
					case DOOR_CLOSED -> result.setUpperLayer(i, j, new Door(i, j));
					case FLOOR -> result.setUpperLayer(i, j, new Floor(i, j));
					case GOAL -> result.setUpperLayer(i, j, new Goal(i, j));
					case PLAYER -> {
						result.setUpperLayer(i, j, new PlayerCharacter(i, j));
						playerPos = new Point(i, j);
					}
					case WALL -> result.setUpperLayer(i, j, new Wall(i, j));
				}
			}
		}

		for (int i = 0;i < map.getWidth();i++) {
			for (int j = 0; j < map.getHeight(); j++) {
				if (map.getBottom(i, j) == ENEMY) {
					result.setBottomLayer(i, j, new ChasingEnemy(i, j, result.getUpperLayer(playerPos.x, playerPos.y)));
				}

				if (map.getTop(i, j) == ENEMY) {
					result.setUpperLayer(i, j, new ChasingEnemy(i, j, result.getUpperLayer(playerPos.x, playerPos.y)));
				}
			}
		}

		Hashtable<Point, List<Point>> buttonObservers = map.getConnections();

		Iterator<java.util.Map.Entry<Point, List<Point>>> it = buttonObservers.entrySet().iterator();

		while (it.hasNext()) {
			java.util.Map.Entry<Point, List<Point>> entry = it.next();

			if (result.getBottomLayer(entry.getKey().x, entry.getKey().y) instanceof Button) {
				for (Point point : entry.getValue()) {
					if (result.getBottomLayer(point.x, point.y) instanceof Door) {
						((Button) result.getBottomLayer(entry.getKey().x, entry.getKey().y)).addObserver((Door)result.getBottomLayer(point.x, point.y));
					} else if (result.getUpperLayer(point.x, point.y) instanceof Door) {
						((Button) result.getBottomLayer(entry.getKey().x, entry.getKey().y)).addObserver((Door)result.getUpperLayer(point.x, point.y));
					}
				}
			} else if (result.getUpperLayer(entry.getKey().x, entry.getKey().y) instanceof Button) {
				for (Point point : entry.getValue()) {
					if (result.getBottomLayer(point.x, point.y) instanceof Door) {
						((Button) result.getUpperLayer(entry.getKey().x, entry.getKey().y)).addObserver((Door)result.getBottomLayer(point.x, point.y));
					} else if (result.getUpperLayer(point.x, point.y) instanceof Door) {
						((Button) result.getUpperLayer(entry.getKey().x, entry.getKey().y)).addObserver((Door)result.getUpperLayer(point.x, point.y));
					}
				}
			}

			it.remove();
		}

		return result;
	}
}
