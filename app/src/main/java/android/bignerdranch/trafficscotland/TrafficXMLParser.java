package android.bignerdranch.trafficscotland;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;

public class TrafficXMLParser {

    private static final String ns = null;
    LinkedList<TrafficDataModel> trafficDataList = new LinkedList<>();


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
                        String[] startAndEndDates = getDates(xpp.getText());
                        trafficDataObj.setStartDate(startAndEndDates[0]);
                        trafficDataObj.setEndDate(startAndEndDates[1]);

//                        Log.v("startDate", getDates(trafficDataObj.description)[0]);
//                        Log.v("endDate", getDates(trafficDataObj.description)[1]);

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



    public static class Entry {
        public final String title;
        public final String link;
        public final String summary;

        private Entry(String title, String summary, String link) {
            this.title = title;
            this.summary = summary;
            this.link = link;
        }
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
// to their respective "read" methods for processing. Otherwise, skips the tag.
    private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String summary = null;
        String link = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("summary")) {
                summary = readSummary(parser);
            } else if (name.equals("link")) {
                link = readLink(parser);
            } else {
                skip(parser);
            }
        }
        Log.v("GAVINROSS", "Item");
        return new Entry(title, summary, link);
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        Log.v("GAVINROSS", title);
        return title;
    }

    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel");
        if (tag.equals("link")) {
            if (relType.equals("alternate")){
                link = parser.getAttributeValue(null, "href");
                parser.nextTag();
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    // Processes summary tags in the feed.
    private String readSummary(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "summary");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "summary");
        return summary;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}

