package lessons.lightbot;

public class LightBotSyntaxErrorException extends Exception {
	private static final long serialVersionUID = 1L;

	public LightBotSyntaxErrorException(String s, String location) {
		super(location+": Syntax error: "+s);
	}
}
