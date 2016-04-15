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

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.uispec4j.ListBox;
import org.uispec4j.MenuItem;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.interception.MainClassAdapter;
import org.uispec4j.interception.PopupMenuInterceptor;

@Test
public class ListPopupBug extends UISpecTestCase {

    static {
        System.setProperty("uispec4j.test.library", "testng");
    }

    @BeforeClass
    public void initUi() throws Exception {
        super.setUp();
        setAdapter(new MainClassAdapter(MainClass.class));
    }

    public void listPopup() {
        // >>> Not found index for pont: java.awt.Point[x=25,y=0]
        ListBox list = getMainWindow().getListBox("list");
        MenuItem popup = PopupMenuInterceptor.run(list.triggerRightClick(0));
        popup.getSubMenu("Popup for entry1").click();
    }

}
