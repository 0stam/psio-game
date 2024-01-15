package IO;

import event.Event;
import event.EventObserver;

public interface IOStrategy {
	void drawGame ();
	void drawEditor ();
	void drawMenu();
	EventObserver getInputHandler();
}
