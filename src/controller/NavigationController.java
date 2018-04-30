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

public class NavigationController extends MouseAdapter {
    private NavigationView navigationView;
    private AddressesModel addressesModel;
    private MapModel mapModel;
    private Graph graph;
    private String startInput = "";
    private String endInput = "";

    public NavigationController(AddressesModel am, MapModel mm, Graph g){
        addressesModel = am;
        mapModel = mm;
        graph = g;
    }

    public void addView(NavigationView nv){
        navigationView = nv;

        // Intercept all mouse events to ensure canvas commands won't be called when clicking on the addressView
        navigationView.addMouseListener(this);
        navigationView.addMouseWheelListener(this);
        navigationView.addMouseMotionListener(this);
    }

    public void setStartAddress(Address address){
        //navigationView.getStartInput().setText(address.toString());
        navigationView.setStartInputText(address.toString());
    }

    public void setEndAddress(Address address) {
        navigationView.getEndInput().setText(address.toString());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
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
     * TODO: Needs documentation, why is this necessary?
     */
    private void setInputText() {
        navigationView.setStartInputText(navigationView.getStartInput().getText());
        navigationView.setEndInputText(navigationView.getEndInput().getText());
    }

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

        // Get source and dest coords
        Coordinates startAddressCoords = addressesModel.getAddress(AddressBuilder.parse(startInput).toKey()).getCoordinates();
        Coordinates endAddressCoords = addressesModel.getAddress(AddressBuilder.parse(endInput).toKey()).getCoordinates();

        // Retrieve nearest way-node to the address-node (we want to use roads to travel, not addresses)
        long startingPointId = mapModel.getNearestNodeId(startAddressCoords);
        long endPointId = mapModel.getNearestNodeId(endAddressCoords);

        Node startingPoint = graph.getNode(startingPointId);
        Node endPoint = graph.getNode(endPointId);

        graph.computePath(startingPoint, endPoint);

        // Update map and view after path has been computed
        updateView();
        MapController.repaintMap(true);
    }

    public VehicleType getVehicleType() {
        return graph.getVehicleType();
    }

    public RouteType getRouteType() {
        return graph.getRouteType();
    }

    public String getTime() { return graph.getTime(); }

    public String getLength() { return graph.getLength(); }

    public void reset() {
        navigationView.setStartInputText("Fra:");
        navigationView.setEndInputText("Til:");
        graph.setVehicleType(VehicleType.CAR);
    }
}
