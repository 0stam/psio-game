package display;

import javax.swing.*;
import java.awt.*;

public class ResizeWindow {
	private JFrame window;

	public ResizeWindow() {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		}
		catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
			   IllegalAccessException e) {
		}

		window = new JFrame("Zmien rozmiar mapy");
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setVisible(true);
		window.setIconImage(new ImageIcon("src/graphics/icon.png").getImage());
		//i tak nie ma sensu robic mniejszego
		window.setMinimumSize(new Dimension(300, 400));

		window.getContentPane().setLayout(new FlowLayout());
		window.getContentPane().add(new JLabel("Width: "));
		window.getContentPane().add(new JLabel("Height: "));
	}
}
