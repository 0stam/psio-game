package display;

import gamemanager.GameManager;

import javax.swing.*;
import java.awt.*;
public class EditorMapDisplay extends JPanel {
    private JPanel container = new JPanel();
    //skala chyba powinna tu zostac bo oblicza ja EditorMapDisplay
    public static float scale = 1;
    //lepiej zeby to byla tablica dla refreshTile i refreshMap, tablice 1D bede zamienial na
    //array listy bo sa szybsze i bardziej elastyczne
    private InteractiveTile[][] mapTiles;

    public EditorMapDisplay() {
        container.setLayout(new GridLayout(GameManager.getInstance().getMap().getHeight(), GameManager.getInstance().getMap().getWidth(),0,0));
        this.mapTiles = new InteractiveTile[GameManager.getInstance().getMap().getWidth()][GameManager.getInstance().getMap().getHeight()];

        for (int j = 0; j < GameManager.getInstance().getMap().getHeight(); j++) {
            for (int i = 0; i < GameManager.getInstance().getMap().getWidth(); i++) {
                InteractiveTile toAdd = new InteractiveTile(new Point(i, j));
                container.add(toAdd);
                mapTiles[i][j] = toAdd;
            }
        }

        this.setBackground(Color.WHITE);
        this.refresh();
        this.add(container);
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        //jesli damy tu kod do modyfikowania mapy to dzieja sie naprawde dziwne rzeczy
        g.setColor(Color.white);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        float scaleX = (float) this.getWidth() / (32.0f * GameManager.getInstance().getMap().getWidth());
        float scaleY = (float) this.getHeight() / (32.0f * GameManager.getInstance().getMap().getHeight());

        scale = Math.min(scaleX, scaleY);
        container.setPreferredSize(new Dimension((int) (32 * scale * GameManager.getInstance().getMap().getWidth()), (int) (32 * (scale-0.01) * GameManager.getInstance().getMap().getHeight())));
    }
    public void refresh()
    {
        switch (GameManager.getInstance().getEditor().getLayer()) {
            case BOTH: {
                for (int j = 0; j < GameManager.getInstance().getMap().getHeight(); j++) {
                    for (int i = 0; i < GameManager.getInstance().getMap().getWidth(); i++) {
                        if (GameManager.getInstance().getMap().getUpperLayer(i, j) == null)
                            mapTiles[i][j].updateGraphics(GameManager.getInstance().getMap().getBottomLayer(i, j).getGraphicsID(), null);
                        else
                            mapTiles[i][j].updateGraphics(GameManager.getInstance().getMap().getBottomLayer(i, j).getGraphicsID(), GameManager.getInstance().getMap().getUpperLayer(i, j).getGraphicsID());
                    }
                }
                break;
            }
            case UPPER: {
                for (int j = 0; j < GameManager.getInstance().getMap().getHeight(); j++) {
                    for (int i = 0; i < GameManager.getInstance().getMap().getWidth(); i++) {
                        if (GameManager.getInstance().getMap().getUpperLayer(i,j) == null)
                            mapTiles[i][j].updateGraphics(enums.Graphics.EMPTY, enums.Graphics.EMPTY);
                        else
                            mapTiles[i][j].updateGraphics(enums.Graphics.EMPTY, GameManager.getInstance().getMap().getUpperLayer(i, j).getGraphicsID());
                    }
                }
                break;
            }
            case BOTTOM:
            {
                for (int j = 0; j < GameManager.getInstance().getMap().getHeight(); j++) {
                    for (int i = 0; i < GameManager.getInstance().getMap().getWidth(); i++) {
                        if (GameManager.getInstance().getMap().getBottomLayer(i,j) == null)
                            mapTiles[i][j].updateGraphics(enums.Graphics.EMPTY, null);
                        else
                            mapTiles[i][j].updateGraphics(GameManager.getInstance().getMap().getBottomLayer(i, j).getGraphicsID(), null);
                    }
                }
                break;
            }
        }

        this.repaint();
    }
    /*
    Komentarz nt tej metody w Editor
    public void repaintTile(enums.Graphics bottomLayer, enums.Graphics upperLayer, int x, int y)
    {
        mapTiles[x][y].updateGraphics(bottomLayer, upperLayer);
        mapTiles[x][y].repaint();
    }
    */
    public JPanel getContainer() {
        return container;
    }

    public void setContainer(JPanel container) {
        this.container = container;
    }
}
