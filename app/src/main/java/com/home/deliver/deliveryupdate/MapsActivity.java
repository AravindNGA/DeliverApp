package com.home.deliver.deliveryupdate;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.home.deliver.deliveryupdate.mapsHelper.DirectionFinder;
import com.home.deliver.deliveryupdate.mapsHelper.DirectionFinderListener;
import com.home.deliver.deliveryupdate.mapsHelper.Route;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

    private GoogleMap mMap;
    private ProgressDialog progressDialog;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    double current_lat, current_lng,dest_lat,dest_lng;
    GPSLocationGetter gps;
    TextView coName;
    public static final String BUNDLE_STRING = "BUNDLE_STRING";
    public static final String BUNDLE_EXTRAN = "BUNDLE_EXTRAS";
    public static final String BUNDLE_NAME = "BUNDLE_INT";
    String currecntLocation,todestination;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        coName = findViewById(R.id.CoName);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        gps = new GPSLocationGetter(MapsActivity.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            current_lat = gps.getLatitude();
            current_lng = gps.getLongitude();

        } else {
            gps.showSettingsAlert();
        }

        String name = getIntent().getBundleExtra(BUNDLE_EXTRAN).getString(BUNDLE_STRING);
        todestination = getIntent().getBundleExtra(BUNDLE_EXTRAN).getString(BUNDLE_NAME);
        coName.setText(name);
        sendRequest();
    }

    // getting his map ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng currentLocation = new LatLng(current_lat, current_lng);
        /*originMarkers.add(mMap.addMarker(new MarkerOptions()
                .title("Im here!")
                .position(currentLocation)));*/
        String lats[] = todestination.split(",");
        dest_lat = Double.parseDouble(lats[0]);
        dest_lng = Double.parseDouble(lats[1]);
        LatLng destLatLang = new LatLng(dest_lat,dest_lng);
        //destinationMarkers.add(mMap.addMarker(new MarkerOptions().title("Destination").position(destLatLang)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    private void sendRequest() {
        currecntLocation = current_lat+","+current_lng;
        String origin = currecntLocation;
        String destination = todestination;
        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route1 : route) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route1.startLocation, 10));

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.red))
                    .title(route1.startAddress)
                    .position(route1.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.red))
                    .title(route1.endAddress)
                    .position(route1.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route1.points.size(); i++)
                polylineOptions.add(route1.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
}
