package gamemanager;
import IO.ConsoleIO;
import IO.GraphicIO;
import IO.IOManager;
import display.GraphicsHashtable;
import editor.Editor;
import enums.Direction;
import levelloader.*;
import map.Map;
import event.*;

import java.io.IOException;

public class GameManager implements EventObserver {
    private static GameManager gameManager;
    private Map map;
    private Editor editor;
    private boolean levelCompleted;

    private GameManager() {
        // TODO: remove when proper Editor initialization is implemented
        editor = new Editor();
    }

    public static GameManager getInstance() {
        if (gameManager == null) {
            gameManager = new GameManager();
        }
        return gameManager;
    }

    public void startGame(){
         //Tworzenie mapy do pozniejszego zapisu
        /*Map map = new Map(10, 10);
        levelCompleted = false;

        map.setupMap();

        this.map = map;

        try {
            LevelLoader.saveLevel("test2", this.map);
        } catch (LevelNotSaved e) {
            e.printStackTrace();
        }
        System.out.println(LevelLoader.getLevelCount());*/

        //Wczytywanie zapisanej mapy
        Map map = null;
        try {
            map = LevelLoader.loadLevel(2);
        } catch (LevelNotLoaded e) {
            e.printStackTrace();
            System.exit(1);
        }
        levelCompleted=false;
        this.map=map;

        //IOManager io = IOManager.getInstance(new ConsoleIO());
        IOManager io = IOManager.getInstance(new GraphicIO());

        //We can swap between editor and main game - uncomment
        //Perhaps simple switch would solve triggering draw menu, editor and game
        io.drawGame();
        io.drawEditor();
    }


    public void endLevel(){
        levelCompleted = true;
        System.out.println("Essa zwyciężyłeś");
    }
    public void setMap(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return map;
    }

    // should have input parameters
    public void startTurn(Direction input) {
        this.map.startTurn(input);
    }

    public void onEvent(Event event) {
        if (!(event instanceof InputEvent)) {
            return;
        }

        if (event instanceof EditorEvent) {
            editor.onEvent(event);
            return;
        }

        if (event instanceof MoveEvent moveEvent) {
            startTurn(moveEvent.getDirection());
            IOManager.getInstance().drawGame();
            return;
        }

        if (event instanceof ResetEvent) {
            startGame();
            return;
        }
    }

    public boolean getLevelCompleted() {
        return levelCompleted;
    }

    public Editor getEditor() {
        return editor;
    }
}