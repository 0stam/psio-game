package levelloader;

public class LevelNotSaved extends Exception {
    public LevelNotSaved(String message) {
        // TODO: implement custom exception properties if it makes sense
        super(message);
    }

    public LevelNotSaved(String message, Throwable e){
        super(message, e);
    }
}
