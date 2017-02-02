package com.jongzazaal.alertme.databaseAlarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jongzazaal on 17/12/2559.
 */

public class DBalarmMe extends SQLiteOpenHelper {
    private static final String DB_NAME = "AlarmMe";
    private static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "Alarm";

    public static final String COL_ID = "_id";
    public static final String COL_PLACENAME = "placeName";
    public static final String COL_LAT= "lat";
    public static final String COL_LNG = "lng";
    public static final String COL_DISTANCE = "distance";

    public DBalarmMe(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_PLACENAME + " TEXT, " + COL_LAT + " TEXT, " + COL_LNG + " TEXT, "
                + COL_DISTANCE + " TEXT);");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COL_PLACENAME + ", " + COL_LAT + ", "
                + COL_LNG + ", " + COL_DISTANCE + ") VALUES ('SamplePlace', '13.781291', '100.476603', '200');");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
