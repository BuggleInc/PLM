package plm.core.model.tracking;

import plm.core.model.lesson.Exercise;

public interface ProgressSpyListener {
	public void executed(Exercise exo);

    public void switched(Exercise exo);

    public void reverted(Exercise exo);
    
    public void heartbeat();

    public String join();

    public void leave();
	
	public void callForHelp(String studentInput);
	public void cancelCallForHelp();

	public void readTip(String id, String mission);
	
	public void idle(String begin, String end, String duration);
}
