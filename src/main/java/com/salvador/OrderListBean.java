package com.salvador;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 12/02/13
 * Time: 06:06
 * To change this template use File | Settings | File Templates.
 */
@Named
public class OrderListBean {

    private List<String> cities = new ArrayList<String>();

    public OrderListBean() {
        cities.add("Istanbul");
        cities.add("Ankara");
        cities.add("Izmir");
        cities.add("Antalya");
        cities.add("Bursa");
    }
    //getter&setter for cities


    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }
}
