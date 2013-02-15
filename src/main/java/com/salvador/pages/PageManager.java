package com.salvador.pages;

import com.salvador.configuration.Configuration;
import com.salvador.utils.FacesUtils;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PageManager {

    public static final String TEST_FOLDER = "test";

    static final Logger log = LoggerFactory.getLogger(PageManager.class);

    XStream xStream;

    @Inject
    Configuration configuration;

    public PageManager() {
        xStream = new XStream();
    }

    public Page getPageFromPageRequestParameter() throws IOException {
        final Page page =  getPage(configuration.getPagesHome(),FacesUtils.getParam("page"));
        if(page != null) {
            setChildren(page);
        }

        return page;
    }

    public void setChildren(Page page) throws IOException {
        List<Page> pages = getPages(page);
        page.setChildren(pages);

        for(Page childPage : pages) {
            setChildren(childPage);
        }
    }

    public Page getPage(String home, String pagePath) throws IOException {

        Page page = new Page();

        if (pagePath != null) {
            final String filePagePath = home + getParentPath(pagePath);
            File directory = new File(filePagePath);
            File contentFile = new File(directory.getAbsolutePath() + File.separator + "content.xml");

            if (contentFile.exists()) {
                final FileInputStream inputStream = new FileInputStream(contentFile.getAbsolutePath());
                page = (Page) xStream.fromXML(inputStream);
                page.setFullPath(filePagePath);
                setChildren(page);
                inputStream.close();
            }
        }

        return page;
    }

    public List<Page> getPages(Page rootPage) throws IOException {
        return getPages(rootPage.getFullPath());
    }

    public List<Page> getPages(String root) throws IOException {
        List<Page> pages = new ArrayList<Page>();

        log.debug("Scanning {} for pages", root);
        File scanDirectory = new File(root);

        if (scanDirectory.exists()) {
            //noinspection ConstantConditions
            for (File file : scanDirectory.listFiles()) {
                if (file.isDirectory()) {
                    final FileInputStream inputStream = new FileInputStream(file.getAbsolutePath()
                            + File.separator + "content.xml");
                    Page page = (Page) xStream.fromXML(inputStream);
                    page.setFullPath(file.getAbsolutePath());
                    pages.add(page);
                    inputStream.close();
                }
            }
        }

        return pages;
    }

    public void save(String home, Page page) throws IOException {
        final String xml = xStream.toXML(page);
        final String parentPath = page.getPath();
        final String cleanPageName = getCleanPageName(page);
        final String pageDirectoryPath = home + parentPath + cleanPageName;
        log.debug("Creating new page directory {}", pageDirectoryPath);
        FileUtils.forceMkdir(new File(pageDirectoryPath));
        final String pageFileName = home + parentPath + cleanPageName + File.separator + "content.xml";
        log.debug("Saving new page to {}", pageFileName);
        FileUtils.writeStringToFile(new File(pageFileName), xml);
    }

    public String getParentPath(String referer) {
        return getParentPath(referer, File.separator);
    }

    public String getParentPath(String referer, String separator) {
        String path = "";

        if (referer.contains(PageManager.TEST_FOLDER)) {
            final String[] parts = referer.split(PageManager.TEST_FOLDER + "/");
            final String fullPath = parts[parts.length - 1];
            path = fullPath.replace("/", separator);

            if (!path.endsWith(separator)) {
                path += separator;
            }
        } else {
            // if we have http then that means we are on the root page
            if (referer.contains("http")) {
                path = "";
            } else {
                path = referer.replace("/", separator);
            }
        }

        return path;
    }

    private String getCleanPageName(Page page) {
        return page.getName().replace(" ", "-");
    }
}
