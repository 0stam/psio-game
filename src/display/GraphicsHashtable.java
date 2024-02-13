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

            images.put(FLOOR, ImageIO.read(new File("src/graphics/floor.png")));
            images.put(DANGER, ImageIO.read(new File("src/graphics/danger.png")));
            images.put(WALL, ImageIO.read(new File("src/graphics/wall.png")));
            images.put(BOX, ImageIO.read(new File("src/graphics/box.png")));
            switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
                case Calendar.MONDAY -> images.put(PLAYER, ImageIO.read(new File("src/graphics/player_mon.png")));
                case Calendar.TUESDAY -> images.put(PLAYER, ImageIO.read(new File("src/graphics/player_tue.png")));
                case Calendar.WEDNESDAY -> images.put(PLAYER, ImageIO.read(new File("src/graphics/player_wed.png")));
                case Calendar.THURSDAY -> images.put(PLAYER, ImageIO.read(new File("src/graphics/player_thu.png")));
                case Calendar.FRIDAY -> images.put(PLAYER, ImageIO.read(new File("src/graphics/player_fri.png")));
                default -> images.put(PLAYER, ImageIO.read(new File("src/graphics/player_wek.png")));
            }
            images.put(ENEMY, ImageIO.read(new File("src/graphics/enemy.png")));
            images.put(MIMIC, ImageIO.read(new File("src/graphics/mimic.png")));
            images.put(SMART, ImageIO.read(new File("src/graphics/enemy_smart.png")));
            images.put(BUTTON_PRESSED, ImageIO.read(new File("src/graphics/button_pressed.png")));
            images.put(BUTTON_RELEASED, ImageIO.read(new File("src/graphics/button_released.png")));
            images.put(BUTTON_PERMANENT_PRESSED, ImageIO.read(new File("src/graphics/button_permanent_pressed.png")));
            images.put(BUTTON_PERMANENT_RELEASED, ImageIO.read(new File("src/graphics/button_permanent_released.png")));
            images.put(DOOR_OPEN, ImageIO.read(new File("src/graphics/door_open.png")));
            images.put(DOOR_CLOSED, ImageIO.read(new File("src/graphics/door_closed.png")));
            images.put(GOAL, ImageIO.read(new File("src/graphics/goal.png")));
            images.put(DEFAULT, ImageIO.read(new File("src/graphics/default.png")));
            images.put(EMPTY, ImageIO.read(new File("src/graphics/empty.png")));
            images.put(ONEWAY_UP, ImageIO.read(new File("src/graphics/oneway_up.png")));
            images.put(ONEWAY_DOWN, ImageIO.read(new File("src/graphics/oneway_down.png")));
            images.put(ONEWAY_LEFT, ImageIO.read(new File("src/graphics/oneway_left.png")));
            images.put(ONEWAY_RIGHT, ImageIO.read(new File("src/graphics/oneway_right.png")));
            images.put(SIGN, ImageIO.read(new File("src/graphics/sign.png")));
            images.put(TELEPORT, ImageIO.read(new File("src/graphics/teleport.png")));
            images.put(ARROW_UP, ImageIO.read(new File("src/graphics/arrow_up.png")));
            images.put(ARROW_DOWN, ImageIO.read(new File("src/graphics/arrow_down.png")));
            images.put(ARROW_LEFT, ImageIO.read(new File("src/graphics/arrow_left.png")));
            images.put(ARROW_RIGHT, ImageIO.read(new File("src/graphics/arrow_right.png")));
            images.put(SMART_SELECTED, ImageIO.read(new File("src/graphics/enemy_smart_selected.png")));
            images.put(BUTTON_SELECTED, ImageIO.read(new File("src/graphics/button_released_selected.png")));
            images.put(BUTTON_PERMANENT_SELECTED, ImageIO.read(new File("src/graphics/button_permanent_released_selected.png")));
            images.put(ENEMY_SELECTED, ImageIO.read(new File("src/graphics/enemy_selected.png")));
            images.put(SIGN_SELECTED, ImageIO.read(new File("src/graphics/sign_selected.png")));
            images.put(TELEPORT_SELECTED, ImageIO.read(new File("src/graphics/teleport_selected.png")));
            images.put(OUT_OF_BOUNDS, ImageIO.read(new File("src/graphics/oob.png")));
            images.put(MINUS, ImageIO.read(new File("src/graphics/minus.png")));
            images.put(CHECKPOINT_UNUSED, ImageIO.read(new File("src/graphics/checkpoint_unused.png")));
            images.put(CHECKPOINT_USED, ImageIO.read(new File("src/graphics/checkpoint_used.png")));
        } catch (IOException e) {
            System.out.println("Nie udalo sie zaladowac grafiki");
            e.printStackTrace();
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
}
