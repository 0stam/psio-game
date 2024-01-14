package map;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class RawConnection implements Serializable {
	private Point sourcePos;

	private ArrayList<Point> destinationPos;

	public RawConnection () {
		sourcePos = null;
		destinationPos = new ArrayList<>();
	}

	public Point getSourcePos() {
		return sourcePos;
	}

	public void setSourcePos(Point sourcePos) {
		this.sourcePos = sourcePos;
	}

	public ArrayList<Point> getDestinationPos() {
		return destinationPos;
	}

	public void addDestination (Point destination) {
		destinationPos.add(destination);
	}
}
