package com.salvador.pages;

import com.salvador.configuration.Configuration;
import com.salvador.utils.FacesUtils;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 14/02/13
 * Time: 10:01
 */
@Named
@RequestScoped
public class PageBean {

    static final Logger log = LoggerFactory.getLogger(PageBean.class);


    private String name;

    Page page;

    @Inject
    PageManager pageManager;

    @Inject
    Configuration configuration;

    public MenuModel getMenuModel() throws IOException {
        MenuModel model = new DefaultMenuModel();
        MenuItem home = new MenuItem();
        home.setValue("Home");
        home.setUrl("/");
        model.addMenuItem(home);

        final String requestedUri = FacesUtils.getDestinationPage();
        String path = "";

        if (requestedUri != null) {
            path = pageManager.getParentPath(requestedUri, "/");
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

        return model;
    }

    public Page getPage() throws IOException {
        return pageManager.getPage(configuration.getHome(), FacesUtils.getDestinationPage());
    }

    public String getPagePath() {
        final String referer = FacesUtils.getHttpHeader("Referer");
        return pageManager.getParentPath(referer, "/");
    }

    public void createPage() {

        final String referer = FacesUtils.getParam("referer");

        page = new Page();
        page.setName(name);
        page.setPath(pageManager.getParentPath(referer, "/"));
        try {
            pageManager.save(configuration.getHome(), page);
        } catch (IOException e) {
            log.error("Could not save page", e);
        }

        FacesUtils.redirect("/" + PageManager.TEST_FOLDER + "/" + page.getPath() + page.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
