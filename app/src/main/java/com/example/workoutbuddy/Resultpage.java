package com.example.workoutbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.example.workoutbuddy.MapsActivity.ak;
import static com.example.workoutbuddy.MapsActivity.b;
import static com.example.workoutbuddy.MapsActivity.c;
import static com.example.workoutbuddy.MapsActivity.d;

public class Resultpage extends AppCompatActivity {
    TextView distancec;
    TextView timeb;
    TextView speedb;
    TextView avgspeedb;

    CountDownTimer hi;
    boolean a = false;
    double distanceab;
    double distanceb;
    double distance;
    double odistance = 0;
    double prevlat;
    double prevlong;
    double speed;
    int amount = 0;
    double allspeed = 0;
    double avgspeed;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultpage);
        distancec = findViewById(R.id.distance);
        timeb = findViewById(R.id.time);
        speedb = findViewById(R.id.speed);
        avgspeedb = findViewById(R.id.avgspeed);
        Intent intent = getIntent();

        double distancea = Math.round(intent.getDoubleExtra(ak, 0) * 100.0) / 100.0;
        double avgspeeda = Math.round(intent.getDoubleExtra(c, 0) * 10.0) / 10.0;
        double speeda = Math.round(intent.getDoubleExtra(b, 0) * 10.0) / 10.0;
        int timea = intent.getIntExtra(d, 0);

        distancec.setText("distance : " + distancea + " miles");
        avgspeedb.setText("average speed : " + avgspeeda + " mph");
        speedb.setText("speed : " + speeda + " mph");
        timeb.setText("time : " + timea + "secs");
        odistance = distancea;
        amount = timea;
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        hi = new CountDownTimer(1000000000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (a == false) {
                    amount++;
                }
                if (a == true) {
                    prevlat = location.getLatitude();
                    prevlong = location.getLongitude();
                }
                if (ActivityCompat.checkSelfPermission(Resultpage.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Resultpage.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (a == true) {
                    distanceab = Math.abs(prevlat - location.getLatitude());
                    distanceb = Math.abs(prevlong - location.getLongitude());
                    distance = Math.sqrt(((distanceab * 69) * (distanceab * 69)) + ((distanceb * 54.6) * (distanceb * 54.6)));
                    allspeed = allspeed + distance;
                    odistance = odistance + distance;
                    if (amount % 4 == 1) {
                        if (amount == 1) {
                            speed = allspeed * 3600;
                            allspeed = 0;
                        } else {
                            speed = (allspeed * 3600) / 4;
                            allspeed = 0;
                        }
                    }

                    avgspeed = distance / amount;
                    amount++;
                }
                distancec.setText("distance : " + /*Math.round(*/odistance/*100.0)/100.0*/+ " miles");
                avgspeedb.setText("average speed : " + /*Math.round(*/avgspeed/*100.0)/100.0*/ + " mph");
                speedb.setText("speed : " + /*Math.round(*/speed/*100.0)/100.0*/ + " mph");
                timeb.setText("time : " + amount + "secs");
                a = true;









            }



            @Override
            public void onFinish() {
                hi.cancel();
            }
        };
        hi.start();
    }
}