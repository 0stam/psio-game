package display;

import enums.Layer;
import gamemanager.GameManager;

import javax.swing.*;
import java.awt.*;

import static display.MapDisplay.imgHeight;
import static display.MapDisplay.imgWidth;

public class EditorMapDisplay extends JPanel {

    private JPanel container = new JPanel();
    private static Layer layer = Layer.UPPER;
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
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        float scaleX = (float) this.getWidth() / (32.0f * imgWidth);
        float scaleY = (float) this.getHeight() / (32.0f * imgHeight);

        scale = Math.min(scaleX, scaleY);
        container.setPreferredSize(new Dimension((int) (32 * scale * imgWidth), (int) (32 * (scale-0.01) * imgHeight)));
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
                //proponuje by zrobic jakis kafelek ktory by dzielil nam plansze na kwadraty
                //ale byl inny od podlogi i byl bardziej kafelkiem uzytkowym - moze to byc
                //default ale ten obecny jest rzucajacy sie w oczy - niech bedzie jakis widoczny ale nie az tak
                //wtedy bysmy go dali na dolna warstwe
                for (int j = 0; j < GameManager.getInstance().getMap().getHeight(); j++) {
                    for (int i = 0; i < GameManager.getInstance().getMap().getWidth(); i++) {
                        if (GameManager.getInstance().getMap().getUpperLayer(i,j) == null)
                            mapTiles[i][j].updateGraphics(enums.Graphics.DEFAULT, enums.Graphics.DEFAULT);
                        else
                            mapTiles[i][j].updateGraphics(enums.Graphics.DEFAULT, GameManager.getInstance().getMap().getUpperLayer(i, j).getGraphicsID());
                    }
                }
                break;
            }
            case BOTTOM:
            {
                for (int j = 0; j < GameManager.getInstance().getMap().getHeight(); j++) {
                    for (int i = 0; i < GameManager.getInstance().getMap().getWidth(); i++) {
                        if (GameManager.getInstance().getMap().getBottomLayer(i,j) == null)
                            mapTiles[i][j].updateGraphics(enums.Graphics.DEFAULT, null);
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
