package com.example.healthcare;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    FloatingActionButton floating_action_button;
    TextInputEditText tvCity, tvState, tvCountry, tvPin, tvLocality;
    Button btn_submit;
    ImageView btn_back;
    String issue;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

        isOnline();

        tvCity = findViewById(R.id.tvCity);
        tvState = findViewById(R.id.tvState);
        btn_back=findViewById(R.id.btn_back);
        tvCountry = findViewById(R.id.tvCountry);
        tvPin = findViewById(R.id.tvPin);
        tvLocality = findViewById(R.id.tvLocality);
        floating_action_button=findViewById(R.id.floating_action_button);
        btn_submit=findViewById(R.id.btn_submit);
        issue=getIntent().getStringExtra("req_type");

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        progressDialog.setMessage("Fetching Your Location...");
        progressDialog.show();
        locationEnabled();
        getLocation();

        floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LocationActivity.this, "Updating Location........", Toast.LENGTH_SHORT).show();
                getLocation();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pinno=tvPin.getText().toString();
                String a="711112",b="711104";
                if (pinno.equals(a) || pinno.equals(b)){
                    Intent i=new Intent(LocationActivity.this,CheckoutActivity.class);
                    i.putExtra("issue", issue);
                    i.putExtra("location", tvLocality.getText().toString());
                    i.putExtra("city", tvCity.getText().toString());
                    i.putExtra("pin", tvPin.getText().toString());
                    startActivity(i);
                }
                else {
                    startActivity(new Intent(LocationActivity.this,Notserviceable.class));
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if(TextUtils.isEmpty(tvLocality.getText().toString())){
            getLocation();
        }

    }

    private void locationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            progressDialog.dismiss();
            new AlertDialog.Builder(LocationActivity.this)
                    .setTitle("Enable GPS Service")
                    .setMessage("We need your GPS to know your location.")
                    .setPositiveButton("Enable", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .show();
        }

    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            tvCity.setText(addresses.get(0).getLocality());
            tvState.setText(addresses.get(0).getAdminArea());
            tvCountry.setText(addresses.get(0).getCountryName());
            tvPin.setText(addresses.get(0).getPostalCode());
            tvLocality.setText(addresses.get(0).getAddressLine(0));
            progressDialog.dismiss();

        } catch (Exception e) {
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

//    @Override
//    public void onProviderDisabled(String provider) {
//        Toast.makeText(this,"Turn on GPS to continue.",Toast.LENGTH_SHORT).show();
//
//    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(LocationActivity.this, "No Internet connection! Turn On Your Internet to Continue", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}