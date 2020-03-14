package android.bignerdranch.trafficscotland;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;

public class RssFeed extends AsyncTask<String, String, String> {

    private WeakReference<TextView> mTextView;
    private String result = "";

    RssFeed(TextView textView) {
        mTextView = new WeakReference<>(textView);
    }

    @Override
    protected String doInBackground(String... strings) {
        URL aurl;
        URLConnection yc;
        BufferedReader in = null;
        String inputLine = "";
        try
        {
            Log.e("MyTag","in try");
            aurl = new URL(strings[0]); // first string from input arg
            yc = aurl.openConnection();
            in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            //
            // Throw away the first 2 header lines before parsing
            //
            //
            //
            while ((inputLine = in.readLine()) != null) {
                result = result + inputLine;
                Log.e("MyTag",inputLine);

            }
            in.close();
        }
        catch (IOException ae)
        {
            Log.e("MyTag", "ioexception");
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        TrafficXMLParser trafficXMLParser = new TrafficXMLParser();
        trafficXMLParser.readEntry();
        mTextView.get().setText(s);
    }
}
