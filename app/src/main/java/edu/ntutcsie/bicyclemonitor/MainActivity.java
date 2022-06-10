package edu.ntutcsie.bicyclemonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    /*Control components*/
    private Button btnGPSEnable;
    private boolean gpsStatus;

    /*Output components*/
    private TextView txtLocationMode, txtLocationLogitude, txtLocationLatitude;
    private TextView txtSpeed;

    /*GPS components*/
    private LocationManager gpsManager;
    private LocationListener gpsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gpsStatus = false;

        btnGPSEnable = findViewById(R.id.gps);
        btnGPSEnable.setOnClickListener(new BtnEvent());

        txtLocationMode = findViewById(R.id.mode);
        txtLocationLogitude = findViewById(R.id.longitude);
        txtLocationLatitude = findViewById(R.id.latitude);

        txtSpeed = findViewById(R.id.speed);
    }

    private class BtnEvent implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int btnID = view.getId();
            switch (btnID) {
                case R.id.gps:
                    if(gpsStatus == false) {
                        EnableGPS();
                        Button btn = (Button)view;
                        btn.setText(R.string.end);
                    }
                    else {
                        gpsManager.removeUpdates(gpsListener);
                        txtLocationLogitude.setText(R.string.GPSdisable);
                        txtLocationLatitude.setText(R.string.GPSdisable);
                        txtLocationMode.setText(R.string.GPSdisable);
                        Button btn = (Button)view;
                        btn.setText(R.string.start);
                        Toast.makeText(MainActivity.this, "GPS disable", Toast.LENGTH_LONG).show();
                        gpsStatus = false;
                    }
                    break;
            }
        }

        private void EnableGPS() {
            gpsManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            gpsListener = new LocationEvent();
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "GPS permission denied", Toast.LENGTH_LONG).show();
                return;
            }
            gpsManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
            gpsManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, gpsListener);
            Toast.makeText(MainActivity.this, "GPS enable", Toast.LENGTH_LONG).show();
            gpsStatus = true;
        }
    }

    private class LocationEvent implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            String positionMode = location.getProvider();

            txtLocationMode.setText("GPS Provider: " + positionMode);
            txtLocationLogitude.setText("經度: " + longitude);
            txtLocationLatitude.setText("緯度: " + latitude);

            txtSpeed.setText(Double.toString(location.getSpeed()));
        }
    }
}