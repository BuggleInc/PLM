package jlm.universe.pancake;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @version 1.2
 * @see PancakesStack
 */
public class PancakeNumberException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Create a PancakeNumberException with errorMessage as message
	 * @version 1.2
	 * @param size : the size of the stack of pancakes 
	 * @return A new stack of size pancakes
	 */
	public PancakeNumberException(String errorMessage) {
		super(errorMessage);
	}

}
