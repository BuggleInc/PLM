package jlm.core.ui.action;

import jlm.core.model.Game;
import jlm.core.ui.CreateCourseDialog;
import jlm.core.ui.TeacherConsoleDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Controller to handle clicks on the create button from the teacher console
 */
public class CreateCourse extends AbstractGameAction {
	private static final long serialVersionUID = 1L;

    private TeacherConsoleDialog teacherConsoleDialog;

    public CreateCourse(Game game, String text, TeacherConsoleDialog teacherConsoleDialog) {
        super(game, text);
        this.teacherConsoleDialog = teacherConsoleDialog;
    }

    public CreateCourse(Game game, String text, ImageIcon icon, TeacherConsoleDialog teacherConsoleDialog) {
        super(game, text, icon);
        this.teacherConsoleDialog = teacherConsoleDialog;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        new CreateCourseDialog().show();
        teacherConsoleDialog.refresh();
    }
}
