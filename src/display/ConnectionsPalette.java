package display;

import connectableinterface.Connectable;
import enums.ConnectableTile;
import enums.EditableTile;
import enums.Graphics;
import gamemanager.GameManager;
import tile.Tile;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

public class ConnectionsPalette extends JPanel {
    EmittersContainer emittersContainer;
    ConnectablesContainer connectablesContainer;

    /**
     * A JPanel for the tree of tiles we can connect other tiles to
     */
    class EmittersContainer extends JPanel {
        JTree tree;
        JLabel label;
        DefaultMutableTreeNode lastSelectedNode;

        // Set the graphics of the tree
        class MyTreeCellRenderer extends DefaultTreeCellRenderer {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                label = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
                if (parentNode != null && parentNode.isRoot()) {
                    // Apply custom rendering only to immediate children of the root
                    if (value.toString().equals("root")) {
                        return label;
                    }
                    label.setIcon(new ImageIcon(GraphicsHashtable.getInstance().getImages().get(EditableTile.valueOf(value.toString()).graphics)));
                    label.setFont(getFont().deriveFont(Font.BOLD));
                }
                else {
                    label.setIcon(null);
                }
                if (node.getUserObject() instanceof Tile) {
                    Tile tile = (Tile) node.getUserObject();
                    label.setText("x: " + tile.getX() + ", y: " + tile.getY());
                }
                return label;
            }
        }

        public EmittersContainer() {
            setLayout(new BorderLayout());

            DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");

            for (ConnectableTile tile : ConnectableTile.values()) {
                if (tile.name().equals("DEFAULT")) {
                    continue;
                }
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(tile.name());
                root.add(node);


            }

            tree = new JTree(root);
            tree.setCellRenderer(new MyTreeCellRenderer());
            tree.setRootVisible(false);
            add(new JScrollPane(tree));
        }
        // Helper method to find a node with a given user object
        private DefaultMutableTreeNode findNode(DefaultMutableTreeNode parent, Object userObject) {
            Enumeration<TreeNode> children = parent.children();
            while (children.hasMoreElements()) {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
                if (child.getUserObject().equals(userObject)) {
                    return child;
                }
            }
            return null;
        }

        public void setLastSelectedNode(DefaultMutableTreeNode node) {
            System.out.println("last selected node: " + node);
            lastSelectedNode = node;
        }
        public DefaultMutableTreeNode getLastSelectedNode() {
            return lastSelectedNode;
        }

        public void refresh() {
            //setLayout(new BorderLayout());
            for (ConnectableTile tile : ConnectableTile.values()) {
                List<Tile> tilesInCategory = GameManager.getInstance().getEditor().getTilesInConnectableCategory(tile);
                DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();

                if (tile.name().equals("DEFAULT")) {
                    continue;
                }

                DefaultMutableTreeNode categoryNode = findNode(root, tile.name());

                if (categoryNode == null) {
                    // The category node doesn't exist, create it
                    categoryNode = new DefaultMutableTreeNode(tile.name());
                    root.add(categoryNode);
                } else {
                    // Clear existing child nodes
                    categoryNode.removeAllChildren();
                }

                for (Tile tileInCategory : tilesInCategory) {
                    DefaultMutableTreeNode tileNode = new DefaultMutableTreeNode(tileInCategory);
                    categoryNode.add(tileNode);
                }
            }
            ((DefaultTreeModel) tree.getModel()).nodeStructureChanged((DefaultMutableTreeNode) tree.getModel().getRoot());

            revalidate();
        }

    }

    /**
     * A JPanel for the list of tiles connected to the selected tile
     */
    class ConnectablesContainer extends JPanel {

        DefaultListModel listModel;
        JList list;
        JLabel label;

        class MyListCellRenderer extends DefaultListCellRenderer {

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setIcon(new ImageIcon(GraphicsHashtable.getInstance().getImage(Graphics.MINUS)));

                if (value instanceof Tile) {
                    Tile tile = (Tile) value;
                    label.setText("x: " + tile.getX() + ", y: " + tile.getY());
                }
                return label;
            }
        }

        public ConnectablesContainer() {
            setLayout(new BorderLayout());

            list = new JList();
            list.add(new JLabel("Select an emitter"));

            list.setCellRenderer(new MyListCellRenderer());
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            add(new JScrollPane(list));
        }
        public void refresh(List<Tile> connectedTiles) {
            listModel = new DefaultListModel<>();

            if (connectedTiles != null) {
                for (Tile tile : connectedTiles) {
                    listModel.addElement(tile);
                }
            }

            list.setModel(listModel);

            revalidate();
        }
    }

    public ConnectionsPalette() {
        setLayout(new GridLayout(1, 2));

        emittersContainer = new EmittersContainer();
        connectablesContainer = new ConnectablesContainer();

        add(emittersContainer, BorderLayout.WEST);
        add(connectablesContainer, BorderLayout.EAST);

        connectablesContainer.list.addListSelectionListener(listSelectionEvent -> {
            if (!listSelectionEvent.getValueIsAdjusting()) {
                int selectedIndex = connectablesContainer.list.getSelectedIndex();
                if (selectedIndex != -1) {
                    Tile from = (Tile) connectablesContainer.list.getSelectedValue();
                    Tile to = (Tile) emittersContainer.getLastSelectedNode().getUserObject();
                    connectablesContainer.listModel.remove(selectedIndex);
                    GameManager.getInstance().onEvent(new event.ConnectionDeletedEvent(to)); //TODO: idk if it should be 'from' or 'to'
                }
            }
        });

        emittersContainer.tree.addTreeSelectionListener(treeSelectionEvent -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeSelectionEvent.getPath().getLastPathComponent();
            emittersContainer.setLastSelectedNode(node);
            // Check if the node is a tile and thus has connections
            if (node.getUserObject() instanceof Tile) {
                List<Tile> tileConnections = ((Connectable) node.getUserObject()).getConnections();
                connectablesContainer.refresh(tileConnections);
            }

            // Store the expanded state before refreshing the tree
            HashSet<TreePath> expandedPaths = new HashSet<>();
            for (int i = 0; i < emittersContainer.tree.getRowCount(); i++) {
                TreePath path = emittersContainer.tree.getPathForRow(i);
                if (emittersContainer.tree.isExpanded(path)) {
                    expandedPaths.add(path);
                }
            }

            refresh();

            // Restore the expanded state after refreshing the tree
            for (TreePath path : expandedPaths) {
                emittersContainer.tree.expandPath(path);
            }


        });
    }

    public void refresh() {

        emittersContainer.refresh();
        revalidate();
    }

}
