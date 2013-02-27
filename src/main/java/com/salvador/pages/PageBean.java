package com.salvador.pages;

import com.salvador.configuration.Configuration;
import com.salvador.items.ItemManager;
import com.salvador.scenarios.Scenario;
import com.salvador.scenarios.ScenarioManager;
import com.salvador.scenarios.ScenarioStep;
import com.salvador.spi.ViewScoped;
import com.salvador.utils.FacesUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
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

    @Inject
    transient PageContent pageContent;

    @Inject
    transient PageManager pageManager;

    @Inject
    transient ScenarioManager scenarioManager;

    @Inject
    transient Configuration configuration;

    @Inject
    transient ItemManager itemManager;

    @PostConstruct
    public void initPage() throws IOException {
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

    public void delete() {
        // todo delete current page
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

        FacesUtils.addMessage("Page saved", FacesMessage.SEVERITY_INFO);
        if (StringUtils.isNotEmpty(page.getPath())) {
            FacesUtils.redirect("/" + PageManager.TEST_FOLDER + "/" + page.getPath());
        } else {
            FacesUtils.redirect("/");
        }
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

    public void handleClose(final String itemId) throws IOException {
        page.getItems().remove(page.getItem(itemId));
        pageManager.save(configuration.getHome(), page);
        FacesUtils.addMessage("Item deleted", FacesMessage.SEVERITY_INFO);
    }

    public void handleEnable(final String itemId) throws IOException {
        page.getItem(itemId).setEnabled(true);
        pageManager.save(configuration.getHome(), page);
    }

    public void handleDisable(final String itemId) throws IOException {
        page.getItem(itemId).setEnabled(false);
        pageManager.save(configuration.getHome(), page);
    }

    public void handleMoveUp(final String itemId) throws IOException {
        itemManager.moveItemUp(page, itemId);
        pageManager.save(configuration.getHome(), page);
    }

    public void handleMoveDown(final String itemId) throws IOException {
        itemManager.moveItemDown(page, itemId);
        pageManager.save(configuration.getHome(), page);
    }

    public void handleFavouriteScenarioStep(final String stepId) throws IOException {
        ScenarioStep step = scenarioManager.getScenarioStep(page, stepId);
        if (step != null) {
            step.setCommon(true);
            FacesUtils.addMessage("Scenario step added to common steps", FacesMessage.SEVERITY_INFO);
            pageManager.save(configuration.getHome(), page);
        }
    }

    public void cancel() {
        FacesUtils.redirect(pageContent.getCurrentPage());
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }
}
