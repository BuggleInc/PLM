package plm.core.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import plm.core.model.Game;
import plm.core.ui.CreateCourseDialog;
import plm.core.ui.TeacherConsoleDialog;

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
        new CreateCourseDialog().setVisible(true);
        teacherConsoleDialog.refresh();
    }
}
