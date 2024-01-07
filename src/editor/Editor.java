package editor;

import IO.IOManager;
import enums.EditableTile;
import enums.Layer;
import event.*;
import levelloader.LevelLoader;
import levelloader.LevelNotSaved;
import tile.*;
import gamemanager.GameManager;
import map.Map;

import static enums.EditableTile.*;


public class Editor implements EventObserver {

    private Layer layer = Layer.BOTH;
    private EditableTile heldTile;
    private boolean change;

    public Editor() {
        this.setHeldTile(EMPTY);
        change=false;
        setDefaultMap(10, 10);
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
            case BOX -> new Box(x, y);
            case GOAL -> new Goal(x, y);
            case WALL -> new Wall(x, y);
            case ENEMY -> new ChasingEnemy(x, y, null);
            case FLOOR -> new Floor(x, y);
            case PLAYER -> new PlayerCharacter(x, y);
            case DOOR -> new Door(x, y);
            case BUTTON -> new Button(x, y);
            default -> null;
        };
    }
    public EditableTile objectToEnum(Tile tile)
    {
        if (tile == null){
            return null;
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
        if (!(event instanceof EditorEvent)) {
            return;
        }

        if (event instanceof PalettePressedEvent palettePressedEvent) {
            this.setHeldTile(palettePressedEvent.getType());
        }
        if (event instanceof TilePressedEvent tilePressedEvent)
        {
            switch (tilePressedEvent.getLayer())
            {
                //TODO: un-yanderedev-ify conditions
                case BOTH:
                {
                    if (heldTile != BUTTON && heldTile != DOOR && heldTile != FLOOR && heldTile != objectToEnum(GameManager.getInstance().getMap().getUpperLayer(tilePressedEvent.getX(), tilePressedEvent.getY()))) {
                        GameManager.getInstance().getMap().setUpperLayer(tilePressedEvent.getX(), tilePressedEvent.getY(), enumToObject(heldTile, tilePressedEvent.getX(), tilePressedEvent.getY()));
                        change = true;
                    }
                    editorPlaceBottomTile(tilePressedEvent);
                    break;
                }
                case UPPER:
                {
                    if (heldTile != objectToEnum(GameManager.getInstance().getMap().getUpperLayer(tilePressedEvent.getX(), tilePressedEvent.getY()))) {
                        GameManager.getInstance().getMap().setUpperLayer(tilePressedEvent.getX(), tilePressedEvent.getY(), enumToObject(heldTile, tilePressedEvent.getX(), tilePressedEvent.getY()));
                        change = true;
                    }
                    break;
                }
                case BOTTOM: {
                    editorPlaceBottomTile(tilePressedEvent);
                    break;
                }
            }
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

    private void editorPlaceBottomTile(TilePressedEvent event) {
        if (heldTile != ENEMY && heldTile != PLAYER && heldTile != BOX && heldTile != EMPTY && heldTile != objectToEnum(GameManager.getInstance().getMap().getBottomLayer(event.getX(), event.getY()))) {
            GameManager.getInstance().getMap().setBottomLayer(event.getX(), event.getY(), enumToObject(heldTile, event.getX(), event.getY()));
            change = true;
        }
        else if (heldTile == EMPTY && FLOOR != objectToEnum(GameManager.getInstance().getMap().getUpperLayer(event.getX(), event.getY()))) {
            GameManager.getInstance().getMap().setBottomLayer(event.getX(), event.getY(), enumToObject(FLOOR, event.getX(), event.getY()));
            change = true;
        }
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }
}
