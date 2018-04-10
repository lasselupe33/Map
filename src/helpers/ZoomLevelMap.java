package helpers;

public class ZoomLevelMap {
    public static int getZoomLevel() {
        double currDist = GetDistance.PxToKm(100) * 10;
        int maxDist = 500;

        int zoomLevel = Math.max((int) ((maxDist - currDist)) + 1, 1);
        return zoomLevel;
    }
}
