package kmutnb.yuwadee.srisawat.carpool;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    //Explicit
    private CarUserTABLE objCarUserTABLE;
    private PassengerUserTABLE objPassengerUserTABLE;

    private EditText userEditText,passwordEditText;
    private String userString, passwordString, tableString = "Driver";
    private RadioGroup userRadioGroup;
    private RadioButton carRadioButton, passengerRadioButton;
    private int tableAnInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind Widget
        bindWidget();

        //RadioButtonController
        radioController();

        //Connected SQLite
        connectedSQLite();

        //Test Add Data to SQLite
        //testAddData();

        //Delete All SQlite
        deleteAllSQLite();

        //synchronize JSON to SQLite

        synJSONtoSQLinte();




    }   //onCreate

    private void radioController() {

        userRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.radCar:
                        tableAnInt = 0;
                        tableString = "Driver";
                        break;
                    case R.id.radpassanger:
                        tableAnInt = 1;
                        tableString = "Passenger";
                        break;
                    default:
                        tableAnInt = 0;
                        tableString = "Driver";
                        break;
                }

            }   // event
        });

    }   // radioController

    // Event From Click Login
    public void clickLogin(View view) {
        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        //Check Zero
        if (userString.equals("") || passwordString.equals("")) {

            //Have Space
            MyErrorDialog objMyErrorDialog = new MyErrorDialog();
            objMyErrorDialog.errorDialog(MainActivity.this,"มีช่องว่าง", "กรุณา กรอกทุกช่อง นะคะ");

        } else {

            //No Space
            chooseTable();

        }   // if

    }   //clickLogin

    private void chooseTable() {
        if (tableAnInt == 0) {
            checkUser();
        } else {
            checkPassengerUser();
        }
    }

    private void checkPassengerUser() {

        try {

            String[] strMyResult = objPassengerUserTABLE.searchPassenger(userString);
            Log.d("Car", "ชื่อผู้โดยสาร = " + strMyResult[3]);

            //Check Password
            if (passwordString.equals(strMyResult[2])) {
                //Intent To ShowLatLng
                Intent objIntent = new Intent(MainActivity.this, ShowLatLngActivity.class);
                objIntent.putExtra("Name", strMyResult[3]);
                objIntent.putExtra("Surname", strMyResult[4]);
                objIntent.putExtra("Table", tableString);
                objIntent.putExtra("Icon", R.drawable.friend);
                startActivity(objIntent);


            } else {
                MyErrorDialog objMyErrorDialog = new MyErrorDialog();
                objMyErrorDialog.errorDialog(MainActivity.this, "Password False", "Please Try Agains Passsword False");
            }


        } catch (Exception e) {
            MyErrorDialog objMyErrorDialog = new MyErrorDialog();
            objMyErrorDialog.errorDialog(MainActivity.this, "ไม่มี User คนนี้", "ไม่มี " + userString + " ในฐานข้อมูล Passenger ของเรา");
        }

    }   // checkPassenterUser



    private void checkUser() {

        try {

            String[] strMyResult = objCarUserTABLE.searchUserCar(userString);

            Log.d("Car", "ชื่อคนขับรถ = " + strMyResult[3]);


            //Check Password
            if (passwordString.equals(strMyResult[2])) {

                Intent objIntent = new Intent(MainActivity.this, ShowLatLngActivity.class);
                objIntent.putExtra("Table", tableString);
                objIntent.putExtra("Name", strMyResult[3]);
                objIntent.putExtra("Surname", strMyResult[4]);
                objIntent.putExtra("Icon", R.drawable.icon_cow);
                startActivity(objIntent);

            } else {

                //Password False
                MyErrorDialog objMyErrorDialog = new MyErrorDialog();
                objMyErrorDialog.errorDialog(MainActivity.this, "Password False", "Please Try Again Password False");

            }

        } catch (Exception e) {
            MyErrorDialog objMyErrorDialog = new MyErrorDialog();
            objMyErrorDialog.errorDialog(MainActivity.this, "ไม่มี User นี่", "ไม่มี " + userString + " ในฐานข้อมูล Driver ของเรา");
            userEditText.setText("");
            passwordEditText.setText("");
        }   // try1

    }   // checkUser

    private void bindWidget() {
        userEditText = (EditText) findViewById(R.id.edtUser);
        passwordEditText = (EditText) findViewById(R.id.edtPassword);
        userRadioGroup = (RadioGroup) findViewById(R.id.ragGroup);
        carRadioButton = (RadioButton) findViewById(R.id.radCar);
        passengerRadioButton = (RadioButton) findViewById(R.id.radpassanger);

    }

    private void synJSONtoSQLinte() {

        //Setup Policy
        StrictMode.ThreadPolicy myPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(myPolicy);

        //setup Variable & Constant
        int intTimes = 0;
        while (intTimes <= 1) {

            InputStream objInputStream = null;
            String strJSON = null;
            HttpPost objHttpPost;
            String[] strUrlJSON = {"http://swiftcodingthai.com/joy/php_get_data_Yawadee.php",
                    "http://swiftcodingthai.com/joy/php_get_passenger_Yawadee.php"};

            //1.Create InputStream
            try {

                HttpClient objHttpClient = new DefaultHttpClient();
                objHttpPost = new HttpPost(strUrlJSON[intTimes]);
                HttpResponse objHttpResponse = objHttpClient.execute(objHttpPost);
                HttpEntity objHttpEntity = objHttpResponse.getEntity();
                objInputStream = objHttpEntity.getContent();


            } catch (Exception e) {
                Log.d("Car", "InputStream ==> " + e.toString());
            }

            //2. Create strJSON
            try {

                BufferedReader objBufferedReader = new BufferedReader(new InputStreamReader(objInputStream, "UTF-8"));
                StringBuilder objStringBuilder = new StringBuilder();
                String strLine = null;
                while ((strLine = objBufferedReader.readLine()) != null  ) {
                    objStringBuilder.append(strLine);
                } //while
                objInputStream.close();
                strJSON = objStringBuilder.toString();


            } catch (Exception e) {
                Log.d("Car", "strJSON ==> " + e.toString());
            }

            //3. Update SQLite
            try {

                final JSONArray objJsonArray = new JSONArray(strJSON);
                for (int i = 0; i < objJsonArray.length(); i++) {

                    JSONObject object = objJsonArray.getJSONObject(i);
                    if (intTimes !=1) {

                        //Update car_user_TABLE
                        String strUser = object.getString("User");
                        String strPassword = object.getString("Password");
                        String strName = object.getString("Name");
                        String strSurname = object.getString("Surname");
                        String strCarID = object.getString("CarID");

                        objCarUserTABLE.addNewUserCar(strUser, strPassword, strName, strSurname, strCarID);
                    } else {
                        //Update passenger_user_TABLE
                        String strUser = object.getString("User");
                        String strPassword = object.getString("Password");
                        String strName = object.getString("Name");
                        String strSurname = object.getString("Surname");
                        String strAddress = object.getString("Address");
                        String strPhone = object.getString("Phone");

                        objPassengerUserTABLE.addNewPassenger(strUser, strPassword, strName,
                                strSurname, strAddress, strPhone);

                    }

                }   //for

            } catch (Exception e) {
                Log.d("Car", "Update ==>" + e.toString());
            }


            // Increase intTimes
            intTimes += 1;
        } //while

    }   // synJSONtoSQLite

    private void deleteAllSQLite() {
        SQLiteDatabase objSqLiteDatabase = openOrCreateDatabase("CarPool.db", MODE_PRIVATE, null);
        objSqLiteDatabase.delete("carUserTABLE", null, null);
        objSqLiteDatabase.delete("passengerUserTABLE", null, null);
    }

    private void testAddData() {
        objCarUserTABLE.addNewUserCar("myUser", "myPass", "ชื่อเจ้าของรถ", "นามสกุลเจ้าของรถ", "กก 1234 กรุงเทพ");
        objPassengerUserTABLE.addNewPassenger("myUser", "myPass", "ชื่อผู้โดยสาร","นามสกุลผู้โดยสาร", "ที่อยู่ผู้โดยสาร", "0881234567");

    }

    private void connectedSQLite() {
        objCarUserTABLE = new CarUserTABLE(this);
        objPassengerUserTABLE = new PassengerUserTABLE(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}   //Main Class
