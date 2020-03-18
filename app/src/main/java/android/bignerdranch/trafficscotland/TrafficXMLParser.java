package android.bignerdranch.trafficscotland;

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
    LinkedList<TrafficDataModel> trafficDataList = new LinkedList<>();
    private DateConvertor dateConvertor;

    public TrafficXMLParser() {
        dateConvertor = new DateConvertor();
    }

    public LinkedList<TrafficDataModel> parse(String string) throws XmlPullParserException, IOException {
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
                        trafficDataObj.setDescription(xpp.getText());
                        // extract the long versions of start and end date from description
                        // if it's the current incedents feed, theres no dates so it returns null
                        String[] startAndEndDates = getDates(xpp.getText());
                        if (startAndEndDates != null) {
                            // convert each one to Calendar object and add to trafficDataObj
                            trafficDataObj.setStartDate(convertRssDateToCalendarObj(startAndEndDates[0]));
                            trafficDataObj.setEndDate(convertRssDateToCalendarObj(startAndEndDates[1]));
                            // Convert long date to short version and add to trafficDataObj (String)
                            trafficDataObj.setStartDateAsString(convertLongDateToShort(startAndEndDates[0]));
                            trafficDataObj.setEndDateAsString(convertLongDateToShort(startAndEndDates[1]));
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

    /**
     * Converts the Rss feed results data format to a Calendar object. Must have only one date
     * at a time and as such relies on the getDates() to split the description into two dates
     * in the format "Wednesday, 01 January 2020 - 00:00".
     * Uses the convertRssDateToCalendarObj() and the convertLongDateToShort().
     * @param date
     * @return
     */
    public Calendar convertRssDateToCalendarObj(String date) {
        String shortDate = convertLongDateToShort(date);
        Calendar c = convertStringToDate(shortDate);
        return c;
    }

    // Method to parse a date from the XML long version Wednesday,  12 Febuary 2020 - 00:00
    // to the short version 12/02/2020
    public String convertLongDateToShort(String dateString) {
        String regex = "\\d+";
        String result = "";
        String[] words = dateString.split(" ");
        for (String word : words) {
            if (word.matches(regex)) {
                result+= word+ "/";
            } else if(!getNumOfMonth(word).isEmpty()) {
                result+= getNumOfMonth(word) + "/";
            }
        }
        return result.substring(0, result.length()-1); // take the last / off the end
    }

    // Converts a date string in the format 01/02/2020 to a Calendar object
    public Calendar convertStringToDate(String dateString) {
        String[] numbers = dateString.split("/");
        int day = Integer.parseInt(numbers[0]);
        int month = Integer.parseInt(numbers[1]);
        int year = Integer.parseInt(numbers[2]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar;
    }

    public String getNumOfMonth(String month) {
        switch(month) {
            case "January":
                return "01";
            case "February":
                return "02";
            case "March":
                return "03";
            case "April":
                return "04";
            case "May":
                return "05";
            case "June":
                return "06";
            case "July":
                return "07";
            case "August":
                return "08";
            case "September":
                return "09";
            case "October":
                return "10";
            case "November":
                return "11";
            case "December":
                return "12";
            default:
                return "";
        }
    }

}

