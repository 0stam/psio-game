package display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import gamemanager.GameManager;
import levelloader.LevelLoader;
import event.LevelSelectedEvent;
import levelloader.LevelNotLoaded;
import map.Map;

public class LevelSelectionDisplay extends JPanel {
    private int currentPage = 0;
    private final int levelsPerPage;
    private final int totalLevels;

    public LevelSelectionDisplay() {
        totalLevels = LevelLoader.getLevelCount();
        // 4x4 siatka poziomów
        levelsPerPage = 16;

        setLayout(new BorderLayout());
        JPanel levelGrid = createLevelGrid();
        add(levelGrid, BorderLayout.CENTER);

        if (totalLevels > levelsPerPage) {
            add(createNavigationPanel(), BorderLayout.SOUTH);
        }
    }

    private JPanel createLevelGrid() {
        JPanel grid = new JPanel(new GridLayout(4, 4)); // 4x4 siatka
        int startLevel = currentPage * levelsPerPage;
        for (int i = 0; i < levelsPerPage; i++) {
            int levelNumber = startLevel + i;
            if (levelNumber < totalLevels) {
                JButton levelButton = new JButton("Level " + (levelNumber + 1));
                levelButton.addActionListener(e -> {
                    LevelSelectedEvent levelSelectedEvent = new LevelSelectedEvent(levelNumber);
                    GameManager.getInstance().onEvent(levelSelectedEvent);
                });
                grid.add(levelButton);
            } else {
                grid.add(new JLabel("")); // Puste miejsce
            }
        }
        return grid;
    }


    private void loadAndStartLevel(int levelNumber) {
        try {
            Map levelMap = LevelLoader.loadLevel(levelNumber + 1);
            GameManager.getInstance().setMap(levelMap);
            GameManager.getInstance().startGame();
        } catch (LevelNotLoaded e) {
            e.printStackTrace();
        }
    }


    private JPanel createNavigationPanel() {
        JPanel navigationPanel = new JPanel(new FlowLayout());

        JButton prevButton = new JButton("<");
        prevButton.addActionListener(e -> navigate(-1));
        navigationPanel.add(prevButton);

        JButton nextButton = new JButton(">");
        nextButton.addActionListener(e -> navigate(1));
        navigationPanel.add(nextButton);

        return navigationPanel;
    }

    private void navigate(int direction) {
        int newPage = currentPage + direction;
        // Sprawdzanie granic
        if (newPage >= 0 && (newPage * levelsPerPage) < totalLevels) {
            currentPage = newPage;
            removeAll();
            add(createLevelGrid(), BorderLayout.CENTER);
            if (totalLevels > levelsPerPage) {
                add(createNavigationPanel(), BorderLayout.SOUTH);
            }
            revalidate();
            repaint();
        }
    }
}
