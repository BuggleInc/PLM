package jlm.universe.smn.pancake.burned;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @see PancakesStack
 */
public class InvalidPancakeNumber extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Create a InvalidPancakeNumber with errorMessage as message
	 * @param size : the size of the stack of pancakes 
	 * @return A new stack of size pancakes
	 */
	public InvalidPancakeNumber(String errorMessage) {
		super(errorMessage);
	}

}
