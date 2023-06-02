package gr.orestislef.geocoder;

import android.annotation.SuppressLint;
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
        return new ResultsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ResultsViewHolder mHolder = (ResultsViewHolder) holder;
        MyResult currentItem = results.get(position);
        mHolder.addressTV.setText(currentItem.address);
        String latTXT = "lat: " + currentItem.getLatLng().getLatitude();
        mHolder.latTV.setText(latTXT);
        String lngTXT = "lng: " + currentItem.getLatLng().getLongitude();
        mHolder.lngTV.setText(lngTXT);
    }

    @Override
    public int getItemCount() {
        if (results == null)
            return 0;
        return results.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void add(ArrayList<MyResult> results) {
        if (this.results != null)
            this.results.clear();
        if (this.results == null)
            this.results = new ArrayList<>();
        this.results.addAll(results);
        notifyDataSetChanged();
    }

    public static class ResultsViewHolder extends RecyclerView.ViewHolder {
        TextView addressTV, latTV, lngTV;

        public ResultsViewHolder(@NonNull View itemView) {
            super(itemView);
            addressTV = itemView.findViewById(R.id.addressTV);
            latTV = itemView.findViewById(R.id.latTV);
            lngTV = itemView.findViewById(R.id.lngTV);

        }
    }
}
