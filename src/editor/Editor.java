package editor;

import event.EditorEvent;
import event.Event;
import event.EventObserver;

public class Editor implements EventObserver {
    public void onEvent(Event event) {
        if (event instanceof EditorEvent editorEvent) {
            // TODO: handle events
        }
    }
}
