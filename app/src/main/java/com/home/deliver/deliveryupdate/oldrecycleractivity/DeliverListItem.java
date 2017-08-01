package com.home.deliver.deliveryupdate.oldrecycleractivity;

/**
 * Created by aravindnga on 16/07/17.
 */

public class DeliverListItem {

    private String Name,latlng;
    String distance;

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public DeliverListItem(String Name, String latlng, String distance) {
        this.Name = Name;
        this.latlng = latlng;

        this.distance = distance;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
