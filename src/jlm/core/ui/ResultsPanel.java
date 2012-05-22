package jlm.core.ui;

import jlm.core.model.Game;
import jlm.core.model.ServerUserData;

import javax.swing.*;
import java.util.Map;

public class ResultsPanel extends JPanel {

    public ResultsPanel() {
        displayResults();
    }

    public void displayResults() {
        Map<String, ServerUserData> serverData = Game.getInstance().getCurrentCourse().getServerData();

        setBorder(BorderFactory.createTitledBorder("Results by student"));

        if (serverData != null) {
            // Add the results graph of each student to serverDataPanel
            for (String student : serverData.keySet()) {
                add(new JButton(student));

                JProgressBar graph = new JProgressBar(0, serverData.get(student).getExercisesTotal());
                graph.setValue(serverData.get(student).getExercisesPassed());
                add(graph);
            }

        } else {
            add(new JLabel("There is no result yet for this course..."));
        }
    }
}
