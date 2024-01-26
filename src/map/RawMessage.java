package map;

import java.awt.*;
import java.io.Serializable;

public class RawMessage implements Serializable {
	private Point signPos;

	private String message;

	RawMessage() {
		signPos = null;
		message = "";
	}
	RawMessage(Point signPos, String message)
	{
		this.signPos = signPos;
		this.message = message;
	}

	public Point getSignPos() {
		return signPos;
	}

	public void setSignPos(Point signPos) {
		this.signPos = signPos;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
