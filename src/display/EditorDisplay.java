package display;

import enums.Layer;
import map.Map;

import javax.swing.*;
import java.awt.*;

import static display.MapDisplay.imgHeight;
import static display.MapDisplay.imgWidth;

//moze editor display tez powinien byc singletonem
public class EditorDisplay extends JPanel {

    private JPanel container = new JPanel();
    private Map mapObject;
    private static Layer layer = Layer.BOTTOM;
    public static float scale = 1;

    public EditorDisplay(Map map) {
        this.mapObject = map;
        refreshMap();

        this.setLayout(new GridLayout(map.getWidth(), map.getHeight(),0,0));
        container.setBackground(Color.red);
        container.add(this);
    }
    @Override
    protected void paintComponent(java.awt.Graphics g) {
        //jesli damy tu kod do modyfikowania mapy to dzieja sie naprawde dziwne rzeczy
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        float scaleX = (float) container.getWidth() / (32.0f * imgWidth);
        float scaleY = (float) container.getHeight() / (32.0f * imgHeight);

        scale = Math.min(scaleX, scaleY);
        this.setPreferredSize(new Dimension((int) (32 * (scale-0.01) * imgWidth), (int) (32 * (scale-0.01) * imgHeight)));
    }
    public void refreshMap()
    {
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
                //proponuje by zrobic jakis kafelek ktory by dzielil nam plansze na kwadraty
                //ale byl inny od podlogi i byl bardziej kafelkiem uzytkowym - moze to byc
                //default ale ten obecny jest rzucajacy sie w oczy - niech bedzie jakis widoczny ale nie az tak
                //wtedy bysmy go dali na dolna warstwe
                for (int j = 0; j < mapObject.getHeight(); j++) {
                    for (int i = 0; i < mapObject.getWidth(); i++) {
                        if (mapObject.getUpperLayer(i,j) == null)
                            this.add(new InteractiveTile(enums.Graphics.DEFAULT, enums.Graphics.DEFAULT, new Point(i, j)));
                        else
                            this.add(new InteractiveTile(null, mapObject.getUpperLayer(i, j).getGraphicsID(), new Point(i, j)));
                    }
                }
                break;
            }
            case BOTTOM:
            {
                for (int j = 0; j < mapObject.getHeight(); j++) {
                    for (int i = 0; i < mapObject.getWidth(); i++) {
                        if (mapObject.getBottomLayer(i,j) == null)
                            this.add(new InteractiveTile(enums.Graphics.DEFAULT, null, new Point(i, j)));
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
