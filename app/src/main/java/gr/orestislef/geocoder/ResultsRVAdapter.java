package gr.orestislef.geocoder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ResultsRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<MyResult> results;

    public ResultsRVAdapter(ArrayList<MyResult> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        view = layoutInflater.inflate(R.layout.single_result, parent, false);
        return new ResutlsViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ResutlsViewHoler mHolder = (ResutlsViewHoler) holder;
        MyResult currentItem = results.get(position);
        mHolder.adressTV.setText(currentItem.address);
        String latTXT = "lat: " + currentItem.lat;
        mHolder.latTV.setText(latTXT);
        String lngTXT = "lng: " + currentItem.lng;
        mHolder.lngTV.setText(lngTXT);
    }

    @Override
    public int getItemCount() {
        if (results == null)
            return 0;
        return results.size();
    }

    public void add(ArrayList<MyResult> results) {
        if (this.results != null)
            this.results.clear();
        if (this.results==null)
            this.results = new ArrayList<>();
        this.results.addAll(results);
        notifyDataSetChanged();
    }

    public static class ResutlsViewHoler extends RecyclerView.ViewHolder {
        TextView adressTV, latTV, lngTV;

        public ResutlsViewHoler(@NonNull View itemView) {
            super(itemView);
            adressTV = itemView.findViewById(R.id.adressTV);
            latTV = itemView.findViewById(R.id.latTV);
            lngTV = itemView.findViewById(R.id.lngTV);

        }
    }
}
