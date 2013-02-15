package com.salvador.pages;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

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

    @Before
    public void setUp() {
        pageManager = new PageManager();
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
}
