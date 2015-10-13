package kmutnb.yuwadee.srisawat.carpool;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private double latCenterADouble, lngCenterADouble;
    private LatLng centerLatLng;
    private String[] nameCarStrings;
    private double[] latCarDoubles, lngCarDoubles;
    private LatLng[] carLatLngs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Receive Lat,lng from Intent
        receiveLatLng();

        //Find LatLng For Every Maker
        findLatLngForEveryMaker();


        setUpMapIfNeeded();
    }   // onCreate

    private void findLatLngForEveryMaker() {

        //Get All Name Car
        CarUserTABLE objCarUserTABLE = new CarUserTABLE(this);
        nameCarStrings = objCarUserTABLE.readAllCar();

        //Check NameCar
        latCarDoubles = new double[nameCarStrings.length];
        lngCarDoubles = new double[nameCarStrings.length];
        carLatLngs = new LatLng[nameCarStrings.length];

        try {

            for (int i = 0; i < nameCarStrings.length; i++) {
                Log.i("Map", "NameCar(" + Integer.toString(i) + ") = " + nameCarStrings[i]);

                SQLiteDatabase objSqLiteDatabase = openOrCreateDatabase("CarPool.db", MODE_PRIVATE, null);
                Cursor carCursor = objSqLiteDatabase.rawQuery("SELECT * FROM mapsTABLE WHERE Name = " + "'" + nameCarStrings[i] + "'", null);
                carCursor.moveToFirst();
                carCursor.moveToLast();
                latCarDoubles[i] = Double.parseDouble(carCursor.getString(carCursor.getColumnIndex("Lat")));
                lngCarDoubles[i] = Double.parseDouble(carCursor.getString(carCursor.getColumnIndex("Lng")));
                carLatLngs[i] = new LatLng(latCarDoubles[i], lngCarDoubles[i]);
                carCursor.close();
            } // for

        } catch (Exception e) {
            Log.e("Map", "e Error ====> " + e.toString());
        }

    }   // findLatLngForEveryMaker

    private void receiveLatLng() {
        latCenterADouble = getIntent().getDoubleExtra("lat", 13.818802);
        lngCenterADouble = getIntent().getDoubleExtra("lng", 100.513771);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    //SetUp Map This here
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }   // setUpMapIfNeeded

    private void setUpMap() {

        //Create LatLng
        createLatLng();

        //Show Map
        showMap();

        //Create Maker
        createMaker();


    }   // setUPMap

    private void createMaker() {

        //Create Center Maker Map
        int intIcon = getIntent().getIntExtra("Icon", R.drawable.icon_cow);
        mMap.addMarker(new MarkerOptions()
                .position(centerLatLng)
                .icon(BitmapDescriptorFactory.fromResource(intIcon)));

        //Create Car Maker
        for (int i = 0; i < nameCarStrings.length; i++) {

            mMap.addMarker(new MarkerOptions().position(carLatLngs[i]));

        }   // for


    }   // createMaker

    private void showMap() {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerLatLng, 15));
    }

    private void createLatLng() {

        //Center Map from Intent
        centerLatLng = new LatLng(latCenterADouble, lngCenterADouble);

    }   // createLatLng


}   // Main Class
