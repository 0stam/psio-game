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

        Tile tile;

        switch (editableTile) {
            case BOX:
                tile = new Box(x, y);
                break;
            case GOAL:
                tile = new Goal(x, y);
                break;
            case WALL:
                tile = new Wall(x, y);
                break;
            case ENEMY:
                tile = new ChasingEnemy(x, y, GameManager.getInstance().getMap().getPlayer());
                break;
            case MIMIC:
                tile = new MimicEnemy(x, y);
                break;
            case SMART:
                tile = new SmartEnemy(x, y);
                break;
            case FLOOR:
                tile = new Floor(x, y);
                break;
            case ONEWAY_UP:
                tile = new OnewayFloor(x, y, UP);
                break;
            case ONEWAY_DOWN:
                tile = new OnewayFloor(x, y, DOWN);
                break;
            case ONEWAY_LEFT:
                tile = new OnewayFloor(x, y, LEFT);
                break;
            case ONEWAY_RIGHT:
                tile = new OnewayFloor(x, y, RIGHT);
                break;
            case DANGER:
                tile = new DangerFloor(x, y);
                break;
            case PLAYER:
                tile = new PlayerCharacter(x, y);
                break;
            case DOOR:
                tile = new Door(x, y);
                break;
            case REVERSE:
                tile = new ReverseDoor(x, y);
                break;
            case BUTTON:
                tile = new Button(x, y);
                break;
            case BUTTON_PERMANENT:
                tile = new ButtonPermanent(x, y);
                break;
            case SIGN:
                tile = new Sign(x, y);
                break;
            case TELEPORT:
                tile = new Teleport(x, y);
                break;
            case TOGGLE:
                tile = new ToggleDoor(x, y);
                break;
            case CHECKPOINT:
                tile = new Checkpoint(x, y);
                break;
            default:
                tile = null;
        }

        return tile;
    }

    public static EditableTile objectToEditable(Tile tile) {
        EditableTile et;
        if (tile == null){
            et = enums.EditableTile.EMPTY;
        } else if (tile instanceof OnewayFloor){
            switch (((OnewayFloor)tile).getDirection()) {
                case UP:
                    et = ONEWAY_UP;
                    break;
                case DOWN:
                    et = ONEWAY_DOWN;
                    break;
                case LEFT:
                    et = ONEWAY_LEFT;
                    break;
                case RIGHT:
                    et = ONEWAY_RIGHT;
                    break;
                default:
                    et = ONEWAY_UP;
                    break;
            }
        } else {
            switch (tile.getClass().getSimpleName()) {
                case "Box":
                    et = BOX;
                    break;
                case "Button":
                    et = BUTTON;
                    break;
                case "ButtonPermanent":
                    et = BUTTON_PERMANENT;
                    break;
                case "ChasingEnemy":
                    et = ENEMY;
                    break;
                case "MimicEnemy":
                    et = MIMIC;
                    break;
                case "SmartEnemy":
                    et = SMART;
                    break;
                case "Door":
                    et = DOOR;
                    break;
                case "Floor":
                    et = FLOOR;
                    break;
                case "DangerFloor":
                    et = DANGER;
                    break;
                case "Goal":
                    et = GOAL;
                    break;
                case "PlayerCharacter":
                    et = PLAYER;
                    break;
                case "Wall":
                    et = WALL;
                    break;
                case "Sign":
                    et = SIGN;
                    break;
                case "Teleport":
                    et = TELEPORT;
                    break;
                case "ToggleDoor":
                    et = TOGGLE;
                    break;
                case "Checkpoint":
                    et = CHECKPOINT;
                    break;
                default:
                    et = enums.EditableTile.EMPTY;
                    break;
            }
        }

        return et;
    }

    public static ConnectableTile objectToConnectable(Tile tile)
    {
        ConnectableTile ct;

        if (tile == null){
            return ConnectableTile.DEFAULT;
        }
        switch (tile.getClass().getSimpleName())
        {
            case "Button":
                ct = ConnectableTile.BUTTON;
                break;
            case "ButtonPermanent":
                ct = ConnectableTile.BUTTON_PERMANENT;
                break;
            case "ChasingEnemy":
                ct = ConnectableTile.ENEMY;
                break;
            case "Teleport":
                ct = ConnectableTile.TELEPORT;
                break;
            default:
                ct = ConnectableTile.DEFAULT;
                break;
        }

        return ct;
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
