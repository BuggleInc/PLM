package jlm.core.model;

import jlm.core.model.lesson.ExecutionProgress;
import jlm.core.model.lesson.Exercise;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;

public class AppEngineSpy implements ServerSpy {
    
    private String username;
    private URL server;

    public AppEngineSpy() {
        username = System.getenv("USER");
        if (username == null)
            username = System.getenv("USERNAME");
        if (username == null)
            username = "John Doe";

        try {
            server = new URL(Game.getProperty("jlm.appengine.url") + "/student");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    
    
    @Override
    public void executed(Exercise exo) {
    	
        HashMap<String, String> parameters = new HashMap<String, String>();
        Game game = Game.getInstance();
    	
        ExecutionProgress lastResult = exo.lastResult;
        
        // Retrieve appropriate parameters regarding the current exercise
        parameters.put("username", 		username );
        parameters.put("course", 	game.getCourseID() );
        
        parameters.put("exoname", 		exo.getName() );
        parameters.put("exolang", 		lastResult.language.toString() );
        parameters.put("passedtests", 	lastResult.passedTests +"" );
        parameters.put("totaltests", 	lastResult.totalTests +"" );
        
        
        try {
        	// Construct request query
        	String query = "";
        	
        	Set<String> paramKeys = parameters.keySet();
        	boolean putDelim = false;
        	for (String key : paramKeys) 
        	{      		
        		if (putDelim) query += "&";
        		else putDelim = true;
        		
        		query += key + "=" + URLEncoder.encode(parameters.get(key), "UTF-8");
        	}
        	
        	this.sendQuery(query);
        	System.out.println(query);
        	
        } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }
        
    }
    
    
    
    public void sendQuery(String query) {
    	try {
    		
	    	// Send data
	        URLConnection conn = server.openConnection();
	        conn.setDoOutput(true);
	        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	        wr.write(query);
	        wr.flush();
	
	        // Get response data and print it
	        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        String line;
	        while((line = br.readLine()) != null)
	            System.err.println(line);
	
	        wr.close();
	        br.close();
	        
	    }  catch (IOException e) {
            // e.printStackTrace();
            System.out.println("Unable to contact JLMServer");
        }
    }

}
