package android.bignerdranch.trafficscotland;

import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Calendar;

import static org.junit.Assert.*;

public class TrafficXMLParserTest {

    String currentIncedents = "";
    TrafficXMLParser trafficXMLParser = new TrafficXMLParser();
    String testData = "Start Date: Wednesday, 01 January 2020 - 00:00<br />End Date: Tuesday, 31 March 2020 - 00:00<br />Delay Information: No reported delay.";

    @Test
    public void parseXML() throws IOException, XmlPullParserException {

    }

    @Test
    public void getDates() {
        String testData = "Start Date: Wednesday, 01 January 2020 - 00:00<br />End Date: Tuesday, 31 March 2020 - 00:00<br />Delay Information: No reported delay.";
        assertEquals("Wednesday, 01 January 2020 - 00:00", trafficXMLParser.getDates(testData)[0]);
        assertEquals("Tuesday, 31 March 2020 - 00:00", trafficXMLParser.getDates(testData)[1]);
    }

    @Test
    public void getDescriptionRoadworks() {
        String testData = "Start Date: Wednesday, 01 January 2020 - 00:00<br />End Date: Tuesday, 31 March 2020 - 00:00<br />Delay Information: No reported delay.";
        assertEquals("Delay Information: No reported delay.", trafficXMLParser.getDescription(testData));
    }
    @Test
    public void getDescriptionIncidents() {
        String testData = "Drivers in Dumfries and Galloway are advised to use caution due to low temperatures affecting driving conditions.";
        assertEquals(testData, trafficXMLParser.getDescription(testData));
    }

    @Test
    public void getDescriptionPlannedRd() {
        String testData = "Start Date: Friday, 20 March 2020 - 00:00<br />End Date: Friday, 20 March 2020 - 00:00<br />Works: Filter Drain Traffic Management: No Obstruction on Carriageway or Footway.";
        assertEquals("Works: Filter Drain Traffic Management: No Obstruction on Carriageway or Footway.", trafficXMLParser.getDescription(testData));
    }

    @Test
    public void getDatesWithNoDelayInfo() {
        String testData2 = "Start Date: Wednesday, 12 February 2020 - 00:00<br />End Date: Friday, 20 March 2020 - 00:00";
        assertEquals("Wednesday, 12 February 2020 - 00:00", trafficXMLParser.getDates(testData2)[0]);
        assertEquals("Friday, 20 March 2020 - 00:00", trafficXMLParser.getDates(testData2)[1]);
    }

    @Test
    public void getDatesEmpty() {
        assertNull(trafficXMLParser.getDates(""));
    }

    @Test
    public void getDatesWithNoDateContext() {
        assertNull(trafficXMLParser.getDates("Roadworks currently being undertaken on the road network."));
    }

    @Test
    public void convertLongDateToShort() {
        String testData = "Wednesday, 02 January 2020 - 00:00";
        //assertTrue(trafficXMLParser.parseDateStringXML(testData) instanceof Date);
        assertEquals("02/01/2020", trafficXMLParser.convertLongDateToShort(testData));
    }

    @Test
    public void getNumOfMonth() {
        assertEquals("01", trafficXMLParser.getNumOfMonth("January"));
        assertEquals("11", trafficXMLParser.getNumOfMonth("November"));
        assertEquals("", trafficXMLParser.getNumOfMonth("JAson"));
    }

    @Test
    public void convertStringToDateError() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 2, 1);
        assertEquals(calendar.getTime().toString(), trafficXMLParser.convertStringToDate("01/02/2020").getTime().toString());
    }

    @Test
    public void convertRssDateToCalendarObj() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 2, 1);
        assertEquals(calendar.getTime().toString(), trafficXMLParser.convertRssDateToCalendarObj("Monday, 01 February 2020 - 00:00").getTime().toString());
    }
}