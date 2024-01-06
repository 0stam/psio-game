package editor;

import IO.IOManager;
import enums.EditableTile;
import enums.Layer;
import event.*;
import levelloader.LevelLoader;
import levelloader.LevelNotSaved;
import tile.*;
import gamemanager.GameManager;

import static enums.EditableTile.*;


public class Editor implements EventObserver {

    private Layer layer = Layer.BOTH;
    private EditableTile heldTile;
    private boolean change;
    private int playerCount;

    public Editor() {
        this.setHeldTile(EMPTY);
        change=false;
        playerCount = 1;
    }

    public EditableTile getHeldTile() {
        return heldTile;
    }

    public void setHeldTile(EditableTile heldTile) {
        this.heldTile = heldTile;
    }
    //Note: enumToObject has functions probably specific to the editor
    //wall and empty sometimes return a different object, enemy finds a player to attach
    //only use if you know what you're doing
    public Tile enumToObject(EditableTile editableTile, int x, int y) {
        switch (editableTile) {
            case BOX: { return new Box(x, y); }
            case GOAL: { return new Goal(x, y); }
            case WALL: { return (layer==Layer.UPPER) ? null : new Wall(x, y); }
            case ENEMY: {
                for (int i=0;i<GameManager.getInstance().getMap().getWidth();i++)
                {
                    for (int j=0;j<GameManager.getInstance().getMap().getHeight();j++)
                    {
                        if (GameManager.getInstance().getMap().getUpperLayer(i,j) instanceof PlayerCharacter)
                        {
                            return new ChasingEnemy(x, y, GameManager.getInstance().getMap().getUpperLayer(i,j));
                        }
                    }
                }
            }
            case FLOOR: { return new Floor(x, y); }
            case PLAYER: { return new PlayerCharacter(x, y); }
            case DOOR: { return new Door(x, y); }
            case BUTTON: { return new Button(x, y); }
            case EMPTY: { return (layer==Layer.UPPER) ? null : new Floor(x,y); }
            default: { return null; }
        }
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
            case "Door" -> DOOR;
            case "Floor" -> FLOOR;
            case "Goal" -> GOAL;
            case "PlayerCharacter" -> PLAYER;
            case "Wall" -> WALL;
            default -> null;
        };
    }
    public void onEvent(Event event) {
        if (!(event instanceof EditorEvent)) {
            return;
        }

        if (event instanceof PalettePressedEvent palettePressedEvent) {
            this.setHeldTile(palettePressedEvent.getType());
        }
        if (event instanceof TilePressedEvent tilePressedEvent)
        {
            this.setLayer(tilePressedEvent.getLayer());
            editorPlaceTile(heldTile, layer, tilePressedEvent.getX(), tilePressedEvent.getY());
            if (change) {
                //nie wiem czy jest sens metody ktora rysowala by 1 tile
                //jasne, ze byloby to efektywniejsze, ale wg naszego systemu chyba chcemy to
                //wywolac przez IOmanager, ktory w interfejsie musialby miec taka metode
                //wymaga duzych zmian, a i tak nikt raczej tego nie zauwazy
                IOManager.getInstance().drawEditor();
                change = false;
            }
        }
        if (event instanceof LevelEvent levelEvent) {
            if (event instanceof LevelLoadedEvent) {
                try {
                    LevelLoader.loadLevel(levelEvent.getPath());
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
                }
            } else if (event instanceof LevelSavedEvent levelSavedEvent) {
                try {
                    LevelLoader.saveLevel(levelSavedEvent.getPath(),GameManager.getInstance().getMap());
                }
                catch (LevelNotSaved e) {
                    System.err.println("Błąd zapisywania poziomu");
                }
            }
        }
        if (event instanceof ConnectionEvent connectionEvent) {
            //TODO: change casting from button/door to more general statements
            if (event instanceof ConnectionCreatedEvent) {
                ((Button) connectionEvent.getFrom()).addObserver((Door) connectionEvent.getTo());
            }
            if (event instanceof ConnectionDeletedEvent) {
                ((Button) connectionEvent.getFrom()).removeObserver((Door) connectionEvent.getTo());
            }
        }
    }
    private void editorPlaceTile(EditableTile tile, Layer layer, int x, int y) {
        switch (layer) {
            case BOTH:
                if (tile.preferredLayer == Layer.BOTH || tile.preferredLayer == Layer.UPPER) {
                    this.layer = Layer.UPPER;
                    editorPlaceTile(tile, Layer.UPPER, x, y);
                }
                if (tile.preferredLayer == Layer.BOTH || tile.preferredLayer == Layer.BOTTOM) {
                    this.layer = Layer.BOTTOM;
                    editorPlaceTile(tile, Layer.BOTTOM, x, y);
                }
                this.layer = Layer.BOTH;
                break;
            case UPPER:
                if (tile.isPlaceableUpper && tile != objectToEnum(GameManager.getInstance().getMap().getUpperLayer(x, y)) && !(GameManager.getInstance().getMap().getUpperLayer(x, y) instanceof PlayerCharacter && playerCount == 1)) {
                    if (tile == PLAYER)
                        playerCount++;
                    if (GameManager.getInstance().getMap().getUpperLayer(x,y) instanceof PlayerCharacter)
                        playerCount--;
                    GameManager.getInstance().getMap().setUpperLayer(x, y, enumToObject(tile, x, y));

                    change = true;
                }
                break;
            case BOTTOM:
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
}
