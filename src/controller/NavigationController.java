package controller;

import helpers.AddressBuilder;
import model.AddressesModel;
import model.Coordinates;
import model.MapModel;
import model.graph.Graph;
import model.graph.Node;
import view.NavigationView;

import java.nio.channels.FileChannel;

public class NavigationController {
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

    public void setStartInput(String s){
        navigationView.getStartInput().setText(s);
    }

    public void onRouteSearch() {
        String startInput = navigationView.getStartInput().getText();
        String endInput = navigationView.getEndInput().getText();

        Coordinates startAddressCoords = addressesModel.getAddress(AddressBuilder.parse(startInput).toKey()).getCoordinates();
        Coordinates endAddressCoords = addressesModel.getAddress(AddressBuilder.parse(endInput).toKey()).getCoordinates();

        Coordinates startingPointCoords = mapModel.getNearestWay(startAddressCoords);
        Coordinates endPointCoords = mapModel.getNearestWay(endAddressCoords);

        Node startingPoint = graph.getNode(startingPointCoords.toString());
        Node endPoint = graph.getNode(endPointCoords.toString());

        graph.computePath(startingPoint, endPoint);
    }
}
