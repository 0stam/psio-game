package display;

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
                Tile[] tilesInCategory = GameManager.getInstance().getEditor().getConnectableTilesInCategory(tile);
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
        public void refresh(Tile[] connectedTiles) {
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
                    GameManager.getInstance().onEvent(new event.ConnectionDeletedEvent(from, to));
                }
            }
        });

        emittersContainer.tree.addTreeSelectionListener(treeSelectionEvent -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeSelectionEvent.getPath().getLastPathComponent();
            emittersContainer.setLastSelectedNode(node);
            // Check if the node is a tile and thus has connections
            if (node.getUserObject() instanceof Tile) {
                Tile tile = (Tile) node.getUserObject();
                Tile[] connectedTiles = GameManager.getInstance().getEditor().getTileConnections(tile);
                connectablesContainer.refresh(connectedTiles);
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

// FIXME: this class is just one big placeholder
//public class ConnectionsPalette extends JPanel {
//    JScrollPane scrollPane;
//    JPanel container;
//
//    public ConnectionsPalette() {
//        setPreferredSize(new Dimension(0, 200));
//        setLayout(new BorderLayout());
//
//        scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
//        container = new JPanel();
//        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
//
//        scrollPane.setViewportView(container);
//        add(scrollPane, BorderLayout.CENTER);
//
//        refresh();
//    }
//
//    class ConnectionListener implements ItemListener {
//        private Button from;
//        private Door to;
//
//        public ConnectionListener(Button from, Door to) {
//            this.from = from;
//            this.to = to;
//        }
//
//        @Override
//        public void itemStateChanged(ItemEvent e) {
//            if (e.getStateChange() == ItemEvent.SELECTED) {
//                GameManager.getInstance().onEvent(new ConnectionCreatedEvent(from, to));
//            } else {
//                GameManager.getInstance().onEvent(new ConnectionDeletedEvent(from, to));
//            }
//        }
//    }
//
//    public void refresh() {
//        Map map = GameManager.getInstance().getMap();
//
//        container.removeAll();
//
//        for (int x = 0; x < map.getWidth(); x++) {
//            for (int y = 0; y < map.getHeight(); y++) {
//                Button button;
//
//                if (map.getBottomLayer(x, y) instanceof Button) {
//                    button = (Button) map.getBottomLayer(x, y);
//                } else if (map.getUpperLayer(x, y) instanceof Button) {
//                    button = (Button) map.getUpperLayer(x, y);
//                } else {
//                    continue;
//                }
//
//                JLabel label = new JLabel("Button (" + x + ", " + y + ")");
//                label.setFont(new Font("Arial", Font.PLAIN, 30));
//                container.add(label);
//
//                for (int i = 0; i < map.getWidth(); i++) {
//                    for (int j = 0; j < map.getHeight(); j++) {
//                        Door door;
//
//                        if (map.getBottomLayer(i, j) instanceof Door) {
//                            door = (Door) map.getBottomLayer(i, j);
//                        } else if (map.getUpperLayer(i, j) instanceof Door) {
//                            door = (Door) map.getUpperLayer(i, j);
//                        } else {
//                            continue;
//                        }
//
//                        JCheckBox checkBox = new JCheckBox("Door (" + i + ", " + j + ")");
//                        checkBox.addItemListener(new ConnectionListener(button, door));
//                        checkBox.setFont(new Font("Arial", Font.PLAIN, 25));
//                        checkBox.setSelected(button.hasObserver(door));
//                        container.add(checkBox);
//                    }
//                }
//            }
//        }
//
//        revalidate();
//    }
//}
