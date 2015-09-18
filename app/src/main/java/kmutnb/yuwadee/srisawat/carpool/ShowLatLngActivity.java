package kmutnb.yuwadee.srisawat.carpool;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ShowLatLngActivity extends AppCompatActivity {

    //Explicit
    private TextView tableTextView, userTextView, latTextView, lngTextView;
    private LocationManager objLocationManager;
    private Criteria objCriteria;
    private double latADouble, lngADouble;
    private boolean GPSABoolean, NetworkABoolean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lat_lng);

        //Bind Widget
        bindWidget();

        //Show View
        showView();

        //Setup Location Manager
        setupLocationManager();

    }   // onCreate

    @Override
    protected void onStop() {
        super.onStop();

        objLocationManager.removeUpdates(objLocationListener);

    }

    @Override
    protected void onResume() {
        super.onResume();

        objLocationManager.removeUpdates(objLocationListener);
        String strLat = "Unknow";
        String strLng = "UnKnow";

        Location networkLocation = requestUpdateFromProvider(LocationManager.NETWORK_PROVIDER, "Error From Cannot Connected Internet");
        if (networkLocation != null) {
            strLat = String.format("%.7f", networkLocation.getLatitude());
            strLng = String.format("%.7f", networkLocation.getLongitude());
        }   // if
        Location GPSLocation = requestUpdateFromProvider(LocationManager.GPS_PROVIDER, "Error From GPS False");
        if (GPSLocation != null) {
            strLat = String.format("%.7f", GPSLocation.getLatitude());
            strLng = String.format("%.7f", GPSLocation.getLongitude());
        }   //if

        //Show Result
        latTextView.setText(strLat);
        lngTextView.setText(strLng);

    }   // onResume

    @Override
    protected void onStart() {
        super.onStart();

        GPSABoolean = objLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!GPSABoolean) {
            NetworkABoolean = objLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!NetworkABoolean) {

                //Not Connected Internet
                Toast.makeText(ShowLatLngActivity.this, "You Stant Alone Please Connected Internet", Toast.LENGTH_SHORT).show();

            }   //if2
        }   // if1

    }   // onStart

    public Location requestUpdateFromProvider(String strProvider, String strError) {

        Location objLocation = null;

        if (objLocationManager.isProviderEnabled(strProvider)) {

            objLocationManager.requestLocationUpdates(strProvider, 1000, 10, objLocationListener);
            objLocation = objLocationManager.getLastKnownLocation(strProvider);

        } else {
            Log.d("Car", "Error ==> " + strError);
        }

        return objLocation;
    }   // requestUpdateFromProvider

    //Create LocationListener
    public final LocationListener objLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            latTextView.setText(String.format("%.7f", location.getLatitude()));
            lngTextView.setText(String.format("%.7f", location.getLongitude()));

        }   // onLocationChange

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };  // Location Listener


    private void setupLocationManager() {

        objLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        objCriteria = new Criteria();
        objCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        objCriteria.setAltitudeRequired(false);
        objCriteria.setBearingRequired(false);

    }   // setupLocationManager

    private void showView() {

        //Show Welcome
        String strTable = getIntent().getStringExtra("Table");
        tableTextView.setText("Welcome " + strTable);

        //Show User
        String strName = getIntent().getStringExtra("Name");
        String strSurname = getIntent().getStringExtra("Surname");
        userTextView.setText(strName + " " + strSurname);

    }

    private void bindWidget() {
        tableTextView = (TextView) findViewById(R.id.txtWelcome);
        userTextView = (TextView) findViewById(R.id.txtShowUser);
        latTextView = (TextView) findViewById(R.id.txtLat);
        lngTextView = (TextView) findViewById(R.id.txtLng);
    }

    public void clickGoToMap(View view) {

        //Get Lat,Lng
        Double latDouble = Double.parseDouble(latTextView.getText().toString());
        Double lngDouble = Double.parseDouble(lngTextView.getText().toString());

        //Get Icon
        int intIcon = getIntent().getIntExtra("Icon", R.drawable.icon_cow);

        Intent objIntent = new Intent(ShowLatLngActivity.this, MapsActivity.class);
        objIntent.putExtra("lat", latDouble);
        objIntent.putExtra("lng", lngDouble);
        objIntent.putExtra("Icon", intIcon);
        startActivity(objIntent);


    }   // clickGoToMap


}   // Main Class
