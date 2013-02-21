package com.salvador.items;

import com.salvador.notes.PageNote;
import com.salvador.pages.Page;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by IntelliJ IDEA.
 * User: mearlam
 * Date: 21/02/13
 * Time: 14:29
 */
public class ItemManagerTest {

    ItemManager itemManager;

    @Before
    public void setUp() {
        itemManager = new ItemManager();
    }

    @Test
    public void testMoveItemUp() {
        Page page = new Page();
        PageNote note1 = new PageNote();
        note1.setName("note1");
        page.getItems().add(note1);
        PageNote note2 = new PageNote();
        note2.setName("note2");
        page.getItems().add(note2);
        itemManager.moveItemUp(page,"note2");
        assertThat(page.getItems().get(0).getName(),is("note2"));
    }

    @Test
    public void testMoveItemDown() {
        Page page = new Page();
        PageNote note1 = new PageNote();
        note1.setName("note1");
        page.getItems().add(note1);
        PageNote note2 = new PageNote();
        note2.setName("note2");
        page.getItems().add(note2);
        itemManager.moveItemDown(page,"note1");
        assertThat(page.getItems().get(0).getName(),is("note2"));
    }
}
