package jlm.core.ui.action;

import jlm.core.model.Course;
import jlm.core.model.Game;
import jlm.core.model.ServerAnswer;
import jlm.core.ui.TeacherConsoleDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RefreshCourse extends AbstractGameAction {

    private Course course;
    private TeacherConsoleDialog parentComponent;

    public RefreshCourse(Game game, String text, TeacherConsoleDialog parentComponent) {
        super(game, text);
        course = game.getCurrentCourse();
        this.parentComponent = parentComponent;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (course.getCourseId() != null) {
            String answer = course.refresh();
            System.out.println(answer);
            try {
                if (ServerAnswer.values()[Integer.parseInt(answer)] == ServerAnswer.WRONG_TEACHER_PASSWORD)
                    JOptionPane.showMessageDialog(parentComponent, "Wrong module teacher password", "Server error",
                            JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException nfe) { /* the answer was not a status message */ }
            parentComponent.refresh();
        }
    }
}
