package editor;

import IO.IOManager;
import enums.EditableTile;
import enums.EditorMode;
import enums.Layer;
import event.*;
import levelloader.LevelLoader;
import levelloader.LevelNotSaved;
import tile.*;
import gamemanager.GameManager;
import map.Map;
import tile.Box;
import turnstrategy.PlayerFollower;

import javax.swing.*;

import static enums.EditableTile.*;


public class Editor implements EventObserver {

    private Layer layer = Layer.BOTH;
    private EditorMode mode = EditorMode.ADD;
    private EditableTile heldTile;
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
    public Tile enumToObject(EditableTile editableTile, int x, int y) {
        return switch (editableTile) {
            case BOX ->  new Box(x, y);
            case GOAL ->  new Goal(x, y);
            case WALL ->  new Wall(x, y);
            case ENEMY ->  new ChasingEnemy(x, y, findPlayer());
            case MIMIC -> new MimicEnemy(x, y);
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
            return EMPTY;
        }
        return switch (tile.getClass().getSimpleName())
        {
            case "Box" -> BOX;
            case "Button" -> BUTTON;
            case "ChasingEnemy" -> ENEMY;
            case "MimicEnemy" -> MIMIC;
            case "Door" -> DOOR;
            case "Floor" -> FLOOR;
            case "Goal" -> GOAL;
            case "PlayerCharacter" -> PLAYER;
            case "Wall" -> WALL;
            default -> EMPTY;
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
            default: return;
        }
    }

    private void editorPlaceTile(EditableTile tile, Layer layer, int x, int y) {
        switch (layer) {
            case BOTH:
                if (tile.preferredLayer == Layer.BOTH || tile.preferredLayer == Layer.UPPER) {
                    editorPlaceTile(tile, Layer.UPPER, x, y);
                    if (tile.fullTileWhenBoth || objectToEnum(GameManager.getInstance().getMap().getBottomLayer(x,y)).fullTileWhenBoth)
                    {
                        editorPlaceTile(EMPTY, Layer.BOTTOM, x, y);
                    }
                }
                if (tile.preferredLayer == Layer.BOTH || tile.preferredLayer == Layer.BOTTOM) {
                    editorPlaceTile(tile, Layer.BOTTOM, x, y);
                    if (tile.fullTileWhenBoth || objectToEnum(GameManager.getInstance().getMap().getUpperLayer(x,y)).fullTileWhenBoth)
                    {
                        editorPlaceTile(EMPTY, Layer.UPPER, x, y);
                    }
                }
                this.layer = Layer.BOTH;
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
                                    if (((PlayerFollower) enemy.getTurnStrategy()).getTargetTile().getX() == x && ((PlayerFollower) (enemy.getTurnStrategy())).getTargetTile().getY() == y) {
                                        ((PlayerFollower) (((ChasingEnemy) (GameManager.getInstance().getMap().getUpperLayer(i, j))).getTurnStrategy())).setTargetTile(findPlayer());
                                    }
                                }
                            }
                        }
                    }
                    else {
                        GameManager.getInstance().getMap().setUpperLayer(x, y, enumToObject(tile, x, y));
                    }

                    change = true;
                }
                break;
            case BOTTOM:
                if (tile == EMPTY)
                {
                    tile = FLOOR;
                }
                if (tile.isPlaceableBottom && tile != objectToEnum(GameManager.getInstance().getMap().getBottomLayer(x, y)) && !(tile == EMPTY && objectToEnum(GameManager.getInstance().getMap().getBottomLayer(x, y)) == FLOOR)) {
                    GameManager.getInstance().getMap().setBottomLayer(x, y, enumToObject(tile, x, y));
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

    public EditorMode getMode() {
        return mode;
    }

    public void setMode(EditorMode mode) {
        this.mode = mode;
    }
}
