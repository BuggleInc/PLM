package plm.core.model.tracking;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class that report the heartbeat of the user
 * Every minute, the presence of the user on the soft is sent to the server
 * This allows to know who is still connected on a specific course on PLM
 */
public class HeartBeatSpy extends TimerTask{

    private final static int timeOut = 60;
    private Timer timer;
    private ArrayList<ProgressSpyListener> spyListeners;

    public HeartBeatSpy(ArrayList<ProgressSpyListener> spyListeners){
        timer = new Timer();
        timer.schedule(this, 0, timeOut * 1000);
        this.spyListeners = spyListeners;
    }

    @Override
    public void run() {
        // Send a report to the server
        for(ProgressSpyListener listener: spyListeners){
            if(listener instanceof ServerSpy)
                listener.heartbeat();
        }
    }

    public void die(){
        timer.cancel();
    }

}
