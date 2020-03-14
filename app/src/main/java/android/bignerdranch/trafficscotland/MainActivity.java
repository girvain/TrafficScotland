package android.bignerdranch.trafficscotland;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView rssFeedText;
    private Button resultsBtn;
    private RadioButton dateSelector;
    private String currentRoadworksUrl = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String futureRoadworksUrl = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    final String currentIncedentsUrl = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button getFeed = findViewById(R.id.get_feed_btn);
        final RadioButton dateSelecter = findViewById(R.id.date_radio_btn);
        RadioButton roadSelector = findViewById(R.id.road_radio_btn);
        final TextView dateInput = findViewById(R.id.date_input);
        rssFeedText = findViewById(R.id.rss_feed);
        resultsBtn = findViewById(R.id.get_results_btn);


        final Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog StartTime = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
//                dateInput.setText(dateFormatter.format(newDate.getTime()));
                dateInput.setText(newDate.getTime().toString());
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        getFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateSelecter.isActivated()) {
                    new RssFeed(rssFeedText).execute(currentRoadworksUrl);
                }
            }
        });
        dateSelecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTime.show();
            }
        });

        resultsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RssFeed(rssFeedText).execute(currentRoadworksUrl);
            }
        });
    }

}
