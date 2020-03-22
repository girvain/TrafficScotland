/**
 * Name: Gavin Ross
 * Matric No: S1821951
 */
package android.bignerdranch.trafficscotland;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class DateConvertorTest {

    DateConvertor dc = new DateConvertor();
    @Test
    public void numberOfDays() {
        Calendar date1 = Calendar.getInstance();
        date1.set(2020,1,1);
        Calendar date2 = Calendar.getInstance();
        date2.set(2020,1,4);

        assertEquals(4, dc.numberOfDays(date1, date2));
    }

    @Test
    public void numberOfDaysWithError() {
//        DateConvertor dc = new DateConvertor();
//        Calendar date1 = Calendar.getInstance();
//        date1.set(2020,1,1);
//        Calendar date2 = Calendar.getInstance();
//        date2.set(2019,1,4);
//
//        assertEquals(4, dc.numberOfDays(date1, date2));
    }

    @Test
    public void convertLongDateToShort() {
        String testData = "Wednesday, 02 January 2020 - 00:00";
        //assertTrue(trafficXMLParser.parseDateStringXML(testData) instanceof Date);
        assertEquals("02/01/2020", dc.convertLongDateToShort(testData));
    }

    @Test
    public void getNumOfMonth() {
        assertEquals("01", dc.getNumOfMonth("January"));
        assertEquals("11", dc.getNumOfMonth("November"));
        assertEquals("", dc.getNumOfMonth("JAson"));
    }

    @Test
    public void convertStringToDateError() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 2, 1);
        assertEquals(calendar.getTime().toString(), dc.convertStringToDate("01/02/2020").getTime().toString());
    }

    @Test
    public void convertRssDateToCalendarObj() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 2, 1);
        assertEquals(calendar.getTime().toString(), dc.convertRssDateToCalendarObj("Monday, 01 February 2020 - 00:00").getTime().toString());
    }
}