package map;

import enums.Direction;
import gamemanager.GameManager;
import tile.*;


// Requires downloading JUnit library. It can be done automatically by IntelliJ.
// After download go to "Menu > Project Structure > Sources" and mark "test" directory as test source folder.
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class MapTest {
    int xMax = 8;
    int yMax = 12;
    
    Map map;
    
    @BeforeEach
    void setup() {
        map = new Map(xMax, yMax);

        GameManager.getInstance().setMap(map);

        // Fill floor
        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {
                map.setBottomLayer(x, y, new Floor(x, y));
            }
        }
    }

    @Test
    void move() {
        int playerX = 2;
        int playerY = 2;

        PlayerCharacter player = new PlayerCharacter(playerX, playerY);
        map.setUpperLayer(playerX, playerY, player);

        // Move an object on an empty map
        for (Direction direction : Direction.values()) {
            if (direction == Direction.DEFAULT) {
                assertEquals(player, map.getUpperLayer(playerX, playerY));
                continue;
            }

            map.move(playerX, playerY, direction);

            assertNull(map.getUpperLayer(playerX, playerY));

            playerX += direction.x;
            playerY += direction.y;

            assertEquals(player, map.getUpperLayer(playerX, playerY));
            assertEquals(playerX, player.getX());
            assertEquals(playerY, player.getY());
        }

        // Move an object into a wall
        for (Direction direction : Direction.values()) {
            if (direction == Direction.DEFAULT) continue;

            int wallX = playerX + direction.x;
            int wallY = playerY + direction.y;
            map.setBottomLayer(wallX, wallY, new Wall(wallX, wallY));

            map.move(playerX, playerY, direction);

            assertEquals(player, map.getUpperLayer(playerX, playerY));
            assertNull(map.getUpperLayer(wallX, wallY));

            map.setBottomLayer(wallX, wallY, new Floor(wallX, wallY));
        }

        // Move an object out of map
        for (Direction direction : Direction.values()) {
            if (direction == Direction.DEFAULT) continue;

            int steps = Math.max(xMax, yMax);
            for (int i = 0; i < steps; i++) {
                map.move(playerX, playerY, direction);
                playerX = Math.clamp(playerX + (long) direction.x, 0, xMax - 1);
                playerY = Math.clamp(playerY + (long) direction.y, 0, yMax - 1);
            }

            assertEquals(player, map.getUpperLayer(playerX, playerY));
        }
    }

    @Test
    void update() {
        PlayerCharacter player = new PlayerCharacter(2, 2);
        map.setUpperLayer(2, 2, player);

        map.startTurn(Direction.RIGHT);
        map.startTurn(Direction.DOWN);

        assertEquals(player, map.getUpperLayer(3, 3));
    }
}