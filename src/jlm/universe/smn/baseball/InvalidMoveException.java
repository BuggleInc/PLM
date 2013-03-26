package jlm.universe.smn.baseball;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @see BaseballField
 */
public class InvalidMoveException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidMoveException(String message) {
		super(message);
	}

}
