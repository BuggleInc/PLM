package plm.core.model.tracking;

import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;

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
				Game.getProperty("plm.oauth.consumerKey"), 
				Game.getProperty("plm.oauth.consumerSecret"), 
				new AccessToken(Game.getProperty("plm.oauth.accessToken"), Game.getProperty("plm.oauth.tokenSecret")));
	}

	@Override
	public void executed(Exercise exo) {
		if (Game.getInstance().studentWork.getPassed(exo, exo.lastResult.language)) {
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
