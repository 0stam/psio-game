package tile;

import enterablestrategy.Empty;
import enums.Graphics;
import java.util.ArrayList;
import java.util.List;

import event.ButtonEvent;
import event.EventObserver;
import event.EventSource;
import enums.Direction;

public class Button extends Tile implements EventSource {
    private final List<EventObserver> observers = new ArrayList<>();
    private boolean isPressed = false;

    public Button(int x, int y) {
        super(x, y);
        this.setEnterableStrategy(new Empty());
        this.setGraphicsID(Graphics.BUTTON_RELEASED);
    }

    public void addObserver(EventObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(EventObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (EventObserver observer : observers) {
            observer.onEvent(new ButtonEvent(isPressed));
        }
    }

    @Override
    public void onEntered(Direction direction, Tile tile) {
        super.onEntered(direction, tile);

        isPressed = true;
        this.setGraphicsID(Graphics.BUTTON_PRESSED);
        notifyObservers();
    }

    @Override
    public void onExited(Direction direction, Tile tile) {
        super.onExited(direction, tile);

        isPressed = false;
        this.setGraphicsID(Graphics.BUTTON_RELEASED);
        notifyObservers();
    }

}