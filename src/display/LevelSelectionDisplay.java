package display;

import javax.swing.*;
import java.awt.*;

import gamemanager.GameManager;
import levelloader.LevelLoader;
import event.game.LevelSelectedEvent;

public class LevelSelectionDisplay extends JPanel {
    private int currentPage = 0;
    private final int levelsPerPage;
    private final int totalLevels;

    public LevelSelectionDisplay() {
        totalLevels = LevelLoader.getLevelCount();
        // 4x4 siatka poziomów
        levelsPerPage = 4;

        setLayout(new BorderLayout());
        JPanel levelGrid = createLevelGrid();
        add(levelGrid, BorderLayout.CENTER);

        setOpaque(false);

        if (totalLevels > levelsPerPage) {
            add(createNavigationPanel(), BorderLayout.SOUTH);
        }
    }

    private JPanel createLevelGrid() {
        GridLayout layout = new GridLayout(2, 2);
        layout.setHgap(5);
        layout.setVgap(5);

        JPanel grid = new JPanel(layout);
        grid.setOpaque(false);

        int startLevel = currentPage * levelsPerPage;

        for (int i = 0; i < levelsPerPage; i++) {
            int levelNumber = startLevel + i;
            if (levelNumber < totalLevels) {
                JButton levelButton = new JButton(getLevelName(LevelLoader.getPath(levelNumber, false)));
                levelButton.addActionListener(e -> {
                    LevelSelectedEvent levelSelectedEvent = new LevelSelectedEvent(levelNumber);
                    GameManager.getInstance().onEvent(levelSelectedEvent);
                });

                // Button style
                levelButton.setForeground(Color.white);
                levelButton.setOpaque(true);
                levelButton.setContentAreaFilled(false);
                levelButton.setFont(new Font("Arial", Font.PLAIN, 40));
                levelButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));

                grid.add(levelButton);
            } else {
                grid.add(new JLabel("")); // Puste miejsce
            }
        }
        return grid;
    }


    private void loadAndStartLevel(int levelNumber) {
        GameManager.getInstance().onEvent(new LevelSelectedEvent(levelNumber));
    }


    private JPanel createNavigationPanel() {
        JPanel navigationPanel = new JPanel(new FlowLayout());
        navigationPanel.setOpaque(false);

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

    private String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    private String getLevelName(String fileName) {
        StringBuilder result = new StringBuilder();
        String[] words = fileName.split("[ _-]");

        try {
            int number = Integer.parseInt(words[0]);
            if (words.length == 1) {
                return "Level: " + number;
            }
        } catch (NumberFormatException e) {
            result.append(capitalize(words[0]));
        }

        for (int i = 1; i < words.length; i++) {
            if (result.length() > 0) {
                result.append(" ");
            }
            result.append(capitalize(words[i]));
        }

        return result.toString();
    }
}
