package jlm.core.ui;

import jlm.core.model.Game;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

public class TeacherConsoleDialog extends JDialog{
    
    private URL server;
    private HashMap<String, Integer> usersResults;
    private HashMap<String, Integer> exoResults;

    public TeacherConsoleDialog() {
        super(MainFrame.getInstance(), "The JLM Teacher Console", false);

        try {
            server = new URL(Game.getProperty("jlm.appengine.url") + "/teacher");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        usersResults = new HashMap<String, Integer>();
        exoResults = new HashMap<String, Integer>();

        initConnection();
        initComponent();
    }

    private void initConnection() {
        // get data from JLMServer
        try {
            // Construct data
            String data;
            data = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode("refresh", "UTF-8");

            // Send data
            URLConnection conn = server.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get results
            InputStream is = conn.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            usersResults = (HashMap<String, Integer>)ois.readObject();
            if(usersResults != null)
                exoResults = (HashMap<String, Integer>)ois.readObject();

            ois.close();
            wr.close();
            is.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Unable to contact JLMServer");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(usersResults);
        System.out.println(exoResults);
    }

    public void initComponent(){
        setLayout(new BorderLayout());

        JToolBar toolBar = new JToolBar();
        toolBar.setBorder(BorderFactory.createEtchedBorder());
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBorderPainted(false);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBorderPainted(false);

        toolBar.add(refreshButton);
        toolBar.add(deleteButton);
        
        add(BorderLayout.NORTH, toolBar);

        pack();
		setMinimumSize(new Dimension(500, 300));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(true);

		setLocationRelativeTo(getParent());

    }
}
