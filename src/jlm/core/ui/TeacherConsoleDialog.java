package jlm.core.ui;

import jlm.core.model.Game;
import jlm.core.model.ServerAnswer;
import jlm.core.ui.action.CreateCourse;
import jlm.core.ui.action.DeleteCourse;
import jlm.core.ui.action.RefreshCourse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class TeacherConsoleDialog extends JDialog {

    private Game game;
    private JLabel courseNameLabel;
    private ResultsPanel resultsPanel;
    
    private JTabbedPane tabbedPanel;

    public TeacherConsoleDialog() {
        super(MainFrame.getInstance(), "JLM Teacher Console", false);

        game = Game.getInstance();

        // refresh the course to display in the teacher console
        if (game.getCurrentCourse().getCourseId() != null && !game.getCurrentCourse().getCourseId().isEmpty()){
            String answer = game.getCurrentCourse().refresh();
            try {
                if (ServerAnswer.values()[Integer.parseInt(answer)] == ServerAnswer.WRONG_TEACHER_PASSWORD)
                    JOptionPane.showMessageDialog(this, "Wrong teacher password for the course",
                            "Server error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException nfe) {
             // the answer was not a status message, it contains course data
            }
        }

        initComponent();
    }

    public void initComponent() {
        setLayout(new BorderLayout());

        //Top toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setBorder(BorderFactory.createEtchedBorder());

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
        tabbedPanel = new JTabbedPane();
        
        resultsPanel = new ResultsPanel(null);
        tabbedPanel.addTab("All results", resultsPanel);
        tabbedPanel.setMnemonicAt(0, KeyEvent.VK_A);
        
        JPanel needingHelpPanel = new ResultsPanel(game.getCurrentCourse().getStudentsNeedingHelp());
        tabbedPanel.addTab("Need help", null, needingHelpPanel, "Students requesting help");
        tabbedPanel.setMnemonicAt(0, KeyEvent.VK_H);

        JPanel unknownPanel = new ResultsPanel(game.getCurrentCourse().getLayaboutStudents());
        tabbedPanel.addTab("No activity", null, unknownPanel, "Students with no recorded activity");
        tabbedPanel.setMnemonicAt(0, KeyEvent.VK_L);

        JPanel failingPanel = new ResultsPanel(game.getCurrentCourse().getBadStudents());
        tabbedPanel.addTab("Failing", null, failingPanel, "Students failing completely");
        tabbedPanel.setMnemonicAt(0, KeyEvent.VK_B);

        JPanel successfulPanel = new ResultsPanel(game.getCurrentCourse().getGoodStudents());
        tabbedPanel.addTab("Successful", null, successfulPanel, "Students doing good");
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
        courseNameLabel.setText(game.getCourseID().isEmpty() ? "" : "[" + game.getCourseID() + "]");
        resultsPanel.displayResults();
    }
}
