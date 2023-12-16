package tile;

import enterablestrategy.Dummy;
import enums.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Pressable extends Tile {
    private final List<ButtonObserver> observers = new ArrayList<>();
    private boolean isPressed = false;

    public Button(int x, int y) {
        super(x, y);
        this.setEnterableStrategy(new Dummy());
        this.setGraphicsID(Graphics.BUTTON_RELEASED);
    }

    public void addObserver(ButtonObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ButtonObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        ButtonEvent event = new ButtonEvent(isPressed);
        for (ButtonObserver observer : observers) {
            observer.onButtonEvent(event);
        }
    }

    @Override
    public void onEntered(Direction direction, Tile tile, ActionTile actionTile) {
        isPressed = true;
        this.setGraphicsID(Graphics.BUTTON_PRESSED);
        notifyObservers();
    }

    @Override
    public void onExited(Direction direction, Tile tile) {
        isPressed = false;
        this.setGraphicsID(Graphics.BUTTON_RELEASED);
        notifyObservers();
    }
}
