package levelloader;

import map.Map;

public class LevelLoader {
    public void saveLevel(String path, Map map) throws LevelNotSaved {
        // TODO: implement level saving
    }

    public Map loadLevel(String path) throws LevelNotLoaded {
        // TODO: implement loading levels from path
        return new Map(1, 1);
    }

    public Map loadLevel(int index) throws LevelNotLoaded {
        // TODO: get level path from the index and load the level
        return new Map(1, 1);
    }
}
