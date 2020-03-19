package android.bignerdranch.trafficscotland;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TrafficDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_data2);

        TextView title = findViewById(R.id.traffic_act_title);
        TextView description = findViewById(R.id.traffic_act_desc);
        TextView startDate = findViewById(R.id.traffic_act_start_date);
        TextView endDate = findViewById(R.id.traffic_act_end_date);
        TextView link = findViewById(R.id.traffic_act_link);
        TextView rssgeo = findViewById(R.id.traffic_act_geo);
        TextView pubDate = findViewById(R.id.traffic_act_pub);
        TextView roadworksLn = findViewById(R.id.traffic_act_road_ln);

        Intent intent = getIntent();
        TrafficDataModel trafficDataModel = intent.getParcelableExtra("TRAFFIC_DATA");

        title.setText(trafficDataModel.getTitle());
        Log.v("TrafficActivity", trafficDataModel.getTitle());
        description.setText(trafficDataModel.getDescription());
        startDate.setText(trafficDataModel.getStartDateAsString());
        endDate.setText(trafficDataModel.getEndDateAsString());
        link.setText(trafficDataModel.getLink());
        rssgeo.setText(trafficDataModel.getGeorss());
        pubDate.setText(trafficDataModel.getPubDate());
        roadworksLn.setText(trafficDataModel.getRoadworksLength()+""); // convert to string
    }
}
