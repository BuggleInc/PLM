package jlm.core.ui.action;

import jlm.core.model.Course;
import jlm.core.model.Game;
import jlm.core.ui.CreateCourseDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CreateCourse extends AbstractGameAction {

    private Course course;

    public CreateCourse(Game game, String text) {
        super(game, text);
        course = game.getCurrentCourse();
    }

    public CreateCourse(Game game, String text, ImageIcon icon) {
        super(game, text, icon);
        course = game.getCurrentCourse();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        new CreateCourseDialog(course).show();
    }
}
