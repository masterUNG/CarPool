package kmutnb.yuwadee.srisawat.carpool;

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
        for (int i = 0; i < nameCarStrings.length;i++ ) {
            Log.i("Map", "NameCar("+ Integer.toString(i)+") = " + nameCarStrings[i]);
        } // for

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

    }   // createMaker

    private void showMap() {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerLatLng, 16));
    }

    private void createLatLng() {

        //Center Map from Intent
        centerLatLng = new LatLng(latCenterADouble, lngCenterADouble);

    }   // createLatLng


}   // Main Class
