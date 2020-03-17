package android.bignerdranch.trafficscotland;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.TrafficViewDataHolder> {

    //private final LinkedList<String> mWordList; // passed in from mainActivity
    private LinkedList<TrafficDataModel> mTrafficDataList;
    private LayoutInflater mInflater;

//    public WordListAdapter(Context context, LinkedList<String> mWordList) {
//        mInflater = LayoutInflater.from(context);
//        this.mWordList = mWordList;
//    }
    public WordListAdapter(Context context, LinkedList<TrafficDataModel> mTrafficDataList) {
        mInflater = LayoutInflater.from(context);
        this.mTrafficDataList = mTrafficDataList;
    }

//    class WordViewHolder extends RecyclerView.ViewHolder {
//
//        public final TextView wordItemView;
//        final WordListAdapter mAdapter;
//
//        public WordViewHolder(@NonNull View itemView, WordListAdapter adapter) {
//            super(itemView);
//            wordItemView = itemView.findViewById(R.id.word);
//            this.mAdapter = adapter;
//        }
//    }

    class TrafficViewDataHolder extends RecyclerView.ViewHolder {
        public TextView titleItemView;
        public TextView startDateItemView;
        public TextView endDateItemView;
        final WordListAdapter mAdapter;

        public TrafficViewDataHolder(@NonNull View itemView, WordListAdapter adapter) {
            super(itemView);
            titleItemView = itemView.findViewById(R.id.title);
            startDateItemView = itemView.findViewById(R.id.start_date);
            endDateItemView = itemView.findViewById(R.id.end_date);
            this.mAdapter = adapter;
        }
    }

//    @Override
//    public WordListAdapter.WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View mItemView = mInflater.inflate(R.layout.wordlist_item,
//                parent, false);
//        return new WordViewHolder(mItemView, this);
//    }
    @Override
    public WordListAdapter.TrafficViewDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.traffic_date_item,
                parent, false);
        return new TrafficViewDataHolder(mItemView, this);
    }

//    @Override
//    public void onBindViewHolder(@NonNull WordListAdapter.WordViewHolder holder, int position) {
//        String mCurrent = mWordList.get(position);
//        holder.wordItemView.setText(mCurrent);
//    }
    @Override
    public void onBindViewHolder(@NonNull WordListAdapter.TrafficViewDataHolder holder, int position) {
        TrafficDataModel mCurrent = mTrafficDataList.get(position);
        holder.titleItemView.setText(mCurrent.getTitle());
        holder.startDateItemView.setText(mCurrent.getStartDateAsString());
        holder.endDateItemView.setText(mCurrent.getEndDateAsString());
    }

    @Override
    public int getItemCount() {
        return mTrafficDataList.size();
    }
}
