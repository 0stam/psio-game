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

    public DefaultMutableTreeNode findNode(DefaultMutableTreeNode root, Object searched)
    {
        if (root == null || searched == null)
        {
            return null;
        }
        /*
        *   If we compare objects like
        *   root.getUserObject.equals(searched)
        *   Even if we have same object the if won't go through - references don't match (Possible from EditorUtils object creation)
        *   Maybe there is better way.
         */
        if (searched.toString().equals(root.getUserObject().toString()))
        {
            return root;
        }
        for (int i = 0; i < root.getChildCount(); ++i)
        {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
            DefaultMutableTreeNode found = findNode((DefaultMutableTreeNode) child, searched);
            if (found != null)
            {
                return found;
            }
        }
        return null;
    }
}
