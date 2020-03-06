package com.example.samplegps;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    Switch btnShowLocation;
    Boolean toggle =null;
    String locationAddress;

    AppLocationService appLocationService;

    private LocationManager locationManager;
    private LocationListener listener;
String uri1;
  /*  Button btnShowLocation;*/
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
   /* private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000) // 5 seconds
            *//*.setFastestInterval(16) // 16ms = 60fps*//*
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);*/

    // GPSTracker class
    GPSTracker gps;
    TextView imei_number, mylocation;
    String IMEI_NUMBER_HOLDER;
    TelephonyManager telephonyManager;
    private static final int REQUEST_CODE = 101;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imei_number = findViewById(R.id.editText2);
        mylocation = findViewById(R.id.location);
        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        appLocationService = new AppLocationService(
                MainActivity.this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {



                LocationAddress locationAddress = new LocationAddress();
                locationAddress.getAddressFromLocation(location.getLatitude(), location.getLongitude(),
                        getApplicationContext(), new GeocoderHandler());


                mylocation.setText("\n " + location.getLongitude() + " " + location.getLatitude() + "\n" + locationAddress);

            }




            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                mylocation.setText("");
            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {


            }
        };



        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnShowLocation =findViewById(R.id.switch2);

        btnShowLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {



                    if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);

                        // check if GPS enabled


                        return;
                    }
                    IMEI_NUMBER_HOLDER = telephonyManager.getImei();
                    imei_number.setText(IMEI_NUMBER_HOLDER);
                    /*gps = new GPSTracker(MainActivity.this);

                    // check if GPS enabled
                    if(gps.canGetLocation()){

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        mylocation.setText("Latitude:"+latitude +" \n " +"Longitude:"+ longitude);
                        // \n is for new line
                        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                                + latitude + "\nLong: " + longitude+"\nIMEI:"+ IMEI_NUMBER_HOLDER, Toast.LENGTH_LONG).show();
                    }else{
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
                    }*/


                    Location location = appLocationService
                            .getLocation(LocationManager.GPS_PROVIDER);

                    //you can hard-code the lat & long if you have issues with getting it
                    //remove the below if-condition and use the following couple of lines
                    //double latitude = 37.422005;
                    //double longitude = -122.084095

                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        uri1 = "http://maps.google.com/?q="+location.getLatitude()+","+location.getLongitude();

                        locationManager.requestLocationUpdates("gps", 5000, 0, listener);


                       /* LocationAddress locationAddress = new LocationAddress();
                        locationAddress.getAddressFromLocation(latitude, longitude,
                                getApplicationContext(), new GeocoderHandler());*/
                        } else {
                        showSettingsAlert();
                    }


                }


                   /* Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    try {
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        street=addresses.get(0).getAddressLine(0);
                        local=addresses.get(0).getSubAdminArea();
                        StateName= addresses.get(0).getAdminArea();
                        CityName = addresses.get(0).getLocality();
                        CountryName = addresses.get(0).getCountryName();
                        premises=addresses.get(0).getPremises();
                        code=addresses.get(0).getPostalCode();
                        // you can get more details other than this . like country code, state code, etc.
                        System.out.println(" StateName " + StateName);
                        System.out.println(" CityName " + CityName);
                        System.out.println(" CountryName " + CountryName);
                        System.out.println(" premises " + premises);

                        add2=street+"\n"+local+"\n"+StateName+"\n"+CountryName+"\n"+code+"";
                        System.out.println("address :"+add2);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
*/

               else if (!isChecked) {

                    imei_number.setText("");
                    mylocation.setText("");

                    locationManager.removeUpdates(listener);




                }


            }
        });
    }
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        MainActivity.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    private class GeocoderHandler extends Handler {

            @Override
            public void handleMessage(Message message) {

                String locationAddress;

                switch (message.what) {
                    case 1:
                        Bundle bundle = message.getData();
                        locationAddress = bundle.getString("address");
                        break;
                    default:

                        locationAddress = null;
                }


                mylocation.setText(locationAddress);


            }
        }

    }
