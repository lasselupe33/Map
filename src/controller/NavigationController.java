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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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
        }
    }

    /** Update the current vehicle type */
    public void changeVehicleType(VehicleType type) {
        graph.setVehicleType(type);
        //setInputText();
        updateView();
        onRouteSearch();
    }

    /** Update the current road type */
    private void changeRouteType(RouteType type) {
        graph.setRouteType(type);
        updateView();
        onRouteSearch();

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
    private void updateView() {
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
        if (startInput.equals("Fra:") || endInput.equals("Til:") || startInput.equals("") || endInput.equals("")) {
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
        navigationActive = true;
        updateView();
        MapController.repaintMap(true);
        MapController.updateStartCoordinates(startAddressCoords);
        MapController.updateLocationCoordinates(endAddressCoords);
        MapController.getInstance().moveScreenNavigation(graph.getRoutePath().getBounds2D());

        textualNavigation(startInput, endInput);
    }

    /**
     * Add textual navigation to route search
     */
    public void textualNavigation(String start, String end) {
        ArrayList<Node> routeNodes = graph.getRouteNodes();
        Collections.reverse(routeNodes);

        System.out.println(routeNodes.size());

        navigationView.addNavigationAddress(start);

        addNavigationStart(routeNodes);

        float x;
        float y;
        float[] vector1 = new float[2];
        float[] vector2 = new float[2];

        if (routeNodes.size() >= 2) {
            for (int i = 0; i < routeNodes.size() - 2; i++) {

                x = routeNodes.get(i+1).getLon() - routeNodes.get(i).getLon();
                y = routeNodes.get(i+1).getLat() - routeNodes.get(i).getLat();
                vector1[0] = x;
                vector1[1] = y;

                x = routeNodes.get(i + 2).getLon() - routeNodes.get(i + 1).getLon();
                y = routeNodes.get(i + 2).getLat() - routeNodes.get(i + 1).getLat();
                vector2[0] = x;
                vector2[1] = y;

                double angle = angle(vector1, vector2);

                if(angle < -135 || angle >= 135){
                    //navigationView.addNavigationText("Tilbage...", "/icons/arrow-up.png");
                } else if(angle < 135 && angle >= 45){
                    navigationView.addNavigationText("Drej til højre og følg...", "/icons/arrow-right.png");
                } else if(angle < 45 && angle >= -45){
                    navigationView.addNavigationText("Fortsæt ned ad...", "/icons/arrow-up.png");
                } else if(angle < -45 && angle >= -135){
                    navigationView.addNavigationText("Drej til venstre og følg...", "/icons/arrow-left.png");
                }
            }

            navigationView.addNavigationText("Destinationen er nået", "/icons/locationIcon.png");
            navigationView.addNavigationAddress(end);
        }
    }

    /**
     * Calculate start direction and add text to navigation
     * @param routeNodes list of nodes in route
     */
    private void addNavigationStart(ArrayList<Node> routeNodes) {
        float startPointX = routeNodes.get(0).getLon();
        float startPointY = routeNodes.get(0).getLat();

        float endPointX = routeNodes.get(1).getLon();
        float endPointY = routeNodes.get(1).getLat();

        double compassReading = Math.atan2(endPointX-startPointX, endPointY-startPointY) * (180 / Math.PI);

        String[] coordNames = new String[] {"syd", "sydøst", "øst", "nordøst", "syd", "nordvest", "vest", "sydvest", "syd"};
        int coordIndex = (int) Math.round(compassReading / 45); // divide 360 degrees by the 8 directions
        if (coordIndex < 0) {
            coordIndex = coordIndex + 8;
        }

        navigationView.addNavigationText("Tag mod " + coordNames[coordIndex] +" ad...", "/icons/arrow-up.png");
    }

    /**
     * Compute the scalar product of two vectors
     * @param p first vector
     * @param q second vector
     * @return scalar product of vector p and vector q
     */
    private double scalarProduct(float[] p, float[] q) {
        double product = 0;
        for (int i = 0; i < p.length; i++) {
            product += p[i] * q[i];
        }
        return product;
    }

    /**
     * Compute the determinant of two vectors
     * @param p first vector
     * @param q second vector
     * @return determinant of vector p and vector q
     */
    private double determinant(float[] p, float[] q) {
        return p[0]*q[1] - p[1]*q[0];
    }

    /**
     * Compute the angle of two vectors
     * @param p first vector
     * @param q second vector
     * @return angle between -180 and 180
     */
    private double angle(float[] p, float[] q) {
        return Math.toDegrees(Math.atan2(determinant(p, q), scalarProduct(p ,q)));
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

    /**
     * Reset navigation view
     */
    public void reset() {
        navigationActive = false;
        navigationView.setStartInputText("Fra:");
        navigationView.setEndInputText("Til:");
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
