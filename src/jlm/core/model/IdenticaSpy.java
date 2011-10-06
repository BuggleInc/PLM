package jlm.core.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jlm.core.model.lesson.Exercise;

import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class IdenticaSpy implements ProgressSpyListener {
	private String username;

	public IdenticaSpy() {
		username = System.getenv("USER");
		if (username == null)
			username = System.getenv("USERNAME");
		if (username == null)
			username = "John Doe";

	}

	@Override
	public void executed(Exercise exo) {
		if (exo.isSuccessfullyPassed()) {
			
			DefaultHttpClient httpclient = new DefaultHttpClient();
			try {
				HttpPost post = new HttpPost(new URI("http://identi.ca/api/statuses/update.json"));

				httpclient.getCredentialsProvider().setCredentials(
						new AuthScope("identi.ca", 80),
						new UsernamePasswordCredentials(Game.getProperty("jlm.identica.username"), 
								Game.getProperty("jlm.identica.password")));
				
				List<NameValuePair> formparams = new ArrayList<NameValuePair>();
				formparams.add(new BasicNameValuePair("status", username+" solved "+exo.getName()+" in "+exo.lastResult.language+"!"));
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
				post.setEntity(entity);					
				
				httpclient.execute(post);
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
}
