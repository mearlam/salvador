package com.salvador.pages;

import com.salvador.configuration.Configuration;
import com.salvador.items.ItemManager;
import com.salvador.notes.PageNote;
import com.salvador.utils.FacesUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.primefaces.model.MenuModel;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by IntelliJ IDEA.
 * User: mearlam
 * Date: 21/02/13
 * Time: 13:22
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(FacesUtils.class)
public class PageBeanTest {

    PageBean pageBean;

    @Mock
    PageManager pageManager;

    @Mock
    ItemManager itemManager;

    @Mock
    Configuration configuration;

    @Mock
    Page page;

    @Mock
    PageContent pageContent;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        pageBean = new PageBean();
        pageBean.configuration = configuration;
        pageBean.pageManager = pageManager;
        pageBean.pageContent = pageContent;
        pageBean.itemManager = itemManager;

        mockStatic(FacesUtils.class);
        when(FacesUtils.getDestinationPage()).thenReturn("page1");
        when(configuration.getHome()).thenReturn("home");
        when(pageContent.getCurrentPage()).thenReturn(page);
        when(page.getPath()).thenReturn("");
        when(pageManager.getPage("home", "page1")).thenReturn(page);
    }

    @Test
    public void testInitPage() throws IOException {
        pageBean.initPage();
        verify(pageContent).setCurrentPage(page);
    }

    @Test
    public void testGetMenuModel() throws IOException {
        when(pageManager.getParentPath("page1", "/")).thenReturn("test/page1/page2/");

        final MenuModel model = pageBean.getMenuModel();
        // should be home/test/page1/page2 - can't figure out how to get the items out
        assertThat(model.getContents().size(),is(4));
    }

    @Test
     public void testCreatePageSingle() throws IOException {
        Page newPage = new Page();
        newPage.setName("testPage1");
        pageBean.setPage(newPage);

        when(pageManager.getPage(configuration.getHome(), "testPage1")).thenReturn(new Page());

        pageBean.createPage();
        verify(pageManager).save(configuration.getHome(), newPage);
    }

    @Test
    public void testCreatePageSingleDuplicate() throws IOException {
        Page newPage = new Page();
        newPage.setName("testPage1");
        pageBean.setPage(newPage);

        when(pageManager.getPage(configuration.getHome(),"testPage1")).thenReturn(newPage);

        pageBean.createPage();
        verify(pageManager,never()).save(configuration.getHome(),newPage);
    }

    @Test
    public void testCreatePageBulk() throws IOException {
        Page newPage = new Page();
        pageBean.setPage(newPage);
        pageBean.setNames("page1,page2");
        when(pageManager.getPage(configuration.getHome(), "page1")).thenReturn(new Page());
        when(pageManager.getPage(configuration.getHome(), "page2")).thenReturn(new Page());

        pageBean.createPage();
        verify(pageManager,times(2)).save(any(String.class), any(Page.class));
    }

    @Test
    public void testCreatePageBulkDuplicate() throws IOException {
        Page page1 = new Page();
        page1.setName("page1");
        Page page2 = new Page();
        page2.setName("page2");
        pageBean.setPage(page1);
        pageBean.setNames("page1,page2");
        when(pageManager.getPage(configuration.getHome(), "page1")).thenReturn(page1);
        when(pageManager.getPage(configuration.getHome(), "page2")).thenReturn(page2);

        pageBean.createPage();
        verify(pageManager,never()).save(any(String.class), any(Page.class));
    }

    @Test
    public void testHandleClose() throws IOException {
        Page page = new Page();
        PageNote note = new PageNote();
        note.setId("note1");
        page.getItems().add(note);
        pageBean.setPage(page);
        pageBean.handleClose("note1");
        assertThat(page.getItems().size(),is(0));
        verify(pageManager).save(configuration.getHome(), page);
    }

    @Test
    public void testHandleEnable() throws IOException {
        Page page = new Page();
        PageNote note = new PageNote();
        note.setId("note1");
        note.setEnabled(false);
        page.getItems().add(note);
        pageBean.setPage(page);
        pageBean.handleEnable("note1");
        assertThat(note.isEnabled(),is(true));
        verify(pageManager).save(configuration.getHome(), page);
    }

    @Test
    public void testHandleDisable() throws IOException {
        Page page = new Page();
        PageNote note = new PageNote();
        note.setId("note1");
        note.setEnabled(true);
        page.getItems().add(note);
        pageBean.setPage(page);
        pageBean.handleDisable("note1");
        assertThat(note.isEnabled(),is(false));
        verify(pageManager).save(configuration.getHome(), page);
    }

    @Test
    public void testHandleMoveUp() throws IOException {
        Page page = new Page();
        pageBean.setPage(page);
        pageBean.handleMoveUp("note");
        verify(itemManager).moveItemUp(page,"note");
        verify(pageManager).save(configuration.getHome(), page);
    }

    @Test
    public void testHandleMoveDown() throws IOException {
        Page page = new Page();
        pageBean.setPage(page);
        pageBean.handleMoveDown("note");
        verify(itemManager).moveItemDown(page,"note");
        verify(pageManager).save(configuration.getHome(), page);
    }
}
