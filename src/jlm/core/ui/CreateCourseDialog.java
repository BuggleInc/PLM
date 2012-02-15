package jlm.core.ui;

import jlm.core.model.Course;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateCourseDialog extends JDialog {

    private static Course course;

    public CreateCourseDialog(Course course) {
        super(MainFrame.getInstance(), "JLM Course", false);

        this.course = course;
        initComponent();
    }

    public void initComponent() {
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Choose the name of the course:");
        final JTextField field = new JTextField();

        // OK/Cancel button
        JButton OKButton = new JButton("OK");
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!field.getText().isEmpty()) {
                    course.setCourseId(field.getText());
                    setVisible(false);
                }
            }
        });
        this.add(OKButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                setVisible(false);
            }
        });
        this.add(cancelButton);

        JPanel bottomButtons = new JPanel();
        bottomButtons.setLayout(new FlowLayout());
        bottomButtons.add(cancelButton);
        bottomButtons.add(OKButton);

        add(label, BorderLayout.NORTH);
        add(field, BorderLayout.CENTER);
        add(bottomButtons, BorderLayout.SOUTH);

        pack();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(true);

        setLocationRelativeTo(getParent());

    }
}
