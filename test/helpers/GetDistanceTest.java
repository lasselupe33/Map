package helpers;

import org.junit.Test;

import java.text.DecimalFormat;

import static org.junit.Assert.*;

public class GetDistanceTest {
    @Test
    public void inKM()  {
        // Correct distance retrieved from https://www.movable-type.co.uk/scripts/latlong.html
        double lat1 = 55.693215;
        double lon1 = 12.450483;
        double lat2 = 55.692679;
        double lon2 = 12.450548;

        double computedDistance = GetDistance.inKM(lat1, lon1, lat2, lon2);
        DecimalFormat df = new DecimalFormat("#.#####");

        String correctDistance = df.format(0.05974);
        String formattedDistance = df.format(computedDistance);

        assertEquals(correctDistance, formattedDistance);
    }

    @Test
    public void coordinatesWraparound()  {
        // Correct distance retrieved from https://www.movable-type.co.uk/scripts/latlong.html
        double lat1 = 89.199192;
        double lon1 = 17.199212;
        double lat2 = 18.129312;
        double lon2 = 34.123221;

        double distance1 = GetDistance.inKM(lat1, lon1, lat2, lon2);

        double wrapDistance = 180;

        double distance2 = GetDistance.inKM(lat1 + wrapDistance, lon1 - wrapDistance,
                lat2 - wrapDistance, lon2 + wrapDistance);

        DecimalFormat df = new DecimalFormat("#.#####");

        String formatedDistance1 = df.format(distance1);
        String formatedDistance2 = df.format(distance2);

        assertEquals(formatedDistance1, formatedDistance2);
    }
}