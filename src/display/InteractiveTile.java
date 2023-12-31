package display;

import event.EditorEvent;
import event.TilePressedEvent;
import gamemanager.GameManager;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

import static enums.Graphics.*;

public class InteractiveTile extends JPanel {
    private enums.Graphics identifierBottom = DEFAULT;
    private enums.Graphics identifierUpper = DEFAULT;
    private Point coords;
    public InteractiveTile(enums.Graphics idBottom, enums.Graphics idUpper, Point coords)
    {
        if (idBottom != null)
            this.identifierBottom = idBottom;
        this.identifierUpper = idUpper;
        this.coords = coords;
        this.setBorder(BorderFactory.createEmptyBorder());
        this.addMouseListener(new EventListener());
    }

    @Override
    public void paintComponent(java.awt.Graphics g)
    {
        this.setPreferredSize(new Dimension((int)(32.0 * EditorDisplay.scale ), (int) (32.0 * EditorDisplay.scale)));
        this.revalidate();
        g.drawImage(GraphicsHashtable.getInstance().getImage(identifierBottom).getScaledInstance((int) (48.0 * EditorDisplay.scale), (int) (48.0 * EditorDisplay.scale), Image.SCALE_AREA_AVERAGING), 0, 0, this);
        if (identifierUpper!=null)
            g.drawImage(GraphicsHashtable.getInstance().getImage(identifierUpper).getScaledInstance((int) (48.0 * EditorDisplay.scale), (int) (48.0 * EditorDisplay.scale), Image.SCALE_AREA_AVERAGING), 0, 0, this);

    }

    public class EventListener extends MouseInputAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("Kliknieto na panel o kordach: "+coords.x+" "+coords.y + " ; przygotowuje event");
            TilePressedEvent editorEvent = new TilePressedEvent(coords.x, coords.y, EditorDisplay.getLayer());
            GameManager.getInstance().onEvent(editorEvent);
        }
    }
}
