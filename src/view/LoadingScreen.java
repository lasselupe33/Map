package view;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class LoadingScreen extends JFrame {
    JLabel progressLabel;
    JLabel timerLabel;
    Long startTime;
    Timer timer;

    public LoadingScreen() {
        super("Loading...");
        setLayout(new GridLayout(2, 1));
        setUndecorated(true);

        progressLabel = new JLabel("Loading...");
        progressLabel.setHorizontalAlignment(progressLabel.CENTER);
        progressLabel.setFont(new Font("Myriad Pro", Font.PLAIN, 40));

        timerLabel = new JLabel();
        timerLabel.setHorizontalAlignment(timerLabel.CENTER);
        timerLabel.setFont(new Font("Myriad Pro", Font.PLAIN, 20));
        timerLabel.setText("Test");

        getContentPane().add(progressLabel);
        getContentPane().add(timerLabel);

        // Setup frame
        setSize(450, 125);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Center on screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        // Display frame
        setVisible(true);

        // Begin tracking loadTime
        startTime = System.currentTimeMillis();

        // Setup timer to update GUI Time
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateTime();
            }
        }, 0, 100);
    }

    public void updateTime() {
        DecimalFormat formatter = new DecimalFormat("#.000");
        timerLabel.setText(formatter.format((System.currentTimeMillis() - startTime) / 1000.0) + " s.");
    }

    public void updateProgress(double progress) {
        updateTime();

        DecimalFormat formatter = new DecimalFormat("#.00");
        progressLabel.setText("Loading... " + formatter.format(progress) + "%");
    }

    public void onLoaded() {
        // Remove JFrame
        dispose();

        // Stop timer
        System.out.println("Time to load: " + (System.currentTimeMillis() - startTime) + "ms.");
        timer.cancel();
    }
}
