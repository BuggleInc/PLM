package plm.core.model.tracking;

import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterSpy implements ProgressSpyListener {
	private String username;
	private Twitter twitter;

	public TwitterSpy() {
		username = System.getenv("USER");
		if (username == null)
			username = System.getenv("USERNAME");
		if (username == null)
			username = "John Doe";

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(Game.getProperty("plm.oauth.consumerKey"))
		  .setOAuthConsumerSecret(Game.getProperty("plm.oauth.consumerSecret"))
		  .setOAuthAccessToken(Game.getProperty("plm.oauth.accessToken"))
		  .setOAuthAccessTokenSecret(Game.getProperty("plm.oauth.tokenSecret"));
		cb.setUseSSL(true);
		TwitterFactory tf = new TwitterFactory(cb.build());
	    twitter = tf.getInstance();
	}

	@Override
	public void executed(Exercise exo) {
		if (Game.getInstance().studentWork.getPassed(exo, exo.lastResult.language)) {
			try {
				Status status = twitter.updateStatus(username+" solved "+exo.getName()+" in "+exo.lastResult.language+"!");
				if (Game.getInstance().isDebugEnabled()) 
					System.out.println("Twitted! Status: "+status.toString());
			} catch (Exception e) {
				// silently ignore network unavailability ;)
				if (Game.getInstance().isDebugEnabled())
					e.printStackTrace();
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
