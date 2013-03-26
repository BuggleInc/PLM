package jlm.core.ui;

import jlm.core.model.Course;
import jlm.core.model.CourseAppEngine;
import jlm.core.model.Game;
import jlm.core.model.ServerAnswer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Dialog to create a course on the server, with a name, a user password and an administrator password
 */
public class CreateCourseDialog extends JDialog {

	private static final long serialVersionUID = 2678745555760465778L;
	
	private Course course;
    private JButton OKButton;
    private JTextField nameField;
    private JTextField passwordField;
    private JTextField teacherPasswordField;

    public CreateCourseDialog() {
        super(MainFrame.getInstance(), "Add a course", false);

        this.course = new CourseAppEngine();
        initComponent();
    }

    public void initComponent() {
        setLayout(new BorderLayout());

        // OK and Cancel buttons

        OKButton = new JButton("OK");
        OKButton.setEnabled(false);
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                createCourse();
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

        // labels describing fields
        JLabel nameLabel = new JLabel("Name: ");
        JLabel passwordLabel = new JLabel("Password (optionnal): ");
        JLabel teacherPasswordLabel = new JLabel("Teacher password: ");

        // fields where to enter data
        nameField = new JTextField(10);
        nameField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
            	boolean valid = !nameField.getText().isEmpty() && !teacherPasswordField.getText().isEmpty();
            	
                OKButton.setEnabled(valid);
                
                if(valid && e.getKeyCode() == KeyEvent.VK_ENTER)
                    createCourse();
            }
        });

        passwordField = new JPasswordField(10);

        teacherPasswordField = new JPasswordField(10);
        teacherPasswordField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
            	boolean valid = !nameField.getText().isEmpty() && !teacherPasswordField.getText().isEmpty();
            	
                OKButton.setEnabled(valid);

                if(valid && e.getKeyCode() == KeyEvent.VK_ENTER)
                    createCourse();
            }
        });

        // panels to contain all those components

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

        JPanel teacherPasswordPanel = new JPanel();
        teacherPasswordPanel.setLayout(new FlowLayout());
        teacherPasswordPanel.add(teacherPasswordLabel);
        teacherPasswordPanel.add(teacherPasswordField);

        fieldsPanel.add(namePanel, BorderLayout.NORTH);
        fieldsPanel.add(passwordPanel, BorderLayout.CENTER);
        fieldsPanel.add(teacherPasswordPanel, BorderLayout.SOUTH);

        add(fieldsPanel, BorderLayout.CENTER);
        add(bottomButtons, BorderLayout.SOUTH);

        pack();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(true);

        setLocationRelativeTo(getParent());

    }

    /**
     * Create a new course with given name, password and admin password, if name is not empty
     * We considerate that a course can be created without any password
     */
    public void createCourse() {
        if (!nameField.getText().isEmpty()) {
            course.setCourseId(nameField.getText());
            course.setPassword(passwordField.getText());
            course.setTeacherPassword(teacherPasswordField.getText());
            setVisible(false);

            if (course.getCourseId() != null){
                ServerAnswer answer = course.create();
                if(answer == ServerAnswer.COURSE_NAME_ALREADY_USED)
                    JOptionPane.showMessageDialog(getParent(), "Course name already used on the server", "Server error",
                            JOptionPane.ERROR_MESSAGE);
                else if(answer == ServerAnswer.ALL_IS_FINE){
                    Game.getInstance().setCurrentCourse(course);
                    MainFrame.getInstance().appendToTitle("[" + course.getCourseId() + "]");
                }
            }
        }
    }
}
