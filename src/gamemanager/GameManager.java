package gamemanager;
import IO.ConsoleIO;
import IO.GraphicIO;
import IO.IOManager;
import display.GraphicsHashtable;
import editor.Editor;
import enums.Direction;
import map.Map;
import event.*;

import java.io.IOException;

public class GameManager implements EventObserver {
    private static GameManager gameManager;
    private Map map;
    private Editor editor;
    private boolean levelCompleted;

    private GameManager() {
        try {
            GraphicsHashtable.setupimages();
        }
        catch (IOException e)
        {
            System.out.println("Nie udalo sie wczytac grafiki : GameManager 25");
            e.printStackTrace();
        }
        // TODO: remove when proper Editor initialization is implemented
        editor = new Editor();
    }

    public static GameManager getInstance() {
        if (gameManager == null) {
            gameManager = new GameManager();
        }
        return gameManager;
    }

    public void startGame() {
        Map map = new Map(10, 10);
        levelCompleted = false;
        map.setupMap();

        this.map = map;

        //IOManager io = IOManager.getInstance(new ConsoleIO());
        IOManager io = IOManager.getInstance(new GraphicIO());

        //We can swap between editor and main game - uncomment
        //Perhaps simple switch would solve triggering draw menu, editor and game
        io.drawGame();
        //io.drawEditor();
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
        if (event instanceof InputEvent) {
            if (event instanceof GameEvent) {
                if (event instanceof MoveEvent moveEvent) {
                    startTurn(moveEvent.getDirection());
                    IOManager.getInstance().drawGame();
                } else if (event instanceof ResetEvent) {
                    startGame();
                }
            } else if (event instanceof EditorEvent) {
                editor.onEvent(event);
            } else if (event instanceof EscapeEvent) {
                // TODO: Go to a proper menu depending on current mode once implemented
            }
        }
    }

    public boolean getLevelCompleted() {
        return levelCompleted;
    }
}