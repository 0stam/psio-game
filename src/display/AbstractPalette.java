package display;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class AbstractPalette extends JPanel {
    protected ArrayList<ImageButton> buttons;
    protected ImageButton selected;
    protected float scale = 1;
    @Override
    protected void paintComponent(java.awt.Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }
    public void selectOne(ImageButton selected) {
        this.selected.setSelected(false);

        selected.setSelected(true);
        this.selected = selected;
    }
}
