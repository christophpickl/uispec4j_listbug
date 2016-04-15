package uispec4j.bug;


import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

public class MainClass {

    public static void main(String[] args) {
        final JList<String> list = new JList<>(new String[] { "entry1", "entry2" });
        list.setName("list");
        list.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                showPopup(e);
            }
            @Override public void mouseReleased(MouseEvent e) {
                showPopup(e);
            }
            private void showPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    // this works, but this method is suboptimal as right click on blank space below last entry is considered as clicked on last entry
//                    int index = list.locationToIndex(e.getPoint());
                    int index = locationToIndexBasedOnBounds(list, e.getPoint());

                    if (index == -1) {
                        return;
                    }
                    String element = list.getModel().getElementAt(index);
                    System.out.println(">>> Right clicked on: " + element);

                    JPopupMenu popup = new JPopupMenu();
                    JMenuItem popupItem = new JMenuItem("Popup for " + element + "");
                    popupItem.addActionListener(e1 -> System.out.println(">>> Popup item clicked for element: " + element));
                    popup.add(popupItem);
                    popup.show(list, e.getPoint().x, e.getPoint().y);
                }
            }
        });

        JFrame frame = new JFrame();
        frame.getContentPane().add(new JScrollPane(list));
        frame.pack();
        frame.setVisible(true);
    }

    // this method is more precise as it checks the cell bounds
    // works when executed normally, but fails when executing with uispec4j!
    private static int locationToIndexBasedOnBounds(JList<String> list, Point point) {
        for (int i = 0; i < list.getModel().getSize(); i++) {
            Rectangle bounds = list.getCellBounds(i, i);
            System.out.println(bounds);

            // when running directly:
//            java.awt.Rectangle[x=0,y=0,width=76,height=17]
//            java.awt.Rectangle[x=0,y=17,width=76,height=17]

            // when running with uispec4j:
//            java.awt.Rectangle[x=0,y=0,width=50,height=-1]
//            java.awt.Rectangle[x=0,y=-1,width=50,height=-1]

            if (point.y <= bounds.y + bounds.height) {
                System.out.println(">>> Found index (" + i + ") for pont: " + point);
                return i;
            }
        }
        System.out.println(">>> Not found index for pont: " + point);
        return -1;
    }
}
