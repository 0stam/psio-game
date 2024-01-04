package editor;

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
        this.setHeldTile(WALL);
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
            if (event instanceof PalettePressedEvent)
            {
                this.setHeldTile(((PalettePressedEvent) event).getType());
            }
            if (event instanceof TilePressedEvent)
            {
                switch (((TilePressedEvent) event).getLayer())
                {
                    //TODO: un-yanderedev-ify conditions
                    case BOTH:
                    {
                        if (HeldTile != BUTTON_RELEASED && HeldTile != DOOR_CLOSED && HeldTile != FLOOR) {
                            GameManager.getInstance().getMap().setUpperLayer(((TilePressedEvent) event).getX(), ((TilePressedEvent) event).getY(), graphicstoobject(HeldTile, ((TilePressedEvent) event).getX(), ((TilePressedEvent) event).getY()));
                        }
                        if (HeldTile != ENEMY && HeldTile != PLAYER && HeldTile != BOX) {
                            GameManager.getInstance().getMap().setBottomLayer(((TilePressedEvent) event).getX(), ((TilePressedEvent) event).getY(), graphicstoobject(HeldTile, ((TilePressedEvent) event).getX(), ((TilePressedEvent) event).getY()));
                        }
                    }
                    case UPPER: {
                        GameManager.getInstance().getMap().setUpperLayer(((TilePressedEvent) event).getX(), ((TilePressedEvent) event).getY(), graphicstoobject(HeldTile, ((TilePressedEvent) event).getX(), ((TilePressedEvent) event).getY()));
                    }
                    case BOTTOM: {
                        if (HeldTile != ENEMY && HeldTile != PLAYER && HeldTile != BOX) {
                            GameManager.getInstance().getMap().setBottomLayer(((TilePressedEvent) event).getX(), ((TilePressedEvent) event).getY(), graphicstoobject(HeldTile, ((TilePressedEvent) event).getX(), ((TilePressedEvent) event).getY()));
                        }
                    }
                }
            }
            if (event instanceof LevelEvent) {
                //TODO: fill out methods
                if (event instanceof LevelLoadedEvent) {
                    //wczytaj poziom
                } else if (event instanceof LevelSavedEvent) {
                    //zapisz poziom
                }
            }
            if (event instanceof ConnectionEvent) {
                //TODO: change casting from button/door to more general statements
                if (event instanceof ConnectionCreatedEvent) {
                    ((Button) ((ConnectionCreatedEvent) event).getFrom()).addObserver((Door) ((ConnectionCreatedEvent) event).getTo());
                }
                if (event instanceof ConnectionDeletedEvent) {
                    ((Button) ((ConnectionDeletedEvent) event).getFrom()).removeObserver((Door) ((ConnectionDeletedEvent) event).getTo());
                }
            }
        }
    }
}
