package com.example.workoutbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {
    Button fab;
    GoogleMap mMap;
    boolean a = false;

    SupportMapFragment mapFragment;
    GoogleMap googleM;
    boolean isPermisionGranted;
    private int GPS_REQUEST_CODE = 9001;
    CountDownTimer hi;
    Polyline polyline;
    private FusedLocationProviderClient client;
    boolean brreak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        fab = findViewById(R.id.button);
        checkMyPermission();
        initmap();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


    }

    private void initmap() {
        if (isGpsEnabled()) {
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }
    }

    private boolean isGpsEnabled() {
        LocationManager locationmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean providerEnable = locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(providerEnable){
            return true;
        }else{
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("GPS permision");
                alert.setMessage("Please enable GPS");
                alert.setPositiveButton("Yes",((dialogInterface,i) -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent,GPS_REQUEST_CODE);

            }))
            .setCancelable(false);
            alert.show();
            return false;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST_CODE){
            LocationManager locationmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean providerEnable = locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(providerEnable) {
            }else{
                Toast.makeText(this,"GPS is not enabled",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void checkMyPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPermisionGranted = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    @SuppressLint({"Missing Permission", "MissingPermission"})
    @Override

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(ContextCompat.getColor(getApplicationContext(),R.color.blue));
        polylineOptions.width(15);

                hi = new CountDownTimer(1000000000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mMap.setMyLocationEnabled(true);
                        client = new FusedLocationProviderClient(MapsActivity.this);
                        client.getLastLocation().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Location location = task.getResult();
                            LatLng location2 = new LatLng(location.getLatitude(), location.getLongitude());

                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location2, 18);
                            mMap.animateCamera(cameraUpdate);
                            mMap.moveCamera(cameraUpdate);
                            polylineOptions.add(location2);


                                polyline = mMap.addPolyline(polylineOptions);


                        }
                    });
                    }

                    @Override
                    public void onFinish() {
                        hi.cancel();
                    }
                };
                hi.start();
    }


        // Add a marker in Sydney and move the camera












    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


}