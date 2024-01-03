package display;

import enums.Layer;
import gamemanager.GameManager;

import javax.swing.*;
import java.awt.*;

import static display.MapDisplay.imgHeight;
import static display.MapDisplay.imgWidth;

public class EditorMapDisplay extends JPanel {

    private JPanel container = new JPanel();
    private static Layer layer = Layer.BOTTOM;
    public static float scale = 1;
    private InteractiveTile[][] mapTiles;

    public EditorMapDisplay() {

        container.setLayout(new GridLayout(GameManager.getInstance().getMap().getWidth(), GameManager.getInstance().getMap().getHeight(),0,0));
        this.mapTiles = new InteractiveTile[GameManager.getInstance().getMap().getWidth()][GameManager.getInstance().getMap().getHeight()];

        for (int j = 0; j < GameManager.getInstance().getMap().getHeight(); j++) {
            for (int i = 0; i < GameManager.getInstance().getMap().getWidth(); i++) {
                InteractiveTile toAdd = new InteractiveTile(new Point(i, j));
                container.add(toAdd);
                mapTiles[i][j] = toAdd;
            }
        }

        this.setBackground(Color.RED);
        this.refreshMap();
        this.add(container);
    }
    @Override
    protected void paintComponent(java.awt.Graphics g) {
        //jesli damy tu kod do modyfikowania mapy to dzieja sie naprawde dziwne rzeczy
        g.setColor(Color.white);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        float scaleX = (float) this.getWidth() / (32.0f * GameManager.getInstance().getMap().getWidth());
        float scaleY = (float) this.getHeight() / (32.0f * GameManager.getInstance().getMap().getHeight());

        scale = Math.min(scaleX, scaleY);
        container.setPreferredSize(new Dimension((int) (32 * scale * GameManager.getInstance().getMap().getWidth()), (int) (32 * (scale-0.01) * GameManager.getInstance().getMap().getHeight())));
    }
    public void refreshMap()
    {
        switch (layer) {
            case BOTH: {
                for (int j = 0; j < GameManager.getInstance().getMap().getHeight(); j++) {
                    for (int i = 0; i < GameManager.getInstance().getMap().getWidth(); i++) {
                        if (GameManager.getInstance().getMap().getUpperLayer(i, j) == null)
                            mapTiles[i][j].updateGraphics(GameManager.getInstance().getMap().getBottomLayer(i, j).getGraphicsID(), null);
                        else
                            mapTiles[i][j].updateGraphics(GameManager.getInstance().getMap().getBottomLayer(i, j).getGraphicsID(), GameManager.getInstance().getMap().getUpperLayer(i, j).getGraphicsID());
                    }
                }
                break;
            }
            case UPPER: {
                for (int j = 0; j < GameManager.getInstance().getMap().getHeight(); j++) {
                    for (int i = 0; i < GameManager.getInstance().getMap().getWidth(); i++) {
                        if (GameManager.getInstance().getMap().getUpperLayer(i,j) == null)
                            mapTiles[i][j].updateGraphics(enums.Graphics.EMPTY, enums.Graphics.EMPTY);
                        else
                            mapTiles[i][j].updateGraphics(enums.Graphics.EMPTY, GameManager.getInstance().getMap().getUpperLayer(i, j).getGraphicsID());
                    }
                }
                break;
            }
            case BOTTOM:
            {
                for (int j = 0; j < GameManager.getInstance().getMap().getHeight(); j++) {
                    for (int i = 0; i < GameManager.getInstance().getMap().getWidth(); i++) {
                        if (GameManager.getInstance().getMap().getBottomLayer(i,j) == null)
                            mapTiles[i][j].updateGraphics(enums.Graphics.EMPTY, null);
                        else
                            mapTiles[i][j].updateGraphics(GameManager.getInstance().getMap().getBottomLayer(i, j).getGraphicsID(), null);
                    }
                }
                break;
            }
        }


        this.repaint();
    }

    public JPanel getContainer() {
        return container;
    }

    public void setContainer(JPanel container) {
        this.container = container;
    }

    public static Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

}
