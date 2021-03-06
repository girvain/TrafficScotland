/**
 * Name: Gavin Ross
 * Matric No: S1821951
 *
 * Application Overview:
 * The app allows a user to get current incidents on the road, get current roadworks and get planned
 * roadworks. The user can search for roadworks or planned roadworks by date, which displays only
 * results that start BEFORE or ON the date input from user and end AFTER or ON the date input from
 * the user.  The results are displayed with a colour icon that represents a timeline of the
 * roadworks/planned roadworks/current incident. This code is “green” for the roadworks having a
 * length of 1 day, “amber” for 7 days and “red” for anything longer than this. The list item when
 * clicked, will display the roadworks/planned roadworks/current incident on the map in a new Activity
 * along with more detailed information about the data.
 */

package android.bignerdranch.trafficscotland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private RadioButton dateSelector;
    private RadioButton roadSelector;
    private RadioButton currentIncidentsSelector;
    private RadioButton currentRoadworksSelector;
    private RadioButton plannedRoadworksSelector;
    private RadioButton noneSelector;
    private TextView mErrorDisplay;
    private Button getFeed;
    private TextView userInput;
    private ArrayList<TrafficDataModel> mTrafficDataList = new ArrayList<TrafficDataModel>();
    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mProgressBar;
    private Parcelable mListState;

    private String currentRoadworksUrl = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String plannedRoadworksUrl = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    final String currentIncedentsUrl = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String dateAsString = "";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("user_input", userInput.getText().toString());

        if (noneSelector.isChecked()) {
            outState.putBoolean("noneState", true);
        } else {
            outState.putBoolean("noneState", false);
        }

        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable("traffic_data_state", mListState);
        outState.putParcelableArrayList("traffic_data", mTrafficDataList);
        Log.v("SAVE", "onSaveInstanceState");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Bundle outState = new Bundle();
        outState.putString("user_input", userInput.getText().toString());

        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable("traffic_data_state", mListState);
        outState.putParcelableArrayList("traffic_data", mTrafficDataList);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("GAV", "why is this fucked");

        getFeed = findViewById(R.id.get_feed_btn);
        dateSelector = findViewById(R.id.date_radio_btn);
        roadSelector = findViewById(R.id.road_radio_btn);
        noneSelector = findViewById(R.id.none_radio_button);
        mErrorDisplay = findViewById(R.id.error_display);
        currentRoadworksSelector = findViewById(R.id.current_roadworks_radio_btn);
        plannedRoadworksSelector = findViewById(R.id.planned_roadworks_radio_btn);
        currentIncidentsSelector = findViewById(R.id.current_incedents_radio_btn);
        mProgressBar = findViewById(R.id.progressBar);
        userInput = findViewById(R.id.user_input);


////        // Create list data for recycler view TEST DATA
//        for (int i = 0; i < 20; i++) {
//            //mWordList.addLast("Word " + i);
//            TrafficDataModel trafficDataModel = new TrafficDataModel();
//            trafficDataModel.setTitle("M74 Ardrishaig");
//            mTrafficDataList.add(trafficDataModel);
//        }
////

        if (savedInstanceState != null) {
            // This is required because the default state of user input is disabled on Activity
            // creation, so when a rotation happens with state the None radioButton not selected,
            // it keeps the userInput enabled with the users text input.
            boolean noneState = savedInstanceState.getBoolean("noneState");
            if (noneState) {
                userInput.setEnabled(false);
            } else {
                userInput.setEnabled(true);
            }

            // This is to collect the parcelized traffic data in the arrayList and set it up
            // before the activity is recreated
            mListState = savedInstanceState.getParcelable("traffic_data_state");
            mTrafficDataList = savedInstanceState.getParcelableArrayList("traffic_data");
            //mRecyclerView.getAdapter().notifyDataSetChanged();
        }

        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new WordListAdapter(this, mTrafficDataList);

        // Give the RecyclerView a default layout manager.
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        final Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog StartTime = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(year, monthOfYear, dayOfMonth);
////                dateInput.setText(dateFormatter.format(newDate.getTime()));
//                dateInput.setText(newDate.getTime().toString());

                dateAsString = Integer.toString(dayOfMonth) + "/" + Integer.toString((monthOfYear + 1)) + "/" + Integer.toString(year);
                userInput.setText(dateAsString);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        getFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectOption1 = ""; // none as the initial selection
                String selectOption2 = currentRoadworksUrl; // set this as initial selection
                String handlerSelection = ""; // set to either d or r for date or road
                if (dateSelector.isChecked()) {
                    selectOption1 = userInput.getText().toString();
                    handlerSelection = "d";
                    //new RssFeed(mRecyclerView).execute(currentRoadworksUrl, dateInput.getText().toString());
                } else if (roadSelector.isChecked()) {
                    selectOption1 = userInput.getText().toString();
                    handlerSelection = "r";
                } else if (noneSelector.isChecked()) {
                    selectOption1 = ""; // reset it to nothing incase the buffer has previous input
                }
                // Second check box selections for roadworks selection
                if (currentRoadworksSelector.isChecked()) {
                    selectOption2 = currentRoadworksUrl;
                } else if (plannedRoadworksSelector.isChecked()) {
                    selectOption2 = plannedRoadworksUrl;
                } else if (currentIncidentsSelector.isChecked()) {
                    selectOption2 = currentIncedentsUrl;
                }

                // check if an options has been selected for selectOption1
                if (selectOption1.isEmpty()) {
                    new RssFeed(mRecyclerView, mProgressBar, mTrafficDataList, mErrorDisplay).execute(selectOption2);
                } else {
                    new RssFeed(mRecyclerView, mProgressBar, mTrafficDataList, mErrorDisplay).execute(selectOption2, selectOption1, handlerSelection);
                }

                // close the keypad on button presses
                userInput.clearFocus();
                closeKeyboard(true);
            }
        });
        dateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTime.show();
                userInput.setEnabled(true);
            }
        });
        noneSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInput.setText("");
                userInput.setEnabled(false); // Disable user input when none is selected
            }
        });
        roadSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInput.setText("");
                userInput.setEnabled(true);
                userInput.setShowSoftInputOnFocus(true);
                //closeKeyboard(false);
            }
        });


    }

    private void closeKeyboard(boolean b) {
        View view = this.getCurrentFocus();
        if (b) {
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } else {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
    }
}
