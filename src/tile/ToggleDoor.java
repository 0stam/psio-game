package tile;

import enterablestrategy.Empty;
import enterablestrategy.Solid;
import enums.Graphics;
import event.game.ButtonEvent;
import event.Event;
import event.EventObserver;

public class ToggleDoor extends Tile implements EventObserver {
	private int numer;

	public ToggleDoor(int x, int y) {
		super(x, y);
		numer = 0;
		close();
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof ButtonEvent buttonEvent) {
			if (buttonEvent.isPressed()) {
				numer = 1 - numer;
			}

			if (numer == 0) {
				close();
			} else {
				open();
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
