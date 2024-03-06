package display;

import enums.Graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.Calendar;
import java.util.Hashtable;

import static enums.Graphics.*;

public class GraphicsHashtable {
    private static GraphicsHashtable graphicsHashtable = new GraphicsHashtable();
    private Hashtable<Graphics, BufferedImage> images;
    private GraphicsHashtable() {
        try {
            images = new Hashtable<>();

            images.put(FLOOR, loadImage("/graphics/floor.png"));
            images.put(DANGER, loadImage("/graphics/danger.png"));
            images.put(WALL, loadImage("/graphics/wall.png"));
            images.put(BOX, loadImage("/graphics/box.png"));
            switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
                case Calendar.MONDAY :
                    images.put(PLAYER, loadImage("/graphics/player_mon.png"));
                    break;
                case Calendar.TUESDAY :
                    images.put(PLAYER, loadImage("/graphics/player_tue.png"));
                    break;
                case Calendar.WEDNESDAY :
                    images.put(PLAYER, loadImage("/graphics/player_wed.png"));
                    break;
                case Calendar.THURSDAY :
                    images.put(PLAYER, loadImage("/graphics/player_thu.png"));
                    break;
                case Calendar.FRIDAY :
                    images.put(PLAYER, loadImage("/graphics/player_fri.png"));
                    break;
                default :
                    images.put(PLAYER, loadImage("/graphics/player_wek.png"));
                    break;
            }
            images.put(ENEMY, loadImage("/graphics/enemy.png"));
            images.put(MIMIC, loadImage("/graphics/mimic.png"));
            images.put(SMART, loadImage("/graphics/enemy_smart.png"));
            images.put(BUTTON_PRESSED, loadImage("/graphics/button_pressed.png"));
            images.put(BUTTON_RELEASED, loadImage("/graphics/button_released.png"));
            images.put(BUTTON_PERMANENT_PRESSED, loadImage("/graphics/button_permanent_pressed.png"));
            images.put(BUTTON_PERMANENT_RELEASED, loadImage("/graphics/button_permanent_released.png"));
            images.put(DOOR_OPEN, loadImage("/graphics/door_open.png"));
            images.put(DOOR_CLOSED, loadImage("/graphics/door_closed.png"));
            images.put(GOAL, loadImage("/graphics/goal.png"));
            images.put(DEFAULT, loadImage("/graphics/default.png"));
            images.put(EMPTY, loadImage("/graphics/empty.png"));
            images.put(ONEWAY_UP, loadImage("/graphics/oneway_up.png"));
            images.put(ONEWAY_DOWN, loadImage("/graphics/oneway_down.png"));
            images.put(ONEWAY_LEFT, loadImage("/graphics/oneway_left.png"));
            images.put(ONEWAY_RIGHT, loadImage("/graphics/oneway_right.png"));
            images.put(SIGN, loadImage("/graphics/sign.png"));
            images.put(TELEPORT, loadImage("/graphics/teleport.png"));
            images.put(ARROW_UP, loadImage("/graphics/arrow_up.png"));
            images.put(ARROW_DOWN, loadImage("/graphics/arrow_down.png"));
            images.put(ARROW_LEFT, loadImage("/graphics/arrow_left.png"));
            images.put(ARROW_RIGHT, loadImage("/graphics/arrow_right.png"));
            images.put(SMART_SELECTED, loadImage("/graphics/enemy_smart_selected.png"));
            images.put(BUTTON_SELECTED, loadImage("/graphics/button_released_selected.png"));
            images.put(BUTTON_PERMANENT_SELECTED, loadImage("/graphics/button_permanent_released_selected.png"));
            images.put(ENEMY_SELECTED, loadImage("/graphics/enemy_selected.png"));
            images.put(SIGN_SELECTED, loadImage("/graphics/sign_selected.png"));
            images.put(TELEPORT_SELECTED, loadImage("/graphics/teleport_selected.png"));
            images.put(OUT_OF_BOUNDS, loadImage("/graphics/oob.png"));
            images.put(MINUS, loadImage("/graphics/minus.png"));
            images.put(CHECKPOINT_UNUSED, loadImage("/graphics/checkpoint_unused.png"));
            images.put(CHECKPOINT_USED, loadImage("/graphics/checkpoint_used.png"));
        } catch (IOException e) {
            System.out.println("Couldn't load graphics");
        }
    }
    public static GraphicsHashtable getInstance()
    {
        return graphicsHashtable;
    }
    public BufferedImage getImage(Graphics g) {
        return images.get(g);
    }
    public Hashtable<Graphics, BufferedImage> getImages() {
        return images;
    }

    // Helper function to load an image using resources system, exposed to be used by other classes
    public static BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(GraphicsHashtable.class.getResourceAsStream(path));
    }
}
