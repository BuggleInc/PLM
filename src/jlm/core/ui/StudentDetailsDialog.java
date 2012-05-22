package jlm.core.ui;

import jlm.core.model.ServerUserData;

import javax.swing.*;
import java.awt.*;

public class StudentDetailsDialog extends JDialog {

    private ServerUserData userData;

    public StudentDetailsDialog(ServerUserData userData) {
        super(MainFrame.getInstance(), "Details on " + userData.getUsername(), false);
        this.userData = userData;

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.PAGE_AXIS));
        detailsPanel.setBorder(BorderFactory.createTitledBorder(userData.getUsername()));

        detailsPanel.add(new JLabel("Last Join: " + userData.getLastJoin()));
        detailsPanel.add(new JLabel("Last Leave: " + userData.getLastLeave()));
        detailsPanel.add(new JLabel("Last Heartbeat: " + userData.getLastHeartbeat()));
        detailsPanel.add(new JLabel("Total number of exercises passed: " + userData.getExercisesTotal()));
        detailsPanel.add(new JLabel("Total number of exercises passed with success: " + userData.getExercisesPassed()));

        add(detailsPanel);
        pack();
        setMinimumSize(new Dimension(500, 300));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(true);
        setVisible(true);
        setLocationRelativeTo(getParent());
    }
}
