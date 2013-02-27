package com.salvador.notes;

import com.salvador.configuration.Configuration;
import com.salvador.pages.Page;
import com.salvador.pages.PageContent;
import com.salvador.pages.PageManager;
import com.salvador.spi.ViewScoped;
import com.salvador.utils.FacesUtils;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 21/02/13
 * Time: 04:39
 */
@Named
@ViewScoped
public class PageNoteBean implements Serializable {

    @Inject
    transient PageContent pageContent;

    @Inject
    transient PageManager pageManager;

    @Inject
    Configuration configuration;

    @Inject
    private PageNote note;

    public void createNote() throws IOException {
        Page page = pageContent.getCurrentPage();
        page.getItems().add(note);
        pageManager.save(configuration.getHome(),page);
        FacesUtils.addMessage("Note saved", FacesMessage.SEVERITY_INFO);
        FacesUtils.redirect(pageContent.getCurrentPage());
    }

    public PageNote getNote() {
        return note;
    }

    public void setNote(PageNote note) {
        this.note = note;
    }
}
