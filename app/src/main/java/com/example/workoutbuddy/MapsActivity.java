package com.example.workoutbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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
    double distancea;
    double distanceb;
    double distance;
    double odistance = 0;
    double prevlat;
    double prevlong;
    double speed;
    int amount=1;
    int oamount=0;
    double allspeed = 0;
    double avgspeed;
    LatLng location2 ;
    double resdistance;
    double resavgspeed;
    double resspeed;
    double resamt;
    Location location;
    Button stats;
    Button endrun;
    SupportMapFragment mapFragment;
    GoogleMap googleM;
    public static final String ak = "Akshay.g";
    public static final String b = "Adithya.g";
    public static final String c = "Jayashree.k";
    public static final String d = "Giri.s";
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
        fab = findViewById(R.id.endrun);
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
        }else if (requestCode == 0) {
            odistance = Math.round(data.getDoubleExtra("retDistance", 0.0) * 100.0) / 100.0;
            avgspeed = Math.round(data.getDoubleExtra("retAvgSpeed", 0.0) * 10.0) / 10.0;
            speed = Math.round(data.getDoubleExtra("retSpeed", 0.0) * 10.0) / 10.0;
            amount = data.getIntExtra("retAmt", 0);
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
        stats = findViewById(R.id.stats);
        endrun = findViewById(R.id.endrun);


                hi = new CountDownTimer(1000000000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                        PackageManager.PERMISSION_GRANTED) {
                            googleMap.setMyLocationEnabled(true);
                            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                        } else {

                        }

                        client = new FusedLocationProviderClient(MapsActivity.this);
                        client.getLastLocation().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                if(a == false){
                                    amount = 1;
                                }

                                if(a == true){
                                   prevlat = location.getLatitude();

                                   prevlong = location.getLongitude();
                                }
                                location = task.getResult();
                                location2 = new LatLng(location.getLatitude(), location.getLongitude());
                                if(a == true){
                                    distancea = Math.abs(prevlat - location.getLatitude());
                                    distanceb = Math.abs(prevlong - location.getLongitude());
                                    distance = Math.sqrt(((distancea*69)*(distancea*69))+((distanceb*54.6)*(distanceb*54.6)));
                                    allspeed = allspeed + distance;
                                    odistance = odistance+distance;
                                    if(amount %4 ==1){
                                        if(amount == 1){
                                            speed = allspeed*3600;
                                            allspeed = 0;
                                        }else {
                                            speed = (allspeed * 3600) / 4;
                                            allspeed = 0;
                                        }
                                    }

                                    avgspeed = distance/(amount*3600);
                                    amount++;
                                }

                                stats.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MapsActivity.this,Resultpage.class);

                                        intent.putExtra("distance",odistance);
                                        intent.putExtra("speed",speed);
                                        intent.putExtra("avgspeed",avgspeed);
                                        intent.putExtra("amt",amount);

                                        startActivityForResult(intent,0);

                                    }
                                });
                                endrun.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location2, 10);
                                        ConstraintLayout layout = findViewById(R.id.PopUpConstraint);
                                        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                        View popview = inflater.inflate(R.layout.endrunquestonare, null);

                                        final PopupWindow pop = new PopupWindow(popview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                        pop.setOutsideTouchable(false);
                                        pop.setFocusable(true);
                                        pop.showAtLocation(layout, Gravity.CENTER, 0, 0);
                                        Button yes = popview.findViewById(R.id.yes);
                                        Button no = popview.findViewById(R.id.no);
                                        yes.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                hi.cancel();
                                                Intent intent2 = new Intent(MapsActivity.this,Endrun.class);
                                                intent2.putExtra("distanceforend",odistance);
                                                intent2.putExtra("speedforend",speed);
                                                intent2.putExtra("avgspeedforend",avgspeed);
                                                intent2.putExtra("amountforend",amount);
                                                startActivity(intent2);
                                            }
                                        });
                                        no.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                pop.dismiss();
                                            }
                                        });


                                    }
                                });


                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location2, 18);
                                mMap.animateCamera(cameraUpdate);
                                mMap.moveCamera(cameraUpdate);


                                    if (a == true) {
                                        polylineOptions.add(location2);
                                        polyline = mMap.addPolyline(polylineOptions);
                                    }
                                a = true;
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