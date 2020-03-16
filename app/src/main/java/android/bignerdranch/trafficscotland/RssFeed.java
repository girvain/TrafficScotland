package android.bignerdranch.trafficscotland;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.LinkedList;

public class RssFeed extends AsyncTask<String, String, String> {

    private WeakReference<TextView> mTextView;

    private WeakReference<RecyclerView> mRecyclerView;

    private String result = "";

    RssFeed(TextView textView) {
        mTextView = new WeakReference<>(textView);
    }

    RssFeed(RecyclerView recyclerView) {
        mRecyclerView = new WeakReference<>(recyclerView);
    }


    @Override
    protected String doInBackground(String... strings) {
        URL aurl;
        URLConnection yc;
        BufferedReader in = null;
        String inputLine = "";
        String date = strings[1]; // Date input from user
        Log.v("DATE FROM USER", date);
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
        LinkedList<TrafficDataModel> mTrafficDataList = new LinkedList<>();

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
}
