package jlm.core.model;

import jlm.core.model.lesson.Exercise;

public interface ServerSpy extends ProgressSpyListener {
    
    public void executed(Exercise exo);
    
    public void sendQuery(String query);
}
