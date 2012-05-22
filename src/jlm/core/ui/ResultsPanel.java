package jlm.core.ui;

import jlm.core.model.Game;
import jlm.core.model.ServerUserData;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ResultsPanel extends JPanel {

    public ResultsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createTitledBorder("Results by student"));
        displayResults();
    }

    public void displayResults() {
        this.removeAll();
        Map<String, ServerUserData> serverData = Game.getInstance().getCurrentCourse().getServerData();

        UIManager.put("ProgressBar.background", Color.RED); //color of the background
        UIManager.put("ProgressBar.foreground", Color.GREEN);  //color of progress bar

        if (serverData != null) {
            // Add the results graph of each student to serverDataPanel
            for (String student : serverData.keySet()) {
                JPanel studentPanel = new JPanel();
                studentPanel.add(new JLabel(student));

                JProgressBar graph = new JProgressBar(0, serverData.get(student).getExercisesTotal());
                graph.setValue(serverData.get(student).getExercisesPassed());
                studentPanel.add(graph);
                JButton studentButton = new JButton();
                studentButton.setContentAreaFilled(false);
                studentButton.add(studentPanel);

                add(studentButton);
            }

        } else {
            add(new JLabel("There is no result yet for this course..."));
        }
    }
}
