package levelloader;

import map.Map;
import map.MapConverter;
import map.NewRawMap;
import map.RawMap;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.Arrays;

public class LevelLoader {
    public static final String userLevelsPath = "/psio-game/userlevels/";
    public static final String[] levelOrder = {
            "introduction", "first_puzzle", "creative_button", "labyrinth", "chained_puzzle", "big_level", "robots_robots",
            "conveyor", "chained_diagonal", "full_adder", "kopara", "hardest_level", "web_of_lies", "the_great_puzzle", "badziewie", "bad_apple"
    };

    public static void createUserLevelDirectory() {
        Path userPath = Paths.get(System.getProperty("user.home"), userLevelsPath);
        if (!Files.exists(userPath)) {
            new File(userPath.toString()).mkdirs();
        }
    }

    public static void saveLevel(String name, Map map) throws LevelNotSaved {
        String userHome = System.getProperty("user.home");
        String path = name;

        try {
            Path folderPath = Paths.get(path).getParent();
            Files.createDirectories(folderPath);

            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(path));
            os.writeObject(MapConverter.saveConvert(map));

        } catch(IOException e) {
            throw new LevelNotSaved("Failed to save level " + name, e);
        }
    }

    public static Map loadLevel(String name) throws LevelNotLoaded {
        String userHome = System.getProperty("user.home");
        String path = name;
        Object read = null;

        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(path));
            try {
                read = is.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                NewRawMap loadedMap = (NewRawMap)read;
                return MapConverter.loadConvert(loadedMap);
            } catch (Exception ex) {
                try {
                    RawMap loadedMap = (RawMap)read;
                    return MapConverter.oldLoadConvert(loadedMap);
                } catch (Exception exc) {
                    exc.printStackTrace();
                    return null;
                }
            }
        } catch(IOException e) {
            throw new LevelNotLoaded("Failed to load level " + name);
        }
    }

    public static String getPath(int index, boolean full) {
        String userHome = System.getProperty("user.home");
        String baseLevels = "/levels/";
        File userLevels = new File(userHome + userLevelsPath);

        if(index < levelOrder.length) {
            if (full) {
                return baseLevels + levelOrder[index];
            } else {
                return levelOrder[index];
            }
        }
        else {
            File[] ufiles = userLevels.listFiles();
            Arrays.sort(ufiles);
            if (full) {
                return ufiles[index - levelOrder.length].getAbsolutePath();
            } else {
                return ufiles[index - levelOrder.length].getName();
            }
        }
    }

    public static String getPath(int index) {
        return getPath(index, true);
    }

    public static Map loadLevel(int index) throws LevelNotLoaded {
        String path = getPath(index);
        Object read = null;

        try {
            ObjectInputStream is;
            if (index < levelOrder.length) {
                is = new ObjectInputStream(LevelLoader.class.getResourceAsStream(path));
            } else {
                is = new ObjectInputStream(new FileInputStream(path));
            }
            try {
                read = is.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                NewRawMap loadedMap = (NewRawMap)read;
                return MapConverter.loadConvert(loadedMap);
            } catch (Exception ex) {
                try {
                    RawMap loadedMap = (RawMap)read;
                    return MapConverter.oldLoadConvert(loadedMap);
                } catch (Exception exc) {
                    return null;
                }
            }
        } catch(Exception e) {
            throw new LevelNotLoaded("Failed to load level " + index);
        }
    }

    public static int getLevelCount() {
        String userHome = System.getProperty("user.home");
        File userLevels = new File(userHome + "/psio-game/userlevels");
        int a=levelOrder.length, b=0;

        if(userLevels.isDirectory()){
            File[] ufiles = userLevels.listFiles();
            b=ufiles.length;
        }

        return a+b;
    }

}
