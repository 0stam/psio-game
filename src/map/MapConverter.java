package map;

import connectableinterface.Connectable;
import editor.EditorUtils;
import enums.EditableTile;
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

import static enums.Direction.*;
import static enums.Graphics.*;

public class MapConverter {
	public static NewRawMap saveConvert (Map map) {
		NewRawMap result = new NewRawMap(map.getWidth(), map.getHeight());
		RawConnection connection;
		RawPath path;
		RawMessage message;

		for (int i = 0;i < map.getWidth();i++) {
			for (int j = 0;j < map.getHeight();j++) {
				if (map.getBottomLayer(i, j) != null) {
					result.setBottom(i, j, EditorUtils.objectToEditable(map.getBottomLayer(i, j)));

					if (isConnectableTile(result.getBottom(i, j))) {
						connection = new RawConnection();
						connection.setSourcePos(new Point(i, j));

						if (((Connectable)(map.getBottomLayer(i, j))).getConnections() != null) {
							for (Tile tile : ((Connectable) (map.getBottomLayer(i, j))).getConnections()) {
								connection.addDestination(new Point(tile.getX(), tile.getY()));
							}
						}

						result.addConnection(connection);
					}

					if (map.getBottomLayer(i, j) instanceof SmartEnemy) {
						path = new RawPath();
						path.setEnemyPos(new Point(i, j));

						if (((SmartEnemy)map.getBottomLayer(i, j)).getPathTileList() != null) {
							for (PathTile tile : ((SmartEnemy) map.getBottomLayer(i, j)).getPathTileList()) {
								path.addPath(tile);
							}
						}

						result.addPath(path);
					}

					if (map.getBottomLayer(i, j) instanceof Sign) {
						message = new RawMessage();
						message.setSignPos(new Point(i, j));
						message.setMessage(((Sign) map.getBottomLayer(i, j)).getMessage());

						result.addMessage(message);
					}
				} else {
					result.setBottom(i, j, EditableTile.EMPTY);
				}

				if (map.getUpperLayer(i, j) != null) {
					result.setTop(i, j, EditorUtils.objectToEditable(map.getUpperLayer(i, j)));

					if (isConnectableTile(result.getTop(i, j))) {
						connection = new RawConnection();
						connection.setSourcePos(new Point(i, j));

						if (((Connectable)(map.getUpperLayer(i, j))).getConnections() != null) {
							for (Tile tile : ((Connectable) (map.getUpperLayer(i, j))).getConnections()) {
								connection.addDestination(new Point(tile.getX(), tile.getY()));
							}
						}

						result.addConnection(connection);
					}

					if (map.getUpperLayer(i, j) instanceof SmartEnemy) {
						path = new RawPath();
						path.setEnemyPos(new Point(i, j));

						if (((SmartEnemy)map.getUpperLayer(i, j)).getPathTileList() != null) {
							for (PathTile tile : ((SmartEnemy) map.getUpperLayer(i, j)).getPathTileList()) {
								path.addPath(tile);
							}
						}

						result.addPath(path);
					}

					if (map.getUpperLayer(i, j) instanceof Sign) {
						message = new RawMessage();
						message.setSignPos(new Point(i, j));
						message.setMessage(((Sign) map.getUpperLayer(i, j)).getMessage());

						result.addMessage(message);
					}
				} else {
					result.setTop(i, j, EditableTile.EMPTY);
				}
			}
		}

		return result;
	}

	public static Map loadConvert (NewRawMap map) {
		Map result = new Map(map.getWidth(), map.getHeight());

		Point playerPos = new Point();

		for (int i = 0;i < map.getWidth();i++) {
			for (int j = 0;j < map.getHeight();j++) {
				result.setBottomLayer(i, j, EditorUtils.editableToObject(map.getBottom(i, j), i, j));
				result.setUpperLayer(i, j, EditorUtils.editableToObject(map.getTop(i, j), i, j));
			}
		}

		for (RawConnection connection : map.getConnections()) {
			if (isConnectableTile(map.getBottom(connection.getSourcePos().x, connection.getSourcePos().y))) {
				for (Point destination : connection.getDestinationPos())
				{
					if (canBeConnected(map.getBottom(destination.x, destination.y))) {
						((Connectable)(result.getBottomLayer(connection.getSourcePos().x, connection.getSourcePos().y))).addConnection(result.getBottomLayer(destination.x, destination.y));
					} else if (canBeConnected(map.getTop(destination.x, destination.y))) {
						((Connectable)(result.getBottomLayer(connection.getSourcePos().x, connection.getSourcePos().y))).addConnection(result.getUpperLayer(destination.x, destination.y));
					}
				}
			} else if (isConnectableTile(map.getTop(connection.getSourcePos().x, connection.getSourcePos().y))) {
				for (Point destination : connection.getDestinationPos())
				{
					if (canBeConnected(map.getBottom(destination.x, destination.y))) {
						((Connectable)(result.getUpperLayer(connection.getSourcePos().x, connection.getSourcePos().y))).addConnection(result.getBottomLayer(destination.x, destination.y));
					} else if (canBeConnected(map.getTop(destination.x, destination.y))) {
						((Connectable)(result.getUpperLayer(connection.getSourcePos().x, connection.getSourcePos().y))).addConnection(result.getUpperLayer(destination.x, destination.y));
					}
				}
			}
		}

		for (RawPath path : map.getPaths()) {
			if (result.getBottomLayer(path.getEnemyPos().x, path.getEnemyPos().y) instanceof SmartEnemy) {
				((SmartEnemy)result.getBottomLayer(path.getEnemyPos().x, path.getEnemyPos().y)).setPathTileList(path.getPath());
			} else if (result.getUpperLayer(path.getEnemyPos().x, path.getEnemyPos().y) instanceof SmartEnemy) {
				((SmartEnemy)result.getUpperLayer(path.getEnemyPos().x, path.getEnemyPos().y)).setPathTileList(path.getPath());
			}
		}

		if (map.getMessages() != null) {
			for (RawMessage message : map.getMessages()) {
				if (result.getBottomLayer(message.getSignPos().x, message.getSignPos().y) instanceof Sign) {
					((Sign) result.getBottomLayer(message.getSignPos().x, message.getSignPos().y)).setMessage(message.getMessage());
				} else if (result.getUpperLayer(message.getSignPos().x, message.getSignPos().y) instanceof Sign) {
					((Sign) result.getUpperLayer(message.getSignPos().x, message.getSignPos().y)).setMessage(message.getMessage());
				}
			}
		}

		return result;
	}

	public static Map oldLoadConvert (RawMap map) {
		Map result = new Map(map.getWidth(), map.getHeight());

		Point playerPos = new Point();

		for (int i = 0;i < map.getWidth();i++) {
			for (int j = 0;j < map.getHeight();j++) {
				switch (map.getBottom(i, j)) {
					case BOX:
						result.setBottomLayer(i, j, new Box(i, j));
						break;
					case BUTTON_RELEASED:
						result.setBottomLayer(i, j, new Button(i, j));
						break;
					case ENEMY:
						result.setBottomLayer(i, j, new ChasingEnemy(i, j, null));
						break;
					case MIMIC:
						result.setBottomLayer(i, j, new MimicEnemy(i, j));
						break;
					case DOOR_CLOSED:
						result.setBottomLayer(i, j, new Door(i, j));
						break;
					case FLOOR:
						result.setBottomLayer(i, j, new Floor(i, j));
						break;
					case ONEWAY_UP:
						result.setBottomLayer(i, j, new OnewayFloor(i, j, UP));
						break;
					case ONEWAY_DOWN:
						result.setBottomLayer(i, j, new OnewayFloor(i, j, DOWN));
						break;
					case ONEWAY_LEFT:
						result.setBottomLayer(i, j, new OnewayFloor(i, j, LEFT));
						break;
					case ONEWAY_RIGHT:
						result.setBottomLayer(i, j, new OnewayFloor(i, j, RIGHT));
						break;
					case DANGER:
						result.setBottomLayer(i, j, new DangerFloor(i, j));
						break;
					case GOAL:
						result.setBottomLayer(i, j, new Goal(i, j));
						break;
					case PLAYER:
						result.setBottomLayer(i, j, new PlayerCharacter(i, j));
						playerPos = new Point(i, j);
						break;
					case WALL:
						result.setBottomLayer(i, j, new Wall(i, j));
						break;
				}

				switch (map.getTop(i, j)) {
					case BOX:
						result.setUpperLayer(i, j, new Box(i, j));
						break;
					case BUTTON_RELEASED:
						result.setUpperLayer(i, j, new Button(i, j));
						break;
					case ENEMY:
						result.setUpperLayer(i, j, new ChasingEnemy(i, j, null));
						break;
					case MIMIC:
						result.setUpperLayer(i, j, new MimicEnemy(i, j));
						break;
					case SMART:
						result.setUpperLayer(i, j, new SmartEnemy(i, j));
						break;
					case DOOR_CLOSED:
						result.setUpperLayer(i, j, new Door(i, j));
						break;
					case FLOOR:
						result.setUpperLayer(i, j, new Floor(i, j));
						break;
					case ONEWAY_UP:
						result.setUpperLayer(i, j, new OnewayFloor(i, j, UP));
						break;
					case ONEWAY_DOWN:
						result.setUpperLayer(i, j, new OnewayFloor(i, j, DOWN));
						break;
					case ONEWAY_LEFT:
						result.setUpperLayer(i, j, new OnewayFloor(i, j, LEFT));
						break;
					case ONEWAY_RIGHT:
						result.setUpperLayer(i, j, new OnewayFloor(i, j, RIGHT));
						break;
					case DANGER:
						result.setUpperLayer(i, j, new DangerFloor(i, j));
						break;
					case GOAL:
						result.setUpperLayer(i, j, new Goal(i, j));
						break;
					case PLAYER:
						result.setUpperLayer(i, j, new PlayerCharacter(i, j));
						playerPos = new Point(i, j);
						break;
					case WALL:
						result.setUpperLayer(i, j, new Wall(i, j));
						break;
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

			if (isConnectableTile(map.getBottom(entry.getKey().x, entry.getKey().y))) {
				for (Point point : entry.getValue()) {
					if (canBeConnected(map.getBottom(point.x, point.y))) {
						((Button) result.getBottomLayer(entry.getKey().x, entry.getKey().y)).addConnection(result.getBottomLayer(point.x, point.y));
					} else if (canBeConnected(map.getTop(point.x, point.y))) {
						((Button) result.getBottomLayer(entry.getKey().x, entry.getKey().y)).addConnection(result.getUpperLayer(point.x, point.y));
					}
				}
			} else if (isConnectableTile(map.getTop(entry.getKey().x, entry.getKey().y))) {
				for (Point point : entry.getValue()) {
					if (canBeConnected(map.getBottom(point.x, point.y))) {
						((Button) result.getUpperLayer(entry.getKey().x, entry.getKey().y)).addConnection(result.getBottomLayer(point.x, point.y));
					} else if (canBeConnected(map.getTop(point.x, point.y))) {
						((Button) result.getUpperLayer(entry.getKey().x, entry.getKey().y)).addConnection(result.getUpperLayer(point.x, point.y));
					}
				}
			}

			it.remove();
		}

		return result;
	}
	public static boolean isConnectableTile(Graphics t)
	{
		switch (t)
		{
			case BUTTON_RELEASED:
			case ENEMY:
				return true;
		}
		return false;
	}
	public static boolean canBeConnected(Graphics t)
	{
		switch (t)
		{
			case DOOR_CLOSED:
			case PLAYER:
			case MIMIC:
				return true;
		}
		return false;
	}
	public static boolean isConnectableTile(EditableTile t)
	{
		switch (t)
		{
			case BUTTON:
			case ENEMY:
			case BUTTON_PERMANENT:
			case TELEPORT:
				return true;
		}
		return false;
	}
	public static boolean canBeConnected(EditableTile t)
	{
		switch (t)
		{
			case DOOR:
			case PLAYER:
			case MIMIC:
			case REVERSE:
			case TELEPORT:
			case TOGGLE:
				return true;
		}
		return false;
	}
}
