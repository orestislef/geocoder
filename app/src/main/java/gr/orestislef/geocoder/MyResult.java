package gr.orestislef.geocoder;

public class MyResult {
    String address;
    LatLng latLng;

    public MyResult(String address, LatLng latLng) {
        this.address = address;
        this.latLng = latLng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
