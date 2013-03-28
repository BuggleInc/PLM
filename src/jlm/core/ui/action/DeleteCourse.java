package jlm.core.ui.action;

import jlm.core.model.Course;
import jlm.core.model.CourseAppEngine;
import jlm.core.model.Game;
import jlm.core.model.ServerAnswer;
import jlm.core.ui.MainFrame;
import jlm.core.ui.TeacherConsoleDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Controller to handle clicks on the Delete button from the teacher console
 */
public class DeleteCourse extends AbstractGameAction {
	private static final long serialVersionUID = 1L;

    private Course course;
    private TeacherConsoleDialog parentComponent;

    public DeleteCourse(Game game, String text, TeacherConsoleDialog parentComponent) {
        super(game, text);
        course = game.getCurrentCourse();
        this.parentComponent = parentComponent;
    }

    public DeleteCourse(Game game, String text, ImageIcon icon, TeacherConsoleDialog parentComponent) {
        super(game, text, icon);
        course = game.getCurrentCourse();
        this.parentComponent = parentComponent;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (course.getCourseId() != null) {
            int choice = JOptionPane.showConfirmDialog(MainFrame.getInstance(), "Do you really want to delete the course on the server?");
            if (choice == JOptionPane.OK_OPTION) {
                String answer = course.delete();
                ServerAnswer serverAnswer = ServerAnswer.values()[Integer.parseInt(answer)];
                if (serverAnswer == ServerAnswer.WRONG_TEACHER_PASSWORD)
                    JOptionPane.showMessageDialog(parentComponent, "Wrong module teacher password", "Server error",
                            JOptionPane.ERROR_MESSAGE);
                if(serverAnswer == ServerAnswer.ALL_IS_FINE){
                    Game.getInstance().setCurrentCourse(new CourseAppEngine());
                    MainFrame.getInstance().appendToTitle("");
                    parentComponent.refresh();
                }
            }
        }
    }
}
