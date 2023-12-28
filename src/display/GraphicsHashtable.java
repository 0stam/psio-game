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
    public static  Hashtable<Graphics, BufferedImage> images = null;
    //wyjatek obsluzylibysmy w superklasie map display albo project manager - tam tez byloby jedyne wywolanie metody
    //tylko jak to zrobic zeby wywolala sie dokladnie raz? (Po namysle mozna sprawdzic czy images to null ale to chyba nieladne)
    public static void setupimages () throws IOException {
        //wazne! wolac to tylko raz!
        images = new Hashtable<>();
        
        images.put(FLOOR, ImageIO.read(new File("src\\graphics\\floor.png")));
        images.put(WALL, ImageIO.read(new File("src\\graphics\\wall.png")));
        images.put(BOX, ImageIO.read(new File("src\\graphics\\box.png")));
        switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY -> images.put(PLAYER, ImageIO.read(new File("src\\graphics\\player_mon.png")));
            case Calendar.TUESDAY -> images.put(PLAYER, ImageIO.read(new File("src\\graphics\\player_tue.png")));
            case Calendar.WEDNESDAY -> images.put(PLAYER, ImageIO.read(new File("src\\graphics\\player_wed.png")));
            case Calendar.THURSDAY -> images.put(PLAYER, ImageIO.read(new File("src\\graphics\\player_thu.png")));
            case Calendar.FRIDAY -> images.put(PLAYER, ImageIO.read(new File("src\\graphics\\player_fri.png")));
            default -> images.put(PLAYER, ImageIO.read(new File("src\\graphics\\player_wek.png")));
        }
        images.put(ENEMY, ImageIO.read(new File("src\\graphics\\enemy.png")));
        images.put(BUTTON_PRESSED, ImageIO.read(new File("src\\graphics\\button_pressed.png")));
        images.put(BUTTON_RELEASED, ImageIO.read(new File("src\\graphics\\button_released.png")));
        images.put(DOOR_OPEN, ImageIO.read(new File("src\\graphics\\door_open.png")));
        images.put(DOOR_CLOSED, ImageIO.read(new File("src\\graphics\\door_closed.png")));
        images.put(GOAL, ImageIO.read(new File("src\\graphics\\goal.png")));
        images.put(DEFAULT, ImageIO.read(new File("src\\graphics\\default.png")));
    }
}
