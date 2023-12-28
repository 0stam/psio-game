package display;

import map.Map;

import javax.swing.*;
import java.awt.*;

import static display.MapDisplay.imgHeight;
import static display.MapDisplay.imgWidth;

public class EditorDisplay extends JPanel {

    private JPanel container = new JPanel();
    public static float scale = 1;

    //w sumie wywolujemy to tylko raz, troche dziwne ze nie jest to w konstruktorze display mapy
    public EditorDisplay(Map map) {
        this.setLayout(new GridLayout(map.getWidth(), map.getHeight(),0,0));
        for (int j = 0; j < map.getHeight(); j++) {
            for (int i = 0; i < map.getWidth(); i++) {
                if (map.getUpperLayer(i, j) == null)
                    this.add(new InteractiveTile(map.getBottomLayer(i, j).getGraphicsID(), null, new Point(i,j)));
                else
                    this.add(new InteractiveTile(map.getBottomLayer(i, j).getGraphicsID(), map.getUpperLayer(i, j).getGraphicsID(), new Point(i,j)));
            }
        }
        container.setBackground(Color.red);
        container.add(this);
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        float scaleX = (float) container.getWidth() / (32.0f * imgWidth);
        float scaleY = (float) container.getHeight() / (32.0f * imgHeight);

        scale = Math.min(scaleX, scaleY);
        this.setPreferredSize(new Dimension((int) (32 * (scale-0.01) * imgWidth), (int) (32 * (scale-0.01) * imgHeight)));

    }

    public JPanel getContainer() {
        return container;
    }

    public void setContainer(JPanel container) {
        this.container = container;
    }
}
