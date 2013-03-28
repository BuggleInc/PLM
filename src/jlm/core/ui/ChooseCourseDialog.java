package jlm.core.ui;

import jlm.core.model.*;
import jlm.core.model.tracking.HeartBeatSpy;
import jlm.core.model.tracking.ProgressSpyListener;
import jlm.core.model.tracking.ServerSpy;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


/**
 * Dialog to allow students or teachers to select the current course
 * It get the updated list of courses from the server, display it
 * You have to enter the course password, and the admin password if you're teacher
 */
public class ChooseCourseDialog extends JDialog {

    private static final long serialVersionUID = 2234402839093122248L;

    protected ArrayList<String> courseListIDs;
    protected JList jListID = new JList();
    protected JButton OKButton;
    protected JPasswordField passwordField;
    protected JPasswordField teacherPasswordField;
    
    private boolean isChoosen;


    public ChooseCourseDialog() {
        super(MainFrame.getInstance(), "JLM Course", false);
        isChoosen = false;

        initComponent(this.getContentPane());

        setMinimumSize(new Dimension(300, 400));
        pack();

        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        //setVisible(true);
    }

    public void initComponent(Container c) {
        c.setLayout(new BorderLayout());
        c.add(new JLabel("Please select your course:", JLabel.CENTER), BorderLayout.NORTH);

        // OK/Cancel button
        OKButton = new JButton("OK");
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                selectCourse();
            }
        });
        OKButton.setEnabled(false);

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
            coursesPanel.add(new JLabel("No course currently opened, sorry.", JLabel.CENTER), BorderLayout.CENTER);
        } else {
            jListID.setListData(courseListIDs.toArray());

            jListID.setSelectedValue(currentCourse.getCourseId(), true);
            if(!currentCourse.getCourseId().isEmpty())
                OKButton.setEnabled(true);
            jListID.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent listSelectionEvent) {
                    OKButton.setEnabled(true);
                    isChoosen = true;
                }
            });
            jListID.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER && isChoosen)
                        selectCourse();
                }
            });
            coursesPanel.add(jListID, BorderLayout.CENTER);
        }

        // Password panel and field (for the student and for the teacher)
        JPanel passwordsPanel = new JPanel(new BorderLayout()); // i hate swing...
        JLabel passwordLabel = new JLabel("Course password: ");
        passwordField = new JPasswordField(10);
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER && isChoosen)
                    selectCourse();
            }
        });
        
        JPanel passwordPanel = new JPanel(new FlowLayout());
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        passwordsPanel.add(passwordPanel, BorderLayout.NORTH);
        
        	//Teacher password field
        if(Game.getProperty("jlm.configuration.teacher").equals("true")) {
            JLabel teacherPasswordLabel = new JLabel("Teacher password: ");
            teacherPasswordField = new JPasswordField(10);
            teacherPasswordField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER && isChoosen)
                        selectCourse();
                }
            });
            
            JPanel teacherPasswordPanel = new JPanel(new FlowLayout());
            teacherPasswordPanel.add(teacherPasswordLabel);
            teacherPasswordPanel.add(teacherPasswordField);
            passwordsPanel.add(teacherPasswordPanel, BorderLayout.SOUTH);
        }

        coursesPanel.add(passwordsPanel, BorderLayout.SOUTH);
        c.add(coursesPanel, BorderLayout.CENTER);
    }

    /**
     * Select a new course in the list
     * if a course was already selected, send a leave and heartbeat die signal
     * Launch new heartbeat and join events, change current course
     */
    public void selectCourse() {
        Game game = Game.getInstance();

        // leave the previous course and kill the heartbeat if existing
        if (game.getHeartBeatSpy() != null)
            game.getHeartBeatSpy().die();
        if (game.getCourseID() != null && !game.getCourseID().isEmpty())
            for (ProgressSpyListener spyListener : game.getProgressSpyListeners())
                spyListener.leave();

        // select the new course
        String courseName = (jListID.getSelectedValue() != null) ? jListID.getSelectedValue().toString() : "";
        String coursePass = (passwordField.getPassword() != null) ? new String(passwordField.getPassword()) : "";
        
        String teacherCoursePass =  "";
        if (teacherPasswordField != null) {
        	teacherCoursePass = (teacherPasswordField.getPassword() != null) ? new String(teacherPasswordField.getPassword()) : "";
        }
        		
        Course course = new CourseAppEngine(courseName, coursePass);
        course.setTeacherPassword(teacherCoursePass);
        game.setCurrentCourse(course);

        // report the user presence on the server and verify password
        boolean passwordError = false;
        for (ProgressSpyListener spyListener : game.getProgressSpyListeners()) {
            String response = spyListener.join();
            if (spyListener instanceof ServerSpy) {
                if (ServerAnswer.values()[Integer.parseInt(response)] == ServerAnswer.WRONG_PASSWORD)
                    passwordError = true;
            }
        }

        if (passwordError) {
            JOptionPane.showMessageDialog(getParent(), "Wrong module password", "Server error",
                    JOptionPane.ERROR_MESSAGE);
            game.getCurrentCourse().setCourseId(null);
            MainFrame.getInstance().appendToTitle("");
        } else {
            // launch the heartbeat timertask and change window title
            game.setHeartBeatSpy(new HeartBeatSpy(game.getProgressSpyListeners()));
            MainFrame.getInstance().appendToTitle("[ " + jListID.getSelectedValue() + " ]");
        }

        setVisible(false);
    }

}
