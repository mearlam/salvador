package com.salvador.pages;

import com.salvador.configuration.Configuration;
import com.salvador.utils.FacesUtils;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
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
    private static final String PAGE_FOLDER = "pages";

    XStream xStream;

    @Inject
    Configuration configuration;

    public PageManager() {
        xStream = new XStream();
    }

    public Page getPageFromPageRequestParameter() throws IOException {
        return getPage(configuration.getHome(), FacesUtils.getParam("page"));
    }

    public void setChildren(Page page) throws IOException {
        List<Page> pages = getPages(page);
        page.setChildren(pages);

        for (Page childPage : pages) {
            setChildren(childPage);
        }
    }

    public Page getPage(String home, String pagePath) throws IOException {
        Page page = new Page();
        String filePagePath = home + File.separator + PAGE_FOLDER + File.separator;

        if (StringUtils.isNotEmpty(pagePath)) {
            filePagePath += getParentPath(pagePath);
        }

        log.debug("looking for page at {}", filePagePath);
        File directory = new File(filePagePath);
        File contentFile = new File(directory.getAbsolutePath() + File.separator + "content.xml");

        if (contentFile.exists()) {
            final FileInputStream inputStream = new FileInputStream(contentFile.getAbsolutePath());
            page = (Page) xStream.fromXML(inputStream);
            page.setName(getPageNameFromPath(directory.getAbsolutePath()));
            inputStream.close();
        } else {
            page.setRootPage(true);
        }

        page.setFullPath(filePagePath);
        setChildren(page);

        return page;
    }

    protected String getPageNameFromPath(final String pagePath) {
        if(StringUtils.isNotEmpty(pagePath) && pagePath.contains(File.separator)) {
            return pagePath.substring(pagePath.lastIndexOf(File.separator) + 1);
        }else {
            return pagePath;
        }
    }

    protected List<Page> getPages(Page rootPage) throws IOException {
        return getPages(rootPage.getFullPath());
    }

    protected List<Page> getPages(String root) throws IOException {
        List<Page> pages = new ArrayList<Page>();

        log.debug("Scanning for pages...");
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
                    page.setName(getPageNameFromPath(file.getAbsolutePath()));
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
        final String pageDirectoryPath = home + PAGE_FOLDER + File.separator + parentPath + cleanPageName;
        log.debug("Creating new page directory {}", pageDirectoryPath);
        FileUtils.forceMkdir(new File(pageDirectoryPath));
        final String pageFileName = home + File.separator + PAGE_FOLDER + File.separator +
                parentPath + cleanPageName + File.separator + "content.xml";
        log.debug("Saving new page to {}", pageFileName);
        FileUtils.writeStringToFile(new File(pageFileName), xml);
    }

    public void update(String home, Page page) throws IOException {
        final String xml = xStream.toXML(page);
        final String parentPath = page.getPath();
        final String cleanPageName = getCleanPageName(page);
        final String pageDirectoryPath = home + PAGE_FOLDER + File.separator + parentPath + cleanPageName;
        String pageFileName = pageDirectoryPath + File.separator + "content.xml";
        log.debug("Moving page directory from {} to {}", page.getFullPath(), pageDirectoryPath);
        FileUtils.moveDirectory(new File(page.getFullPath()), new File(pageDirectoryPath));
        log.debug("Saving new page to {}", pageFileName);
        FileUtils.writeStringToFile(new File(pageFileName), xml);
    }

    public void delete(Page page) throws IOException {
        log.debug("Deleting page {}", page.getFullPath());
        FileUtils.forceDelete(new File((page.getFullPath())));
    }

    public String getParentPath(String referer) {
        return getParentPath(referer, File.separator);
    }

    public String getParentPath(String referer, String separator) {
        String path;

        if (referer.contains(PageManager.TEST_FOLDER)) {
            final String[] parts = referer.split(PageManager.TEST_FOLDER + "/");
            final String fullPath = parts[parts.length - 1];
            path = fullPath.replace("/", separator);

            int semi = path.indexOf(";");

            if (semi > 0) {
                path = path.substring(0, semi);
            }

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
