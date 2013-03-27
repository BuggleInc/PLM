package jlm.core.ui;

import jlm.core.model.ServerExerciseData;
import jlm.core.model.ServerUserData;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog to display detailed data about a student in a course
 * For now, it is only text, but it could be graphs of progression, ...
 */
public class StudentDetailsDialog extends JDialog {
	private static final long serialVersionUID = 1L;

    public StudentDetailsDialog(ServerUserData userData) {
        super(MainFrame.getInstance(), "Details on " + userData.getUsername(), false);
        setLayout(new BorderLayout());

        JPanel infosPanel = new JPanel();
        infosPanel.setLayout(new BoxLayout(infosPanel, BoxLayout.PAGE_AXIS));
        infosPanel.setBorder(BorderFactory.createTitledBorder("Infos on " + userData.getUsername()));

        infosPanel.add(new JLabel("Last Join: " + userData.getLastJoin()));
        infosPanel.add(new JLabel("Last Leave: " + userData.getLastLeave()));
        infosPanel.add(new JLabel("Last Heartbeat: " + userData.getLastHeartbeat()));
        infosPanel.add(new JLabel("Total number of exercises passed: " + userData.getExercisesTotal()));
        infosPanel.add(new JLabel("Total number of exercises passed with success: " + userData.getExercisesPassed()));

        JPanel exercisesPanel = new JPanel();
        exercisesPanel.setLayout(new BoxLayout(exercisesPanel, BoxLayout.PAGE_AXIS));
        exercisesPanel.setBorder(BorderFactory.createTitledBorder("Exercises done"));
        for(ServerExerciseData exo: userData.getExercises()){
            exercisesPanel.add(new JLabel(exo.getName() + " (" + exo.getLang() + ") " + exo.getPassedTests()
            + "/" + exo.getTotalTests() + " - " + exo.getDate()));
        }

        add(infosPanel, BorderLayout.NORTH);
        add(exercisesPanel, BorderLayout.CENTER);

        pack();
        setMinimumSize(new Dimension(500, 300));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(true);
        setVisible(true);
        setLocationRelativeTo(getParent());
    }
}
