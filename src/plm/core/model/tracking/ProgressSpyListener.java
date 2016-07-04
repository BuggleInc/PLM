package plm.core.model.tracking;

import plm.core.model.lesson.Exercise;

public interface ProgressSpyListener {
	void executed(Exercise exo);

    void switched(Exercise exo);

    void reverted(Exercise exo);

    void heartbeat();

    String join();

    void leave();

	void callForHelp(String studentInput);
	void cancelCallForHelp();

	void readTip(String id, String mission);

	void idle(String begin, String end, String duration);
}
