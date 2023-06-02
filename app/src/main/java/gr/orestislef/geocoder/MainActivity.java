package gr.orestislef.geocoder;

import android.location.Address;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView resultsRV;
    ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingPB = findViewById(R.id.loadingPB);

        resultsRV = findViewById(R.id.resultsRV);
        resultsRV.setLayoutManager(new LinearLayoutManager(this));
        ResultsRVAdapter adapter = new ResultsRVAdapter(new ArrayList<>());
        resultsRV.setAdapter(adapter);

        EditText inputAddressET = findViewById(R.id.addressET);

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
                        if (addresses == null)
                            return;
                        ArrayList<MyResult> resultArrayList = new ArrayList<>();
                        for (Address address : addresses) {
                            MyResult result = new MyResult(
                                    address.getAddressLine(0),
                                    new LatLng(
                                            address.getLatitude(),
                                            address.getLongitude()));
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
}