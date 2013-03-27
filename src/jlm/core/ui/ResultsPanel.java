package jlm.core.ui;

import jlm.core.model.Game;
import jlm.core.model.ServerUserData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

/**
 * Panel to display students results from the server
 * You can add a filter of student to display only a list of usernames depending of a criteria (good, bad student...)
 */
public class ResultsPanel extends JPanel {
	private static final long serialVersionUID = 1L;

    // all data on students from the server
    private Map<String, ServerUserData> serverData;
    // list of students to filter the selection to display (null if all data has to be displayed)
    private ArrayList<String> userFilter;

    public ResultsPanel(ArrayList<String> userFilter) {
        this.userFilter = userFilter;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createTitledBorder("Results by student"));
        displayResults();
    }

    public void displayResults() {
        this.removeAll();
        serverData = Game.getInstance().getCurrentCourse().getServerData();

        UIManager.put("ProgressBar.background", Color.RED); //color of the background
        UIManager.put("ProgressBar.foreground", Color.GREEN);  //color of progress bar

        if (serverData != null) {
            // Add the results graph of each student to serverDataPanel
            // if a filter has been set, iterate only through these students
            // else, iterate through all students of the course
            for (final String student : (userFilter == null ? serverData.keySet() : userFilter)) {
                JPanel studentPanel = new JPanel();
                studentPanel.add(new JLabel(student));

                JProgressBar graph = new JProgressBar(0, serverData.get(student).getExercisesTotal());
                graph.setValue(serverData.get(student).getExercisesPassed());
                studentPanel.add(graph);
                JButton studentButton = new JButton();
                studentButton.setContentAreaFilled(false);
                studentButton.add(studentPanel);
                studentButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        new StudentDetailsDialog(serverData.get(student));
                    }
                });

                add(studentButton);
            }

        } else {
            add(new JLabel("There is no result yet for this course..."));
        }

        this.repaint();
    }
}
