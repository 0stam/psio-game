package tile;

import enterablestrategy.Empty;
import enterablestrategy.Solid;
import enums.Graphics;
import observers.ButtonObserver;

public class Door extends Tile implements ButtonObserver {
    public Door(int x, int y) {
        super(x, y);
        close();
    }

    @Override
    public void onButtonEvent(boolean pressed) {
        if (pressed) {
            open();
        } else {
            close();
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
