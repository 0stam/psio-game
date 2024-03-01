package tile;

import enterablestrategy.Empty;
import enterablestrategy.Solid;
import enums.Graphics;
import event.game.ButtonEvent;
import event.Event;
import event.EventObserver;

public class ReverseDoor extends Tile implements EventObserver {
    private int numer;

    public ReverseDoor(int x, int y) {
        super(x, y);
        numer = 0;
        open();
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof ButtonEvent) {
            ButtonEvent buttonEvent = (ButtonEvent)event;
            if (buttonEvent.isPressed()) {
                numer++;
            } else {
                numer--;
            }

            if (numer == 0) {
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
