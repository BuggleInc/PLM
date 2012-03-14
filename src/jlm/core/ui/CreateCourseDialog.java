package jlm.core.ui;

import jlm.core.model.Course;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateCourseDialog extends JDialog {

    private static Course course;

    public CreateCourseDialog(Course course) {
        super(MainFrame.getInstance(), "Add a course", false);

        this.course = course;
        initComponent();
    }

    public void initComponent() {
        setLayout(new BorderLayout());

        JLabel nameLabel = new JLabel("Name:");
        final JTextField nameField = new JTextField(10);

        JLabel passwordLabel = new JLabel("Password (optionnal):");
        final JPasswordField passwordField = new JPasswordField(10);

        JButton OKButton = new JButton("OK");
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!nameField.getText().isEmpty()) {
                    course.setCourseId(nameField.getText());
                    course.setPassword(passwordField.getText());
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

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BorderLayout());
        
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout());
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new FlowLayout());
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        fieldsPanel.add(namePanel, BorderLayout.NORTH);
        fieldsPanel.add(passwordPanel, BorderLayout.SOUTH);


        add(fieldsPanel, BorderLayout.CENTER);
        add(bottomButtons, BorderLayout.SOUTH);

        pack();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(true);

        setLocationRelativeTo(getParent());

    }
}
