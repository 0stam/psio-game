package editor;

import IO.IOManager;
import enums.Graphics;
import event.*;
import levelloader.LevelLoader;
import levelloader.LevelNotSaved;
import tile.*;
import gamemanager.GameManager;

import static enums.Layer.*;
import static enums.Graphics.*;


public class Editor implements EventObserver {
    private Graphics HeldTile;
    private boolean change;
    public Editor()
    {
        this.setHeldTile(EMPTY);
        change=false;
    }
    public Graphics getHeldTile() {
        return HeldTile;
    }

    public void setHeldTile(Graphics heldTile) {
        HeldTile = heldTile;
    }

    //pewnie powinno być gdzie indziej
    public Tile graphicstoobject(Graphics graphics, int x, int y) {
        return switch (graphics) {
            case BOX -> new Box(x, y);
            case GOAL -> new Goal(x, y);
            case WALL -> new Wall(x, y);
            case ENEMY -> new ChasingEnemy(x, y, null);
            case FLOOR -> new Floor(x, y);
            case PLAYER -> new PlayerCharacter(x, y);
            case DOOR_CLOSED -> new Door(x, y);
            case BUTTON_RELEASED -> new Button(x, y);
            default -> null;
        };
    }
    public Graphics objecttographics(Tile tile)
    {
        if (tile == null){
            return null;
        }
        return switch (tile.getClass().getSimpleName())
        {
            case "Box" -> BOX;
            case "Button" -> BUTTON_RELEASED;
            case "ChasingEnemy" -> ENEMY;
            case "Door" -> DOOR_CLOSED;
            case "Floor" -> FLOOR;
            case "Goal" -> GOAL;
            case "PlayerCharacter" -> PLAYER;
            case "Wall" -> WALL;
            default -> null;
        };
    }
        public void onEvent(Event event) {
        if (event instanceof EditorEvent editorEvent) {
            if (event instanceof PalettePressedEvent palettePressedEvent)
            {
                this.setHeldTile(palettePressedEvent.getType());
            }
            if (event instanceof TilePressedEvent tilePressedEvent)
            {
                switch (tilePressedEvent.getLayer())
                {
                    //TODO: un-yanderedev-ify conditions
                    case BOTH:
                    {
                        if (HeldTile != BUTTON_RELEASED && HeldTile != DOOR_CLOSED && HeldTile != FLOOR && HeldTile != objecttographics(GameManager.getInstance().getMap().getUpperLayer(tilePressedEvent.getX(), tilePressedEvent.getY()))) {
                            GameManager.getInstance().getMap().setUpperLayer(tilePressedEvent.getX(), tilePressedEvent.getY(), graphicstoobject(HeldTile, tilePressedEvent.getX(), tilePressedEvent.getY()));
                            change = true;
                        }
                        editorPlaceBottomTile(tilePressedEvent);
                        break;
                    }
                    case UPPER:
                    {
                        if (HeldTile != objecttographics(GameManager.getInstance().getMap().getUpperLayer(tilePressedEvent.getX(), tilePressedEvent.getY()))) {
                            GameManager.getInstance().getMap().setUpperLayer(tilePressedEvent.getX(), tilePressedEvent.getY(), graphicstoobject(HeldTile, tilePressedEvent.getX(), tilePressedEvent.getY()));
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
    }

    private void editorPlaceBottomTile(TilePressedEvent event) {
        if (HeldTile != ENEMY && HeldTile != PLAYER && HeldTile != BOX && HeldTile != EMPTY && HeldTile != objecttographics(GameManager.getInstance().getMap().getBottomLayer(event.getX(), event.getY()))) {
            GameManager.getInstance().getMap().setBottomLayer(event.getX(), event.getY(), graphicstoobject(HeldTile, event.getX(), event.getY()));
            change = true;
        }
        else if (HeldTile == EMPTY && FLOOR != objecttographics(GameManager.getInstance().getMap().getUpperLayer(event.getX(), event.getY()))) {
            GameManager.getInstance().getMap().setBottomLayer(event.getX(), event.getY(), graphicstoobject(FLOOR, event.getX(), event.getY()));
            change = true;
        }
    }
}
