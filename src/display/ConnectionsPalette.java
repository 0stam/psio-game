package display;

import event.LegacyConnectionCreatedEvent;
import event.LegacyConnectionDeletedEvent;
import gamemanager.GameManager;
import map.Map;
import tile.Button;
import tile.Door;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

// FIXME: this class is just one big placeholder
public class ConnectionsPalette extends JPanel {
    JScrollPane scrollPane;
    JPanel container;

    public ConnectionsPalette() {
        setPreferredSize(new Dimension(0, 200));
        setLayout(new BorderLayout());

        scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

        scrollPane.setViewportView(container);
        add(scrollPane, BorderLayout.CENTER);

        refresh();
    }

    class ConnectionListener implements ItemListener {
        private Button from;
        private Door to;

        public ConnectionListener(Button from, Door to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                GameManager.getInstance().onEvent(new LegacyConnectionCreatedEvent(from, to));
            } else {
                GameManager.getInstance().onEvent(new LegacyConnectionDeletedEvent(from, to));
            }
        }
    }

    public void refresh() {
        Map map = GameManager.getInstance().getMap();

        container.removeAll();

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Button button;

                if (map.getBottomLayer(x, y) instanceof Button) {
                    button = (Button) map.getBottomLayer(x, y);
                } else if (map.getUpperLayer(x, y) instanceof Button) {
                    button = (Button) map.getUpperLayer(x, y);
                } else {
                    continue;
                }

                JLabel label = new JLabel("Button (" + x + ", " + y + ")");
                label.setFont(new Font("Arial", Font.PLAIN, 30));
                container.add(label);

                for (int i = 0; i < map.getWidth(); i++) {
                    for (int j = 0; j < map.getHeight(); j++) {
                        Door door;

                        if (map.getBottomLayer(i, j) instanceof Door) {
                            door = (Door) map.getBottomLayer(i, j);
                        } else if (map.getUpperLayer(i, j) instanceof Door) {
                            door = (Door) map.getUpperLayer(i, j);
                        } else {
                            continue;
                        }

                        JCheckBox checkBox = new JCheckBox("Door (" + i + ", " + j + ")");
                        checkBox.addItemListener(new ConnectionListener(button, door));
                        checkBox.setFont(new Font("Arial", Font.PLAIN, 25));
                        checkBox.setSelected(button.hasObserver(door));
                        container.add(checkBox);
                    }
                }
            }
        }

        revalidate();
    }
}
