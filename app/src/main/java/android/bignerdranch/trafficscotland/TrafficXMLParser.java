package android.bignerdranch.trafficscotland;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

public class TrafficXMLParser {

    XmlPullParserFactory factory;
    XmlPullParser xpp;

    public TrafficXMLParser(XmlPullParserFactory factory, XmlPullParser xpp) {
        this.factory = factory;
        this.xpp = xpp;
    }

    public String parseXML (String input)
            throws XmlPullParserException, IOException
    {

        String parsedString = "";
        xpp.setInput( new StringReader(input) );
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_DOCUMENT) {
                System.out.println("Start document");
                //parsedString += "Start document";
            } else if(eventType == XmlPullParser.START_TAG) {
                System.out.println("Start tag "+xpp.getName());
                //parsedString += xpp.getName();
            } else if(eventType == XmlPullParser.END_TAG) {
                System.out.println("End tag "+xpp.getName());
                //parsedString += xpp.getName();
            } else if(eventType == XmlPullParser.TEXT) {
                System.out.println("Text "+xpp.getText());
                parsedString += xpp.getText();
            }
            eventType = xpp.next();
        }

        return parsedString;
    }

}
