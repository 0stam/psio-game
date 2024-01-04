package editor;

import IO.IOManager;
import enterablestrategy.Empty;
import enums.Graphics;
import event.*;
import tile.*;
import gamemanager.GameManager;

import static enums.Layer.*;
import static enums.Graphics.*;


public class Editor implements EventObserver {
    private Graphics HeldTile;
    public Editor()
    {
        this.setHeldTile(EMPTY);
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
                        if (HeldTile != BUTTON_RELEASED && HeldTile != DOOR_CLOSED && HeldTile != FLOOR) {
                            GameManager.getInstance().getMap().setUpperLayer(tilePressedEvent.getX(), tilePressedEvent.getY(), graphicstoobject(HeldTile, tilePressedEvent.getX(), tilePressedEvent.getY()));
                        }
                        editorPlaceBottomTile(tilePressedEvent);
                        break;
                    }
                    case UPPER: {
                        GameManager.getInstance().getMap().setUpperLayer(tilePressedEvent.getX(), tilePressedEvent.getY(), graphicstoobject(HeldTile, tilePressedEvent.getX(), tilePressedEvent.getY()));
                        break;
                    }
                    case BOTTOM: {
                        editorPlaceBottomTile(tilePressedEvent);
                        break;
                    }
                }
                IOManager.getInstance().drawEditor();
            }
            if (event instanceof LevelEvent) {
                //TODO: fill out methods
                if (event instanceof LevelLoadedEvent) {
                    //wczytaj poziom
                } else if (event instanceof LevelSavedEvent) {
                    //zapisz poziom
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
        if (HeldTile != ENEMY && HeldTile != PLAYER && HeldTile != BOX && HeldTile != EMPTY) {
            GameManager.getInstance().getMap().setBottomLayer(event.getX(), event.getY(), graphicstoobject(HeldTile, event.getX(), event.getY()));
        }
        else if (HeldTile == EMPTY) {
            GameManager.getInstance().getMap().setBottomLayer(event.getX(), event.getY(), graphicstoobject(FLOOR, event.getX(), event.getY()));
        }
    }
}
