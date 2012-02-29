package jlm.core.ui;

import jlm.core.model.Game;
import jlm.core.ui.action.CreateCourse;
import jlm.core.ui.action.DeleteCourse;
import jlm.core.ui.action.RefreshCourse;

import javax.swing.*;
import java.awt.*;

public class TeacherConsoleDialog extends JDialog {

    private Game game;

    public TeacherConsoleDialog() {
        super(MainFrame.getInstance(), "The JLM Teacher Console", false);

        game = Game.getInstance();
        initComponent();
    }

    public void initComponent() {
        setLayout(new BorderLayout());

        ResultsPanel resultsPanel = new ResultsPanel(game.getCurrentCourse());

        JToolBar toolBar = new JToolBar();
        toolBar.setBorder(BorderFactory.createEtchedBorder());

        JButton newButton = new JButton(new CreateCourse(game, "Create course"));
        newButton.setBorderPainted(false);

        JButton refreshButton = new JButton(new RefreshCourse(game, "Refresh"));
        refreshButton.setBorderPainted(false);

        JButton deleteButton = new JButton(new DeleteCourse(game, "Delete"));
        deleteButton.setBorderPainted(false);

        toolBar.add(newButton);
        toolBar.add(refreshButton);
        toolBar.add(deleteButton);

        add(BorderLayout.NORTH, toolBar);
        add(BorderLayout.CENTER, resultsPanel);

        pack();
        setMinimumSize(new Dimension(500, 300));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(true);

        setLocationRelativeTo(getParent());

    }
}
