package apps.njl.gosafe;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import apps.njl.gosafe.RoomDB.Trip;


public class DriverHistoryAdapter extends RecyclerView.Adapter<DriverHistoryAdapter.HistoryHolder> {

    List<Trip> trips;
    Context context;

    public DriverHistoryAdapter(List<Trip> trips) {
        this.trips = trips;
    }

    /**Inflate one card**/
    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_history_fragment, parent, false);
        HistoryHolder historyHolder = new HistoryHolder(v);
        return historyHolder;
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, final int position) {

        holder.txt_date.setText("" + trips.get(position).getDateTime());
        holder.txt_total_time.setText("" + MapController.generateTimeString(trips.get(position).getTotalDuration()));
        holder.txt_destination.setText("" + trips.get(position).getRoute());
        holder.txt_distance.setText("" + MapController.generateDistanceString(trips.get(position).getTotalDistance()));
        holder.txt_speed.setText("" + MapController.generateSpeedString(trips.get(position).getAverageSpeed()));
        holder.txt_earnedScore.setText(trips.get(position).getEarned_score() + " Points");
        holder.txt_reducedScore.setText(trips.get(position).getReduced_score() + " Points");
        holder.txt_totalScore.setText(trips.get(position).getTotal_score() + " Points");

        holder.btn_speedMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HistorySpeedMap.class);
                intent.putExtra("speedMarkerList", trips.get(position).getSpeedMarkerList());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public static class HistoryHolder extends RecyclerView.ViewHolder {

        TextView txt_date, txt_destination;
        TextView txt_distance, txt_total_time, txt_speed;
        TextView txt_earnedScore, txt_reducedScore, txt_totalScore;
        Button btn_speedMap;

        public HistoryHolder(View itemView) {
            super(itemView);
            txt_date = itemView.findViewById(R.id.txt_historyfrag_date);
            txt_destination = itemView.findViewById(R.id.txt_historyfrag_destination);
            txt_distance = itemView.findViewById(R.id.txt_historyfrag_totdistance);
            txt_total_time = itemView.findViewById(R.id.txt_historyfrag_tottime);
            txt_speed = itemView.findViewById(R.id.txt_historyfrag_avgspeed);
            txt_earnedScore = itemView.findViewById(R.id.txt_historyfrag_earnedscore);
            txt_reducedScore = itemView.findViewById(R.id.txt_historyfrag_reducedscore);
            txt_totalScore = itemView.findViewById(R.id.txt_historyfrag_totalscore);
            btn_speedMap = itemView.findViewById(R.id.btn_history_map);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
