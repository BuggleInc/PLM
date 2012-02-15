package jlm.core.ui.action;

import jlm.core.model.Course;
import jlm.core.model.Game;

import java.awt.event.ActionEvent;

public class RefreshCourse extends AbstractGameAction {

    private Course course;

    public RefreshCourse(Game game, String text) {
        super(game, text);
        course = game.getCurrentCourse();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(course.getCourseId() != null)
            course.refresh();
    }
}
