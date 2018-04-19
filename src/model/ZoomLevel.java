package model;

import helpers.KDTree;

import java.io.Serializable;

public enum ZoomLevel implements Serializable {
    ONE (-100), TWO (1), THREE (2), FOUR (3), FIVE (5), SIX (8);

    private final int zoomValue;

    ZoomLevel(int zoom) {
        zoomValue = zoom;
    }

    // public int getZoomValue() { return zoomValue * KDTree.CalculateZoomLevel.getScale(); }

    //private int calculateScale(){}
}
