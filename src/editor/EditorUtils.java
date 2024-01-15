package editor;

import IO.IOManager;
import connectableinterface.Connectable;
import enums.ConnectableTile;
import enums.EditableTile;
import enums.Graphics;
import event.LevelEvent;
import event.editor.SavePathEvent;
import gamemanager.GameManager;
import levelloader.LevelLoader;
import levelloader.LevelNotSaved;
import map.Map;
import tile.*;

import javax.swing.tree.DefaultMutableTreeNode;

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
            case PLAYER ->  new PlayerCharacter(x, y);
            case DOOR ->  new Door(x, y);
            case BUTTON ->  new Button(x, y);
            default ->  null;
        };
    }

    public static EditableTile objectToEditable(Tile tile) {
        if (tile == null){
            return enums.EditableTile.EMPTY;
        }

        return switch (tile.getClass().getSimpleName()) {
            case "Box" -> BOX;
            case "Button" -> BUTTON;
            case "ChasingEnemy" -> ENEMY;
            case "MimicEnemy" -> MIMIC;
            case "SmartEnemy" -> SMART;
            case "Door" -> DOOR;
            case "Floor" -> FLOOR;
            case "Goal" -> GOAL;
            case "PlayerCharacter" -> PLAYER;
            case "Wall" -> WALL;
            default -> enums.EditableTile.EMPTY;
        };
    }

    public static ConnectableTile objectToConnectable(Tile tile)
    {
        if (tile == null){
            return ConnectableTile.DEFAULT;
        }
        return switch (tile.getClass().getSimpleName())
        {
            case "Button" -> ConnectableTile.BUTTON;
            case "ChasingEnemy" -> ConnectableTile.ENEMY;
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
            GameManager.getInstance().getEditor().setTreeModel(new editor.TreeModel());
            for (int i = 0 ; i < GameManager.getInstance().getMap().getWidth() ; ++i)
            {
                for (int j = 0 ; j < GameManager.getInstance().getMap().getWidth() ; ++j)
                {
                    if (GameManager.getInstance().getMap().getUpperLayer(i,j) instanceof SmartEnemy)
                    {
                        GameManager.getInstance().getEditor().getTreeModel().addNode("Smart enemy ("+i+", "+j+")");
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
