package controller;

import helpers.AddressBuilder;
import model.AddressesModel;
import model.Coordinates;
import model.MapModel;
import model.graph.Graph;
import model.graph.Node;
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

    public NavigationController(AddressesModel am, MapModel mm, Graph g){
        addressesModel = am;
        mapModel = mm;
        graph = g;
    }

    public void addView(NavigationView nv){
        navigationView = nv;
    }

    public void setStartAddress(Address address){
        navigationView.getStartInput().setText(address.toString());
    }

    public void setEndAddress(Address address) {
        navigationView.getEndInput().setText(address.toString());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch(e.getComponent().getName()) {
            case "car":
                onCarClick();
                break;
            case "cycle":
                onCycleClick();
                break;
            case "pedestrian":
                onPedestrianClick();
                break;
        }
    }

    private void onCarClick() {
        graph.setVehicleType(VehicleType.CAR);
        updateView();
    }

    private void onCycleClick() {
        graph.setVehicleType(VehicleType.BICYCLE);
        updateView();
    }

    private void onPedestrianClick() {
        graph.setVehicleType(VehicleType.PEDESTRIAN);
        updateView();
    }

    private void updateView() {
        navigationView.update();
    }

    public void onRouteSearch() {
        String startInput = navigationView.getStartInput().getText();
        String endInput = navigationView.getEndInput().getText();

        Coordinates startAddressCoords = addressesModel.getAddress(AddressBuilder.parse(startInput).toKey()).getCoordinates();
        Coordinates endAddressCoords = addressesModel.getAddress(AddressBuilder.parse(endInput).toKey()).getCoordinates();

        long startingPointId = mapModel.getNearestNodeId(startAddressCoords);
        long endPointId = mapModel.getNearestNodeId(endAddressCoords);

        System.out.println(startingPointId);
        Node startingPoint = graph.getNode(startingPointId);
        Node endPoint = graph.getNode(endPointId);

        graph.computePath(startingPoint, endPoint);
    }

    public VehicleType getVehicleType() {
        return graph.getType();
    }
}
