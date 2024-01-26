package event.editor;

import tile.Sign;

public class SignSelectedEvent extends EditorEvent{
	private final Sign sign;
	public SignSelectedEvent(Sign sign) {
		this.sign = sign;
	}

	public Sign getSign() {
		return sign;
	}
}
