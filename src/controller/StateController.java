package controller;

import view.MainWindowView;

import java.util.Timer;
import java.util.TimerTask;

public class StateController {
    private ViewStates currentState;
    private MainWindowView mainView;

    public StateController() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mainView.getWindow().repaint();
                System.out.println(mainView.getWindow().getHeight());
            }
        }, 2000);
        // Setup initial state
        currentState = ViewStates.ADDRESS_ENTERED;
    }

    public void addMainView(MainWindowView view) {
        mainView = view;
    }

    public ViewStates getCurrentState() {
        return currentState;
    }

    public void updateCurrentState(ViewStates newState) {
        currentState = newState;
        mainView.getWindow().repaint();
    }
}
