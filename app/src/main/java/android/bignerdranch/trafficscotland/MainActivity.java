package android.bignerdranch.trafficscotland;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RadioButton dateSelector;
    private RadioButton roadSelector;
    private Button getFeed;
    private TextView dateInput;
    //private LinkedList<String> mWordList = new LinkedList<>();
    private LinkedList<TrafficDataModel> mTrafficDataList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;

    private String currentRoadworksUrl = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String futureRoadworksUrl = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    final String currentIncedentsUrl = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String dateAsString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFeed = findViewById(R.id.get_feed_btn);
        dateSelector = findViewById(R.id.date_radio_btn);
        roadSelector = findViewById(R.id.road_radio_btn);
        dateInput = findViewById(R.id.date_input);


//        // Create list data for recycler view
//        for (int i = 0; i < 20; i++) {
//            //mWordList.addLast("Word " + i);
//            TrafficDataModel trafficDataModel = new TrafficDataModel();
//            trafficDataModel.setTitle("M74 Ardrishaig");
//            mTrafficDataList.addLast(trafficDataModel);
//        }
//
//        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview);
//        // Create an adapter and supply the data to be displayed.
//        mAdapter = new WordListAdapter(this, mTrafficDataList);
//        // Connect the adapter with the RecyclerView.
//        mRecyclerView.setAdapter(mAdapter);
//        // Give the RecyclerView a default layout manager.
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        final Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog StartTime = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(year, monthOfYear, dayOfMonth);
////                dateInput.setText(dateFormatter.format(newDate.getTime()));
//                dateInput.setText(newDate.getTime().toString());

                dateAsString = Integer.toString(dayOfMonth) + "/" + Integer.toString((monthOfYear+1)) + "/" + Integer.toString(year);
                dateInput.setText(dateAsString);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        getFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateSelector.isChecked()) {
                    //String[] input = {currentIncedentsUrl, dateInput.getText().toString()};
                    new RssFeed(mRecyclerView).execute(currentRoadworksUrl, dateInput.getText().toString());
                }
            }
        });
        dateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTime.show();
            }
        });


    }

}
