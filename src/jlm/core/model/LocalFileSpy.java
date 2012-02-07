package jlm.core.model;

import jlm.core.model.lesson.Exercise;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LocalFileSpy implements ProgressSpyListener {

    private String username;
    
    public LocalFileSpy() {
        username = System.getenv("USER");
        if (username == null)
            username = System.getenv("USERNAME");
        if (username == null)
            username = "John Doe";
    }

    @Override
    public void executed(Exercise exo) {
        if(exo.isSuccessfullyPassed()) {
            String log = username+" solved "+exo.getName()+" in "+exo.lastResult.language+"!";
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("log.file"));
                bw.write(log);
                bw.newLine();
                bw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
