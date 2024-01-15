package display;

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
    }

    private void onConnectionDeleted(ConnectionDeletedEvent event) {
        GameManager.getInstance().onEvent(new TilesDisconnectedEvent((Connectable) connectingTile, event.getTile()));
    }

    private void onEnemySelected(EnemySelectedEvent event) {
        mode = EditorMode.SELECT;
        layer = Layer.PATH;
        SmartEnemy previousEnemy = currentEnemy;
        currentEnemy = event.getSmartEnemy();
        GameManager.getInstance().onEvent(event);
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
}
