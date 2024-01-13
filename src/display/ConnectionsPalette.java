package display;

import enums.ConnectableTile;
import enums.EditableTile;
import enums.Graphics;
import gamemanager.GameManager;
import tile.Tile;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class ConnectionsPalette extends JPanel {
    EmittersContainer emittersContainer;
    ConnectablesContainer connectablesContainer;
    ConnectableTile selectedTile;

    class EmittersContainer extends JPanel {
        JTree tree;

        class MyTreeCellRenderer extends DefaultTreeCellRenderer {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
                if (value.toString().equals("root")) {
                    return label;
                }
                label.setIcon(new ImageIcon(GraphicsHashtable.getInstance().getImages().get(EditableTile.valueOf(value.toString()).graphics)));
                label.setFont(getFont().deriveFont(Font.BOLD));

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
        
        public JTree getTree() {
            return tree;
        }

        public void refresh() {
            //setLayout(new BorderLayout());
            for (ConnectableTile tile : ConnectableTile.values()) {
                Tile[] tilesInCategory = GameManager.getInstance().getEditor().getConnectableTilesInCategory(tile);

                if (tile.name().equals("DEFAULT")) {
                    continue;
                }
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(tile.name());
                ((DefaultMutableTreeNode) tree.getModel().getRoot()).add(node);

                for (Tile tileInCategory : tilesInCategory) {
                    DefaultMutableTreeNode tileNode = new DefaultMutableTreeNode("x: " + tileInCategory.getX() + ", y: " + tileInCategory.getY());
                    node.add(tileNode);
                }
            }

            revalidate();
        }

    }

    class ConnectablesContainer extends JPanel {

        DefaultListModel listModel;
        JList list;

        class MyListCellRenderer extends DefaultListCellRenderer {

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setIcon(new ImageIcon(GraphicsHashtable.getInstance().getImage(Graphics.MINUS)));

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
        public void refresh(EditableTile[] connectableList) {

            listModel = new DefaultListModel<>();
            for (EditableTile allowedTile : connectableList) {
                listModel.addElement(allowedTile.name());
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
                    connectablesContainer.listModel.remove(selectedIndex);
                }
            }
        });

        emittersContainer.tree.addTreeSelectionListener(treeSelectionEvent -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeSelectionEvent.getPath().getLastPathComponent();

            if (node != null) {
                ConnectableTile tile = ConnectableTile.valueOf(node.getUserObject().toString());
                connectablesContainer.refresh(tile.allowedTiles);
            }

            refresh();
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
