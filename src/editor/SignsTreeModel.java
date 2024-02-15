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

	private DefaultMutableTreeNode findNodeByName(String nodeName) {
		//typ zniknal z Enumeration, ale kod jest bezpieczny wiec mozna
		@SuppressWarnings("rawtypes")
		Enumeration enumeration = ((DefaultMutableTreeNode)this.getRoot()).breadthFirstEnumeration();
		while (enumeration.hasMoreElements()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
			if (nodeName.equals(node.getUserObject().toString())) {
				return node;
			}
		}
		return null;
	}
	public void removeNode(String name)
	{
		this.removeNodeFromParent(findNodeByName(name));
	}

}
