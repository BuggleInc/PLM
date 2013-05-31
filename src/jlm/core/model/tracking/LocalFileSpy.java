package jlm.core.model.tracking;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import jlm.core.model.Game;
import jlm.core.model.lesson.Exercise;

/**
 * Spy that registers events in a local file
 */
public class LocalFileSpy implements ProgressSpyListener {

    private String username;
    private String filePath;

    public LocalFileSpy(File path) {
        username = System.getenv("USER");
        if (username == null)
            username = System.getenv("USERNAME");
        if (username == null)
            username = "John Doe";

        filePath = path.getAbsolutePath() + System.getProperty("file.separator")+ "progress.spy";
    }

    @Override
    public void executed(Exercise exo) {
        if (Game.getInstance().studentWork.getPassed(exo.getId(), exo.lastResult.language)) {
            write(username + " solved " + exo.getName() + " in "
                    + exo.lastResult.language + "!");
        } else {
            write(username + " failed " + exo.getName() + " in "
                    + exo.lastResult.language + "!");
        }
    }

    private void write(String msg) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath,
                    true));
            bw.write("[" + formatter.format(new java.util.Date()) + "] " + msg);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void switched(Exercise exo) {    /* i don't care, i'm a viking */ }

    @Override
    public void heartbeat() { /* don't talk to me */ }

    @Override
    public String join() { return ""; }

    @Override
    public void leave() { /* good idea, go away */ }
}
