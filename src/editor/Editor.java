package editor;

import IO.IOManager;
import enums.*;
import event.*;
import levelloader.LevelLoader;
import levelloader.LevelNotSaved;
import tile.*;
import gamemanager.GameManager;
import map.Map;
import tile.Box;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import turnstrategy.Follower;

import java.util.ArrayList;

import static enums.Arrow.*;
import static enums.Arrow.EMPTY;
import static enums.Direction.*;
import static enums.EditableTile.*;
import static enums.Layer.UPPER;


public class Editor implements EventObserver {
    // Variables required for the graphics to work
    private Layer layer = Layer.BOTH;
    private EditorMode mode = EditorMode.PREADD;
    private EditorGraphics heldTile;
    private ConnectableTile connectingTile;
    private EditorGraphics[][] currentPath;
    private SmartEnemy currentEnemy = null;

    // Variables required for the logic to work
    private boolean change;
    private int playerCount;
    private TreeModel treeModel;

    public Editor() {
        this.setHeldTile(EMPTY);
        change=false;
        setDefaultMap(10, 10);
        playerCount = 1;
        treeModel = new TreeModel();
    }

    public EditorGraphics getHeldTile() {
        return heldTile;
    }

    public void setHeldTile(EditorGraphics heldTile) {
        this.heldTile = heldTile;
    }

    //pewnie powinno być gdzie indziej
    public Tile enumToObject(EditableTile editableTile, int x, int y) {
        return switch (editableTile) {
            case BOX ->  new Box(x, y);
            case GOAL ->  new Goal(x, y);
            case WALL ->  new Wall(x, y);
            case ENEMY ->  new ChasingEnemy(x, y, findPlayer());
            case MIMIC -> new MimicEnemy(x, y);
            case SMART -> new SmartEnemy(x, y);
            case FLOOR ->  new Floor(x, y);
            case PLAYER ->  new PlayerCharacter(x, y);
            case DOOR ->  new Door(x, y);
            case BUTTON ->  new Button(x, y);
            default ->  null;
        };
    }
    public EditableTile objectToEnum(Tile tile)
    {
        if (tile == null){
            return enums.EditableTile.EMPTY;
        }
        return switch (tile.getClass().getSimpleName())
        {
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

    public void setDefaultMap(int x, int y) {
        Map map = new Map(x, y);

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                map.setBottomLayer(i, j, new Floor(i, j));
            }
        }

        map.setUpperLayer(0, 0, new PlayerCharacter(0, 0));

        GameManager.getInstance().setMap(map);
    }

    public void onEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "PalettePressedEvent": {
                PalettePressedEvent palettePressedEvent = (PalettePressedEvent) event;
                this.setHeldTile(palettePressedEvent.getType());
                break;
            }
            //TODO: fix edge case where second button is pressed while the other is still pressed down (no idea why it happens)
            case "TilePressedEvent": {
                TilePressedEvent tilePressedEvent = (TilePressedEvent) event;
                this.setLayer(tilePressedEvent.getLayer());
                //right click functionality
                if (tilePressedEvent.isRightMouseButton()) {
                    editorPlaceTile(EMPTY, layer, tilePressedEvent.getX(), tilePressedEvent.getY());
                }
                //left click functionality
                else {
                    editorPlaceTile(heldTile, layer, tilePressedEvent.getX(), tilePressedEvent.getY());
                }
                if (change) {
                    IOManager.getInstance().drawEditor();
                    change = false;
                }
                break;
            }
            case "LevelLoadedEvent": {
                LevelEvent levelEvent = (LevelEvent) event;
                try {
                    GameManager.getInstance().setMap(LevelLoader.loadLevel(levelEvent.getPath()));
                    IOManager.getInstance().drawGame();
                    IOManager.getInstance().drawEditor();
                    playerCount = 0;
                    for (int i=0;i<GameManager.getInstance().getMap().getWidth();i++)
                    {
                        for (int j=0;j<GameManager.getInstance().getMap().getHeight();j++)
                        {
                            if (GameManager.getInstance().getMap().getUpperLayer(i,j) instanceof PlayerCharacter)
                            {
                                playerCount++;
                            }
                        }
                    }
                }
                catch (levelloader.LevelNotLoaded e)
                {
                    //TODO: Zrobić tu coś mądrzejszego
                    System.err.println("Błąd wczytywania poziomu");
                    setDefaultMap(10, 10);
                }
                IOManager.getInstance().drawEditor();
                break;
            }
            case "LevelSavedEvent": {
                LevelEvent levelEvent = (LevelEvent) event;
                try {
                    LevelLoader.saveLevel(levelEvent.getPath(),GameManager.getInstance().getMap());
                }
                catch (LevelNotSaved e) {
                    System.err.println("Błąd zapisywania poziomu");
                }
                break;
            }
            //TODO: change casting from button/door to more general statements
            case "ConnectionCreatedEvent": {
                ConnectionEvent connectionEvent = (ConnectionEvent) event;
                ((Button) connectionEvent.getFrom()).addObserver((Door) connectionEvent.getTo());
                break;
            }
            case "ConnectionDeletedEvent": {
                ConnectionEvent connectionEvent = (ConnectionEvent) event;
                ((Button) connectionEvent.getFrom()).removeObserver((Door) connectionEvent.getTo());
                break;
            }
            case "EnemySelectedEvent":
            {
                updateEnemyPath();
                EnemySelectedEvent enemySelectedEvent = (EnemySelectedEvent) event;
                currentEnemy = enemySelectedEvent.getSmartEnemy();
                currentPath = listToPath();
                break;
            }
            case "SavePathEvent":
            {
                updateEnemyPath();
                break;
            }
            default: return;
        }
    }

    private void editorPlaceTile(EditorGraphics incomingTile, Layer layer, int x, int y) {
        switch (layer) {
            case BOTH:
                if (incomingTile instanceof EditableTile tile) {
                    {
                        if (tile.preferredLayer == Layer.BOTH || tile.preferredLayer == UPPER) {
                            editorPlaceTile(tile, UPPER, x, y);
                            if (tile.fullTileWhenBoth || objectToEnum(GameManager.getInstance().getMap().getBottomLayer(x, y)).fullTileWhenBoth) {
                                editorPlaceTile(EMPTY, Layer.BOTTOM, x, y);
                            }
                        }
                        if (tile.preferredLayer == Layer.BOTH || tile.preferredLayer == Layer.BOTTOM) {
                            editorPlaceTile(tile, Layer.BOTTOM, x, y);
                            if (tile.fullTileWhenBoth || objectToEnum(GameManager.getInstance().getMap().getUpperLayer(x, y)).fullTileWhenBoth) {
                                editorPlaceTile(EMPTY, UPPER, x, y);
                            }
                        }
                        this.layer = Layer.BOTH;
                    }
                }
                break;
            case UPPER:
                if (tile.isPlaceableUpper && tile != objectToEnum(GameManager.getInstance().getMap().getUpperLayer(x, y)) && !(GameManager.getInstance().getMap().getUpperLayer(x, y) instanceof PlayerCharacter && playerCount == 1)) {
                    if (tile == PLAYER) {
                        playerCount++;
                    }
                    if (GameManager.getInstance().getMap().getUpperLayer(x,y) instanceof PlayerCharacter)
                    {
                        playerCount--;
                        GameManager.getInstance().getMap().setUpperLayer(x, y, enumToObject(tile, x, y));
                        for (int i=0;i<GameManager.getInstance().getMap().getWidth();i++)
                        {
                            for (int j=0;j<GameManager.getInstance().getMap().getHeight();j++)
                            {
                                if (GameManager.getInstance().getMap().getUpperLayer(i,j) instanceof ChasingEnemy enemy)
                                {
                                    if (((Follower) enemy.getTurnStrategy()).getTargetTile().getX() == x && ((Follower) (enemy.getTurnStrategy())).getTargetTile().getY() == y) {
                                        ((Follower) (((ChasingEnemy) (GameManager.getInstance().getMap().getUpperLayer(i, j))).getTurnStrategy())).setTargetTile(findPlayer());
                                    }
                                }
                            }
                        }
                    }
                    else {
                        GameManager.getInstance().getMap().setUpperLayer(x, y, enumToObject(tile, x, y));
                    }
                if (incomingTile instanceof EditableTile tile) {
                    if (tile.isPlaceableUpper && tile != objectToEnum(GameManager.getInstance().getMap().getUpperLayer(x, y)) && !(GameManager.getInstance().getMap().getUpperLayer(x, y) instanceof PlayerCharacter && playerCount == 1)) {
                        switch (tile) {
                            case PLAYER: {
                                playerCount++;
                                break;
                            }
                            case SMART: {
                                treeModel.addNode("Smart enemy (" + x + ", " + y + ")");
                                break;
                            }
                        }
                        Tile mapTile = GameManager.getInstance().getMap().getUpperLayer(x, y);
                        switch (objectToEnum(mapTile)) {
                            case SMART: {
                                treeModel.removeNode("Smart enemy (" + x + ", " + y + ")");
                                break;
                            }
                            //moze w przyszlosci bedzie wiecej
                        }
                        if (GameManager.getInstance().getMap().getUpperLayer(x, y) instanceof PlayerCharacter) {
                            playerCount--;
                            GameManager.getInstance().getMap().setUpperLayer(x, y, enumToObject(tile, x, y));
                            for (int i = 0; i < GameManager.getInstance().getMap().getWidth(); i++) {
                                for (int j = 0; j < GameManager.getInstance().getMap().getHeight(); j++) {
                                    if (GameManager.getInstance().getMap().getUpperLayer(i, j) instanceof ChasingEnemy enemy) {
                                        if (((PlayerFollower) enemy.getTurnStrategy()).getTargetTile().getX() == x && ((PlayerFollower) (enemy.getTurnStrategy())).getTargetTile().getY() == y) {
                                            ((PlayerFollower) (((ChasingEnemy) (GameManager.getInstance().getMap().getUpperLayer(i, j))).getTurnStrategy())).setTargetTile(findPlayer());
                                        }
                                    }
                                }
                            }
                        } else {
                            GameManager.getInstance().getMap().setUpperLayer(x, y, enumToObject(tile, x, y));
                        }

                        change = true;
                    }
                }
                break;
            case BOTTOM:
                if (incomingTile instanceof EditableTile tile) {
                    if (tile == enums.EditableTile.EMPTY) {
                        tile = FLOOR;
                    }
                    if (tile.isPlaceableBottom && tile != objectToEnum(GameManager.getInstance().getMap().getBottomLayer(x, y)) && !(tile == enums.EditableTile.EMPTY && objectToEnum(GameManager.getInstance().getMap().getBottomLayer(x, y)) == FLOOR)) {
                        GameManager.getInstance().getMap().setBottomLayer(x, y, enumToObject(tile, x, y));
                        change = true;
                    }
                }
                break;
            case PATH:
            {
                if (incomingTile instanceof Arrow) {
                    if (currentPath!=null) {
                        currentPath[x][y] = incomingTile;
                        change = true;
                    }
                }
            }
        }
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }
    
    public Tile findPlayer()
    {
        for (int i=0;i<GameManager.getInstance().getMap().getWidth();i++)
        {
            for (int j=0;j<GameManager.getInstance().getMap().getHeight();j++)
            {
                if (GameManager.getInstance().getMap().getUpperLayer(i,j) instanceof PlayerCharacter)
                {
                    return GameManager.getInstance().getMap().getUpperLayer(i,j);
                }
            }
        }
        return null;
    }
    //metody do pathow
    public EditorGraphics[][] generateEmptyPath()
    {
        EditorGraphics[][] path = new EditorGraphics[GameManager.getInstance().getMap().getWidth()][GameManager.getInstance().getMap().getHeight()];
        for (int i = 0 ; i < path.length ; ++i)
        {
            for (int j = 0 ; j < path[0].length ; ++j)
            {
                path[i][j] = EMPTY;
            }
        }
        return path;
    }
    public EditorGraphics[][] listToPath()
    {
        if (currentEnemy != null) {
            EditorGraphics[][] path = generateEmptyPath();
            ArrayList<PathTile> pathTiles = currentEnemy.getPathTileList();
            if (pathTiles != null)
            {
                for (PathTile i : pathTiles) {
                    EditorGraphics arrow = null;
                    switch (i.getDirection()) {
                        case UP: {
                            arrow = ARROW_UP;
                            break;
                        }
                        case DOWN: {
                            arrow = ARROW_DOWN;
                            break;
                        }
                        case LEFT: {
                            arrow = ARROW_LEFT;
                            break;
                        }
                        case RIGHT: {
                            arrow = ARROW_RIGHT;
                            break;
                        }
                    }
                    path[i.getX()][i.getY()] = arrow;
                }
            }
            else
                path = generateEmptyPath();
            return path;
        }
        return null;
    }
    public ArrayList<PathTile> pathToList()
    {
        ArrayList<PathTile> toReturn = new ArrayList<>();
        for (int i = 0 ; i < currentPath.length ; ++i)
        {
            for (int j = 0 ; j < currentPath[0].length ; ++j)
            {
                if (currentPath[i][j] != EMPTY && currentPath[i][j] != null)
                {
                    Direction dir;
                    /*
                        java: patterns in switch statements are a preview feature and are disabled by default.
                        (use --enable-preview to enable patterns in switch statements)

                        jak macie cos takiego to zmiencie java machine na najnowsza wersje dodatkowo moze pomoc
                        zmienienie w ustawieniach projektu wersj
                     */
                    switch (currentPath[i][j])
                    {
                        case ARROW_UP -> dir = UP;
                        case ARROW_DOWN -> dir = DOWN;
                        case ARROW_LEFT -> dir = LEFT;
                        case ARROW_RIGHT -> dir = RIGHT;
                        default -> throw new IllegalStateException("Unexpected value: " + currentPath[i][j]);
                    }
                    toReturn.add((new PathTile(i, j, dir)));
                }
            }
        }
        return toReturn;
    }
    public void updateEnemyPath()
    {
        if (currentEnemy != null && currentPath!=null)
        {
            currentEnemy.setPathTileList(pathToList());
        }
    }


    public Tile[] getConnectibleTilesInCategory(ConnectableTile category) {
        // TODO: return all tiles belonging to a given category, eg. buttons, enemies etc.
        return null;
    }

    public Tile[] getTileConnections(Tile tile) {
        // TODO: return all tiles connected to the tile passed as an argument
        return null;
    }

    public EditorMode getMode() {
        return mode;
    }

    public void setMode(EditorMode mode) {
        this.mode = mode;
    }

    public ConnectableTile getConnectingTile() {
        return connectingTile;
    }

    public void setConnectingTile(ConnectableTile connectingTile) {
        this.connectingTile = connectingTile;
    }

    public TreeModel getTreeModel() {
        return treeModel;
    }

    public void setTreeModel(TreeModel treeModel) {
        this.treeModel = treeModel;
    }

    public SmartEnemy getCurrentEnemy() {
        return currentEnemy;
    }

    public void setCurrentEnemy(SmartEnemy currentEnemy) {
        this.currentEnemy = currentEnemy;
    }

    public EditorGraphics[][] getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(EditorGraphics[][] currentPath) {
        this.currentPath = currentPath;
    }

}
