package map;

import connectableinterface.Connectable;
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
		Hashtable<String, EditableTile>graphicsConvert = new Hashtable<>();

		graphicsConvert.put("Box", EditableTile.BOX);
		graphicsConvert.put("Button", EditableTile.BUTTON);
		graphicsConvert.put("ButtonPermanent", EditableTile.BUTTON_PERMANENT);
		graphicsConvert.put("ChasingEnemy", EditableTile.ENEMY);
		graphicsConvert.put("MimicEnemy", EditableTile.MIMIC);
		graphicsConvert.put("SmartEnemy", EditableTile.SMART);
		graphicsConvert.put("Door", EditableTile.DOOR);
		graphicsConvert.put("ReverseDoor", EditableTile.REVERSE);
		graphicsConvert.put("Floor", EditableTile.FLOOR);
		graphicsConvert.put("DangerFloor", EditableTile.DANGER);
		graphicsConvert.put("Goal", EditableTile.GOAL);
		graphicsConvert.put("PlayerCharacter", EditableTile.PLAYER);
		graphicsConvert.put("Wall", EditableTile.WALL);
		graphicsConvert.put("Sign", EditableTile.SIGN);
		graphicsConvert.put("Teleport", EditableTile.TELEPORT);

		NewRawMap result = new NewRawMap(map.getWidth(), map.getHeight());
		RawConnection connection;
		RawPath path;
		RawMessage message;

		for (int i = 0;i < map.getWidth();i++) {
			for (int j = 0;j < map.getHeight();j++) {
				if (map.getBottomLayer(i, j) != null) {
					if (map.getBottomLayer(i, j) instanceof OnewayFloor) {
						switch (((OnewayFloor)map.getBottomLayer(i, j)).getDirection()) {
							case UP -> result.setBottom(i, j, EditableTile.ONEWAY_UP);
							case DOWN -> result.setBottom(i, j, EditableTile.ONEWAY_DOWN);
							case LEFT -> result.setBottom(i, j, EditableTile.ONEWAY_LEFT);
							case RIGHT -> result.setBottom(i, j, EditableTile.ONEWAY_RIGHT);
							default -> result.setBottom(i, j, EditableTile.ONEWAY_UP);
						}
					} else {
						result.setBottom(i, j, graphicsConvert.get(map.getBottomLayer(i, j).getClass().getSimpleName()));
					}

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

					if (map.getUpperLayer(i, j) instanceof OnewayFloor) {
						switch (((OnewayFloor)map.getUpperLayer(i, j)).getDirection()) {
							case UP -> result.setTop(i, j, EditableTile.ONEWAY_UP);
							case DOWN -> result.setTop(i, j, EditableTile.ONEWAY_DOWN);
							case LEFT -> result.setTop(i, j, EditableTile.ONEWAY_LEFT);
							case RIGHT -> result.setTop(i, j, EditableTile.ONEWAY_RIGHT);
							default -> result.setTop(i, j, EditableTile.ONEWAY_UP);
						}
					} else {
						result.setTop(i, j, graphicsConvert.get(map.getUpperLayer(i, j).getClass().getSimpleName()));
					}

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
				switch (map.getBottom(i, j)) {
					case BOX -> result.setBottomLayer(i, j, new Box(i, j));
					case BUTTON -> result.setBottomLayer(i, j, new Button(i, j));
					case BUTTON_PERMANENT -> result.setBottomLayer(i, j, new ButtonPermanent(i, j));
					case ENEMY -> result.setBottomLayer(i, j, new ChasingEnemy(i, j, null));
					case MIMIC -> result.setBottomLayer(i, j, new MimicEnemy(i, j));
					case SMART -> result.setUpperLayer(i, j, new SmartEnemy(i, j));
					case DOOR -> result.setBottomLayer(i, j, new Door(i, j));
					case REVERSE -> result.setBottomLayer(i, j, new ReverseDoor(i, j));
					case FLOOR -> result.setBottomLayer(i, j, new Floor(i, j));
					case ONEWAY_UP -> result.setBottomLayer(i, j, new OnewayFloor(i, j, UP));
					case ONEWAY_DOWN -> result.setBottomLayer(i, j, new OnewayFloor(i, j, DOWN));
					case ONEWAY_LEFT -> result.setBottomLayer(i, j, new OnewayFloor(i, j, LEFT));
					case ONEWAY_RIGHT -> result.setBottomLayer(i, j, new OnewayFloor(i, j, RIGHT));
					case DANGER -> result.setBottomLayer(i, j, new DangerFloor(i, j));
					case SIGN -> result.setBottomLayer(i, j, new Sign(i, j));
					case GOAL -> result.setBottomLayer(i, j, new Goal(i, j));
					case PLAYER -> {
						result.setBottomLayer(i, j, new PlayerCharacter(i, j));
						playerPos = new Point(i, j);
					}
					case WALL -> result.setBottomLayer(i, j, new Wall(i, j));
					case TELEPORT -> result.setBottomLayer(i, j, new Teleport(i, j));
				}

				switch (map.getTop(i, j)) {
					case BOX -> result.setUpperLayer(i, j, new Box(i, j));
					case BUTTON -> result.setUpperLayer(i, j, new Button(i, j));
					case BUTTON_PERMANENT -> result.setUpperLayer(i, j, new ButtonPermanent(i, j));
					case ENEMY -> result.setUpperLayer(i, j, new ChasingEnemy(i, j, null));
					case MIMIC -> result.setUpperLayer(i, j, new MimicEnemy(i, j));
					case SMART -> result.setUpperLayer(i, j, new SmartEnemy(i, j));
					case DOOR -> result.setUpperLayer(i, j, new Door(i, j));
					case FLOOR -> result.setUpperLayer(i, j, new Floor(i, j));
					case ONEWAY_UP -> result.setUpperLayer(i, j, new OnewayFloor(i, j, UP));
					case ONEWAY_DOWN -> result.setUpperLayer(i, j, new OnewayFloor(i, j, DOWN));
					case ONEWAY_LEFT -> result.setUpperLayer(i, j, new OnewayFloor(i, j, LEFT));
					case ONEWAY_RIGHT -> result.setUpperLayer(i, j, new OnewayFloor(i, j, RIGHT));
					case DANGER -> result.setUpperLayer(i, j, new DangerFloor(i, j));
					case SIGN -> result.setUpperLayer(i, j, new Sign(i, j));
					case GOAL -> result.setUpperLayer(i, j, new Goal(i, j));
					case PLAYER -> {
						result.setUpperLayer(i, j, new PlayerCharacter(i, j));
						playerPos = new Point(i, j);
					}
					case WALL -> result.setUpperLayer(i, j, new Wall(i, j));
					case TELEPORT -> result.setUpperLayer(i, j, new Teleport(i, j));
				}
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
					case BOX -> result.setBottomLayer(i, j, new Box(i, j));
					case BUTTON_RELEASED -> result.setBottomLayer(i, j, new Button(i, j));
					case ENEMY -> result.setBottomLayer(i, j, new ChasingEnemy(i, j, null));
					case MIMIC -> result.setBottomLayer(i, j, new MimicEnemy(i, j));
					case DOOR_CLOSED -> result.setBottomLayer(i, j, new Door(i, j));
					case FLOOR -> result.setBottomLayer(i, j, new Floor(i, j));
					case ONEWAY_UP -> result.setBottomLayer(i, j, new OnewayFloor(i, j, UP));
					case ONEWAY_DOWN -> result.setBottomLayer(i, j, new OnewayFloor(i, j, DOWN));
					case ONEWAY_LEFT -> result.setBottomLayer(i, j, new OnewayFloor(i, j, LEFT));
					case ONEWAY_RIGHT -> result.setBottomLayer(i, j, new OnewayFloor(i, j, RIGHT));
					case DANGER -> result.setBottomLayer(i, j, new DangerFloor(i, j));
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
					case SMART -> result.setUpperLayer(i, j, new SmartEnemy(i, j));
					case DOOR_CLOSED -> result.setUpperLayer(i, j, new Door(i, j));
					case FLOOR -> result.setUpperLayer(i, j, new Floor(i, j));
					case ONEWAY_UP -> result.setUpperLayer(i, j, new OnewayFloor(i, j, UP));
					case ONEWAY_DOWN -> result.setUpperLayer(i, j, new OnewayFloor(i, j, DOWN));
					case ONEWAY_LEFT -> result.setUpperLayer(i, j, new OnewayFloor(i, j, LEFT));
					case ONEWAY_RIGHT -> result.setUpperLayer(i, j, new OnewayFloor(i, j, RIGHT));
					case DANGER -> result.setUpperLayer(i, j, new DangerFloor(i, j));
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
			case BUTTON_RELEASED, ENEMY -> {return true;}
		}
		return false;
	}
	public static boolean canBeConnected(Graphics t)
	{
		switch (t)
		{
			case DOOR_CLOSED,PLAYER,MIMIC:
			{
				return true;
			}
		}
		return false;
	}
	public static boolean isConnectableTile(EditableTile t)
	{
		switch (t)
		{
			case BUTTON, ENEMY, BUTTON_PERMANENT, TELEPORT -> {return true;}
		}
		return false;
	}
	public static boolean canBeConnected(EditableTile t)
	{
		switch (t)
		{
			case DOOR,PLAYER,MIMIC,REVERSE:
			{
				return true;
			}
		}
		return false;
	}
}
