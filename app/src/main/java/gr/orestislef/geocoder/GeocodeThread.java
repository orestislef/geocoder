package gr.orestislef.geocoder;

import static android.content.Context.LOCATION_SERVICE;

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
        new Handler(Looper.getMainLooper()).post(listener::onGeocodeLoading);
        Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationName, MAX_RESULTS);
            new Handler(Looper.getMainLooper()).post(() -> listener.onGeocodeCompleted(addresses));
        } catch (IOException e) {
            e.printStackTrace();
            new Handler(Looper.getMainLooper()).post(() -> listener.onGeoCodeError(e.getLocalizedMessage()));
        }
    }

    public interface GeocodeListener {
        void onGeocodeCompleted(List<Address> addresses);

        void onGeocodeLoading();

        void onGeoCodeError(String localizedMessage);
    }
}

