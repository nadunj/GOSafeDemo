package apps.njl.gosafe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.model.AutocompletePrediction;

import java.util.List;
import java.util.zip.Inflater;

import apps.njl.gosafe.R;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder> {
    private List<AutocompletePrediction> predictionList;
    private LayoutInflater mInflater;
    private LocationCallBack locationCallBack;

    public LocationAdapter(List<AutocompletePrediction> predictionList, Context context, LocationCallBack locationCallBack) {
        this.predictionList = predictionList;
        this.mInflater = LayoutInflater.from(context);
        this.locationCallBack = locationCallBack;
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.location_item, parent, false);
        return new LocationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationHolder holder, int position) {
        AutocompletePrediction prediction = predictionList.get(position);
        holder.txtLocation.setText(prediction.getFullText(null).toString());
        holder.itemView.setOnClickListener(v->locationCallBack.onPlaceTapped(prediction));
    }

    @Override
    public int getItemCount() {
        return predictionList.size();
    }

    public class LocationHolder extends RecyclerView.ViewHolder {
        private TextView txtLocation;
        public LocationHolder(@NonNull View itemView) {
            super(itemView);
            txtLocation = itemView.findViewById(R.id.txtLocation);
        }
    }

    public interface LocationCallBack{
        void onPlaceTapped(AutocompletePrediction autocompletePrediction);
    }
}
