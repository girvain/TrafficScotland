package android.bignerdranch.trafficscotland;

import org.junit.Test;

import static org.junit.Assert.*;

public class TrafficDataActivityTest {

    @Test
    public void getLatAndLong() {
        TrafficDataActivity trafficDataActivity = new TrafficDataActivity();
        String testInput = "57.0860645380357 -2.22611934311089";
        double[] testOutput = {57.0860645380357, -2.22611934311089};
        assertEquals(testOutput[0], trafficDataActivity.getLatAndLong(testInput)[0], 0);
        assertEquals(testOutput[1], trafficDataActivity.getLatAndLong(testInput)[1], 0);
    }
}