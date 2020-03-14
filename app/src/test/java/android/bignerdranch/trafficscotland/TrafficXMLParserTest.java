package android.bignerdranch.trafficscotland;

import org.junit.Test;
import org.mockito.Mock;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;

import static org.junit.Assert.*;

public class TrafficXMLParserTest {

    @Mock
    XmlPullParserFactory xmlPullParserFactory;
    @Mock
    XmlPullParser xpp;


    String currentIncedents = "<Fart>This is a fart</Fart>";
    @Test
    public void parseXML() throws IOException, XmlPullParserException {
        //XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        //factory.setNamespaceAware(true);
        //XmlPullParser xpp = factory.newPullParser();
        TrafficXMLParser trafficXMLParser = new TrafficXMLParser(xmlPullParserFactory, xpp);
        //assertEquals("This is a fart", trafficXMLParser.parseXML(currentIncedents));
    }

}