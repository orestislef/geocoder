package gr.orestislef.geocoder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final long MIN_TIME_INTERVAL = 1;
    private static final float MIN_DISTANCE_INTERVAL = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 11888;
    RecyclerView resultsRV;
    ProgressBar loadingPB;
    CardView currentLocationCV;
    TextView currentLocationTV;
    LocationListener locationListener = location -> {
        // Handle the updated location here
        Log.d("lefk", "loc: "+location.getAltitude());
        GeocodeCurrentLocationThread geocodeCurrentLocationThread = new GeocodeCurrentLocationThread(this, new GeocodeCurrentLocationThread.GeocodeListener() {
            @Override
            public void onGeocodeCompleted(List<Address> addresses) {
                currentLocationCV.setVisibility(View.VISIBLE);
                currentLocationTV.setText(addresses.get(0).getAddressLine(0));
            }

            @Override
            public void onGeoCodeError(String localizedMessage) {
                currentLocationCV.setVisibility(View.GONE);
            }
        }, location);
        Thread thread = new Thread(geocodeCurrentLocationThread);
        thread.start();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkForGeocoder();

        checkForPermissions();

        loadingPB = findViewById(R.id.loadingPB);

        resultsRV = findViewById(R.id.resultsRV);
        resultsRV.setLayoutManager(new LinearLayoutManager(this));
        ResultsRVAdapter adapter = new ResultsRVAdapter(new ArrayList<>());
        resultsRV.setAdapter(adapter);

        EditText inputAddressET = findViewById(R.id.addressET);

        currentLocationCV = findViewById(R.id.currentLocCV);
        currentLocationTV = findViewById(R.id.currentLocTV);

        inputAddressET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                GeocodeThread geocodeThread = new GeocodeThread(MainActivity.this, new GeocodeThread.GeocodeListener() {
                    @Override
                    public void onGeocodeCompleted(List<Address> addresses) {
                        if (addresses == null) return;
                        ArrayList<MyResult> resultArrayList = new ArrayList<>();
                        for (Address address : addresses) {
                            MyResult result = new MyResult(address.getAddressLine(0), new LatLng(address.getLatitude(), address.getLongitude()));
                            resultArrayList.add(result);
                        }
                        adapter.add(resultArrayList);
                        loadingPB.setVisibility(View.GONE);
                        inputAddressET.setError(null);
                    }

                    @Override
                    public void onGeocodeLoading() {
                        inputAddressET.setError(null);
                        loadingPB.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onGeoCodeError(String localizedMessage) {
                        loadingPB.setVisibility(View.GONE);
                        inputAddressET.setError(localizedMessage);
                    }
                }, s.toString());

                Thread thread = new Thread(geocodeThread);
                thread.start();
            }
        });
    }

    private void checkForGeocoder() {
        if (!Geocoder.isPresent()) {
            //TODO: show that service is not implemented
        }
    }

    private void checkForPermissions() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            OnLocationPermissionDenied();
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            OnLocationPermissionGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, proceed with location-related functionality
                OnLocationPermissionGranted();
            } else {
                // Location permission denied, handle accordingly (e.g., show a message, disable location-related features)
                OnLocationPermissionDenied();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressLint("MissingPermission")
    private void OnLocationPermissionGranted() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_INTERVAL, MIN_DISTANCE_INTERVAL, locationListener);
    }

    private void OnLocationPermissionDenied() {
        //TODO: show Error in dialog
    }
}