package kmutnb.yuwadee.srisawat.carpool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Adminn on 10/8/2558.
 */
public class MyOpenHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "CarPool.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_USER_CAR = "create table carUserTABLE (_id integer primary key, User text, Password text, Name text, Surname text, CarID text, Picture text, id_office text);";
    private static final String CREATE_USER_PASSENGER = "create table passengerUserTABLE (_id integer primary key, User text, Password text,Name text, Surname text,Address text, Phone text);";
    private static final String CREATE_MAPS = "create table mapsTABLE (_id integer primary key, Name text, Surname text, Icon text, Lat text, Lng text, Date text);";

    public MyOpenHelper(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);

    }   //Constructor

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_CAR);
        db.execSQL(CREATE_USER_PASSENGER);
        db.execSQL(CREATE_MAPS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}   //Main Class
