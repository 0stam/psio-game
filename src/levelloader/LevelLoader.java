package levelloader;

import map.Map;
import map.MapConverter;
import map.RawMap;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.Arrays;

public class LevelLoader {
    public static final String userLevelsPath = "/psio-game/userlevels/";

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

        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(path));
            RawMap loadedMap = (RawMap)is.readObject();
            return MapConverter.oldLoadConvert(loadedMap);
        } catch(IOException | ClassNotFoundException e) {
            throw new LevelNotLoaded("Failed to load level " + name);
        }
    }

    public static Map loadLevel(int index) throws LevelNotLoaded {
        String userHome = System.getProperty("user.home");
        String path;
        File baseLevels = new File("src/levels");
        File userLevels = new File(userHome + userLevelsPath);

        File[] bfiles = baseLevels.listFiles();
        Arrays.sort(bfiles);
        if(index < bfiles.length) {
            path = bfiles[index].getAbsolutePath();
        }
        else {
            File[] ufiles = userLevels.listFiles();
            Arrays.sort(ufiles);
            path = ufiles[index-bfiles.length].getAbsolutePath();
        }

        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(path));
            RawMap loadedMap = (RawMap)is.readObject();
            return MapConverter.oldLoadConvert(loadedMap);
        } catch(IOException | ClassNotFoundException e) {
            throw new LevelNotLoaded("Failed to load level " + index);
        }
    }

    public static int getLevelCount(){
        String userHome = System.getProperty("user.home");
        File baseLevels = new File("src/levels");
        File userLevels = new File(userHome + "/psio-game/userlevels");
        int a=0, b=0;

        if(baseLevels.isDirectory()){
            File[] bfiles = baseLevels.listFiles();
            a = bfiles.length;
        }
        if(userLevels.isDirectory()){
            File[] ufiles = userLevels.listFiles();
            b=ufiles.length;
        }

        return a+b;
    }

}
