package helpers;

import org.junit.Test;

import java.text.DecimalFormat;

import static org.junit.Assert.*;

public class GetDistanceTest {
    @Test
    public void inKM() throws Exception {
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
}