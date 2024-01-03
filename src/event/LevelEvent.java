package event;

public class LevelEvent extends EditorEvent {
    // Path relative to the directory with user-created levels
    private final String path;

    public LevelEvent(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
