package com.toni.gwftest.view.model;

import org.json.JSONObject;

import java.util.Date;

public class Meter {

    private String meterId, mpName, commModType, commModSerial, lastEntry, batteryLifetime, lat, lng, volume;
    private JSONObject state;

    public Meter(String lat, String lng, String mpName, String meterId, String commModType, String commModSerial,
                 String lastEntry, String volume, String batteryLifetime, JSONObject state) {
        this.lat = lat;
        this.lng = lng;
        this.mpName = mpName;
        this.meterId = meterId;
        this.commModType = commModType;
        this.commModSerial = commModSerial;
        this.lastEntry = lastEntry;
        this.volume = volume;
        this.batteryLifetime = batteryLifetime;
        this.state = state;
    }

    public String getMpName() {
        return mpName;
    }

    public void setMpName(String mpName) {
        this.mpName = mpName;
    }

    public String getCommModType() {
        return commModType;
    }

    public void setCommModType(String commModType) {
        this.commModType = commModType;
    }

    public String getCommModSerial() {
        return commModSerial;
    }

    public void setCommModSerial(String commModSerial) {
        this.commModSerial = commModSerial;
    }

    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public String getBatteryLifetime() {
        return batteryLifetime;
    }

    public void setBatteryLifetime(String batteryLifetime) {
        this.batteryLifetime = batteryLifetime;
    }

    public String getLastEntry() {
        return lastEntry;
    }

    public void setLastEntry(String lastEntry) {
        this.lastEntry = lastEntry;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public JSONObject getState() {
        return state;
    }

    public void setState(JSONObject state) {
        this.state = state;
    }

}
