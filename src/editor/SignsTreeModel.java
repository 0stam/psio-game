package editor;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.Enumeration;

public class SignsTreeModel extends DefaultTreeModel{
	//Look up EnemiesTreeModel.java for context
	public SignsTreeModel() {
		super(new DefaultMutableTreeNode("Signs"));
	}
}
