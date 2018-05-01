package helpers;

import controller.MapController;

import java.awt.*;
import java.awt.geom.Point2D;

/** Helper class that can convert model coordinates to distance */
public class GetDistance {
    public static double PxToKm(int px) {
        Point2D startModelPoint = MapController.getInstance().toModelCoords(new Point(0, 0));
        Point2D endModelPoint = MapController.getInstance().toModelCoords(new Point(px, 0));
        return GetDistance.inKM(startModelPoint.getX(), startModelPoint.getY(), endModelPoint.getX(), endModelPoint.getY());
    }

    /** Helper that gets the distance in kilometers between two coordinates */
    public static double inKM(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = toRadians(lat2-lat1);
        double dLon = toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(toRadians(lat1))*Math.cos(toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d;
    }

    /** Simple helper that converts the inKM output to MM's */
    public static double inMM(double lat1, double lon1, double lat2, double lon2) {
        return 1000000 * GetDistance.inKM(lat1, lon1, lat2, lon2);
    }

    private static double toRadians(double degrees){
        return degrees * (Math.PI/180);
    }
}
