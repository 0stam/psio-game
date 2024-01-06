package levelloader;

public class LevelNotLoaded extends Exception {
    public LevelNotLoaded(String message) {
        // TODO: implement custom exception properties if it makes sense
        super(message);
    }

    public LevelNotLoaded(String message, Throwable e){
        super(message, e);
    }
}
