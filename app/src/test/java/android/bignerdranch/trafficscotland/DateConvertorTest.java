package android.bignerdranch.trafficscotland;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class DateConvertorTest {

    @Test
    public void numberOfDays() {
        DateConvertor dc = new DateConvertor();
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
}