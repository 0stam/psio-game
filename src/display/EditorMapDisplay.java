package display;

import IO.IOManager;
import enums.EditableTile;
import enums.EditorGraphics;
import gamemanager.GameManager;
import tile.ButtonPermanent;
import tile.ChasingEnemy;
import tile.SmartEnemy;

import javax.swing.*;
import java.awt.*;

import static enums.Arrow.EMPTY;

public class EditorMapDisplay extends JPanel {
    private JPanel container = new JPanel();
    //skala chyba powinna tu zostac bo oblicza ja EditorMapDisplay
    public static float scale = 1;
    //lepiej zeby to byla tablica dla refreshTile i refreshMap, tablice 1D bede zamienial na
    //array listy bo sa szybsze i bardziej elastyczne
    private InteractiveTile[][] mapTiles;

    public EditorMapDisplay() {
        container.setLayout(new GridLayout(GameManager.getInstance().getMap().getHeight(), GameManager.getInstance().getMap().getWidth(),0,0));
        this.mapTiles = new InteractiveTile[GameManager.getInstance().getMap().getWidth()][GameManager.getInstance().getMap().getHeight()];

        for (int j = 0; j < GameManager.getInstance().getMap().getHeight(); j++) {
            for (int i = 0; i < GameManager.getInstance().getMap().getWidth(); i++) {
                InteractiveTile toAdd = new InteractiveTile(new Point(i, j));
                container.add(toAdd);
                mapTiles[i][j] = toAdd;
            }
        }

        this.setBackground(Color.WHITE);
        this.refresh();
        this.add(container);
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        //jesli damy tu kod do modyfikowania mapy to dzieja sie naprawde dziwne rzeczy
        g.setColor(Color.white);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        container.setPreferredSize(new Dimension((int) (32 * scale * GameManager.getInstance().getMap().getWidth()), (int) (32 * (scale-0.01) * GameManager.getInstance().getMap().getHeight())));
    }
    public void refresh()
    {
        EditorInputHandler inputHandler = (EditorInputHandler) IOManager.getInstance().getInputHandler();
        switch (inputHandler.getLayer()) {
            case BOTH: {
                for (int j = 0; j < GameManager.getInstance().getMap().getHeight(); j++) {
                    for (int i = 0; i < GameManager.getInstance().getMap().getWidth(); i++) {
                        enums.Graphics newBottom = ((GameManager.getInstance().getMap().getBottomLayer(i, j) == null) ? enums.Graphics.DEFAULT : GameManager.getInstance().getMap().getBottomLayer(i, j).getGraphicsID());
                        enums.Graphics newUpper = ((GameManager.getInstance().getMap().getUpperLayer(i, j) == null) ? enums.Graphics.EMPTY : GameManager.getInstance().getMap().getUpperLayer(i, j).getGraphicsID());

                        if (inputHandler.getConnectingTile() != null) {
                            if (GameManager.getInstance().getMap().getBottomLayer(i, j) == inputHandler.getConnectingTile()) {
                                newBottom = ((inputHandler.getConnectingTile() instanceof ChasingEnemy) ? enums.Graphics.ENEMY_SELECTED : ((inputHandler.getConnectingTile() instanceof ButtonPermanent) ? enums.Graphics.BUTTON_PERMANENT_SELECTED : enums.Graphics.BUTTON_SELECTED));
                            }

                            if (GameManager.getInstance().getMap().getUpperLayer(i, j) == inputHandler.getConnectingTile()) {
                                newUpper = ((inputHandler.getConnectingTile() instanceof ChasingEnemy) ? enums.Graphics.ENEMY_SELECTED : ((inputHandler.getConnectingTile() instanceof ButtonPermanent) ? enums.Graphics.BUTTON_PERMANENT_SELECTED : enums.Graphics.BUTTON_SELECTED));
                            }
                        }

                        if (inputHandler.getCurrentSign() != null) {
                            if (GameManager.getInstance().getMap().getBottomLayer(i, j) == inputHandler.getCurrentSign()) {
                                newBottom = enums.Graphics.SIGN_SELECTED;
                            }

                            if (GameManager.getInstance().getMap().getUpperLayer(i, j) == inputHandler.getCurrentSign()) {
                                newUpper = enums.Graphics.SIGN_SELECTED;
                            }
                        }

                        mapTiles[i][j].updateGraphics(newBottom, null, newUpper);
                    }
                }
                break;
            }
            case UPPER: {
                for (int j = 0; j < GameManager.getInstance().getMap().getHeight(); j++) {
                    for (int i = 0; i < GameManager.getInstance().getMap().getWidth(); i++) {
                        mapTiles[i][j].updateGraphics(enums.Graphics.EMPTY, null, GameManager.getInstance().getMap().getUpperLayer(i,j) == null ? enums.Graphics.EMPTY : GameManager.getInstance().getMap().getUpperLayer(i, j).getGraphicsID());
                    }
                }
                break;
            }
            case BOTTOM:
            {
                for (int j = 0; j < GameManager.getInstance().getMap().getHeight(); j++) {
                    for (int i = 0; i < GameManager.getInstance().getMap().getWidth(); i++) {
                        mapTiles[i][j].updateGraphics(GameManager.getInstance().getMap().getBottomLayer(i,j) == null ? enums.Graphics.EMPTY : GameManager.getInstance().getMap().getBottomLayer(i,j).getGraphicsID(), null, null);
                    }
                }
                break;
            }
            case PATH: {
                EditorGraphics[][] path = ((EditorInputHandler) IOManager.getInstance().getInputHandler()).getCurrentPath();
                SmartEnemy currentEnemy = ((EditorInputHandler) IOManager.getInstance().getInputHandler()).getCurrentEnemy();
                if (path != null && currentEnemy != null)
                {
                    for (int j = 0; j < GameManager.getInstance().getMap().getHeight(); j++) {
                        for (int i = 0; i < GameManager.getInstance().getMap().getWidth(); i++) {
                            switch (path[i][j]) {
                                //jednak da sie laczyc ale nie tak jak zwykle nie wiem w ogole co to jest XDD to nawet nie jest skladnia laczenia ktora byla wczesniej z przecinkami
                                case null:
                                case EMPTY: {
                                    mapTiles[i][j].updateGraphics(GameManager.getInstance().getMap().getBottomLayer(i, j).getGraphicsID(), enums.Graphics.EMPTY, GameManager.getInstance().getMap().getUpperLayer(i, j) == null ? null : (GameManager.getInstance().getMap().getUpperLayer(i, j) == currentEnemy ? enums.Graphics.SMART_SELECTED : GameManager.getInstance().getMap().getUpperLayer(i, j).getGraphicsID()));
                                    break;
                                }
                                default: {
                                    mapTiles[i][j].updateGraphics(GameManager.getInstance().getMap().getBottomLayer(i, j).getGraphicsID(), path[i][j].getGraphics(), GameManager.getInstance().getMap().getUpperLayer(i, j) == null ? null : (GameManager.getInstance().getMap().getUpperLayer(i, j) == currentEnemy ? enums.Graphics.SMART_SELECTED : GameManager.getInstance().getMap().getUpperLayer(i, j).getGraphicsID()));
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            }
        }

        this.repaint();
    }
    /*
    Komentarz nt tej metody w Editor
    public void repaintTile(enums.Graphics bottomLayer, enums.Graphics upperLayer, int x, int y)
    {
        mapTiles[x][y].updateGraphics(bottomLayer, upperLayer);
        mapTiles[x][y].repaint();
    }
    */
    public JPanel getContainer() {
        return container;
    }

    public void setContainer(JPanel container) {
        this.container = container;
    }

    public static float getScale() {
        return scale;
    }

    public static void setScale(float scale) {
        EditorMapDisplay.scale = scale;
    }
}
