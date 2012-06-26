package jlm.core.model;

import jlm.core.model.lesson.Exercise;

public interface ProgressSpyListener {
	public void executed(Exercise exo);

    public void switched(Exercise exo);

    public void heartbeat();

    public String join();

    public void leave();
}
