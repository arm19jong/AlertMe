package com.jongzazaal.alertme.databaseAlarm;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jongzazaal on 29/12/2559.
 */

public class SingleDBalarmMe {

    private int id,distance;
    private String placeName ;
    private Double lat, lng;

    private static SingleDBalarmMe instance;
    private SingleDBalarmMe(){

    }
    public static SingleDBalarmMe getInstance(){
        if (instance == null){
            instance = new SingleDBalarmMe();
        }
        return instance;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void  setAll(int id, String placeName, Double lat, Double lng, int distance){

        this.id = id;
        this.placeName = placeName;
        this.lat = lat;
        this.lng = lng;
        this.distance = distance;

    }
}
