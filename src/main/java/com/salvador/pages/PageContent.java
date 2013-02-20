package com.salvador.pages;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: mearlam
 * Date: 20/02/13
 * Time: 15:04
 */
@Named
@SessionScoped
public class PageContent implements Serializable {

    private Page currentPage;

    public Page getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Page currentPage) {
        this.currentPage = currentPage;
    }
}
