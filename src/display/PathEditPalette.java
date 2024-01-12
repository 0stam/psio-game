package display;
import enums.Arrow;
import enums.EditableTile;
import enums.Layer;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class PathEditPalette extends JPanel{
    private JTree roamingEnemyTree;
    private TilePalette arrows;
    private float scale = 1;
    PathEditPalette ()
    {
        //TODO: podmienic ikonki na ikonki z gry zamiast javowych
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Enemy list");
        DefaultMutableTreeNode enemy1 = new DefaultMutableTreeNode("enemy1");
        root.add(enemy1);
        roamingEnemyTree = new JTree(root);
        //roboczo
        roamingEnemyTree.setBackground(Color.RED);

        arrows = new TilePalette(Arrow.values());
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.add(arrows, BorderLayout.CENTER);
        container.setPreferredSize(new Dimension(100, 100));
        this.add(roamingEnemyTree);
        this.add(container);
        this.revalidate();
    }
    @Override
    protected void paintComponent(java.awt.Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }
}
