package display;

import IO.GraphicIO;
import IO.IOManager;

import javax.swing.*;
import java.awt.*;

public class PopupClass extends JPanel {
	String textBuffer = "";
	JLabel textArea = new JLabel("Empty");
	public PopupClass(){
		init();
	}
	public PopupClass(String buffer)
	{
		textBuffer = buffer;
		textArea = new JLabel(buffer);
		textArea.setFont(new Font("Times New Roman", Font.PLAIN, 40));
		textArea.setBackground(new Color(0, 0, 0, 0));
		this.setBackground(new Color(255, 255, 255));
		init();
	}
	private void init()
	{
		this.setPreferredSize(new Dimension((int) (((GraphicIO) IOManager.getInstance().getStrategy()).getWindow().getContentPane().getWidth() * 0.9), (int) (((GraphicIO) IOManager.getInstance().getStrategy()).getWindow().getContentPane().getHeight() * 0.9 / 2)));
		this.add(textArea);
	}
}
