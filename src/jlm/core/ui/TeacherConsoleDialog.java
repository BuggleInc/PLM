package jlm.core.ui;

import jlm.core.model.Game;
import jlm.core.ui.action.CreateCourse;
import jlm.core.ui.action.DeleteCourse;
import jlm.core.ui.action.RefreshCourse;

import javax.swing.*;
import java.awt.*;

public class TeacherConsoleDialog extends JDialog {

    private Game game;
    private JLabel courseNameLabel;
    private ResultsPanel resultsPanel;

    public TeacherConsoleDialog() {
        super(MainFrame.getInstance(), "The JLM Teacher Console", false);

        game = Game.getInstance();
        if (game.getCurrentCourse().getCourseId() != null)
            game.getCurrentCourse().refresh();

        initComponent();
    }

    public void initComponent() {
        setLayout(new BorderLayout());

        resultsPanel = new ResultsPanel();

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
        add(BorderLayout.CENTER, resultsPanel);

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
