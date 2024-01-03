package display;

import enums.Layer;
import map.Map;

import javax.swing.*;
import java.awt.*;

import static display.MapDisplay.imgHeight;
import static display.MapDisplay.imgWidth;

//moze editor display tez powinien byc singletonem
public class EditorMapDisplay extends JPanel {

    private JPanel container = new JPanel();
    private Map mapObject;
    private static Layer layer = Layer.UPPER;
    public static float scale = 1;

    public EditorMapDisplay(Map map) {
        this.mapObject = map;
        refreshMap();

        this.setLayout(new GridLayout(map.getWidth(), map.getHeight(),0,0));
        container.setBackground(Color.white);
        container.add(this);
    }
    @Override
    protected void paintComponent(java.awt.Graphics g) {
        //jesli damy tu kod do modyfikowania mapy to dzieja sie naprawde dziwne rzeczy
        g.setColor(Color.white);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        float scaleX = (float) container.getWidth() / (32.0f * mapObject.getWidth());
        float scaleY = (float) container.getHeight() / (32.0f * mapObject.getHeight());

        scale = Math.min(scaleX, scaleY);

        Component[] tiles = this.getComponents();

        for (int i = 0;i < tiles.length;i++) {
            ((InteractiveTile)tiles[i]).setScale(scale);
        }

        this.setPreferredSize(new Dimension((int) (32 * (scale-0.01) * mapObject.getWidth()), (int) (32 * (scale-0.01) * mapObject.getHeight())));
    }

    public void refreshMap () {
        switch (layer) {
            case BOTH: {
                for (int j = 0; j < mapObject.getHeight(); j++) {
                    for (int i = 0; i < mapObject.getWidth(); i++) {
                        if (mapObject.getUpperLayer(i, j) == null)
                            this.add(new InteractiveTile(mapObject.getBottomLayer(i, j).getGraphicsID(), null, new Point(i, j)));
                        else
                            this.add(new InteractiveTile(mapObject.getBottomLayer(i, j).getGraphicsID(), mapObject.getUpperLayer(i, j).getGraphicsID(), new Point(i, j)));
                    }
                }
                break;
            }
            case UPPER: {
                for (int j = 0; j < mapObject.getHeight(); j++) {
                    for (int i = 0; i < mapObject.getWidth(); i++) {
                        if (mapObject.getUpperLayer(i,j) == null)
                            this.add(new InteractiveTile(enums.Graphics.EMPTY, enums.Graphics.EMPTY, new Point(i, j)));
                        else
                            this.add(new InteractiveTile(enums.Graphics.EMPTY, mapObject.getUpperLayer(i, j).getGraphicsID(), new Point(i, j)));
                    }
                }
                break;
            }
            case BOTTOM:
            {
                for (int j = 0; j < mapObject.getHeight(); j++) {
                    for (int i = 0; i < mapObject.getWidth(); i++) {
                        if (mapObject.getBottomLayer(i,j) == null)
                            this.add(new InteractiveTile(enums.Graphics.EMPTY, null, new Point(i, j)));
                        else
                            this.add(new InteractiveTile(mapObject.getBottomLayer(i, j).getGraphicsID(), null, new Point(i, j)));
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

    public Map getMapObject() {
        return mapObject;
    }

    public void setMapObject(Map mapObject) {
        this.mapObject = mapObject;
    }

    public static Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

}
