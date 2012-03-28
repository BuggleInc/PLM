package jlm.core.ui.action;

import jlm.core.model.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class HelpMe extends AbstractGameAction {

    // url to the course server
    private URL server;
    private String username;
    // the user is already requesting help
    private boolean isRequestingHelp;

    public HelpMe(Game game, String text, ImageIcon icon) {
        super(game, text, icon);

        username = System.getenv("USER");
        if (username == null)
            username = System.getenv("USERNAME");
        if (username == null)
            username = "John Doe";

        try {
            server = new URL(Game.getProperty("jlm.appengine.url") + "/alert");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        isRequestingHelp = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String courseId = Game.getInstance().getCourseID();
        if (!courseId.isEmpty()) {
            JButton helpMeButton = (JButton) e.getSource();

            try {
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                data += "&" + URLEncoder.encode("course", "UTF-8") + "=" + URLEncoder.encode(courseId, "UTF-8");


                if (!isRequestingHelp) {
                    // the user is not yet requesting help, launch a request
                    data += "&" + URLEncoder.encode("request_help", "UTF-8") + "=" + URLEncoder.encode(courseId, "true");
                    helpMeButton.setText("Discard help");

                } else {
                    // the user got some help, abort the request
                    data += "&" + URLEncoder.encode("request_help", "UTF-8") + "=" + URLEncoder.encode(courseId, "false");
                    helpMeButton.setText("Help");

                }

                isRequestingHelp = !isRequestingHelp;
                URLConnection conn = server.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
