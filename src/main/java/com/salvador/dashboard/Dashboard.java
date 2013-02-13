package com.salvador.dashboard;

import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 12/02/13
 * Time: 08:39
 */
@Named
@SessionScoped
public class Dashboard implements Serializable {

    private DashboardModel model;

    public Dashboard() {
        model = new DefaultDashboardModel();
        DashboardColumn column1 = new DefaultDashboardColumn();

        column1.addWidget("one");
        column1.addWidget("two");
        column1.addWidget("three");

        model.addColumn(column1);
    }

    public DashboardModel getModel() {
        return model;
    }
}
