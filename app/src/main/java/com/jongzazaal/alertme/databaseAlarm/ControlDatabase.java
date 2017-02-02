package com.jongzazaal.alertme.databaseAlarm;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by jongzazaal on 17/12/2559.
 */

public class ControlDatabase {
    private static ControlDatabase controlDatabase;
    private static Context context;
    private DBalarmMe mHelper;
    private SQLiteDatabase mDb;
    Cursor mCursor;

    private ControlDatabase(){
        mHelper = new DBalarmMe(context);
        mDb = mHelper.getReadableDatabase();
    }

    public static ControlDatabase getInstance(Context contextN){
        if(controlDatabase==null){
            context = contextN;
            controlDatabase = new ControlDatabase();

        }
        return controlDatabase;
    }

    public RecentClass viewRecent(){

        mCursor = mDb.rawQuery("SELECT * FROM " + DBalarmMe.TABLE_NAME, null);

//        ArrayList<String> arr_list = new ArrayList<String>();
        mCursor.moveToFirst();
        while(!mCursor.isAfterLast() ){
            RecentClass.getInstance().addId(mCursor.getInt(mCursor.getColumnIndex(DBalarmMe.COL_ID)));
            RecentClass.getInstance().addPlaceName(mCursor.getString(mCursor.getColumnIndex(DBalarmMe.COL_PLACENAME)));
            RecentClass.getInstance().addLat(mCursor.getString(mCursor.getColumnIndex(DBalarmMe.COL_LAT)));
            RecentClass.getInstance().addLng(mCursor.getString(mCursor.getColumnIndex(DBalarmMe.COL_LNG)));
            RecentClass.getInstance().addDistance(mCursor.getString(mCursor.getColumnIndex(DBalarmMe.COL_DISTANCE)));
            mCursor.moveToNext();
        }
        return RecentClass.getInstance();
    }

    public void saveRecord(String placeName, LatLng latLng, Integer distance){
        mDb.execSQL("INSERT INTO " + DBalarmMe.TABLE_NAME + " ("
                + DBalarmMe.COL_PLACENAME + ", " + DBalarmMe.COL_LAT + ", " + DBalarmMe.COL_LNG  + ", "
                + DBalarmMe.COL_DISTANCE + ") VALUES ('"
                + placeName + "', '" + latLng.latitude + "', '" + latLng.longitude + "', '" + distance
                + "');");
    }

    public void delRecord(int id){
        mDb.execSQL("DELETE FROM " + DBalarmMe.TABLE_NAME
                + " WHERE " + DBalarmMe.COL_ID + " = " + id + ";");
    }

    public void updateRecord(int id, String placeName, LatLng latLng, Integer distance){

        mDb.execSQL("UPDATE " + DBalarmMe.TABLE_NAME  + " SET "
                + DBalarmMe.COL_PLACENAME + "='" + placeName + "', "
                + DBalarmMe.COL_LAT + "='" + latLng.latitude + "', "
                + DBalarmMe.COL_LNG + "='" + latLng.longitude + "', "
                + DBalarmMe.COL_DISTANCE + "='" + distance + "'"
                + " WHERE " + DBalarmMe.COL_ID + " = " + id + ";");
    }
}
