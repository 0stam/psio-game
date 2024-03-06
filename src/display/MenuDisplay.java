package display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import IO.IOManager;
import event.editor.EditorSelectedEvent;
import gamemanager.GameManager;

public class MenuDisplay extends JPanel {
    private JPanel container;
    private JButton playButton;
    private JButton editorButton;
    private JLabel titleLabel;
    private Image backgroundImage;

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

        playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MenuDisplay.this.onPlayClicked();
			}
		});
        editorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MenuDisplay.this.onEditorClicked();
			}
		});

        container.add(Box.createVerticalGlue());
        container.add(titleLabel);
        container.add(Box.createRigidArea(new Dimension(0, 50)));
        container.add(playButton);
        container.add(Box.createRigidArea(new Dimension(0, 30)));
        container.add(editorButton);
        container.add(Box.createVerticalGlue());
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
                newWidth = width;
                newHeight = (int) (width / aspectRatio);
            } else {
                newHeight = height;
                newWidth = (int) (height * aspectRatio);
            }

            // Wycentrowanie obrazu
            int x = (width - newWidth) / 2;
            int y = (height - newHeight) / 2;

            // Rysowanie przeskalowanego obrazu
            g.drawImage(backgroundImage, x, y, newWidth, newHeight, this);
        }
    }

}
