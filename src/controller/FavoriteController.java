package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FavoriteController extends MouseAdapter{
    StateController stateController;
    TextController t = new TextController();


    public FavoriteController(StateController sc){
    this.stateController = sc;

    }
    public void mouseClicked(MouseEvent e){
        String adress = e.getComponent().getName();
        System.out.println(stateController.getCurrentState());
        System.out.println(stateController.getPrevPanel());
        if (stateController.getPrevState() == ViewStates.INITIAL){
            stateController.updateCurrentState(ViewStates.INITIAL);
        }

    }

}
