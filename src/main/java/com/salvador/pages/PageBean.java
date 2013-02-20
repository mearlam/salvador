package com.salvador.pages;

import com.salvador.configuration.Configuration;
import com.salvador.scenarios.Scenario;
import com.salvador.scenarios.ScenarioManager;
import com.salvador.spi.ViewScoped;
import com.salvador.utils.FacesUtils;
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
    private String name;
    private Page page;

    @Inject
    transient PageContent pageContent;

    @Inject
    transient PageManager pageManager;

    @Inject
    transient Configuration configuration;

    @Inject
    transient ScenarioManager scenarioManager;

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
                for(Page page : pageContent.getCurrentPage().getChildren()) {
                    MenuItem item = new MenuItem();
                    item.setValue(page.getName());
                    item.setUrl(page.getPath() + page.getName());
                    model.addMenuItem(item);
                }

//                path = pageManager.getParentPath(requestedUri, "/");
//                final String[] pages = path.split("/");
//                String currentPath = "/" + PageManager.TEST_FOLDER + "/";
//                for (String page : pages) {
//                    currentPath += page + "/";
//                    MenuItem item = new MenuItem();
//                    item.setValue(page);
//                    item.setUrl(currentPath);
//                    model.addMenuItem(item);
//                }
            }
        }

        return model;
    }

    public Page getPage() throws IOException {
        return page;
    }

    public String getPagePath() {
        final String referer = FacesUtils.getHttpHeader("Referer");
        return pageManager.getParentPath(referer, "/");
    }

    public void createPage() {

        final String referer = FacesUtils.getParam("referer");

        Page newPage = new Page();
        newPage.setName(name);
        newPage.setPath(pageManager.getParentPath(referer, "/"));
        try {
            pageManager.save(configuration.getHome(), newPage);
        } catch (IOException e) {
            log.error("Could not save page", e);
        }

        pageContent.setCurrentPage(newPage);
        FacesUtils.redirect("/" + PageManager.TEST_FOLDER + "/" + newPage.getPath() + newPage.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void handleClose(final String scenarioName) throws IOException {
        page.getScenarios().remove(scenarioManager.getScenario(page, scenarioName));
        pageManager.save(configuration.getHome(), page);
    }

    public void handleEnable(final String scenarioName) throws IOException {
        scenarioManager.getScenario(page, scenarioName).setEnabled(true);
        pageManager.save(configuration.getHome(), page);
    }

    public void handleDisable(final String scenarioName) throws IOException {
        scenarioManager.getScenario(page, scenarioName).setEnabled(false);
        pageManager.save(configuration.getHome(), page);
    }

    public void handleMoveUp(final String scenarioName) throws IOException {
        scenarioManager.moveScenarioUp(page, scenarioName);
        pageManager.save(configuration.getHome(), page);
    }

    public void handleMoveDown(final String scenarioName) throws IOException {
        scenarioManager.moveScenarioDown(page, scenarioName);
        pageManager.save(configuration.getHome(), page);
    }
}
