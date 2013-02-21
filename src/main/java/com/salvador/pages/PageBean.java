package com.salvador.pages;

import com.salvador.configuration.Configuration;
import com.salvador.items.ItemManager;
import com.salvador.scenarios.Scenario;
import com.salvador.scenarios.ScenarioManager;
import com.salvador.spi.ViewScoped;
import com.salvador.utils.FacesUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 14/02/13
 * Time: 10:01
 */
@Named
@ViewScoped
public class PageBean implements Serializable {

    static final Logger log = LoggerFactory.getLogger(PageBean.class);

    private MenuModel model;

    // used for bulk add
    private String names;
    private Page page;
    private boolean editMode;

    @Inject
    transient PageContent pageContent;

    @Inject
    transient PageManager pageManager;

    @Inject
    transient Configuration configuration;

    @Inject
    transient ItemManager itemManager;

    @PostConstruct
    public void initPage() throws IOException {
        editMode = false;
        if (page == null) {
            page = pageManager.getPage(configuration.getHome(), FacesUtils.getDestinationPage());
            pageContent.setCurrentPage(page);
        }
    }

    public MenuModel getMenuModel() throws IOException {

        if (model == null) {
            model = new DefaultMenuModel();
            MenuItem home = new MenuItem();
            home.setValue("Home");
            home.setUrl("/");
            model.addMenuItem(home);

            final String requestedUri = FacesUtils.getDestinationPage();
            String path = "";

            if (requestedUri != null) {

                path = pageManager.getParentPath(requestedUri, "/");
                if (path != null) {
                    final String[] pages = path.split("/");
                    String currentPath = "/" + PageManager.TEST_FOLDER + "/";
                    for (String page : pages) {
                        currentPath += page + "/";
                        MenuItem item = new MenuItem();
                        item.setValue(page);
                        item.setUrl(currentPath);
                        model.addMenuItem(item);
                    }
                }
            }
        }

        return model;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Page getPage() throws IOException {
        return page;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public void createPage() throws IOException {

        final String referer = FacesUtils.getParam("referer");

        if (StringUtils.isNotEmpty(names)) {
            Page newPage = new Page();
            for (String pageName : names.split(",")) {
                if (!isDuplicate(pageName)) {
                    newPage.setName(pageName);
                    newPage.setPath(pageManager.getParentPath(referer, "/"));
                    pageManager.save(configuration.getHome(), newPage);
                }
            }
        } else if (StringUtils.isNotEmpty(page.getName())) {
            if (!isDuplicate(page.getName())) {
                page.setPath(pageManager.getParentPath(referer, "/"));
                pageManager.save(configuration.getHome(), page);
            }
        }

        FacesUtils.redirect("/" + PageManager.TEST_FOLDER + "/" + page.getPath());
    }

    private boolean isDuplicate(String pageName) {
        boolean isDuplicate = false;
        try {
            if (StringUtils.isNotEmpty(pageManager.getPage(configuration.getHome(),
                    pageContent.getCurrentPage().getPath() + pageName).getName())) {
                isDuplicate = true;
            }
        } catch (IOException e) {
            isDuplicate = false;
        }

        return isDuplicate;
    }

    public void handleClose(final String itemName) throws IOException {
        page.getItems().remove(page.getItem(itemName));
        pageManager.save(configuration.getHome(), page);
    }

    public void handleEnable(final String itemName) throws IOException {
        page.getItem(itemName).setEnabled(true);
        pageManager.save(configuration.getHome(), page);
    }

    public void handleDisable(final String itemName) throws IOException {
        page.getItem(itemName).setEnabled(false);
        pageManager.save(configuration.getHome(), page);
    }

    public void handleMoveUp(final String itemName) throws IOException {
        itemManager.moveItemUp(page, itemName);
        pageManager.save(configuration.getHome(), page);
    }

    public void handleMoveDown(final String itemName) throws IOException {
        itemManager.moveItemDown(page, itemName);
        pageManager.save(configuration.getHome(), page);
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }
}
