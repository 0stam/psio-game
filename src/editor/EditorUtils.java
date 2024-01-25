package editor;

import IO.IOManager;
import enums.ConnectableTile;
import enums.EditableTile;
import gamemanager.GameManager;
import levelloader.LevelLoader;
import levelloader.LevelNotSaved;
import map.Map;
import tile.*;

import static enums.Direction.*;
import static enums.EditableTile.*;
import static enums.EditableTile.WALL;

public class EditorUtils {
    public static Tile editableToObject(EditableTile editableTile, int x, int y) {
        return switch (editableTile) {
            case BOX ->  new Box(x, y);
            case GOAL ->  new Goal(x, y);
            case WALL ->  new Wall(x, y);
            case ENEMY ->  new ChasingEnemy(x, y, GameManager.getInstance().getMap().getPlayer());
            case MIMIC -> new MimicEnemy(x, y);
            case SMART -> new SmartEnemy(x, y);
            case FLOOR ->  new Floor(x, y);
            case ONEWAY_UP ->  new OnewayFloor(x, y, UP);
            case ONEWAY_DOWN ->  new OnewayFloor(x, y, DOWN);
            case ONEWAY_LEFT ->  new OnewayFloor(x, y, LEFT);
            case ONEWAY_RIGHT ->  new OnewayFloor(x, y, RIGHT);
            case DANGER -> new DangerFloor(x, y);
            case PLAYER ->  new PlayerCharacter(x, y);
            case DOOR ->  new Door(x, y);
            case REVERSE ->  new ReverseDoor(x, y);
            case BUTTON ->  new Button(x, y);
            case BUTTON_PERMANENT ->  new ButtonPermanent(x, y);
            case SIGN -> new Sign(x, y);
            case TELEPORT -> new Teleport(x, y);
            default ->  null;
        };
    }

    public static EditableTile objectToEditable(Tile tile) {
        if (tile == null){
            return enums.EditableTile.EMPTY;
        } else if (tile instanceof OnewayFloor){
            return switch (((OnewayFloor)tile).getDirection()) {
                case UP -> ONEWAY_UP;
                case DOWN -> ONEWAY_DOWN;
                case LEFT -> ONEWAY_LEFT;
                case RIGHT -> ONEWAY_RIGHT;
                default -> ONEWAY_UP;
            };
        } else {
            return switch (tile.getClass().getSimpleName()) {
                case "Box" -> BOX;
                case "Button" -> BUTTON;
                case "ButtonPermanent" -> BUTTON_PERMANENT;
                case "ChasingEnemy" -> ENEMY;
                case "MimicEnemy" -> MIMIC;
                case "SmartEnemy" -> SMART;
                case "ReverseDoor" -> REVERSE;
                case "Door" -> DOOR;
                case "Floor" -> FLOOR;
                case "DangerFloor" -> DANGER;
                case "Goal" -> GOAL;
                case "PlayerCharacter" -> PLAYER;
                case "Wall" -> WALL;
                case "Sign" -> SIGN;
                case "Teleport" -> TELEPORT;
                default -> enums.EditableTile.EMPTY;
            };
        }
    }

    public static ConnectableTile objectToConnectable(Tile tile)
    {
        if (tile == null){
            return ConnectableTile.DEFAULT;
        }
        return switch (tile.getClass().getSimpleName())
        {
            case "Button" -> ConnectableTile.BUTTON;
            case "ButtonPermanent" -> ConnectableTile.BUTTON_PERMANENT;
            case "ChasingEnemy" -> ConnectableTile.ENEMY;
            case "Teleport" -> ConnectableTile.TELEPORT;
            default -> ConnectableTile.DEFAULT;
        };
    }

    public static void setDefaultMap(int x, int y) {
        Map map = new Map(x, y);

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                map.setBottomLayer(i, j, new Floor(i, j));
            }
        }

        map.setUpperLayer(0, 0, new PlayerCharacter(0, 0));

        GameManager.getInstance().setMap(map);
    }

    public static void loadLevel(String path) {
        try {
            GameManager.getInstance().setMap(LevelLoader.loadLevel(path));
            GameManager.getInstance().getEditor().setEnemiesTreeModel(new EnemiesTreeModel());
            GameManager.getInstance().getEditor().setSignsTreeModel(new SignsTreeModel());
            for (int i = 0 ; i < GameManager.getInstance().getMap().getWidth() ; ++i)
            {
                for (int j = 0 ; j < GameManager.getInstance().getMap().getHeight() ; ++j)
                {
                    if (GameManager.getInstance().getMap().getUpperLayer(i,j) instanceof SmartEnemy)
                    {
                        GameManager.getInstance().getEditor().getEnemiesTreeModel().addNode("Smart enemy ("+i+", "+j+")");
                    }
                    if (GameManager.getInstance().getMap().getBottomLayer(i,j) instanceof Sign)
                    {
                        GameManager.getInstance().getEditor().getSignsTreeModel().addNode("Sign ("+i+", "+j+")");
                    }
                }
            }
            IOManager.getInstance().drawGame();
            IOManager.getInstance().drawEditor();
        }
        catch (levelloader.LevelNotLoaded e)
        {
            //TODO: Do something smarter here
            System.err.println("Błąd wczytywania poziomu");
            EditorUtils.setDefaultMap(10, 10);
        }
    }

    public static void saveLevel(String path) {
        try {
            LevelLoader.saveLevel(path,GameManager.getInstance().getMap());
        }
        catch (LevelNotSaved e) {
            System.err.println("Błąd zapisywania poziomu");
        }
    }
}
