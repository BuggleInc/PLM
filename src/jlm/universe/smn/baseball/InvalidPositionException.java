package jlm.universe.smn.baseball;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @see BaseballBase
 */
public class InvalidPositionException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidPositionException(String message){
		super(message);
	}
	
}
