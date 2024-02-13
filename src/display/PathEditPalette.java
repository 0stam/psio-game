package display;
import IO.IOManager;
import enums.Arrow;
import event.editor.EnemySelectedEvent;
import gamemanager.GameManager;
import tile.SmartEnemy;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;


public class PathEditPalette extends JPanel{
    private JSplitPane splitPane;
    private JTree jTree;
    private TilePalette arrows;
    private float scale = 1;
    PathEditPalette ()
    {
        setPreferredSize(new Dimension(0, 200));
        setLayout(new BorderLayout());

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        splitPane.setResizeWeight(0.4f);

        jTree = new JTree(GameManager.getInstance().getEditor().getEnemiesTreeModel().getDefaultTreeModel());
        //roamingEnemyTree.setEditable(true);
        jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        jTree.setShowsRootHandles(true);
        jTree.setBackground(Color.WHITE);
        jTree.setFont(new Font("Arial", Font.PLAIN, 25));
        jTree.setRowHeight(30);
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) jTree.getCellRenderer();
        renderer.setClosedIcon(new ImageIcon(GraphicsHashtable.getInstance().getImage(enums.Graphics.SMART)));
        renderer.setOpenIcon(new ImageIcon(GraphicsHashtable.getInstance().getImage(enums.Graphics.SMART)));
        renderer.setLeafIcon(null);

        arrows = new TilePalette(Arrow.values());

        jTree.addTreeSelectionListener(new SelectionListener());

        JScrollPane scrollPane = new JScrollPane(jTree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setMinimumSize(new Dimension(300, 0));
        //GameManager.getInstance().getEditor().setReferenceTree(jTree);
        splitPane.setLeftComponent(scrollPane);
        splitPane.setRightComponent(arrows);
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
            if (selected.isLeaf() && !((String) selected.getUserObject()).isEmpty())
            {
                String chosen = (String) selected.getUserObject();
                chosen = chosen.substring(13, chosen.length()-1);
                int dot = chosen.indexOf(',');
                String s = chosen.substring(0, dot);
                int x = Integer.parseInt(chosen.substring(0, dot));
                int y = Integer.parseInt(chosen.substring(dot+2));
                EditorInputHandler inputHandler = (EditorInputHandler) IOManager.getInstance().getInputHandler();
                inputHandler.onEvent(new EnemySelectedEvent((SmartEnemy)GameManager.getInstance().getMap().getUpperLayer(x, y)));
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

    public TilePalette getArrows() {
        return arrows;
    }

    public void setArrows(TilePalette arrows) {
        this.arrows = arrows;
    }

    public JTree getTree() {
        return jTree;
    }

    public void setTree(JTree jTree) {
        this.jTree = jTree;
    }
}
