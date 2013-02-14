package com.salvador.pages;

import com.salvador.configuration.Configuration;
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

    @Inject
    Configuration configuration;

    XStream xStream;

    public PageManager() {
        xStream = new XStream();
    }

    public Page getPage(String pagePath) throws IOException {

        Page page = null;

        if (pagePath != null) {
            final String filePagePath = configuration.getPagesHome() + getParentPath(pagePath);
            File directory = new File(filePagePath);
            File contentFile = new File(directory.getAbsolutePath() + File.separator + "content.xml");

            if (contentFile.exists()) {
                final FileInputStream inputStream = new FileInputStream(contentFile.getAbsolutePath());
                page = (Page) xStream.fromXML(inputStream);
                inputStream.close();
            }
        }

        return page;
    }

    public List<Page> getPages(String root) throws IOException {
        List<Page> pages = new ArrayList<Page>();

        final String scanDirectoryPath = configuration.getPagesHome() + root;
        log.debug("Scanning {} for pages", scanDirectoryPath);
        File scanDirectory = new File(scanDirectoryPath);

        if (scanDirectory.exists()) {
            //noinspection ConstantConditions
            for (File file : scanDirectory.listFiles()) {
                if (file.isDirectory()) {
                    final FileInputStream inputStream = new FileInputStream(file.getAbsolutePath()
                            + File.separator + "content.xml");
                    Page page = (Page) xStream.fromXML(inputStream);
                    pages.add(page);
                    inputStream.close();
                }
            }
        }

        return pages;
    }

    public void save(Page page) throws IOException {
        final String xml = xStream.toXML(page);
        final String parentPath = page.getPath();
        final String cleanPageName = getCleanPageName(page);
        final String pageDirectoryPath = configuration.getPagesHome() + parentPath + cleanPageName;
        log.debug("Creating new page directory {}", pageDirectoryPath);
        FileUtils.forceMkdir(new File(pageDirectoryPath));
        final String pageFileName = configuration.getPagesHome() + parentPath + cleanPageName + File.separator + "content.xml";
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
        }else {
            path = referer.replace("/", separator);
        }

        return path;
    }

    private String getCleanPageName(Page page) {
        return page.getName().replace(" ", "-");
    }
}
