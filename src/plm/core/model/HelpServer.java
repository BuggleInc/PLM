package plm.core.model;

/**
 * Abstract class that allows the user to ask help to a teacher by clicking on help button
 * This class only constructs the request with JSON, an implementation must be done to send requests
 * For now it's App Engine which is used
 */
public abstract class HelpServer {
	protected String username;
    // the user is already requesting help

	public HelpServer() {
		username = System.getenv("USER");
		if (username == null)
			username = System.getenv("USERNAME");
		if (username == null)
			username = "John Doe";
	}

    /**
     * Construct a request to ask teacher help in a course
     */
    public void setStatus(boolean isRequestingHelp){
    }

   	/**
	 * Method to implement to indicate how/where to send data
	 *
	 * @param request
	 *            request in json to send to the server
	 */
    public abstract String sendRequest(String request);

}
