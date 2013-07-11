package lessons.sort.pancake;

public class InvalidPancakeRank extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Create a InvalidPancakeNumber with errorMessage as message
	 * @param size : the size of the stack of pancakes 
	 * @return A new stack of size pancakes
	 */
	public InvalidPancakeRank(String errorMessage) {
		super(errorMessage);
	}

}
