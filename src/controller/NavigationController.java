package controller;

import helpers.AddressBuilder;
import model.AddressesModel;
import model.Coordinates;
import model.MapModel;
import model.graph.Graph;
import model.graph.Node;
import model.graph.RouteType;
import model.graph.VehicleType;
import model.Address;
import view.NavigationView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;

public class NavigationController extends MouseAdapter {
    private NavigationView navigationView;
    private AddressesModel addressesModel;
    private MapModel mapModel;
    private Graph graph;
    private AddressController addressController;
    private Coordinates startAddressCoords;
    private Coordinates endAddressCoords;
    private Node startingPoint;
    private Node endPoint;
    private String startInput = "";
    private String endInput = "";
    private boolean navigationActive = false;
    private boolean navigationFailed = false;

    public NavigationController(AddressesModel am, MapModel mm, Graph g){
        addressesModel = am;
        mapModel = mm;
        graph = g;
    }

    public void addAddressController(AddressController ac) {
        addressController = ac;
    }

    /**
     * Add navigation view
     * @param nv the view
     */
    public void addView(NavigationView nv){
        navigationView = nv;

        // Intercept all mouse events to ensure canvas commands won't be called when clicking on the addressView
        navigationView.addMouseListener(this);
        navigationView.addMouseWheelListener(this);
        navigationView.addMouseMotionListener(this);
    }

    /**
     * Set start address in navigation view
     * @param address the address to be set
     */
    public void setStartAddress(Address address){
        navigationView.getStartInput().setText(address.toString());
        navigationView.setStartInputText(address.toString());
    }

    /**
     * Set end address in navigation view
     * @param address the address to set
     */
    public void setEndAddress(Address address) {
        navigationView.getEndInput().setText(address.toString());
        navigationView.setEndInputText(address.toString());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Bail out if a nameless/no component was clicked
        if (e.getComponent() == null || e.getComponent().getName() == null) {

            return;
        }

        switch(e.getComponent().getName()) {
            case "car":
                changeVehicleType(VehicleType.CAR);
                break;
            case "cycle":
                changeVehicleType(VehicleType.BICYCLE);
                break;
            case "pedestrian":
                changeVehicleType(VehicleType.PEDESTRIAN);
                break;
            case "fastest":
                changeRouteType(RouteType.FASTEST);
                break;
            case "shortest":
                changeRouteType(RouteType.SHORTEST);
                break;
            default:
            System.out.print("test");
                break;
        }
    }

    /** Update the current vehicle type */
    public void changeVehicleType(VehicleType type) {
        graph.setVehicleType(type);
        //setInputText();
        onRouteSearch();
        updateView();
    }

    /** Update the current road type */
    private void changeRouteType(RouteType type) {
        graph.setRouteType(type);
        onRouteSearch();
        updateView();
    }

    /**
     * Store the inputs in fields for when the view is updated
     * This is needed when the user is switching between views and the input data has to stay in the navigation view
     */
    private void setInputText() {
        navigationView.setStartInputText(navigationView.getStartInput().getText());
        navigationView.setEndInputText(navigationView.getEndInput().getText());
    }

    /**
     * Update the view
     */
    public void updateView() {
        setInputText();
        navigationView.update();
    }

    /**
     * Method to be called once the user wishes to perform a navigation between two points
     */
    public void onRouteSearch() {
        startInput = navigationView.getStartInput().getText();
        endInput = navigationView.getEndInput().getText();

        // Check that inputs have been filled
        if (startInput.equals(navigationView.getStartInput().getName()) || endInput.equals(navigationView.getEndInput().getName()) || startInput.equals("") || endInput.equals("")) {
            return;
        }

        addressController.setCurrentAddress(addressesModel.getAddress(AddressBuilder.parse(startInput).toKey()));

        // Get source and dest coords
        startAddressCoords = addressesModel.getAddress(AddressBuilder.parse(startInput).toKey()).getCoordinates();
        endAddressCoords = addressesModel.getAddress(AddressBuilder.parse(endInput).toKey()).getCoordinates();

        // Retrieve nearest way-node to the address-node (we want to use roads to travel, not addresses)
        long startingPointId = mapModel.getNearestNodeId(startAddressCoords);
        long endPointId = mapModel.getNearestNodeId(endAddressCoords);

        startingPoint = graph.getNode(startingPointId);
        endPoint = graph.getNode(endPointId);

        graph.computePath(startingPoint, endPoint);

        // Update map and view after path has been computed
        if (!graph.didError()) {
            navigationFailed = false;
            navigationActive = true;
            updateView();
            MapController.repaintMap(true);
            MapController.updateStartCoordinates(startAddressCoords);
            MapController.updateLocationCoordinates(endAddressCoords);
        } else {
            navigationFailed = true;
            navigationActive = false;
        }
    }

    /**
     * Switch from input and to input
     */
    public void switchFromAndTo() {
        String startTextHolder = navigationView.getStartInput().getText();
        String endTextHolder = navigationView.getEndInput().getText();
        if(startTextHolder.equals("Fra:") && endTextHolder.equals("Til:")){
            return;
        } else if (startTextHolder.equals("Fra:")){
            navigationView.getStartInput().setText(endTextHolder);
            navigationView.getEndInput().setText("Til:");
        } else if (endTextHolder.equals("Til:")){
            navigationView.getEndInput().setText(startTextHolder);
            navigationView.getStartInput().setText("Fra:");
        } else {
            navigationView.getStartInput().setText(endTextHolder);
            navigationView.getEndInput().setText(startTextHolder);
            onRouteSearch();
        }
    }

    public boolean didError() {
        return navigationFailed;
    }

    /**
     * Get vehicle type
     * @return vehicle type
     */
    public VehicleType getVehicleType() {
        return graph.getVehicleType();
    }

    public RouteType getRouteType() {
        return graph.getRouteType();
    }

    /**
     * Get the time of the route
     * @return time
     */
    public String getTime() { return graph.getTime(); }

    /**
     * Get length of the route in km
     * @return length of route
     */
    public String getLength() { return graph.getLength(); }

    /**
     * Check whether the navigation view i open
     * @return true if view is open; else false
     */
    public boolean isNavigationActive() {
        return navigationActive;

    }
    public JTextField getStartInput(){
        return navigationView.getStartInput();
    }
    public JTextField getEndInput(){
        return navigationView.getEndInput();
    }
    /**
     * Reset navigation view
     */
    public void reset() {
        navigationActive = false;
        navigationView.setStartInputText(navigationView.getStartInput().getName());
        navigationView.setEndInputText(navigationView.getEndInput().getName());
        graph.setVehicleType(VehicleType.CAR);
    }

    /**
     * Path that goes from the input start adress to the beginning of the route
     * @return path from start adress to start of route
     */
    public Path2D getStartAddressPath() {
        Path2D fromStartToRoute = new Path2D.Float();
        fromStartToRoute.moveTo(startAddressCoords.getX(), startAddressCoords.getY());
        fromStartToRoute.lineTo(startingPoint.getLon(), startingPoint.getLat());
        return fromStartToRoute;
    }

    /**
     * Path that goes from the input end adress to the end of the route
     * @return path from end of route to end adress
     */
    public Path2D getEndAddressPath() {
        Path2D fromRouteToEnd = new Path2D.Float();
        fromRouteToEnd.moveTo(endAddressCoords.getX(), endAddressCoords.getY());
        fromRouteToEnd.lineTo(endPoint.getLon(), endPoint.getLat());
        return fromRouteToEnd;
    }
}
