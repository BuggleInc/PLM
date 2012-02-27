package jlm.core.ui.action;

import jlm.core.model.Course;
import jlm.core.model.Game;
import jlm.core.ui.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class DeleteCourse extends AbstractGameAction {

    private Course course;

    public DeleteCourse(Game game, String text) {
        super(game, text);
        course = game.getCurrentCourse();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (course.getCourseId() != null) {
            int choice = JOptionPane.showConfirmDialog(MainFrame.getInstance(), "Do you really want to delete the course on the server?");
            if(choice == JOptionPane.OK_OPTION)
                course.delete();
        }
    }
}
