package helpers;

import controller.MapController;

import java.awt.*;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;

/** Helper class that can convert model coordinates to distance */
public class UnitConverter {
    public static double PxToKm(int px) {
        Point2D startModelPoint = MapController.getInstance().toModelCoords(new Point(0, 0));
        Point2D endModelPoint = MapController.getInstance().toModelCoords(new Point(px, 0));
        return UnitConverter.DistInKM(startModelPoint.getX(), startModelPoint.getY(), endModelPoint.getX(), endModelPoint.getY());
    }

    /** Helper that gets the distance in kilometers between two coordinates */
    public static double DistInKM(double lat1, double lon1, double lat2, double lon2) {
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
    public static double DistInMM(double lat1, double lon1, double lat2, double lon2) {
        return 1000000 * UnitConverter.DistInKM(lat1, lon1, lat2, lon2);
    }

    /** Simple helper that formates a distance in mm's to a properly formatted text string */
    public static String formatDistance(double dist) {
        // Get length in KM's
        double length = dist / 1000000;

        // Convert distance to meters
        int m = (int) ((length % 1) * 1000);

        // Nicely format distance
        return (int) length != 0 ? Math.round(length * 100.0) / 100.0 + "km" : m + "m";
    }

    /**
     * Helper that formats a timestamp in hours to a properly formatted text string
     * @return
     */
    public static String formatTime(double time) {
        // We always round the time up
        int hours = (int) time;
        int min = (int) ((time % 1) * 60);
        int seconds = (int) ((((time % 1) * 60) % 1) * 60);

        // Return a string only containing fields if necessary (e.g. hours will only be shown if there's one or more)
        return (hours != 0 ? hours + "h " : "") + (min != 0 || hours != 0 ? min + "min " : "") + (seconds != 0 || min != 0 || hours != 0 ? seconds + "sek" : "");
    }

    private static double toRadians(double degrees){
        return degrees * (Math.PI/180);
    }
}
