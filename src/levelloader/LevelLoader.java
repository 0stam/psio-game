package levelloader;

import map.Map;
import tile.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.Arrays;

public class LevelLoader {
    public static void saveLevel(String name, Map map) throws LevelNotSaved {
        String userHome = System.getProperty("user.home");
        String path = userHome + "/psio-game/userlevels/" + name;

        try{
            Path folderPath = Paths.get(path).getParent();
            Files.createDirectories(folderPath);

            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(path));
            os.writeObject(map);

        } catch(IOException e){
            throw new LevelNotSaved("Failed to save level " + name, e);
        }
    }

    public static Map loadLevel(String name) throws LevelNotLoaded {
        String userHome = System.getProperty("user.home");
        String path = userHome + "/psio-game/userlevels/" + name;

        try{
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(path));
            Map deserMap = (Map)is.readObject();
            Map loadedMap = new Map(deserMap.getWidth(), deserMap.getHeight());

            for (int i = 0; i < loadedMap.getWidth(); i++) {
                for (int j = 0; j < loadedMap.getHeight(); j++) {
                    Tile bottomTile = deserMap.getBottomLayer(i, j);
                    Tile upperTile = deserMap.getUpperLayer(i, j);

                    loadedMap.setBottomLayer(i, j, bottomTile);
                    loadedMap.setUpperLayer(i, j, upperTile);
                    if (upperTile instanceof ActionTile) {
                        loadedMap.addCurrentActionTile((ActionTile)upperTile);
                    }

                }
            }
            return loadedMap;

        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        throw new LevelNotLoaded("Failed to load level " + name);
    }

    public static Map loadLevel(int index) throws LevelNotLoaded {
        String userHome = System.getProperty("user.home");
        String path;
        File baseLevels = new File("src/levels");
        File userLevels = new File(userHome + "/psio-game/userlevels");

        File[] bfiles = baseLevels.listFiles();
        Arrays.sort(bfiles);
        if(index <= bfiles.length){
            path = bfiles[index-1].getAbsolutePath();
        }
        else{
            File[] ufiles = userLevels.listFiles();
            Arrays.sort(ufiles);
            path = ufiles[index-bfiles.length-1].getAbsolutePath();
        }

        try{
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(path));
            Map deserMap = (Map)is.readObject();
            Map loadedMap = new Map(deserMap.getWidth(), deserMap.getHeight());

            for (int i = 0; i < loadedMap.getWidth(); i++) {
                for (int j = 0; j < loadedMap.getHeight(); j++) {
                    Tile bottomTile = deserMap.getBottomLayer(i, j);
                    Tile upperTile = deserMap.getUpperLayer(i, j);

                    loadedMap.setBottomLayer(i, j, bottomTile);
                    loadedMap.setUpperLayer(i, j, upperTile);
                    if (upperTile instanceof ActionTile) {
                        loadedMap.addCurrentActionTile((ActionTile)upperTile);
                    }

                }
            }
            return loadedMap;

        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        throw new LevelNotLoaded("Failed to load level " + index);
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
