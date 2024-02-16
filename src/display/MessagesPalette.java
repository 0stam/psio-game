package display;
import IO.IOManager;
import event.editor.EnemySelectedEvent;
import event.editor.SignSelectedEvent;
import gamemanager.GameManager;
import tile.Sign;
import tile.SmartEnemy;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;


public class MessagesPalette extends JPanel{
	private JSplitPane splitPane;
	private JTree jTree;
	private static JTextArea textArea;
	private float scale = 1;

	public MessagesPalette ()
	{
		setPreferredSize(new Dimension(0, 200));
		setLayout(new BorderLayout());

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		splitPane.setResizeWeight(0.4f);

		jTree = new JTree(GameManager.getInstance().getEditor().getSignsTreeModel());
		jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		jTree.setShowsRootHandles(true);
		jTree.setBackground(Color.WHITE);
		jTree.setFont(new Font("Arial", Font.PLAIN, 25));
		jTree.setRowHeight(30);
		DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) jTree.getCellRenderer();
		renderer.setClosedIcon(new ImageIcon(GraphicsHashtable.getInstance().getImage(enums.Graphics.SIGN)));
		renderer.setOpenIcon(new ImageIcon(GraphicsHashtable.getInstance().getImage(enums.Graphics.SIGN)));
		renderer.setLeafIcon(null);

		textArea = new JTextArea();
		textArea.setBackground(Color.WHITE);
		textArea.setFont(new Font("Arial", Font.PLAIN, 25));

		jTree.addTreeSelectionListener(new SelectionListener());

		JScrollPane scrollPane = new JScrollPane(jTree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.setMinimumSize(new Dimension(300, 0));
		//GameManager.getInstance().getEditor().setReferenceTree(jTree);
		splitPane.setLeftComponent(scrollPane);
		splitPane.setRightComponent(textArea);
		this.add(splitPane);

		createKeyBindings();

		this.revalidate();
	}
	@Override
	protected void paintComponent(java.awt.Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		jTree.expandRow(0);
	}
	public class SelectionListener implements TreeSelectionListener
	{
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			DefaultMutableTreeNode selected = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
			if (selected == null)
				return;
			if (selected.getUserObject() instanceof Sign sign)
			{
				EditorInputHandler inputHandler = (EditorInputHandler) IOManager.getInstance().getInputHandler();
				inputHandler.onEvent(new SignSelectedEvent((Sign)GameManager.getInstance().getMap().getBottomLayer(sign.getX(), sign.getY())));
			}
		}
	}

	public void createKeyBindings() {
		InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = getActionMap();

		inputMap.put(KeyStroke.getKeyStroke("P"), "select_enemy");
		actionMap.put("select_enemy", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: put code here
				// System.out.println("S pressed");
			}
		});
	}

	public JTree getTree() {
		return jTree;
	}

	public void setTree(JTree jTree) {
		this.jTree = jTree;
	}

	public static String getText () {
		return textArea.getText();
	}

	public static void setText (String text) {
		textArea.setText(text);
	}
}
