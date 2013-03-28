package jlm.core.model.tracking;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import jlm.core.model.Game;
import jlm.core.model.lesson.Exercise;

public class TwitterSpy implements ProgressSpyListener {
	private String username;
	private Twitter twitter;

	public TwitterSpy() {
		username = System.getenv("USER");
		if (username == null)
			username = System.getenv("USERNAME");
		if (username == null)
			username = "John Doe";

		twitter = new TwitterFactory().getOAuthAuthorizedInstance(
				Game.getProperty("jlm.oauth.consumerKey"), 
				Game.getProperty("jlm.oauth.consumerSecret"), 
				new AccessToken(Game.getProperty("jlm.oauth.accessToken"), Game.getProperty("jlm.oauth.tokenSecret")));
	}

	@Override
	public void executed(Exercise exo) {
		if (Game.getInstance().studentWork.getPassed(exo.getId(), exo.lastResult.language)) {
			try {
				twitter.updateStatus(username+" solved "+exo.getName()+" in "+exo.lastResult.language+"!");
			} catch (Exception e) {
				// silently ignore network unavailability ;)
				//e.printStackTrace();
			}
		}
	}

    @Override
    public void switched(Exercise exo) {    /* i don't care, i'm a viking */    }

    @Override
    public void heartbeat() {}

    @Override
    public String join() { return ""; }

    @Override
    public void leave() {
    }

}
