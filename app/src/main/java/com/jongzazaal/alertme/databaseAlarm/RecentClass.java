package com.jongzazaal.alertme.databaseAlarm;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by jongzazaal on 17/12/2559.
 */

public class RecentClass {
    private ArrayList<Integer> id = new ArrayList<>();
    private ArrayList<Double> lat = new ArrayList<>();
    private ArrayList<Double> lng = new ArrayList<>();
    private ArrayList<Integer> distance = new ArrayList<>();
    private ArrayList<String> placeName = new ArrayList<>();

    private static RecentClass instance;

    private RecentClass(){}
    public static RecentClass getInstance(){
        if(instance==null){
            instance = new RecentClass();
        }
        return instance;
    }

    public void ClearDate(){
        id.clear();
        lat.clear();
        lng.clear();
        distance.clear();
        placeName.clear();
    }

    public ArrayList<Integer> getId() {
        return id;
    }

    public void addId(Integer id) {
        this.id.add(id);
    }

    public ArrayList<Integer> getDistance() {
        return distance;
    }

    public void addDistance(Integer distance) {
        this.distance.add(distance);
    }
    public void addDistance(String distance) {

        this.distance.add(Integer.valueOf(distance));
    }

    public ArrayList<Double> getLat() {
        return lat;
    }

    public void addLat(Double lat) {
        this.lat.add(lat);
    }
    public void addLat(String lat) {
        this.lat.add(Double.valueOf(lat));
    }

    public ArrayList<Double> getLng() {
        return lng;
    }

    public void addLng(Double lng) {
        this.lng.add(lng);
    }

    public void addLng(String lng) {
        this.lng.add(Double.valueOf(lng));
    }

    public ArrayList<String> getPlaceName() {
        return placeName;
    }

    public void addPlaceName(String placeName) {
        this.placeName.add(placeName);
    }

    public void removeItem(int position){
        id.remove(position);
        lat.remove(position);
        lng.remove(position);
        distance.remove(position);
        placeName.remove(position);

    }
}
