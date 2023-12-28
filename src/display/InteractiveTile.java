package display;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

import static enums.Graphics.*;

public class InteractiveTile extends JPanel {
    private enums.Graphics identifierBottom = DEFAULT;
    private enums.Graphics identifierUpper = DEFAULT;
    //moim zdaniem powinnismy miec dostep do jakiegos statica z hashtablem enum.Graphics -> Image
    private Point coords;
    public InteractiveTile(enums.Graphics idBottom, enums.Graphics idUpper, Point coords)
    {
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
        //scale powinno byc w klasie ogolnej - map display
        g.drawImage(GraphicsHashtable.images.get(identifierBottom).getScaledInstance((int) (48.0 * EditorDisplay.scale), (int) (48.0 * EditorDisplay.scale), Image.SCALE_AREA_AVERAGING), 0, 0, this);
        if (identifierUpper!=null)
            g.drawImage(GraphicsHashtable.images.get(identifierUpper).getScaledInstance((int) (48.0 * EditorDisplay.scale), (int) (48.0 * EditorDisplay.scale), Image.SCALE_AREA_AVERAGING), 0, 0, this);

    }

    public class EventListener extends MouseInputAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("Kliknieto na panel o kordach: "+coords.x+" "+coords.y);
        }
    }
}
