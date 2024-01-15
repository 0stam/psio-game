package editor;

import IO.IOManager;
import enums.*;
import event.*;
import event.display.ChangeLayerEvent;
import event.editor.*;
import tile.*;
import gamemanager.GameManager;

import java.util.List;
import java.util.ArrayList;

import static enums.Arrow.*;
import static enums.Arrow.EMPTY;
import static enums.Direction.*;
import static enums.EditableTile.*;
import static enums.Layer.UPPER;


public class Editor implements EventObserver {
    // Variables required for the graphics to work
    private EditorGraphics[][] currentPath;
    private SmartEnemy currentEnemy = null;
    //private JTree referenceTree = null;

    // Variables required for the logic to work
    private boolean change;
    private int playerCount;
    private TreeModel treeModel;

    public Editor() {
        change=false;
        EditorUtils.setDefaultMap(10, 10);
        playerCount = 1;
        treeModel = new TreeModel();
    }

    public void onEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "TileModifiedEvent":
                onTileModified((TileModifiedEvent) event);
                break;

            case "TileConnectionModifiedEvent":
                onTileConnectionModified((TileConnectionModifiedEvent) event);
                break;

            case "TilesDisconnectedEvent":
                onTilesDisconnected((TilesDisconnectedEvent) event);
                break;

            case "ArrowModifiedEvent":
                onArrowModified((ArrowModifiedEvent) event);
                break;

            case "LevelLoadedEvent":
                EditorUtils.loadLevel(((LevelLoadedEvent) event).getPath());
                IOManager.getInstance().drawEditor();
                break;

            case "LevelSavedEvent":
                GameManager.getInstance().onEvent(new SavePathEvent());
                EditorUtils.saveLevel(((LevelSavedEvent) event).getPath());
                break;

            case "EnemySelectedEvent":
            {
                updateEnemyPath();
                EnemySelectedEvent enemySelectedEvent = (EnemySelectedEvent) event;
                currentEnemy = enemySelectedEvent.getSmartEnemy();
                currentPath = listToPath();
                /*
                DefaultMutableTreeNode path = new DefaultMutableTreeNode("Smart entities");
                path.add(new DefaultMutableTreeNode("Smart enemy ("+enemySelectedEvent.getSmartEnemy().getX()+", "+enemySelectedEvent.getSmartEnemy().getY()+")"));
                //DefaultMutableTreeNode path = new DefaultMutableTreeNode("Smart enemy ("+enemySelectedEvent.getSmartEnemy().getX()+", "+enemySelectedEvent.getSmartEnemy().getY()+")");
                referenceTree.getSelectionModel().setSelectionPath(new TreePath(path));
                */

                IOManager.getInstance().drawEditor();
                break;
            }
            case "SavePathEvent":
            {
                updateEnemyPath();
                break;
            }
            case "ChangeLayerEvent":
            {
                ChangeLayerEvent chlev = (ChangeLayerEvent) event;
                //layer = chlev.getLayer();
                IOManager.getInstance().drawEditor();
                break;
            }

            case "EditorStateEvent":
            {
                EditorStateEvent est = (EditorStateEvent) event;
                //layer = est.getLayer();
                currentEnemy = est.getEnemy();
                if (currentEnemy != null)
                    currentPath = listToPath();
                else
                    currentPath = null;
                //mode = est.getMode();
                IOManager.getInstance().drawEditor();
                break;
            }

            default: return;
        }

        if (change) {
            IOManager.getInstance().drawEditor();
            change = false;
        }
    }

    private void onTileModified(TileModifiedEvent event) {
        if (event.isRemove()) {
            editorPlaceTile(EditableTile.EMPTY, event.getLayer(), event.getX(), event.getY());
        } else {
            editorPlaceTile(event.getType(), event.getLayer(), event.getX(), event.getY());
        }
    }

    private void editorPlaceTile(EditorGraphics incomingTile, Layer layer, int x, int y) {
        switch (layer) {
            case BOTH:
                if (incomingTile instanceof EditableTile tile) {
                    {
                        if (tile.preferredLayer == Layer.BOTH || tile.preferredLayer == UPPER) {
                            editorPlaceTile(tile, UPPER, x, y);
                            if (tile.fullTileWhenBoth || EditorUtils.objectToEditable(GameManager.getInstance().getMap().getBottomLayer(x, y)).fullTileWhenBoth) {
                                editorPlaceTile(EMPTY, Layer.BOTTOM, x, y);
                            }
                        }
                        if (tile.preferredLayer == Layer.BOTH || tile.preferredLayer == Layer.BOTTOM) {
                            editorPlaceTile(tile, Layer.BOTTOM, x, y);
                            if (tile.fullTileWhenBoth || EditorUtils.objectToEditable(GameManager.getInstance().getMap().getUpperLayer(x, y)).fullTileWhenBoth) {
                                editorPlaceTile(EMPTY, UPPER, x, y);
                            }
                        }
                    }
                }
                break;
            case UPPER:
                if (incomingTile instanceof EditableTile tile) {
                    if (tile.isPlaceableUpper && tile != EditorUtils.objectToEditable(GameManager.getInstance().getMap().getUpperLayer(x, y)) && !(GameManager.getInstance().getMap().getUpperLayer(x, y) instanceof PlayerCharacter && playerCount == 1)) {
                        Tile mapTile = GameManager.getInstance().getMap().getUpperLayer(x, y);
                        switch (EditorUtils.objectToEditable(mapTile)) {
                            case SMART: {
                                treeModel.removeNode("Smart enemy (" + x + ", " + y + ")");
                                break;
                            }
                            //moze w przyszlosci bedzie wiecej
                        }
                        if (!(GameManager.getInstance().getMap().getUpperLayer(x, y) instanceof PlayerCharacter)) {
                            GameManager.getInstance().getMap().setUpperLayer(x, y, EditorUtils.editableToObject(tile, x, y));
                            change = true;
                        }

                        switch (tile) {
                            case SMART: {
                                treeModel.addNode("Smart enemy (" + x + ", " + y + ")");
                                break;
                            }
                            case PLAYER: {
                                for (int i = 0; i < GameManager.getInstance().getMap().getWidth(); i++) {
                                    for (int j = 0; j < GameManager.getInstance().getMap().getHeight(); j++) {
                                        if (GameManager.getInstance().getMap().getUpperLayer(i, j) instanceof ChasingEnemy enemy) {
                                            enemy.addConnection(GameManager.getInstance().getMap().getUpperLayer(x, y));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            case BOTTOM:
                if (incomingTile instanceof EditableTile tile) {
                    if (tile == enums.EditableTile.EMPTY) {
                        tile = FLOOR;
                    }
                    if (tile.isPlaceableBottom && tile != EditorUtils.objectToEditable(GameManager.getInstance().getMap().getBottomLayer(x, y))) {
                        GameManager.getInstance().getMap().setBottomLayer(x, y, EditorUtils.editableToObject(tile, x, y));
                        change = true;
                    }
                }
                break;
        }
    }

    private void onTileConnectionModified(TileConnectionModifiedEvent event) {

        ArrayList<Tile> potentialConnections = new ArrayList<>();

        if (event.getLayer() == Layer.BOTTOM || event.getLayer() == Layer.BOTH) {
            potentialConnections.add(GameManager.getInstance().getMap().getBottomLayer(event.getX(), event.getY()));
        } else if (event.getLayer() == UPPER || event.getLayer() == Layer.BOTH) {
            potentialConnections.add(GameManager.getInstance().getMap().getUpperLayer(event.getX(), event.getY()));
        }

        for (Tile to : potentialConnections) {
            if (EditorUtils.objectToConnectable((Tile) event.getFrom()).canConnect(EditorUtils.objectToEditable(to))) {
                if (!event.isRemove()) {
                    event.getFrom().addConnection(to);
                } else {
                    event.getFrom().removeConnection(to);
                }
                change = true;
            }
        }
    }

    private void onTilesDisconnected(TilesDisconnectedEvent event) {
        event.getFrom().removeConnection(event.getTo());
        change = true;
    }

    private void onArrowModified(ArrowModifiedEvent event) {
        if (currentPath!=null) {
            Arrow arrow = event.getArrow();

            if (event.isRemove()) {
                arrow = EMPTY;
            }

            currentPath[event.getX()][event.getY()] = arrow;
            change = true;
        }
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

    public EditorGraphics[][] resizePath()
    {
        EditorGraphics[][] path = new EditorGraphics[GameManager.getInstance().getMap().getWidth()][GameManager.getInstance().getMap().getHeight()];
        for (int i = 0 ; i < path.length ; ++i)
        {
            for (int j = 0 ; j < path[0].length ; ++j)
            {
                if (i < currentPath.length && j < currentPath[0].length) {
                    path[i][j] = currentPath[i][j];
                } else {
                    path[i][j] = EMPTY;
                }
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
                    if (i.getX() < GameManager.getInstance().getMap().getWidth() && i.getY() < GameManager.getInstance().getMap().getHeight()) {
                        path[i.getX()][i.getY()] = arrow;
                    }
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


    public List<Tile> getTilesInConnectableCategory(ConnectableTile category) {
        ArrayList<Tile> list = new ArrayList<>();
        for (int i=0;i<GameManager.getInstance().getMap().getWidth();i++)
        {
            for (int j=0;j<GameManager.getInstance().getMap().getHeight();j++)
            {
                if (EditorUtils.objectToConnectable(GameManager.getInstance().getMap().getBottomLayer(i,j)) == category) {
                    list.add(GameManager.getInstance().getMap().getBottomLayer(i,j));
                }
                else if (EditorUtils.objectToConnectable(GameManager.getInstance().getMap().getUpperLayer(i,j)) == category) {
                    list.add(GameManager.getInstance().getMap().getUpperLayer(i, j));
                }
            }
        }
        return list;
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
    /*
    public JTree getReferenceTree() {
        return referenceTree;
    }

    public void setReferenceTree(JTree referenceTree) {
        this.referenceTree = referenceTree;
    }

     */
}
