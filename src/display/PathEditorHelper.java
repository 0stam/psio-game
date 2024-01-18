package display;

import enums.Direction;
import enums.EditorGraphics;
import gamemanager.GameManager;
import tile.PathTile;
import tile.SmartEnemy;

import java.util.ArrayList;

import static enums.Arrow.*;
import static enums.Arrow.EMPTY;
import static enums.Direction.*;
import static enums.Direction.RIGHT;

public class PathEditorHelper {
    public static void updateEnemyPath(SmartEnemy currentEnemy, EditorGraphics[][] currentPath)
    {
        if (currentEnemy != null && currentPath!=null)
        {
            currentEnemy.setPathTileList(pathToList(currentEnemy, currentPath));
        }
    }
    public static ArrayList<PathTile> pathToList(SmartEnemy currentEnemy, EditorGraphics[][] currentPath)
    {
        ArrayList<PathTile> toReturn = new ArrayList<>();
        for (int i = 0 ; i < currentPath.length ; ++i)
        {
            for (int j = 0 ; j < currentPath[0].length ; ++j)
            {
                if (currentPath[i][j] != EMPTY && currentPath[i][j] != null)
                {
                    Direction dir;
                    /*
                        java: patterns in switch statements are a preview feature and are disabled by default.
                        (use --enable-preview to enable patterns in switch statements)

                        jak macie cos takiego to zmiencie java machine na najnowsza wersje dodatkowo moze pomoc
                        zmienienie w ustawieniach projektu wersj
                     */
                    switch (currentPath[i][j])
                    {
                        case ARROW_UP -> dir = UP;
                        case ARROW_DOWN -> dir = DOWN;
                        case ARROW_LEFT -> dir = LEFT;
                        case ARROW_RIGHT -> dir = RIGHT;
                        default -> throw new IllegalStateException("Unexpected value: " + currentPath[i][j]);
                    }
                    toReturn.add((new PathTile(i, j, dir)));
                }
            }
        }
        return toReturn;
    }
    public static EditorGraphics[][] generateEmptyPath()
    {
        EditorGraphics[][] path = new EditorGraphics[GameManager.getInstance().getMap().getWidth()][GameManager.getInstance().getMap().getHeight()];
        for (int i = 0 ; i < path.length ; ++i)
        {
            for (int j = 0 ; j < path[0].length ; ++j)
            {
                path[i][j] = EMPTY;
            }
        }

        return path;
    }
    public static EditorGraphics[][] listToPath(SmartEnemy currentEnemy)
    {
        if (currentEnemy != null) {
            EditorGraphics[][] path = generateEmptyPath();
            ArrayList<PathTile> pathTiles = currentEnemy.getPathTileList();
            if (pathTiles != null)
            {
                for (PathTile i : pathTiles) {
                    EditorGraphics arrow = null;
                    switch (i.getDirection()) {
                        case UP: {
                            arrow = ARROW_UP;
                            break;
                        }
                        case DOWN: {
                            arrow = ARROW_DOWN;
                            break;
                        }
                        case LEFT: {
                            arrow = ARROW_LEFT;
                            break;
                        }
                        case RIGHT: {
                            arrow = ARROW_RIGHT;
                            break;
                        }
                    }
                    if (i.getX() < GameManager.getInstance().getMap().getWidth() && i.getY() < GameManager.getInstance().getMap().getHeight()) {
                        path[i.getX()][i.getY()] = arrow;
                    }
                }
            }
            else
                path = generateEmptyPath();
            return path;
        }
        return null;
    }
    public static EditorGraphics[][] resizePath(EditorGraphics[][] currentPath)
    {
        EditorGraphics[][] path = new EditorGraphics[GameManager.getInstance().getMap().getWidth()][GameManager.getInstance().getMap().getHeight()];
        for (int i = 0 ; i < path.length ; ++i)
        {
            for (int j = 0 ; j < path[0].length ; ++j)
            {
                if (i < currentPath.length && j < currentPath[0].length) {
                    path[i][j] = currentPath[i][j];
                } else {
                    path[i][j] = EMPTY;
                }
            }
        }

        return path;
    }
}
