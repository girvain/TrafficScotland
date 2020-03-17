package android.bignerdranch.trafficscotland;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class RssFeedTest {



    RssFeed rssFeed = new RssFeed();
    @Test
    public void doInBackground() {
        //assertEquals("STring" instanceof String, "string" instanceof  String);
    }

    @Test
    public void filterByDate() {
        LinkedList<TrafficDataModel> testData = new LinkedList<>();


        Calendar testDate = Calendar.getInstance();
        testDate.set(2020,1,1);
        //assertEquals(new LinkedList<TrafficDataModel>().isEmpty(), rssFeed.filterByDate(testDate), testData);
    }
}