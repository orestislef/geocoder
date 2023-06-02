package gr.orestislef.geocoder;

import android.location.Address;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView resultsRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                GeocodeThread geocodeThread = new GeocodeThread(getApplicationContext(), addresses -> {
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
                }, s.toString());

                Thread thread = new Thread(geocodeThread);
                thread.start();
            }
        });
    }
}