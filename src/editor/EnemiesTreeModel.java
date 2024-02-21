package editor;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.Enumeration;

public class EnemiesTreeModel extends DefaultTreeModel{
    /*
    *   This class propably should not be deleted completely
    *   Reason:
    *   If the default structure of the tree changes ex. more tiles with paths
    *   the constructor should also change. Since we are reusing the constructor,
    *   we should use this class instead of duplicating code in the future.
     */
    public EnemiesTreeModel() {
        super(new DefaultMutableTreeNode("Smart entities"));
    }

}
