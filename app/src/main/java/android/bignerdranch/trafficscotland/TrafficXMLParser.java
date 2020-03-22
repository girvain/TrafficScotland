/**
 * Name: Gavin Ross
 * Matric No: S1821951
 */
package android.bignerdranch.trafficscotland;

import android.os.Parcel;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class TrafficXMLParser {

    private static final String ns = null;
    ArrayList<TrafficDataModel> trafficDataList = new ArrayList<>();
    private DateConvertor dateConvertor;

    public TrafficXMLParser() {
        dateConvertor = new DateConvertor();
    }

    public ArrayList<TrafficDataModel> parse(String string) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        String parsedResult = "";

        xpp.setInput( new StringReader (string) );
        int eventType = xpp.getEventType();


        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_DOCUMENT) {
                System.out.println("Start document");
            } else if(eventType == XmlPullParser.START_TAG) {
                if (xpp.getName().equals("item")) {
                    TrafficDataModel trafficDataObj = new TrafficDataModel();
                    eventType = xpp.nextTag();
                    if (xpp.getName().equals("title")) {
                        eventType = xpp.next();
                        trafficDataObj.setTitle(xpp.getText());
                        Log.v("GAVINROSS", xpp.getText());
                        eventType = xpp.nextTag(); // </title> end tag
                        eventType = xpp.nextTag(); // <description>
                        //Log.v("GAVINROSS", xpp.getName());

                    }
                    if (xpp.getName().equals("description")) {
                        eventType = xpp.next();
                        trafficDataObj.setDescription(getDescription(xpp.getText()));// use the getDesc to extract
                        // extract the long versions of start and end date from description
                        // if it's the current incedents feed, theres no dates so it returns null
                        String[] startAndEndDates = getDates(xpp.getText());
                        if (startAndEndDates != null) {
                            // convert each one to Calendar object and add to trafficDataObj
                            trafficDataObj.setStartDate(dateConvertor.convertRssDateToCalendarObj(startAndEndDates[0]));
                            trafficDataObj.setEndDate(dateConvertor.convertRssDateToCalendarObj(startAndEndDates[1]));
                            // Convert long date to short version and add to trafficDataObj (String)
                            trafficDataObj.setStartDateAsString(dateConvertor.convertLongDateToShort(startAndEndDates[0]));
                            trafficDataObj.setEndDateAsString(dateConvertor.convertLongDateToShort(startAndEndDates[1]));
                            // set the length of time the roadworks will last
                            trafficDataObj.setRoadworksLength(dateConvertor.numberOfDays(trafficDataObj.getStartDate(), trafficDataObj.getEndDate()));
                        }
//                        Log.v("startDate", trafficDataObj.getStartDate());
//                        Log.v("endDate", trafficDataObj.getEndDate());

                        Log.v("GAVINROSS", xpp.getText());
                        eventType = xpp.nextTag(); // </title> end tag
                        eventType = xpp.nextTag(); // <... next tag

                    }
                    if (xpp.getName().equals("link")) {
                        eventType = xpp.next();
                        trafficDataObj.setLink(xpp.getText());
                        Log.v("GAVINROSS", xpp.getText());
                        eventType = xpp.nextTag(); // </title> end tag
                        eventType = xpp.nextTag(); // <description>
                        //Log.v("GAVINROSS", xpp.getName());
                    }
                    if (xpp.getName().equals("point")) {
                        eventType = xpp.next();
                        trafficDataObj.setGeorss(xpp.getText());
                        Log.v("GAVINROSS", xpp.getText());
                        eventType = xpp.nextTag(); // </title> end tag
                        eventType = xpp.nextTag(); // <description>
                        //Log.v("GAVINROSS", xpp.getName());
                    }
                    eventType = xpp.nextTag(); // </title> end tag
                    eventType = xpp.nextTag(); // <description>
                    eventType = xpp.nextTag(); // </title> end tag
                    eventType = xpp.nextTag(); // <description>
                    if (xpp.getName().equals("pubDate")) {
                        eventType = xpp.next();
                        trafficDataObj.setPubDate(dateConvertor.convertLongDateToShort(xpp.getText()));
                        Log.v("GAVINROSS", xpp.getText());
                        eventType = xpp.nextTag(); // </title> end tag
                        eventType = xpp.nextTag(); // <description>
                        //Log.v("GAVINROSS", xpp.getName());
                    }
                    trafficDataList.add(trafficDataObj);
                }
            }
            eventType = xpp.next();
        }
        System.out.println("End document");
        return trafficDataList;
    }

    public String[] getDates(String date) throws StringIndexOutOfBoundsException {
        if (date.indexOf("Start Date: ") == -1 || date.indexOf("End Date: ") == -1) {
            return null;
        }

        else {
            String startDateIndex = date.substring(date.indexOf("Start Date: "), date.indexOf(':'));
            String data1 = date.substring(startDateIndex.length() + 2, date.indexOf('<'));
            String leftOverString = date.substring(date.indexOf('>'));

            String endDateIndex = leftOverString.substring(leftOverString.indexOf("End Date: "), date.indexOf(':'));
            String data2 = "";
            if (date.indexOf("<br />Delay") != -1) {
                data2 = leftOverString.substring(endDateIndex.length() + 2, leftOverString.indexOf('<'));
            } else {
                data2 = leftOverString.substring(endDateIndex.length() + 2);
            }
            String[] results = new String[2];
            results[0] = data1;
            results[1] = data2;
            return results;
        }

    }

    public String getDescription(String desc) {
        int result = desc.lastIndexOf("<br />");
        if (result == -1) {
            return desc;
        } else {
            return desc.substring(result+6, desc.length());
        }
    }

}

