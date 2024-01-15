package display;

import IO.IOManager;
import connectableinterface.Connectable;
import editor.EditorUtils;
import enums.ConnectableTile;
import enums.EditableTile;
import enums.Graphics;
import event.display.ConnectableTileSelectedEvent;
import event.display.ConnectionDeletedEvent;
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

    JSplitPane splitPane;

    /**
     * A JPanel for the tree of tiles we can connect other tiles to
     */
    class EmittersContainer extends JPanel {
        JTree tree;
        JLabel label;

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
                    //label.setFont(getFont().deriveFont(Font.BOLD));
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
            tree.setShowsRootHandles(true);
            add(new JScrollPane(tree));
        }
        @Override
        public void paintComponent(java.awt.Graphics g)
        {

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

        public void refresh() {
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
                }

                Enumeration<TreeNode> currentNodes = categoryNode.children();
                while (currentNodes.hasMoreElements()) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) currentNodes.nextElement();
                    if (!tilesInCategory.contains(node.getUserObject())) {
                        categoryNode.remove(node);
                    }
                }

                for (Tile tileInCategory : tilesInCategory) {
                    DefaultMutableTreeNode tileNode = findNode(categoryNode, tileInCategory);

                    if (tileNode == null) {
                        tileNode = new DefaultMutableTreeNode(tileInCategory);
                        categoryNode.add(tileNode);
                    }
                }
            }
            ((DefaultTreeModel) tree.getModel()).nodeStructureChanged((DefaultMutableTreeNode) tree.getModel().getRoot());
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
                    label.setText(EditorUtils.objectToEditable(tile).name + " (x: " + tile.getX() + ", y: " + tile.getY() + ")");
                }
                return label;
            }

        }

        public ConnectablesContainer() {
            setLayout(new BorderLayout());

            list = new JList();
            list.setCellRenderer(new MyListCellRenderer());
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            add(new JScrollPane(list));
        }

        public void refresh(HashSet<Tile> connectedTiles) {
            listModel = new DefaultListModel<>();

            if (connectedTiles != null) {
                for (Tile tile : connectedTiles) {
                    listModel.addElement(tile);
                }
            }
            list.setModel(listModel);
        }
    }

    public ConnectionsPalette() {
        setPreferredSize(new Dimension(0, 200));
        setLayout(new BorderLayout());

        emittersContainer = new EmittersContainer();
        emittersContainer.setMinimumSize(new Dimension(300, 0));
        connectablesContainer = new ConnectablesContainer();
        connectablesContainer.setMinimumSize(new Dimension(300, 0));

        emittersContainer.tree.setRowHeight(30);
        emittersContainer.tree.setFont(new Font("Arial", Font.PLAIN, 25));

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        splitPane.setResizeWeight(0.4f);

        splitPane.setLeftComponent(emittersContainer);
        splitPane.setRightComponent(connectablesContainer);

        this.add(splitPane, BorderLayout.CENTER);

        connectablesContainer.list.addListSelectionListener(listSelectionEvent -> {
            if (!listSelectionEvent.getValueIsAdjusting()) {
                int selectedIndex = connectablesContainer.list.getSelectedIndex();
                if (selectedIndex != -1) {
                    Tile from = (Tile) connectablesContainer.list.getSelectedValue();
                    connectablesContainer.listModel.remove(selectedIndex);
                    EditorInputHandler inputHandler = (EditorInputHandler) IOManager.getInstance().getInputHandler();
                    inputHandler.onEvent(new ConnectionDeletedEvent(from));
                }
            }
        });

        HashSet<TreePath> expandedPaths = new HashSet<>();

        emittersContainer.tree.addTreeSelectionListener(treeSelectionEvent -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeSelectionEvent.getPath().getLastPathComponent();
            // Check if the node is a tile and thus has connections
            if (node.getUserObject() instanceof Connectable) {
                EditorInputHandler inputHandler = (EditorInputHandler) IOManager.getInstance().getInputHandler();

                HashSet<Tile> tileConnections = ((Connectable) node.getUserObject()).getConnections();
                inputHandler.onEvent(new ConnectableTileSelectedEvent((Tile) node.getUserObject()));
                connectablesContainer.refresh(tileConnections);
                //emittersContainer.tree.setSelectionPath(new TreePath(node));

                // Store the expanded state before refreshing the tree
                for (int i = 0; i < emittersContainer.tree.getRowCount(); i++) {
                    TreePath path = emittersContainer.tree.getPathForRow(i);
                    emittersContainer.tree.expandPath(path);
                    if (emittersContainer.tree.isExpanded(path)) {
                        expandedPaths.add(path);
                    }
                }

                // Restore the expanded state after refreshing the tree
                for (TreePath path : expandedPaths) {
                    emittersContainer.tree.expandPath(path);
                }
            }
        });


    }

    public void refresh() {
        emittersContainer.refresh();
    }
}
