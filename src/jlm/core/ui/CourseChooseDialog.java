package jlm.core.ui;

import jlm.core.model.Course;
import jlm.core.model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class CourseChooseDialog extends JDialog {

    private static final long serialVersionUID = 2234402839093122248L;

    protected ArrayList<String> courseListIDs;
    protected JList jListID = null;
    protected JButton OKButton;
    protected JPasswordField passwordField;


    public CourseChooseDialog() {
        super(MainFrame.getInstance(), "JLM Course", false);

        initComponent(this.getContentPane());

        setMinimumSize(new Dimension(300, 400));
        pack();

        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    public void initComponent(Container c) {
        c.setLayout(new BorderLayout());
        c.add(new JLabel("Please select your course:"), BorderLayout.NORTH);

        // OK/Cancel button
        OKButton = new JButton("OK");
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                selectCourse();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                setVisible(false);
            }
        });

        JPanel bottomButtons = new JPanel();
        bottomButtons.setLayout(new FlowLayout());
        bottomButtons.add(cancelButton);
        bottomButtons.add(OKButton);

        c.add(bottomButtons, BorderLayout.SOUTH);


        JPanel coursesPanel = new JPanel(new BorderLayout());

        // Load the list of available "courses", or a message to say nope.
        Course currentCourse = Game.getInstance().getCurrentCourse();
        courseListIDs = currentCourse.getAllCoursesId();

        if (courseListIDs.isEmpty()) {
            c.add(new JLabel("No course currently opened, sorry.", JLabel.CENTER), BorderLayout.CENTER);
            OKButton.setEnabled(false);
        } else {
            jListID = new JList(courseListIDs.toArray());
            jListID.setSelectedValue(currentCourse.getCourseId(), true);
            coursesPanel.add(jListID, BorderLayout.CENTER);
        }

        // Password panel and field
        JLabel passwordLabel = new JLabel("Course password: ");
        passwordField = new JPasswordField(10);

        JPanel passwordPanel = new JPanel(new FlowLayout());
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        coursesPanel.add(passwordPanel, BorderLayout.SOUTH);

        c.add(coursesPanel, BorderLayout.CENTER);

    }

    public void selectCourse() {
        System.out.println(jListID.getSelectedValue());
        MainFrame.getInstance().appendToTitle("[ " + jListID.getSelectedValue() + " ]");
        Course course = Game.getInstance().getCurrentCourse();
        course.setCourseId(jListID.getSelectedValue().toString());
        course.setPassword(new String(passwordField.getPassword()));

        setVisible(false);
    }
}
