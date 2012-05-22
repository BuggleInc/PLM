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

        JPanel serverDataPanel = new JPanel();
        serverDataPanel.setBorder(BorderFactory.createTitledBorder("Results by student"));

        if (serverData != null) {
            // Add the results graph of each student to serverDataPanel
            for (String student : serverData.keySet()) {
                serverDataPanel.add(new JLabel(student));

                JProgressBar graph = new JProgressBar(1, 100);
                serverDataPanel.add(graph);
//            graph.setValue(serverData.get(student).);
            }

            add(serverDataPanel);
        } else {
            add(new JLabel("There is no result yet for this course..."));
        }
    }
}
