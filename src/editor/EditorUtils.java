package editor;

import IO.ConsoleIO;
import IO.IOManager;
import enums.ConnectableTile;
import enums.EditableTile;
import gamemanager.GameManager;
import levelloader.LevelLoader;
import levelloader.LevelNotSaved;
import map.Map;
import tile.*;

import javax.swing.tree.DefaultMutableTreeNode;

import static enums.Direction.*;
import static enums.EditableTile.*;
import static enums.EditableTile.WALL;

public class EditorUtils {
    public static Tile editableToObject(EditableTile editableTile, int x, int y) {
        if (editableTile == ENEMY && GameManager.getInstance().getMap() == null) {
            return new ChasingEnemy(x, y, null);
        }

        switch (editableTile) {
            case BOX:
                return new Box(x, y);
            case GOAL:
                return new Goal(x, y);
            case WALL:
                return new Wall(x, y);
            case ENEMY:
                return new ChasingEnemy(x, y, GameManager.getInstance().getMap().getPlayer());
            case MIMIC:
                return new MimicEnemy(x, y);
            case SMART:
                return new SmartEnemy(x, y);
            case FLOOR:
                return new Floor(x, y);
            case ONEWAY_UP:
                return new OnewayFloor(x, y, UP);
            case ONEWAY_DOWN:
                return new OnewayFloor(x, y, DOWN);
            case ONEWAY_LEFT:
                return new OnewayFloor(x, y, LEFT);
            case ONEWAY_RIGHT:
                return new OnewayFloor(x, y, RIGHT);
            case DANGER:
                return new DangerFloor(x, y);
            case PLAYER:
                return new PlayerCharacter(x, y);
            case DOOR:
                return new Door(x, y);
            case REVERSE:
                return new ReverseDoor(x, y);
            case BUTTON:
                return new Button(x, y);
            case BUTTON_PERMANENT:
                return new ButtonPermanent(x, y);
            case SIGN:
                return new Sign(x, y);
            case TELEPORT:
                return new Teleport(x, y);
            case TOGGLE:
                return new ToggleDoor(x, y);
            case CHECKPOINT:
                return new Checkpoint(x, y);
            default:
                return null;
        }
    }

    public static EditableTile objectToEditable(Tile tile) {
        if (tile == null){
            return enums.EditableTile.EMPTY;
        } else if (tile instanceof OnewayFloor){
            switch (((OnewayFloor)tile).getDirection()) {
                case UP:
                    return ONEWAY_UP;
                case DOWN:
                    return ONEWAY_DOWN;
                case LEFT:
                    return ONEWAY_LEFT;
                case RIGHT:
                    return ONEWAY_RIGHT;
                default:
                    return ONEWAY_UP;
            }
        } else {
            switch (tile.getClass().getSimpleName()) {
                case "Box":
                    return BOX;
                case "Button":
                    return BUTTON;
                case "ButtonPermanent":
                    return BUTTON_PERMANENT;
                case "ChasingEnemy":
                    return ENEMY;
                case "MimicEnemy":
                    return MIMIC;
                case "SmartEnemy":
                    return SMART;
                case "Door":
                    return DOOR;
                case "Floor":
                    return FLOOR;
                case "DangerFloor":
                    return DANGER;
                case "Goal":
                    return GOAL;
                case "PlayerCharacter":
                    return PLAYER;
                case "Wall":
                    return WALL;
                case "Sign":
                    return SIGN;
                case "Teleport":
                    return TELEPORT;
                case "ReverseDoor":
                    return REVERSE;
                case "ToggleDoor":
                    return TOGGLE;
                case "Checkpoint":
                    return CHECKPOINT;
                default:
                    return enums.EditableTile.EMPTY;
            }
        }
    }

    public static ConnectableTile objectToConnectable(Tile tile)
    {
        if (tile == null){
            return ConnectableTile.DEFAULT;
        }
        switch (tile.getClass().getSimpleName())
        {
            case "Button":
                return ConnectableTile.BUTTON;
            case "ButtonPermanent":
                return ConnectableTile.BUTTON_PERMANENT;
            case "ChasingEnemy":
                return ConnectableTile.ENEMY;
            case "Teleport":
                return ConnectableTile.TELEPORT;
            default:
                return ConnectableTile.DEFAULT;
        }
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
                        SmartEnemy smartEnemy = (SmartEnemy)GameManager.getInstance().getMap().getUpperLayer(i,j);
                        GameManager.getInstance().getEditor().getEnemiesTreeModel().insertNodeInto(new DefaultMutableTreeNode(smartEnemy), (DefaultMutableTreeNode)GameManager.getInstance().getEditor().getEnemiesTreeModel().getRoot(), 0);
                    }
                    if (GameManager.getInstance().getMap().getBottomLayer(i,j) instanceof Sign)
                    {
                        Sign sign = (Sign)GameManager.getInstance().getMap().getBottomLayer(i,j);
                        GameManager.getInstance().getEditor().getSignsTreeModel().insertNodeInto(new DefaultMutableTreeNode(sign), (DefaultMutableTreeNode)GameManager.getInstance().getEditor().getSignsTreeModel().getRoot(), 0);
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
