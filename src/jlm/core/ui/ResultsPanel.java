package jlm.core.ui;

import jlm.core.model.Course;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class ResultsPanel extends JPanel {

    private HashMap<String, Integer> studentsResults;
    private HashMap<String, Integer> exosResults;

    public ResultsPanel(Course course) {
        studentsResults = course.getStudentsResults();
        exosResults = course.getExoResults();

        // for test only
        studentsResults.put("julien", 90);
        exosResults.put("pidr", 10);

        initComponents();
    }

    private void initComponents() {

        setLayout(new BorderLayout());

        JPanel studentsResultsPanel = new JPanel();
        studentsResultsPanel.setBorder(BorderFactory.createTitledBorder("Results by student"));

        JPanel exosResultsPanel = new JPanel();
        exosResultsPanel.setBorder(BorderFactory.createTitledBorder("Results by exercise"));


        // Add the results graph of each student to studentsResultsPanel
        for (String student : studentsResults.keySet()) {
            studentsResultsPanel.add(new JLabel(student));

            JProgressBar graph = new JProgressBar(1, 100);
            studentsResultsPanel.add(graph);
            graph.setValue(studentsResults.get(student));
        }

        // Add the results graph of each exercise to exoResultsPanel
        for (String exo : exosResults.keySet()) {

            exosResultsPanel.add(new JLabel(exo));

            JProgressBar graph = new JProgressBar(1, 100);
            exosResultsPanel.add(graph);
            graph.setValue(exosResults.get(exo));
        }

        add(BorderLayout.NORTH, studentsResultsPanel);
        add(BorderLayout.SOUTH, exosResultsPanel);
    }
}
