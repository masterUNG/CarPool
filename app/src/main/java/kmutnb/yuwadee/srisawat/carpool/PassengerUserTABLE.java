package kmutnb.yuwadee.srisawat.carpool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Adminn on 10/8/2558.
 */
public class PassengerUserTABLE {

    //Explicit
    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeSqLiteDatabase, readSqLiteDatabase;

    public static final String PASSENGER_USER_TABLE = "passengerUserTABLE";
    public static final String COLUMN_ID_PASSENGETR = "_id";
    public static final String COLUMN_USER_PASSENGETR = "User";
    public static final String COLUMN_PASSWORD_PASSENGETR = "Password";
    public static final String COLUMN_NAME_PASSENGETR = "Name";
    public static final String COLUMN_SURNAME_PASSENGETR = "Surname";
    public static final String COLUMN_ADDRESS_PASSENGETR = "Address";
    public static final String COLUMN_PHONE_PASSENGER = "Phone";


    public PassengerUserTABLE(Context context) {
        objMyOpenHelper = new MyOpenHelper(context);
        writeSqLiteDatabase = objMyOpenHelper.getWritableDatabase();
        readSqLiteDatabase = objMyOpenHelper.getReadableDatabase();

    }   //Consturctor

    //Search Passenger
    public String[] searchPassenger(String strUser) {

        try {

            String[] strResult = null;
            Cursor objCursor = readSqLiteDatabase.query(PASSENGER_USER_TABLE,
                    new String[]{COLUMN_ID_PASSENGETR, COLUMN_USER_PASSENGETR, COLUMN_PASSWORD_PASSENGETR, COLUMN_NAME_PASSENGETR, COLUMN_SURNAME_PASSENGETR},
                    COLUMN_USER_PASSENGETR + "=?",
                    new String[]{String.valueOf(strUser)},
                    null, null, null, null);
            if (objCursor != null) {
                if (objCursor.moveToFirst()) {
                    strResult = new String[5];
                    strResult[0] = objCursor.getString(0);
                    strResult[1] = objCursor.getString(1);
                    strResult[2] = objCursor.getString(2);
                    strResult[3] = objCursor.getString(3);
                    strResult[4] = objCursor.getString(4);
                }   //if2
            }   //if1
            objCursor.close();
            return strResult;

        } catch (Exception e) {
            return null;
        }

        //return new String[0];
    }


    //Add New Passenger
    public long addNewPassenger(String strUser, String strPassword,
                                String strName, String strSurname,
                                String strAddress, String strPhone) {
        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_USER_PASSENGETR, strUser);
        objContentValues.put(COLUMN_PASSWORD_PASSENGETR, strPassword);
        objContentValues.put(COLUMN_NAME_PASSENGETR, strName);
        objContentValues.put(COLUMN_SURNAME_PASSENGETR, strSurname);
        objContentValues.put(COLUMN_ADDRESS_PASSENGETR, strAddress);
        objContentValues.put(COLUMN_PHONE_PASSENGER, strPhone);

        return writeSqLiteDatabase.insert(PASSENGER_USER_TABLE, null, objContentValues);
    }

}   //Main Class
