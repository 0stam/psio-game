package display;

import IO.IOManager;
import editor.TreeModel;
import enums.EditorMode;
import enums.Layer;
import event.SavePathEvent;
import gamemanager.GameManager;
import map.Map;
import tile.Floor;
import tile.SmartEnemy;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class ResizeWindow {
	private JFrame window;
	private JPanel topRow;
	private JPanel centerRow;
	private JPanel bottomRow;
	private JTextField widthField;
	private JTextField heightField;
	private JButton OKButton;
	private JButton cancelButton;

	class OKListener implements ActionListener {
		private ResizeWindow parent;
		private JFrame window;

		public OKListener(ResizeWindow parent, JFrame window) {
			this.parent = parent;
			this.window = window;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int newWidth = parent.getWidth();
			int newHeight = parent.getHeight();

			if (newWidth > 0 && newHeight > 0) {
				Map newMap = new Map(newWidth, newHeight);

				for (int i = 0;i < newWidth;i++) {
					for (int j = 0;j < newHeight;j++) {
						if (i < GameManager.getInstance().getMap().getWidth() && j < GameManager.getInstance().getMap().getHeight()) {
							newMap.setBottomLayer(i, j, GameManager.getInstance().getMap().getBottomLayer(i, j));
							if (GameManager.getInstance().getMap().getUpperLayer(i, j) != null) {
								newMap.setUpperLayer(i, j, GameManager.getInstance().getMap().getUpperLayer(i, j));
							}
						} else {
							newMap.setBottomLayer(i, j, new Floor(i, j));
						}
					}
				}

				for (int i = 0;i < GameManager.getInstance().getMap().getWidth(); i++) {
					for (int j = 0;j < GameManager.getInstance().getMap().getHeight();j++) {
						if ((i >= newWidth || j >= newHeight) && ((GameManager.getInstance().getMap().getUpperLayer(i, j) instanceof SmartEnemy) || (GameManager.getInstance().getMap().getBottomLayer(i, j) instanceof SmartEnemy))) {

							GameManager.getInstance().getEditor().getTreeModel().removeNode("Smart enemy (" + i + ", " + j + ")");
						}
					}
				}

				GameManager.getInstance().setMap(newMap);
				if (GameManager.getInstance().getEditor().getCurrentPath() != null) {
					GameManager.getInstance().getEditor().setCurrentPath(GameManager.getInstance().getEditor().resizePath());
					GameManager.getInstance().onEvent(new SavePathEvent());
				}
				GameManager.getInstance().getEditor().setCurrentEnemy(null);
				GameManager.getInstance().getEditor().setMode(EditorMode.PREADD);
				GameManager.getInstance().getEditor().setLayer(Layer.BOTH);

				IOManager.getInstance().drawGame();
				IOManager.getInstance().drawEditor();
			}

			window.dispose();
		}
	}

	class cancelListener implements ActionListener {
		private ResizeWindow parent;
		private JFrame window;

		public cancelListener(ResizeWindow parent, JFrame window) {
			this.parent = parent;
			this.window = window;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			window.dispose();
		}
	}

	public ResizeWindow() {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
				 IllegalAccessException e) {
		}

		window = new JFrame("Zmien rozmiar mapy");
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setVisible(true);
		window.setIconImage(new ImageIcon("src/graphics/icon.png").getImage());
		//i tak nie ma sensu robic mniejszego
		window.setSize(new Dimension(250, 150));
		window.setResizable(false);

		widthField = new JTextField();
		widthField.setPreferredSize(new Dimension(100, 20));
		topRow = new JPanel(new FlowLayout());
		topRow.add(new JLabel("Width: "));
		topRow.add(widthField);
		window.getContentPane().add(topRow, BorderLayout.NORTH);

		heightField = new JTextField();
		heightField.setPreferredSize(new Dimension(100, 20));
		centerRow = new JPanel(new FlowLayout());
		centerRow.add(new JLabel("Height: "));
		centerRow.add(heightField);
		window.getContentPane().add(centerRow, BorderLayout.CENTER);

		OKButton = new JButton("OK");
		cancelButton = new JButton("Cancel");
		bottomRow = new JPanel(new FlowLayout());
		bottomRow.add(OKButton);
		bottomRow.add(cancelButton);
		window.getContentPane().add(bottomRow, BorderLayout.SOUTH);

		OKButton.addActionListener(new OKListener(this, window));
		cancelButton.addActionListener(new cancelListener(this, window));
	}

	int getWidth () {
		int result = 0;

		try {
			result = Integer.parseInt(this.widthField.getText());
		} catch (Exception e) {
			result = -1;
		}

		return (result > 0 ? result : -1);
	}

	int getHeight () {
		int result = 0;

		try {
			result = Integer.parseInt(this.heightField.getText());
		} catch (Exception e) {
			result = -1;
		}

		return (result > 0 ? result : -1);
	}
}
