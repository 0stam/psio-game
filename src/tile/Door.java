package tile;

import enterablestrategy.Empty;
import enterablestrategy.Solid;
import enums.Graphics;
import event.ButtonEvent;
import event.Event;
import event.EventObserver;

public class Door extends Tile implements EventObserver {
    public Door(int x, int y) {
        super(x, y);
        close();
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof ButtonEvent buttonEvent) {
            if (buttonEvent.isPressed()) {
                open();
            } else {
                close();
            }
        }
    }

    private void close() {
        setEnterableStrategy(new Solid());
        setGraphicsID(Graphics.DOOR_CLOSED);
    }

    private void open() {
        setEnterableStrategy(new Empty());
        setGraphicsID(Graphics.DOOR_OPEN);
    }
}
