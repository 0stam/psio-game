package display;
import IO.IOManager;
import enums.Arrow;
import event.EnemySelectedEvent;
import event.PalettePressedEvent;
import gamemanager.GameManager;
import tile.SmartEnemy;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

public class PathEditPalette extends JPanel{
    private JTree jTree;
    private TilePalette arrows;
    private float scale = 1;
    PathEditPalette ()
    {
        //TODO: podmienic ikonki na ikonki z gry zamiast javowych
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Enemy list");
        DefaultMutableTreeNode enemy1 = new DefaultMutableTreeNode("enemy1");
        root.add(enemy1);

        jTree = new JTree(GameManager.getInstance().getEditor().getTreeModel().getDefaultTreeModel());
        jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        //roamingEnemyTree.setEditable(true);
        jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        jTree.setShowsRootHandles(true);
        //roboczo
        jTree.setBackground(Color.RED);

        arrows = new TilePalette(Arrow.values());
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.add(arrows, BorderLayout.CENTER);
        container.setPreferredSize(new Dimension(100, 100));

        jTree.addTreeSelectionListener(new SelectionListener());
        this.add(jTree);
        this.add(container);
        this.revalidate();
    }
    @Override
    protected void paintComponent(java.awt.Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }
    public class SelectionListener implements TreeSelectionListener
    {
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
            if (selected == null)
                return;
            if (selected.isLeaf())
            {
                String chosen = (String) selected.getUserObject();
                chosen = chosen.substring(13, chosen.length()-1);
                int dot = chosen.indexOf(',');
                String s = chosen.substring(0, dot);
                int x = Integer.parseInt(chosen.substring(0, dot));
                int y = Integer.parseInt(chosen.substring(dot+2));
                GameManager.getInstance().onEvent(new EnemySelectedEvent((SmartEnemy)GameManager.getInstance().getMap().getUpperLayer(x, y)));
                IOManager.getInstance().drawEditor();
            }
        }
    }

    public TilePalette getArrows() {
        return arrows;
    }

    public void setArrows(TilePalette arrows) {
        this.arrows = arrows;
    }
}
