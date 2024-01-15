package event.editor;

import enums.Arrow;

public class ArrowModifiedEvent extends EditorEvent {
    private final Arrow arrow;
    private final int x;
    private final int y;
    private final boolean remove;

    public ArrowModifiedEvent(Arrow arrow, int x, int y, boolean remove) {
        this.arrow = arrow;
        this.x = x;
        this.y = y;
        this.remove = remove;
    }

    public Arrow getArrow() {
        return arrow;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isRemove() {
        return remove;
    }
}
