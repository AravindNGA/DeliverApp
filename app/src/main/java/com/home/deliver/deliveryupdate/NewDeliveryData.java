package com.home.deliver.deliveryupdate;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.test.mock.MockPackageManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

public class NewDeliveryData extends AppCompatActivity {


    EditText et;
    Button add, placepic;
    String address, Consigneesname;
    TextView textView;

    GPSLocationGetter gps;
    String mPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int REQUEST_CODE_PERMISSION = 2;

    private static final int Request_Code = 1;
    public static final String BUNDLE_STRINGG = "BUNDLE_STRING";
    public static final String BUNDLE_NAME = "BUNDLE_INT";
    public static final String BUNDLE_DIST = "BUNDLE_DIST";
    double current_lat, current_lng, tolat, tolng;
    double distane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_delivery_data);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        et = (EditText) findViewById(R.id.editTextname);
        add = (Button) findViewById(R.id.buttonadd);

        placepic = (Button) findViewById(R.id.button3);
        textView = (TextView) findViewById(R.id.textViewaddress);

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        gps = new GPSLocationGetter(NewDeliveryData.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            current_lat = gps.getLatitude();
            current_lng = gps.getLongitude();

        }else{
            gps.showSettingsAlert();
        }

        placepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(NewDeliveryData.this), Request_Code);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et.getText().toString().equals("")) {
                    Snackbar.make(view, "EnterData", Snackbar.LENGTH_SHORT).show();
                } else {
                    Consigneesname = et.getText().toString();
                    Intent in = new Intent();
                    in.putExtra(BUNDLE_STRINGG, address);
                    in.putExtra(BUNDLE_NAME, Consigneesname);
                    in.putExtra(BUNDLE_DIST, Double.toString(distane/1000));
                    setResult(RESULT_OK, in);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Request_Code) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                LatLng latLng = place.getLatLng();
                tolat = latLng.latitude;
                tolng = latLng.longitude;
                address = String.valueOf(tolat)+","+String.valueOf(tolng);
                String address1 = String.format("%s", place.getAddress());
                float result[] = new float[1];
                Location.distanceBetween(current_lat, current_lng, tolat, tolng, result);
                distane = result[0];
                textView.setText("Address : "+address1);
            }
        }
    }

}
