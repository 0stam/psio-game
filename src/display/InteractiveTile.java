package display;

import IO.IOManager;
import enums.EditorMode;
import event.display.InteractiveTilePressedEvent;
import event.display.ModeActiveChangedEvent;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
//import enums.EditorModes;

import static enums.Graphics.*;

public class InteractiveTile extends JPanel {
    private enums.Graphics identifierBottom = DEFAULT;
    private enums.Graphics identifierMiddle = DEFAULT;
    private enums.Graphics identifierUpper = DEFAULT;
    private Point coords;
    public InteractiveTile(enums.Graphics idBottom, enums.Graphics idMiddle,enums.Graphics idUpper, Point coords) {
        if (idBottom != null)
            this.identifierBottom = idBottom;
        this.identifierUpper = idUpper;
        this.identifierMiddle = idMiddle;
        this.coords = coords;
        this.setBorder(BorderFactory.createEmptyBorder());
        this.addMouseListener(new EventListener());
    }

    public InteractiveTile(Point coords) {
        this.coords = coords;
        this.setBorder(BorderFactory.createEmptyBorder());
        this.addMouseListener(new EventListener());
    }

    @Override
    public void paintComponent(java.awt.Graphics g) {
        this.setPreferredSize(new Dimension((int)(32.0 * EditorMapDisplay.scale ), (int) (32.0 * EditorMapDisplay.scale)));
        this.revalidate();
        g.drawImage(GraphicsHashtable.getInstance().getImage(identifierBottom).getScaledInstance((int) (32.0 * EditorMapDisplay.scale), (int) (32.0 * EditorMapDisplay.scale), Image.SCALE_AREA_AVERAGING), 0, 0, this);
        if (identifierMiddle!=null)
            g.drawImage(GraphicsHashtable.getInstance().getImage(identifierMiddle).getScaledInstance((int) (32.0 * EditorMapDisplay.scale), (int) (32.0 * EditorMapDisplay.scale), Image.SCALE_AREA_AVERAGING), 0, 0, this);
        if (identifierUpper!=null)
            g.drawImage(GraphicsHashtable.getInstance().getImage(identifierUpper).getScaledInstance((int) (32.0 * EditorMapDisplay.scale), (int) (32.0 * EditorMapDisplay.scale), Image.SCALE_AREA_AVERAGING), 0, 0, this);

    }

    public class EventListener extends MouseInputAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            EditorInputHandler inputHandler = (EditorInputHandler) IOManager.getInstance().getInputHandler();
            inputHandler.onEvent(new ModeActiveChangedEvent(true));
            sendTilePressedEvent(e);
        }
        @Override
        public void mouseEntered(MouseEvent e) {
            EditorInputHandler inputHandler = (EditorInputHandler) IOManager.getInstance().getInputHandler();
            if (inputHandler.getMode()!=EditorMode.PATHEDIT)
                sendTilePressedEvent (e);
        }
        @Override
        public void mouseReleased(MouseEvent e)
        {
            EditorInputHandler inputHandler = (EditorInputHandler) IOManager.getInstance().getInputHandler();
            inputHandler.onEvent(new ModeActiveChangedEvent(false));
        }
        public void sendTilePressedEvent (MouseEvent e) {
            EditorInputHandler inputHandler = (EditorInputHandler) IOManager.getInstance().getInputHandler();
            InteractiveTilePressedEvent editorEvent = new InteractiveTilePressedEvent(coords.x, coords.y, SwingUtilities.isRightMouseButton(e));
            inputHandler.onEvent(editorEvent);

//            switch (GameManager.getInstance().getEditor().getMode())
//            {
//                case ADD, CONNECT:
//                {
//                    editorEvent = new TilePressedEvent(coords.x, coords.y, GameManager.getInstance().getEditor().getLayer(), SwingUtilities.isRightMouseButton(e));
//                    break;
//                }
//                case SELECT:
//                {
//                    editorEvent = new TilePressedEvent(coords.x, coords.y, Layer.PATH, SwingUtilities.isRightMouseButton(e));
//                    break;
//                }
//                case PREPATHEDIT: {
//                    if (GameManager.getInstance().getMap().getUpperLayer((int)coords.getX(), (int)coords.getY()) instanceof SmartEnemy enemy) {
//                        GameManager.getInstance().onEvent(new EnemySelectedEvent(enemy));
//                    }
//                    break;
//                }
//            }
//            if (editorEvent != null)
//                GameManager.getInstance().onEvent(editorEvent);
        }
    }
    public void updateGraphics(enums.Graphics idBottom, enums.Graphics idMiddle,enums.Graphics idUpper) {
        this.identifierBottom = idBottom;
        this.identifierUpper = idUpper;
        this.identifierMiddle = idMiddle;
    }
}
