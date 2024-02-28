package editor;

import IO.IOManager;
import enums.*;
import event.*;
import event.display.ChangeLayerEvent;
import event.editor.*;
import levelloader.LevelLoader;
import tile.*;
import gamemanager.GameManager;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import java.util.List;
import java.util.ArrayList;

import static enums.Arrow.EMPTY;
import static enums.EditableTile.*;
import static enums.Layer.UPPER;


public class Editor implements EventObserver {

    // Variables required for the logic to work
    private boolean change;
    private int playerCount;
    private EnemiesTreeModel enemiesTreeModel;
    private SignsTreeModel signsTreeModel;

    public Editor() {
        change=false;
        EditorUtils.setDefaultMap(10, 10);
        playerCount = 1;
        enemiesTreeModel = new EnemiesTreeModel();
        signsTreeModel = new SignsTreeModel();
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

            case "LevelLoadedEvent":
                EditorUtils.loadLevel(((LevelLoadedEvent) event).getPath());
                IOManager.getInstance().drawEditor();
                break;

            case "LevelSavedEvent":
                GameManager.getInstance().onEvent(new SaveMessageEvent());
                GameManager.getInstance().onEvent(new SavePathEvent());
                EditorUtils.saveLevel(((LevelSavedEvent) event).getPath());
                break;
            case "EditorChangeEvent":
                change = true;
                break;
            case "ChangeLayerEvent":
                ChangeLayerEvent chlev = (ChangeLayerEvent) event;
                IOManager.getInstance().getInputHandler().onEvent((ChangeLayerEvent)event);
                IOManager.getInstance().drawEditor();
                break;
            case "ArrowModifiedEvent":
                IOManager.getInstance().getInputHandler().onEvent((ArrowModifiedEvent) event);
                break;
            case "EnemySelectedEvent":
                IOManager.getInstance().getInputHandler().onEvent((EnemySelectedEvent)event);
                break;
            case "SignSelectedEvent":
                IOManager.getInstance().getInputHandler().onEvent((SignSelectedEvent)event);
                break;
            case "SavePathEvent":
                IOManager.getInstance().getInputHandler().onEvent((SavePathEvent)event);
                break;
            case "SaveMessageEvent":
                IOManager.getInstance().getInputHandler().onEvent((SaveMessageEvent)event);
                break;

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
                                editorPlaceTile(EditableTile.EMPTY, Layer.BOTTOM, x, y);
                            }
                        }
                        if (tile.preferredLayer == Layer.BOTH || tile.preferredLayer == Layer.BOTTOM) {
                            editorPlaceTile(tile, Layer.BOTTOM, x, y);
                            if (tile.fullTileWhenBoth || EditorUtils.objectToEditable(GameManager.getInstance().getMap().getUpperLayer(x, y)).fullTileWhenBoth) {
                                editorPlaceTile(EditableTile.EMPTY, UPPER, x, y);
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
                                enemiesTreeModel.removeNodeFromParent(TreeDFS.findNode((DefaultMutableTreeNode) enemiesTreeModel.getRoot(),mapTile));
                                break;
                            }
                            case SIGN: {
                                signsTreeModel.removeNodeFromParent(TreeDFS.findNode((DefaultMutableTreeNode) signsTreeModel.getRoot(),mapTile));
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
                                enemiesTreeModel.insertNodeInto(new DefaultMutableTreeNode(EditorUtils.editableToObject(tile, x, y)), (DefaultMutableTreeNode)enemiesTreeModel.getRoot(), 0);
                                break;
                            }
                            case SIGN: {
                                signsTreeModel.insertNodeInto(new DefaultMutableTreeNode(EditorUtils.editableToObject(tile, x, y)), (DefaultMutableTreeNode)signsTreeModel.getRoot(), 0);
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
                        Tile mapTile = GameManager.getInstance().getMap().getBottomLayer(x, y);
                        switch (EditorUtils.objectToEditable(mapTile)) {
                            case SMART: {
                                enemiesTreeModel.removeNodeFromParent(TreeDFS.findNode((DefaultMutableTreeNode) enemiesTreeModel.getRoot(),mapTile));
                                break;
                            }
                            case SIGN: {
                                signsTreeModel.removeNodeFromParent(TreeDFS.findNode((DefaultMutableTreeNode) signsTreeModel.getRoot(),mapTile));
                                break;
                            }
                            //moze w przyszlosci bedzie wiecej
                        }
                        GameManager.getInstance().getMap().setBottomLayer(x, y, EditorUtils.editableToObject(tile, x, y));
                        change = true;
                        switch (tile) {
                            case SMART: {
                                enemiesTreeModel.insertNodeInto(new DefaultMutableTreeNode(EditorUtils.editableToObject(tile, x, y)), (DefaultMutableTreeNode)enemiesTreeModel.getRoot(), 0);
                                break;
                            }
                            case SIGN: {
                                signsTreeModel.insertNodeInto(new DefaultMutableTreeNode(EditorUtils.editableToObject(tile, x, y)), (DefaultMutableTreeNode)signsTreeModel.getRoot(), 0);
                                break;
                            }
                            //moze w przyszlosci bedzie wiecej
                        }
                    }
                }
                break;
        }
    }

    private void onTileConnectionModified(TileConnectionModifiedEvent event) {

        ArrayList<Tile> potentialConnections = new ArrayList<>();

        if (event.getLayer() == Layer.BOTTOM || event.getLayer() == Layer.BOTH) {
            potentialConnections.add(GameManager.getInstance().getMap().getBottomLayer(event.getX(), event.getY()));
        }
        if (event.getLayer() == UPPER || event.getLayer() == Layer.BOTH) {
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
/*
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

 */

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

    public EnemiesTreeModel getEnemiesTreeModel() {
        return enemiesTreeModel;
    }

    public void setEnemiesTreeModel(EnemiesTreeModel enemiesTreeModel) {
        this.enemiesTreeModel = enemiesTreeModel;
    }

    public SignsTreeModel getSignsTreeModel() {
        return signsTreeModel;
    }

    public void setSignsTreeModel(SignsTreeModel signsTreeModel) {
        this.signsTreeModel = signsTreeModel;
    }
}
