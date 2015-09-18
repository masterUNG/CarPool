package kmutnb.yuwadee.srisawat.carpool;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShowLatLngActivity extends AppCompatActivity {

    //Explicit
    private TextView tableTextView, userTextView, latTextView, lngTextView;
    private LocationManager objLocationManager;
    private Criteria objCriteria;
    private double latADouble, lngADouble;
    private boolean GPSABoolean, NetworkABoolean;
    private String strName, strSurname, strTable;

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
        strTable = getIntent().getStringExtra("Table");
        tableTextView.setText("Welcome " + strTable);

        //Show User
        strName = getIntent().getStringExtra("Name");
        strSurname = getIntent().getStringExtra("Surname");
        userTextView.setText(strName + " " + strSurname);

    }

    private void bindWidget() {
        tableTextView = (TextView) findViewById(R.id.txtWelcome);
        userTextView = (TextView) findViewById(R.id.txtShowUser);
        latTextView = (TextView) findViewById(R.id.txtLat);
        lngTextView = (TextView) findViewById(R.id.txtLng);
    }

    public void clickGoToMap(View view) {

        //Check Value
        checkValue();

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

    private void checkValue() {

        //Get Lat,Lng
        String strLat = latTextView.getText().toString();
        String strLng = lngTextView.getText().toString();

        //Get Time
        DateFormat myDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date currentDate = new Date();
        String strDate = myDateFormat.format(currentDate);


        Log.d("Map", "Name = " + strName);
        Log.d("Map", "Surname = " + strSurname);
        Log.d("Map", "Icon = " + strTable);
        Log.d("Map", "Lat = " + strLat);
        Log.d("Map", "Lng = " + strLng);
        Log.d("Map", "Date = " + strDate);

        //Update Value to mySQL

        StrictMode.ThreadPolicy myPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(myPolicy);

        try {

            ArrayList<NameValuePair> objNameValuePairs = new ArrayList<NameValuePair>();
            objNameValuePairs.add(new BasicNameValuePair("isAdd", "true"));
            objNameValuePairs.add(new BasicNameValuePair("Name", strName));
            objNameValuePairs.add(new BasicNameValuePair("Surname", strSurname));
            objNameValuePairs.add(new BasicNameValuePair("Icon", strTable));
            objNameValuePairs.add(new BasicNameValuePair("Lat", strLat));
            objNameValuePairs.add(new BasicNameValuePair("Lng", strLng));
            objNameValuePairs.add(new BasicNameValuePair("Date", strDate));

            HttpClient objHttpClient = new DefaultHttpClient();
            HttpPost objHttpPost = new HttpPost("http://swiftcodingthai.com/joy/php_add_data_maps.php");
            objHttpPost.setEntity(new UrlEncodedFormEntity(objNameValuePairs, "UTF-8"));
            objHttpClient.execute(objHttpPost);

            Toast.makeText(ShowLatLngActivity.this, "Update mySQL Success", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(ShowLatLngActivity.this, "Cannot Update mySQL ?", Toast.LENGTH_SHORT).show();
        }


    }   // checkValue


}   // Main Class
