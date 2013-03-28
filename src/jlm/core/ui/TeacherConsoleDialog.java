package jlm.core.ui;

import jlm.core.model.Course;
import jlm.core.model.Game;
import jlm.core.model.ServerAnswer;
import jlm.core.ui.action.CreateCourse;
import jlm.core.ui.action.DeleteCourse;
import jlm.core.ui.action.RefreshCourse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Dialog to display all options available to the teacher
 * Once the course selected, he can view its students results in this console
 * He can also manage courses : create or delete
 */
public class TeacherConsoleDialog extends JDialog {
	private static final long serialVersionUID = 1L;

    //private Game game;
    //private Course course;
    private JLabel courseNameLabel;
    private ResultsPanel allResultsPanel;
    private ResultsPanel helpPanel;
    private ResultsPanel layaboutPanel;
    private ResultsPanel badPanel;
    private ResultsPanel goodPanel;

    public TeacherConsoleDialog() {
        super(MainFrame.getInstance(), "JLM Teacher Console", false);

        Game game = Game.getInstance();
        Course course = game.getCurrentCourse();

        // automatically refresh the course to display in the teacher console if it's empty
        if (course.getCourseId() != null && !course.getCourseId().isEmpty()
                && course.getServerData() == null){
            String answer = course.refresh();
            try {
                if (ServerAnswer.values()[Integer.parseInt(answer)] == ServerAnswer.WRONG_TEACHER_PASSWORD)
                    JOptionPane.showMessageDialog(this, "Wrong teacher password for the course",
                            "Server error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException nfe) {
             // the answer was not a status message, it contains course data
            }
            // refresh the needing help, layout, bad and good students lists
            course.refreshStudentsLists();
        }

        initComponent();
    }

    public void initComponent() {
        setLayout(new BorderLayout());

        //Top toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setBorder(BorderFactory.createEtchedBorder());

        Game game = Game.getInstance();
        
        courseNameLabel = new JLabel(game.getCourseID().isEmpty() ? "No course selected" : "[" + game.getCourseID() + "]");

        JButton newButton = new JButton(new CreateCourse(game, "New course",
                ResourcesCache.getIcon("resources/add.png"), this));
        newButton.setBorderPainted(false);

        JButton refreshButton = new JButton(new RefreshCourse(game, "Refresh",
                ResourcesCache.getIcon("resources/refresh.png"), this));
        refreshButton.setBorderPainted(false);

        JButton deleteButton = new JButton(new DeleteCourse(game, "Delete",
                ResourcesCache.getIcon("resources/delete.png"), this));
        deleteButton.setBorderPainted(false);

        toolBar.add(courseNameLabel);
        toolBar.add(newButton);
        toolBar.add(refreshButton);
        toolBar.add(deleteButton);
        
        add(BorderLayout.NORTH, toolBar);
        
        //Center tabbed panel, with the different resultsPanel
        JTabbedPane tabbedPanel = new JTabbedPane();
        
        allResultsPanel = new ResultsPanel(null);
        tabbedPanel.addTab("All results", new JScrollPane(allResultsPanel));
        tabbedPanel.setMnemonicAt(0, KeyEvent.VK_A);
        
        Course course = game.getCurrentCourse();
        
        helpPanel = new ResultsPanel(course.getNeedingHelpStudents());
        tabbedPanel.addTab("Need help", null,
                new JScrollPane(helpPanel), "Students requesting help");
        tabbedPanel.setMnemonicAt(0, KeyEvent.VK_H);

        layaboutPanel = new ResultsPanel(course.getLayaboutStudents());
        tabbedPanel.addTab("No activity", null,
                new JScrollPane(layaboutPanel), "Students with no recorded activity");
        tabbedPanel.setMnemonicAt(0, KeyEvent.VK_L);

        badPanel = new ResultsPanel(course.getBadStudents());
        tabbedPanel.addTab("Failing", null,
                new JScrollPane(badPanel), "Students failing completely");
        tabbedPanel.setMnemonicAt(0, KeyEvent.VK_B);

        goodPanel = new ResultsPanel(course.getGoodStudents());
        tabbedPanel.addTab("Successful", null,
                new JScrollPane(goodPanel), "Students doing good");
        tabbedPanel.setMnemonicAt(0, KeyEvent.VK_G);
        
        add(BorderLayout.CENTER, tabbedPanel);

        pack();
        setMinimumSize(new Dimension(500, 300));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(true);
        setVisible(true);

        setLocationRelativeTo(getParent());

    }

    public void refresh() {
    	Game game = Game.getInstance();
        courseNameLabel.setText(game.getCourseID().isEmpty() ? "" : "[" + game.getCourseID() + "]");
        allResultsPanel.displayResults();
        helpPanel.displayResults();
        layaboutPanel.displayResults();
        badPanel.displayResults();
        goodPanel.displayResults();
        this.repaint();
    }
}
