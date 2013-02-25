package com.salvador.pages;

import com.salvador.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 14/02/13
 * Time: 12:09
 */
public class PageManagerTest {

    PageManager pageManager;
    String home;

    @Before
    public void setUp() {
        pageManager = new PageManager();
        home = TestUtils.getTestResourcesDirectory() + "home" + File.separator;
    }

    @Test
    public void testGetParentPathOneItem() {
        final String parentPath = pageManager.getParentPath("http://localhost:8080/test/another");
        assertThat(parentPath,is("another" + File.separator));
    }

    @Test
    public void testGetParentPathOneItemWithSessionId() {
        final String parentPath = pageManager.getParentPath("http://localhost:8080/test/Page1;jsessionid=806D59DA73ED925B1121203753865AD4");
        assertThat(parentPath,is("Page1" + File.separator));
    }

    @Test
    public void testGetParentPathMultipleItem() {
        final String parentPath = pageManager.getParentPath("http://localhost:8080/test/another/level/is/here");
        assertThat(parentPath,is("another" + File.separator + "level" + File.separator + "is" + File.separator + "here" + File.separator));
    }

    @Test
    public void testGetPage() throws IOException {
        final Page page1 = pageManager.getPage(home, "page1");
        assertThat(page1.getChildren().size(),is(1));
        assertThat(page1.getName(),is("page1"));
    }

    @Test
    public void testGetRootPage() throws IOException {
        final Page root = pageManager.getPage(home, "");
        assertThat(root.getName(),is(""));
        assertThat(root.isRootPage(),is(true));
    }

    @Test
    public void testGetPages() throws IOException {
        final Page root = pageManager.getPage(home, "");
        final List<Page> pages = pageManager.getPages(root);
        assertThat(pages.size(),is(1));
        assertThat(pages.get(0).getName(),is("page1"));
    }

    @Test
    public void testSaveNewPage() throws IOException {
        Page page = new Page();
        page.setName("NewPage1");
        pageManager.save(home,page);

        final Page loadedPage = pageManager.getPage(home, "NewPage1");
        assertThat(loadedPage.getName(),is("NewPage1"));

        pageManager.delete(loadedPage);
    }

    @Test
    public void testUpdatePage() throws IOException {
        Page page = new Page();
        page.setName("NewPage1");
        pageManager.save(home,page);

        final Page loadedPage = pageManager.getPage(home, "NewPage1");
        assertThat(loadedPage.getName(),is("NewPage1"));

        loadedPage.setName("Updated");
        pageManager.update(home,loadedPage);

        final Page updatedPage = pageManager.getPage(home, "Updated");
        assertThat(loadedPage.getName(),is("Updated"));

        pageManager.delete(updatedPage);
    }

    @Test
    public void testGetPageNameFromPath() {
        assertThat(pageManager.getPageNameFromPath("test\\Page1"), is("Page1"));
        assertThat(pageManager.getPageNameFromPath(""), is(""));
        assertThat(pageManager.getPageNameFromPath("page1"), is("page1"));
    }
}
