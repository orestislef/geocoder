package gr.orestislef.geocoder;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.List;

public class GeocodeThread implements Runnable {
    private final Context context;
    private final GeocodeListener listener;
    private final String locationName;

    int MAX_RESULTS = 5;

    public GeocodeThread(Context context, GeocodeListener listener, String locationName) {
        this.context = context;
        this.listener = listener;
        this.locationName = locationName;
    }

    @Override
    public void run() {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(locationName, MAX_RESULTS);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final List<Address> finalAddresses = addresses;
        new  Handler(Looper.getMainLooper()).post(() -> listener.onGeocodeCompleted(finalAddresses));
    }

    public interface GeocodeListener {
        void onGeocodeCompleted(List<Address> addresses);
    }
}

