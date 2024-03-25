package display;

import javax.swing.*;
import java.awt.*;

import IO.IOManager;
import event.editor.EditorSelectedEvent;
import gamemanager.GameManager;

public class MenuDisplay extends JPanel {
    private JPanel container;
    private JButton playButton;
    private JButton editorButton;
    private JButton exitButton;
    private JLabel titleLabel;
    private Image backgroundImage;
    private double time = 0.0;

    public MenuDisplay() {
        // Ładowanie obrazu tła
        try {
            backgroundImage = new ImageIcon(getClass().getResource("/graphics/background.png")).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        add(container, BorderLayout.CENTER);


        titleLabel = new JLabel("NANOGRID");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 50));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        playButton = new JButton("PLAY");
        stylizeButton(playButton);

        editorButton = new JButton("EDITOR");
        stylizeButton(editorButton);

        exitButton = new JButton("EXIT");
        stylizeButton(exitButton);

        playButton.addActionListener(e -> onPlayClicked());
        editorButton.addActionListener(e -> onEditorClicked());
        exitButton.addActionListener(e -> onExitClicked());

        container.add(Box.createVerticalGlue());
        container.add(titleLabel);
        container.add(Box.createRigidArea(new Dimension(0, 50)));
        container.add(playButton);
        container.add(Box.createRigidArea(new Dimension(0, 30)));
        container.add(editorButton);
        container.add(Box.createRigidArea(new Dimension(0, 30)));
        container.add(exitButton);
        container.add(Box.createVerticalGlue());

        Runnable backgroundAnimator = new AnimateBackground(this);
        Thread animationThread = new Thread(backgroundAnimator);

        animationThread.start();

    }

    private void onPlayClicked() {
        // Obsługa przycisku PLAY
        //JFrame window = (JFrame) SwingUtilities.getWindowAncestor(this);
        //window.getContentPane().removeAll();
        //window.getContentPane().add(new LevelSelectionDisplay(), BorderLayout.CENTER);
        //window.revalidate();
        //window.repaint();

        container.removeAll();
        container.setLayout(new BorderLayout());
        container.add(new LevelSelectionDisplay(), BorderLayout.CENTER);
        container.revalidate();
        IOManager.getInstance().drawMenu();
    }

    private void onEditorClicked() {
        // Obsługa przycisku EDITOR
        GameManager.getInstance().onEvent(new EditorSelectedEvent());
    }

    private void onExitClicked () {
        System.exit(0);
    }

    private void stylizeButton(JButton button) {
        button.setFont(new Font("Serif", Font.PLAIN, 24));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(100, 100, 100));
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.setOpaque(true);
        button.setBorderPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            // Ustawienie skalowania obrazu, aby pasował do komponentu
            int width = this.getWidth();
            int height = this.getHeight();

            // Obliczenie nowych wymiarów zachowując proporcje
            int imgWidth = backgroundImage.getWidth(this);
            int imgHeight = backgroundImage.getHeight(this);

            float aspectRatio = (float) imgWidth / imgHeight;
            float containerRatio = (float) width / height;

            int newWidth, newHeight;

            // Dostosuj rozmiar obrazu do proporcji kontenera
            if (containerRatio > aspectRatio) {
                newWidth = (int) (width * 1.2f);
                newHeight = (int) (1.2f * width / aspectRatio);
            } else {
                newHeight = (int) (height * 1.2f);
                newWidth = (int) (1.2f * height * aspectRatio);
            }

            // Wycentrowanie obrazu
            int x = (int) ((width - newWidth) / 2 * (Math.cos(0.83 * time) + 1));
            int y = (int) ((height - newHeight) / 2 * (Math.cos(0.27 * time) + 1));

            // Rysowanie przeskalowanego obrazu
            g.drawImage(backgroundImage, x, y, newWidth, newHeight, this);
        }
    }

    private class AnimateBackground implements Runnable {
        MenuDisplay parent;

        AnimateBackground (MenuDisplay parent) {
            this.parent = parent;
        }

        @Override
        public void run() {
            while (true) {
                //Thread.currentThread().run();
                try {
                    parent.time += 0.0025;
                    parent.repaint();
                    Thread.currentThread().sleep(5);
                } catch (Exception e) {
                    break;
                }
            }

        }
    }
}
