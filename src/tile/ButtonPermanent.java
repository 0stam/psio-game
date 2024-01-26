package tile;

import connectableinterface.Connectable;
import enterablestrategy.Empty;
import enums.Graphics;

import java.util.HashSet;
import java.util.Iterator;

import event.game.ButtonEvent;
import event.EventObserver;
import event.EventSource;
import enums.Direction;

public class ButtonPermanent extends Tile implements EventSource, Connectable {
	private HashSet<EventObserver> observers = new HashSet<>();
	private boolean isPressed = false;

	public ButtonPermanent(int x, int y) {
		super(x, y);
		this.setEnterableStrategy(new Empty());
		this.setGraphicsID(Graphics.BUTTON_PERMANENT_RELEASED);
	}

	public void addConnection (Tile tile) {
		if (tile instanceof EventObserver)
		{
			addObserver((EventObserver) tile);
		}
	}

	@Override
	public void setConnections(HashSet<Tile> list) {
		observers = new HashSet<>();

		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			this.addObserver((EventObserver)iter.next());
		}
	}

	public void removeConnection(Tile tile)
	{
		if (observers.contains((EventObserver)tile)) {
			removeObserver((EventObserver) tile);
		}
	}
	public HashSet<Tile> getConnections()
	{
		return ( (HashSet<Tile>) ( (HashSet<?>)  observers));
	}



	public void addObserver(EventObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(EventObserver observer) {
		observers.remove(observer);
	}

	public HashSet<EventObserver> getObservers() {
		return observers;
	}

	public boolean hasObserver(EventObserver eventObserver) {
		return observers.contains(eventObserver);
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
		this.setGraphicsID(Graphics.BUTTON_PERMANENT_PRESSED);
		notifyObservers();
	}

	@Override
	public void onExited(Direction direction, Tile tile) {
		super.onExited(direction, tile);
	}

}