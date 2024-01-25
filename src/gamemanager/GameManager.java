package gamemanager;
import IO.GraphicIO;
import IO.IOManager;
import display.PopupClass;
import editor.Editor;
import enums.Direction;
import event.display.ChangeLayerEvent;
import event.editor.EditorEvent;
import event.editor.EditorSelectedEvent;
import event.game.*;
import event.InputEvent;
import event.game.ResetEvent;
import levelloader.*;
import map.Map;
import event.*;

import javax.swing.*;

public class GameManager implements EventObserver {
    private static GameManager gameManager;
    private Map map;
    private Editor editor;
    private boolean levelCompleted;
    private int currentLevel;
    private boolean endLevel = false;

    private Popup currentPopup;

    private GameManager() {

    }

    public static GameManager getInstance() {
        if (gameManager == null) {
            gameManager = new GameManager();
        }
        return gameManager;
    }

    public void startLevel(int index) {
        try {
            map = LevelLoader.loadLevel(index);
        } catch (LevelNotLoaded e) {
            System.out.println("Incorrect level selected for loading in GameManager");
            return;
        }

        currentLevel = index;
        levelCompleted = false;
        IOManager.getInstance().drawGame();
    }

    public void endLevel(){
        levelCompleted = true;
        endLevel = false;

        if (currentLevel < LevelLoader.getLevelCount() - 1) {
            startLevel(currentLevel + 1);
        } else {
            IOManager.getInstance().drawMenu();
        }
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
        if (endLevel) {
            endLevel();
        }
    }

    public void resetLevel() {
        startLevel(currentLevel);
    }

    public void onEvent(Event event) {

        //???????????????????
        if (!(event instanceof InputEvent)) {
            return;
        }

        if (event instanceof EditorEvent) {
            editor.onEvent(event);
            return;
        }

        if (event instanceof ChangeLayerEvent) {
            editor.onEvent(event);
            return;
        }

        if (event instanceof EscapeEvent) {
            editor = null;
            IOManager.getInstance().drawMenu();
            if (currentPopup != null) {
                currentPopup.hide();
                currentPopup = null;
            }
        }

        if (event instanceof LevelSelectedEvent levelSelectedEvent) {
            startLevel(levelSelectedEvent.getIndex());
            if (currentPopup != null) {
                currentPopup.hide();
                currentPopup = null;
            }
            return;
        }

        if (event instanceof EditorSelectedEvent) {
            editor = new Editor();
            IOManager.getInstance().drawEditor();
            return;
        }

        if (event instanceof MoveEvent moveEvent) {
            startTurn(moveEvent.getDirection());
            if (!levelCompleted) {
                IOManager.getInstance().drawGame();
            }
            return;
        }

        if (event instanceof PopupEvent) {
            JFrame frame = ((GraphicIO)IOManager.getInstance().getStrategy()).getWindow();
            currentPopup = new PopupFactory().getPopup(frame, ((PopupEvent)event).getPopupClass(), (int) (frame.getLocationOnScreen().x + 0.05 * frame.getWidth()), frame.getLocationOnScreen().y + frame.getHeight() / 2);
            currentPopup.show();
            return;
        }

        if (event instanceof PopupResetEvent) {
            if (currentPopup != null) {
                currentPopup.hide();
                currentPopup = null;
            }
        }

        if (event instanceof ResetEvent) {
            resetLevel();
            if (currentPopup != null) {
                currentPopup.hide();
                currentPopup = null;
            }
            return;
        }
    }

    public boolean getLevelCompleted() {
        return levelCompleted;
    }

    public Editor getEditor() {
        return editor;
    }

    public boolean getEndLevel() {
        return endLevel;
    }

    public void setEndLevel(boolean endLevel) {
        this.endLevel = endLevel;
    }
}