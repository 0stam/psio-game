package display;

import IO.IOManager;
import connectableinterface.Connectable;
import editor.EnemiesTreeModel;
import editor.SignsTreeModel;
import enums.*;
import event.Event;
import event.EventObserver;
import event.display.*;
import event.editor.*;
import gamemanager.GameManager;
import tile.Sign;
import tile.SmartEnemy;
import tile.Tile;

import static enums.Arrow.*;

public class EditorInputHandler implements EventObserver {
    // Layer
    private Layer layer = Layer.BOTH;

    // Mode (ADD/CONNECT/SELECT (for pathedit)/PATH (for pathedit)
    private EditorMode mode = EditorMode.ADD;
    private boolean modeActive = false;

    // Current tile selected for ADD
    private EditableTile heldTile = EditableTile.EMPTY;

    // Current tile selected for CONNECT
    private Tile connectingTile;

    // Current enemy, arrow and tree model for pathedit
    private SmartEnemy currentEnemy;
    private Arrow heldArrow = ARROW_UP;
    private EditorGraphics[][] currentPath;
    private EnemiesTreeModel enemiesTreeModel;

    private Sign currentSign;
    private SignsTreeModel signsTreeModel;

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
            case "SignSelectedEvent":
                onSignSelected((SignSelectedEvent) event);
                break;
            case "SavePathEvent":
                onEnemySaved((SavePathEvent) event);
                break;
            case "SaveMessageEvent":
                onSignSaved((SaveMessageEvent) event);
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
        if (event.getType() instanceof EditableTile) {
            EditableTile editableTile = (EditableTile)event.getType();
            heldTile = editableTile;
        } else if (event.getType() instanceof Arrow) {
            Arrow arrow = (Arrow)event.getType();
            heldArrow = arrow;
        }
    }

    private void onInteractiveTilePressed(InteractiveTilePressedEvent event) {
        if (modeActive) {
            switch (mode) {
                case ADD:
                    GameManager.getInstance().onEvent(new TileModifiedEvent(heldTile, event.getX(), event.getY(), layer, event.isRightMouseButton()));
                    break;
                case CONNECT:
                    GameManager.getInstance().onEvent(new TileConnectionModifiedEvent((Connectable) connectingTile, event.getX(), event.getY(), layer, event.isRightMouseButton()));
                    break;
                case SELECT:
                    GameManager.getInstance().onEvent(new ArrowModifiedEvent(heldArrow, event.getX(), event.getY(), event.isRightMouseButton()));
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
                currentSign = null;
                break;
            case SELECT:
            case PATHEDIT:
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

    private void onSignSelected(SignSelectedEvent event) {
        mode = EditorMode.TEXTEDIT;
        layer = Layer.BOTH;
        if (currentSign != null)
            onSignSaved(new SaveMessageEvent());

        currentSign = event.getSign();
        MessagesPalette.setText(currentSign.getMessage());

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

    private void onSignSaved(SaveMessageEvent event)
    {
        if (currentSign != null) {
            currentSign.setMessage(MessagesPalette.getText());
        }
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

    public EnemiesTreeModel getEnemiesTreeModel() {
        return enemiesTreeModel;
    }
    public EditorGraphics[][] getCurrentPath() {
        return currentPath;
    }

    public Sign getCurrentSign() {
        return currentSign;
    }
}
