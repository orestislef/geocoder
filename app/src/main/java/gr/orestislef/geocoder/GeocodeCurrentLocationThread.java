package gr.orestislef.geocoder;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.List;

public class GeocodeCurrentLocationThread implements Runnable {
    private final Context context;
    private final GeocodeListener listener;
    private final Location location;

    public GeocodeCurrentLocationThread(Context context, GeocodeListener listener, Location location) {
        this.context = context;
        this.listener = listener;
        this.location = location;
    }

    @Override
    public void run() {
        Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            new Handler(Looper.getMainLooper()).post(() -> listener.onGeocodeCompleted(addresses));
        } catch (IOException e) {
            new Handler(Looper.getMainLooper()).post(() -> listener.onGeoCodeError(e.getLocalizedMessage()));
        }
    }

    public interface GeocodeListener {
        void onGeocodeCompleted(List<Address> addresses);

        void onGeoCodeError(String localizedMessage);
    }
}
