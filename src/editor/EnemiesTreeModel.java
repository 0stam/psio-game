package editor;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import java.util.Enumeration;

public class EnemiesTreeModel {
    private DefaultTreeModel defaultTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode("Smart entities"));
    private DefaultMutableTreeNode findNodeByName(String nodeName) {
        //typ zniknal z Enumeration, ale kod jest bezpieczny wiec mozna
        @SuppressWarnings("rawtypes")
        Enumeration enumeration = ((DefaultMutableTreeNode)defaultTreeModel.getRoot()).breadthFirstEnumeration();
        while (enumeration.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
            if (nodeName.equals(node.getUserObject().toString())) {
                return node;
            }
        }
        return null;
    }
    public void addNode(String name)
    {
        DefaultMutableTreeNode newEnemy = new DefaultMutableTreeNode(name);
        defaultTreeModel.insertNodeInto(newEnemy, (MutableTreeNode) defaultTreeModel.getRoot(), 0);
    }
    public void removeNode(String name)
    {
        defaultTreeModel.removeNodeFromParent(findNodeByName(name));
    }

    public DefaultTreeModel getDefaultTreeModel() {
        return defaultTreeModel;
    }

    public void setDefaultTreeModel(DefaultTreeModel treeModel) {
        this.defaultTreeModel = treeModel;
    }

}
