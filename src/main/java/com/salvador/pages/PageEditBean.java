package com.salvador.pages;

import com.salvador.configuration.Configuration;
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
public class PageEditBean implements Serializable {

    static final Logger log = LoggerFactory.getLogger(PageEditBean.class);

    private Page page;

    @Inject
    transient PageContent pageContent;

    @Inject
    transient PageManager pageManager;

    @Inject
    transient Configuration configuration;

    @PostConstruct
    public void initPage() throws IOException {
        page = pageContent.getCurrentPage();
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Page getPage() throws IOException {
        return page;
    }

    public void updatePage() {

        try {
            pageManager.update(configuration.getHome(), page);
        } catch (IOException e) {
            log.error("Could not save page", e);
        }

        FacesUtils.redirect("/" + PageManager.TEST_FOLDER + "/" + page.getPath() + page.getName());
    }
}
