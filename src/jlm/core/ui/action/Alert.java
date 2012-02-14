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

public class Alert extends AbstractGameAction {

    private URL server;
    private String username;

    public Alert(Game game, String text, ImageIcon icon) {
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            URLConnection conn = server.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
