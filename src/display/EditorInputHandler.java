package display;

import IO.IOManager;
import connectableinterface.Connectable;
import editor.TreeModel;
import enums.*;
import event.Event;
import event.EventObserver;
import event.display.*;
import event.editor.*;
import gamemanager.GameManager;
import tile.SmartEnemy;
import tile.Tile;

import java.nio.file.Path;

import static enums.Arrow.*;

public class EditorInputHandler implements EventObserver {
    // Layer
    private Layer layer = Layer.BOTH;

    // Mode (ADD/CONNECT/SELECT (for pathedit)/PATH (for pathedit)
    private EditorMode mode = EditorMode.ADD;
    private boolean modeActive = false;

    // Current tile selected for ADD
    private EditorGraphics heldTile = EditableTile.EMPTY;

    // Current tile selected for CONNECT
    private Tile connectingTile;

    // Current enemy and tree model for pathedit
    private SmartEnemy currentEnemy;
    private EditorGraphics[][] currentPath;
    private TreeModel treeModel;

    @Override
    public void onEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "InteractiveTilePressedEvent":
                onInteractiveTilePressed((InteractiveTilePressedEvent) event);
                break;

            case "PalettePressedEvent":
                onTilePalettePressed((PalettePressedEvent) event);
                break;

            case "ChangeLayerEvent":
                onChangeLayer((ChangeLayerEvent) event);
                break;

            case "ModeChangedEvent":
                onModeChanged((ModeChangedEvent) event);
                break;

            case "ModeActiveChangedEvent":
                onModeActiveChanged((ModeActiveChangedEvent) event);
                break;

            case "ConnectableTileSelectedEvent":
                onConnectingTileChanged((ConnectableTileSelectedEvent) event);
                break;

            case "ConnectionDeletedEvent":
                onConnectionDeleted((ConnectionDeletedEvent) event);
                break;

            case "EnemySelectedEvent":
                onEnemySelected((EnemySelectedEvent) event);
                break;
            case "SavePathEvent":
                onEnemySaved((SavePathEvent) event);
                break;
            case "ArrowModifiedEvent":
                onArrowModified((ArrowModifiedEvent) event);
                break;
            case "MapResizedEvent":
                onMapResized(new MapResizeEvent());
                break;
        }
    }

    private void onTilePalettePressed(PalettePressedEvent event) {
        heldTile = event.getType();
    }

    private void onInteractiveTilePressed(InteractiveTilePressedEvent event) {
        if (modeActive) {
            switch (mode) {
                case ADD:
                    GameManager.getInstance().onEvent(new TileModifiedEvent((EditableTile) heldTile, event.getX(), event.getY(), layer, event.isRightMouseButton()));
                    break;
                case CONNECT:
                    GameManager.getInstance().onEvent(new TileConnectionModifiedEvent((Connectable) connectingTile, event.getX(), event.getY(), layer, event.isRightMouseButton()));
                    break;
                case SELECT:
                    GameManager.getInstance().onEvent(new ArrowModifiedEvent((Arrow) heldTile, event.getX(), event.getY(), event.isRightMouseButton()));
                    break;
            }
        }
    }

    private void onChangeLayer(ChangeLayerEvent event) {
        layer = event.getLayer();
    }

    private void onModeChanged(ModeChangedEvent event) {
        mode = event.getMode();

        switch (mode) {
            case ADD:
                currentEnemy = null;
                currentPath = null;
                heldTile = EditableTile.EMPTY;
                break;
            case SELECT, PATHEDIT:
                heldTile = Arrow.ARROW_UP;
                break;
        }
    }

    private void onModeActiveChanged(ModeActiveChangedEvent event) {
        modeActive = event.isState();
    }

    private void onConnectingTileChanged(ConnectableTileSelectedEvent event) {
        connectingTile = event.getTile();
        IOManager.getInstance().drawEditor();
    }

    private void onConnectionDeleted(ConnectionDeletedEvent event) {
        GameManager.getInstance().onEvent(new TilesDisconnectedEvent((Connectable) connectingTile, event.getTile()));
    }

    private void onEnemySelected(EnemySelectedEvent event) {
        mode = EditorMode.SELECT;
        layer = Layer.PATH;
        if (currentEnemy != null)
            //mimo ze wyglada jakbysmy tworzyli obiekt "na darmo" to chyba jest to spojne z przyjetymi zasadami
            onEnemySaved(new SavePathEvent());

        currentEnemy = event.getSmartEnemy();
        if (currentEnemy != null)
            currentPath = PathEditorHelper.listToPath(currentEnemy);

        GameManager.getInstance().onEvent(new EditorChangeEvent());
    }

    private void onArrowModified(ArrowModifiedEvent event) {
        if (currentPath!=null) {
            Arrow arrow = event.getArrow();

            if (event.isRemove()) {
                arrow = EMPTY;
            }

            currentPath[event.getX()][event.getY()] = arrow;
            GameManager.getInstance().onEvent(new EditorChangeEvent());
        }
    }
    private void onEnemySaved(SavePathEvent event)
    {
        PathEditorHelper.updateEnemyPath(currentEnemy, currentPath);
    }
    private void onMapResized(MapResizeEvent event)
    {
        PathEditorHelper.resizePath(currentPath);
        if (currentEnemy != null)
            onEnemySaved(new SavePathEvent());
    }

    public Layer getLayer() {
        return layer;
    }

    public EditorMode getMode() {
        return mode;
    }

    public boolean isModeActive() {
        return modeActive;
    }

    public EditorGraphics getHeldTile() {
        return heldTile;
    }

    public Tile getConnectingTile() {
        return connectingTile;
    }

    public SmartEnemy getCurrentEnemy() {
        return currentEnemy;
    }

    public TreeModel getTreeModel() {
        return treeModel;
    }
    public EditorGraphics[][] getCurrentPath() {
        return currentPath;
    }
}
