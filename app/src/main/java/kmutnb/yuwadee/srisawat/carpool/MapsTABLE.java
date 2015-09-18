package kmutnb.yuwadee.srisawat.carpool;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by masterUNG on 9/18/15 AD.
 */
public class MapsTABLE {

    //Explicit
    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeSqLiteDatabase, readSqLiteDatabase;

    public static final String MAPS_TABLE = "mapsTABLE";
    public static final String COLUMN_ID_MAPS = "_id";
    public static final String COLUMN_NAME_MAPS = "Name";
    public static final String COLUMN_SERNAME_MAPS = "Surname";
    public static final String COLUMN_ICON = "Icon";
    public static final String COLUMN_LAT = "Lat";
    public static final String COLUMN_LNG = "Lng";
    public static final String COLUMN_DATE = "Date";

    public MapsTABLE(Context context) {
        objMyOpenHelper = new MyOpenHelper(context);
        writeSqLiteDatabase = objMyOpenHelper.getWritableDatabase();
        readSqLiteDatabase = objMyOpenHelper.getReadableDatabase();
    }   // Constructor

    public long addValue(String strName,
                         String strSurname,
                         String strIcon,
                         String strLat,
                         String strLng,
                         String strDate) {

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_NAME_MAPS, strName);
        objContentValues.put(COLUMN_SERNAME_MAPS, strSurname);
        objContentValues.put(COLUMN_ICON, strIcon);
        objContentValues.put(COLUMN_LAT, strLat);
        objContentValues.put(COLUMN_LNG, strLng);
        objContentValues.put(COLUMN_DATE, strDate);

        return writeSqLiteDatabase.insert(MAPS_TABLE, null, objContentValues);
    }

}   // Main Class
