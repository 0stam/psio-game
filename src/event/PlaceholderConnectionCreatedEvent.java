package event;

import tile.PlayerCharacter;

public class PlaceholderConnectionCreatedEvent extends EditorEvent {
	private final int x;
	private final int y;
	private final boolean isRightButton;

	public PlaceholderConnectionCreatedEvent(int x, int y, boolean isRightButton) {
		this.x = x;
		this.y = y;
		this.isRightButton = isRightButton;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isRightButton() {
		return isRightButton;
	}
}
