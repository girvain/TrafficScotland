package android.bignerdranch.trafficscotland;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
/**
 * Name: Gavin Ross
 * Matric No: S1821951
 */
import java.util.ArrayList;
import java.util.LinkedList;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.TrafficViewDataHolder> {

    private ArrayList<TrafficDataModel> mTrafficDataList;
    private LayoutInflater mInflater;
    private Context context;

    public WordListAdapter(Context context, ArrayList<TrafficDataModel> mTrafficDataList) {
        mInflater = LayoutInflater.from(context);
        this.mTrafficDataList = mTrafficDataList;
        this.context = context;
    }

    class TrafficViewDataHolder extends RecyclerView.ViewHolder {
        public TextView titleItemView;
        public ImageView iconImageView;
        public TextView startDateItemView;
        public TextView endDateItemView;
        final WordListAdapter mAdapter;

        public TrafficViewDataHolder(@NonNull View itemView, WordListAdapter adapter) {
            super(itemView);
            titleItemView = itemView.findViewById(R.id.title);
            iconImageView = itemView.findViewById(R.id.imageView);
            iconImageView.setImageResource(R.drawable.green);
            startDateItemView = itemView.findViewById(R.id.start_date);
            endDateItemView = itemView.findViewById(R.id.end_date);
            this.mAdapter = adapter;
        }
    }

    @Override
    public WordListAdapter.TrafficViewDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.traffic_date_item,
                parent, false);
        return new TrafficViewDataHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull final WordListAdapter.TrafficViewDataHolder holder, int position) {
        final TrafficDataModel mCurrent = mTrafficDataList.get(position);
        holder.titleItemView.setText(mCurrent.getTitle());
        holder.startDateItemView.setText(mCurrent.getStartDateAsString());
        holder.endDateItemView.setText(mCurrent.getEndDateAsString());
        setTrafficIcon(holder.iconImageView, mCurrent);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("onClick", "traffic data list item");
                Intent intent = new Intent(context, TrafficDataActivity.class);
                intent.putExtra("TRAFFIC_DATA", mCurrent);
                context.startActivity(intent);
            }
        });
    }

    /**
     * Method to set the Image resources of the imageView's on each item in the RecyclerView.
     * Sets the images to green for works that only last a day, amber for a week and red for any
     * longer.
     * @param view
     * @param tdm
     */
    public void setTrafficIcon(ImageView view, TrafficDataModel tdm) {
        if (tdm.getRoadworksLength() < 2) {
            view.setImageResource(R.drawable.green);
        } else if (tdm.getRoadworksLength() < 8) {
            view.setImageResource(R.drawable.orange);
        }
        else {
            view.setImageResource(R.drawable.red);
        }
    }

    @Override
    public int getItemCount() {
        return mTrafficDataList.size();
    }
}
