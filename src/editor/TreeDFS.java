package editor;

import javax.swing.tree.DefaultMutableTreeNode;

public class TreeDFS {
    public static DefaultMutableTreeNode findNode(DefaultMutableTreeNode root, Object searched)
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
