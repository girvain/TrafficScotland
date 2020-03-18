package android.bignerdranch.trafficscotland;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;

public class RssFeed extends AsyncTask<String, String, String> {

    private WeakReference<TextView> mTextView;
    private WeakReference<RecyclerView> mRecyclerView;

    private LinkedList<TrafficDataModel> mTrafficDataList;
    private String result = "";
    private String userInput = "";
    private String handlerSelection = "";
    private DateConvertor dateConvertor; // used to parse userInputDate to Calendar object

    RssFeed(TextView textView) {
        mTextView = new WeakReference<>(textView);
    }

    RssFeed() {
        // This is for test use only
    }

    RssFeed(RecyclerView recyclerView) {
        mRecyclerView = new WeakReference<>(recyclerView);
        dateConvertor = new DateConvertor();
    }


    @Override
    protected String doInBackground(String... strings) {
        URL aurl;
        URLConnection yc;
        BufferedReader in = null;
        String inputLine = "";
        // if the strings has a second value of the date or road from the user
        if (strings.length > 1) {
            String date = strings[1]; // Date input from user
            userInput = strings[1]; // pass the user input date to variable so onPostExecute can access it
            handlerSelection = strings[2];
            Log.v("DATE FROM USER", date);
        } else {
            // clear the userInput string incase there was a previous entry
            userInput = "";
        }

        try {

            Log.e("MyTag","in try");
            aurl = new URL(strings[0]); // first string from input arg
            yc = aurl.openConnection();
            in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            //
            // Throw away the first 2 header lines before parsing
            //

            while ((inputLine = in.readLine()) != null) {
                result = result + inputLine;
                Log.e("MyTag",inputLine);

            }
            in.close();
        }
        catch (IOException ae)
        {
            Log.e("MyTag", ae.toString());
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        String parsedString = "";
        mTrafficDataList = new LinkedList<>();

//        try {
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            factory.setNamespaceAware(true);
//            XmlPullParser xpp = factory.newPullParser();
//
//            xpp.setInput(new StringReader(s));
//            int eventType = xpp.getEventType();
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                if (eventType == XmlPullParser.START_DOCUMENT) {
//
//                } else if (eventType == XmlPullParser.START_TAG) {
//
//                } else if (eventType == XmlPullParser.END_TAG) {
//
//                } else if (eventType == XmlPullParser.TEXT) {
//
//                    parsedString += xpp.getText();
//                }
//                eventType = xpp.next();
//            }
//            System.out.println("End document");
//        } catch (Exception e) {
//            Log.e("PARSE FAIL", "Gavin Ross");
//        }

        TrafficXMLParser trafficXMLParser = new TrafficXMLParser();
        try {
            mTrafficDataList = trafficXMLParser.parse(s);
            // if there is a present input from the user, filter the data
            if (!userInput.isEmpty()) {
                // select the right filter method from accessing the handlerSelection value
                if (handlerSelection.equals("d")) {
                    mTrafficDataList = filterByDate(dateConvertor.convertStringToDate(userInput), mTrafficDataList);
                } else if (handlerSelection.equals("r")) {
                    mTrafficDataList = filterByRoad(userInput, mTrafficDataList);
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Create an adapter and supply the data to be displayed.
        WordListAdapter mAdapter = new WordListAdapter(mRecyclerView.get().getContext(), mTrafficDataList);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.get().setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.get().setLayoutManager(new LinearLayoutManager(mRecyclerView.get().getContext()));

        //mTextView.get().setText(parsedString);

    }

    public LinkedList<TrafficDataModel> filterByDate(Calendar date, LinkedList list) {
        LinkedList<TrafficDataModel> filteredTrafficDataList = new LinkedList<>();
        Iterator<TrafficDataModel> dataModelIterator = list.iterator();
        while (dataModelIterator.hasNext()) {
            TrafficDataModel trafficDataModel = dataModelIterator.next();
            // if the date is in the range of the trafficData's start and end date's range
            if (date.compareTo(trafficDataModel.getStartDate()) >= 0 &&
                    date.compareTo(trafficDataModel.getEndDate()) <= 0) {
                filteredTrafficDataList.add(trafficDataModel);
            }
        }
        return filteredTrafficDataList;
    }

    public LinkedList<TrafficDataModel> filterByRoad(String road, LinkedList list) {
        LinkedList<TrafficDataModel> filteredTrafficDataList = new LinkedList<>();
        Iterator<TrafficDataModel> dataModelIterator = list.iterator();
        while (dataModelIterator.hasNext()) {
            TrafficDataModel trafficDataModel = dataModelIterator.next();
            try {
                if (trafficDataModel.getTitle().contains(road)) {
                    filteredTrafficDataList.add(trafficDataModel);
                }
            } catch (Exception e) {

            }
        }
        return filteredTrafficDataList;
    }
}
