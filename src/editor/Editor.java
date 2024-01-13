package editor;

import connectableinterface.Connectable;
import IO.IOManager;
import enums.ConnectableTile;
import enums.EditableTile;
import enums.EditorMode;
import enums.Layer;
import event.*;
import levelloader.*;
import tile.*;
import gamemanager.GameManager;
import map.Map;
import tile.Box;
import turnstrategy.PlayerFollower;
import java.util.List;
import java.util.ArrayList;

import static enums.EditableTile.*;


public class Editor implements EventObserver {
    // Variables required for the graphics to work
    private Layer layer = Layer.BOTH;
    private EditorMode mode = EditorMode.DEFAULT;
    private boolean modeEnabled = false;
    private EditableTile heldTile;
    private Tile connectingTile;

    // Variables required for the logic to work
    private boolean change;
    private int playerCount;

    public Editor() {
        this.setHeldTile(EMPTY);
        change=false;
        setDefaultMap(10, 10);
        playerCount = 1;
    }

    public EditableTile getHeldTile() {
        return heldTile;
    }

    public void setHeldTile(EditableTile heldTile) {
        this.heldTile = heldTile;
    }

    //pewnie powinno być gdzie indziej
    public Tile editableToObject(EditableTile editableTile, int x, int y) {
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
    public EditableTile objectToEditable(Tile tile)
    {
        if (tile == null){
            return EMPTY;
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
            default -> EMPTY;
        };
    }
    public ConnectableTile objectToConnectable(Tile tile)
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
            //TODO: IMPORTANT: implement mode handling
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
            case "ConnectableTileSelectedEvent": {
                ConnectableTileSelectedEvent connectableTileSelectedEvent = (ConnectableTileSelectedEvent) event;
                setConnectingTile(connectableTileSelectedEvent.getTile());
            }
            case "ConnectionCreatedEvent": {
                ConnectionEvent connectionEvent = (ConnectionEvent) event;
                if (connectingTile instanceof Connectable source)
                {
                    source.addConnection(connectionEvent.getTile());
                }
                else if (connectionEvent.getTile() instanceof Connectable source)
                {
                    source.addConnection(connectingTile);
                }
                break;
            }
            case "ConnectionDeletedEvent": {
                ConnectionEvent connectionEvent = (ConnectionEvent) event;
                if (connectingTile instanceof Connectable source)
                {
                    source.removeConnection(connectionEvent.getTile());
                }
                else if (connectionEvent.getTile() instanceof Connectable source)
                {
                    source.removeConnection(connectingTile);
                }
                break;
            }
            default: return;
        }
    }

    private void editorPlaceTile(EditableTile tile, Layer layer, int x, int y) {
        switch (layer) {
            case BOTH:
                if (tile.preferredLayer == Layer.BOTH || tile.preferredLayer == Layer.UPPER) {
                    editorPlaceTile(tile, Layer.UPPER, x, y);
                    if (tile.fullTileWhenBoth || objectToEditable(GameManager.getInstance().getMap().getBottomLayer(x,y)).fullTileWhenBoth)
                    {
                        editorPlaceTile(EMPTY, Layer.BOTTOM, x, y);
                    }
                }
                if (tile.preferredLayer == Layer.BOTH || tile.preferredLayer == Layer.BOTTOM) {
                    editorPlaceTile(tile, Layer.BOTTOM, x, y);
                    if (tile.fullTileWhenBoth || objectToEditable(GameManager.getInstance().getMap().getUpperLayer(x,y)).fullTileWhenBoth)
                    {
                        editorPlaceTile(EMPTY, Layer.UPPER, x, y);
                    }
                }
                this.layer = Layer.BOTH;
                break;
            case UPPER:
                if (tile.isPlaceableUpper && tile != objectToEditable(GameManager.getInstance().getMap().getUpperLayer(x, y)) && !(GameManager.getInstance().getMap().getUpperLayer(x, y) instanceof PlayerCharacter && playerCount == 1)) {
                    if (tile == PLAYER) {
                        playerCount++;
                    }
                    if (GameManager.getInstance().getMap().getUpperLayer(x,y) instanceof PlayerCharacter)
                    {
                        playerCount--;
                        GameManager.getInstance().getMap().setUpperLayer(x, y, editableToObject(tile, x, y));
                        for (int i=0;i<GameManager.getInstance().getMap().getWidth();i++)
                        {
                            for (int j=0;j<GameManager.getInstance().getMap().getHeight();j++)
                            {
                                if (GameManager.getInstance().getMap().getUpperLayer(i,j) instanceof ChasingEnemy enemy)
                                {
                                    if (((PlayerFollower) enemy.getTurnStrategy()).getTargetTile().getX() == x && ((PlayerFollower) (enemy.getTurnStrategy())).getTargetTile().getY() == y) {
                                        ((PlayerFollower) (((ChasingEnemy) (GameManager.getInstance().getMap().getUpperLayer(i, j))).getTurnStrategy())).setTargetTile(findPlayer());
                                    }
                                }
                            }
                        }
                    }
                    else {
                        GameManager.getInstance().getMap().setUpperLayer(x, y, editableToObject(tile, x, y));
                    }

                    change = true;
                }
                break;
            case BOTTOM:
                if (tile == EMPTY) {
                    tile = FLOOR;
                }
                if (tile.isPlaceableBottom && tile != objectToEditable(GameManager.getInstance().getMap().getBottomLayer(x, y))) {
                    GameManager.getInstance().getMap().setBottomLayer(x, y, editableToObject(tile, x, y));
                    change = true;
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

    public List<Tile> getTilesInConnectableCategory(ConnectableTile category) {
        ArrayList<Tile> list = new ArrayList<>();
        for (int i=0;i<GameManager.getInstance().getMap().getWidth();i++)
        {
            for (int j=0;j<GameManager.getInstance().getMap().getHeight();j++)
            {
                if (objectToConnectable(GameManager.getInstance().getMap().getBottomLayer(i,j)) == category) {
                    list.add(GameManager.getInstance().getMap().getBottomLayer(i,j));
                }
                else if (objectToConnectable(GameManager.getInstance().getMap().getUpperLayer(i,j)) == category) {
                    list.add(GameManager.getInstance().getMap().getBottomLayer(i, j));
                }
            }
        }
        return list;
    }

    public EditorMode getMode() {
        return mode;
    }

    public void setMode(EditorMode mode) {
        this.mode = mode;
    }

    public boolean isModeEnabled() {
        return modeEnabled;
    }

    public void setModeEnabled(boolean modeEnabled) {
        this.modeEnabled = modeEnabled;
    }

    public Tile getConnectingTile() {
        return connectingTile;
    }

    public void setConnectingTile(Tile connectingTile) {
        this.connectingTile = connectingTile;
    }
}