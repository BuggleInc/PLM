package jlm.universe.smn.pancake.raw;


/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @see Pancake
 */
public class PancakesStack {
	/**
	 * Create a new stack of pancakes, which can be mixed or not
	 * @param size : the size of the stack of pancakes 
	 * @return A new stack of size pancakes
	 */
	public static PancakesStack create(int size, boolean mixed) {
		PancakesStack pancakesStack = new PancakesStack(size);
		if (mixed)
		{
			pancakesStack.mix();
		}
		return pancakesStack;
	}
	
	private boolean flipped; // Used in order to improve the visual of the flipping
	private Pancake[] pancakeStack; // The stack of pancakes
	
	/**
	 * Constructor of the class PancakesStack
	 * @param size :the size of the stack of pancakes 
	 */
	private PancakesStack(int size) {
		this.pancakeStack = new Pancake[size];
		for (int i = 0; i < size; i++) 
		{
			this.pancakeStack[i] = new Pancake(i + 1);
		}
		this.flipped =false ;
	}
	
	/**
	 * Make a copy of the given object
	 * @return A copy of the given object
	 */
	public PancakesStack copy() {
		PancakesStack stack = new PancakesStack(this.getSize());
		for (int i = 0; i < this.getSize(); i++) 
		{
			stack.pancakeStack[i] = this.getPancake(i);
		}
		stack.flipped = this.flipped;
		return stack;
	}
	
	/**
	 * Make a textual description of the differences between the caller and stack
	 * @param stack : the stack of pancakes with which you want to compare your current stack 
	 * @return A textual description of the differences between the caller and stack
	 */
	public String diffTo(PancakesStack stack) {
		String s="These two stacks are identical";
		if ( !this.equals(stack))
		{
			int amountOfPancakes = this.getSize();
			if ( amountOfPancakes != stack.getSize())
			{
				s = "These two stacks don't have the same size ( "+amountOfPancakes+" vs "+ stack.getSize() +" )";
			}
			else
			{
				s="";
				for ( int i = 0;i< amountOfPancakes;i++)
				{
					if ( !this.getPancake(i).equals(stack.getPancake(i)))
					{
						s+=" Pancake number "+(i+1)+" : "+this.getPancake(i).toString() +" vs "+stack.getPancake(i).toString();
						s+="\n";
					}
				}
			}
		}
		return s;
	}
	
	/**
	 * Indicate whether some other object is "equal to" this one
	 * @return If the two objects are equals
	 * @param Object o: the reference object with which to compare
	 */
	@Override
	public boolean equals(Object o) {
		boolean sw=true;
		if (o == null || !(o instanceof PancakesStack) )
		{
			sw = false ;
		}
		else
		{
			PancakesStack other = (PancakesStack) o;
			int size = this.getSize();
			sw = other.getSize() == size;
			for (int i=0;i<size&&sw;i++) 
			{
				sw = this.getPancake(i).equals(other.getPancake(i));
			}
		}
		return sw;
	}

	/**
	 * Flip a certain amount of pancakes
	 * @param numberOfPancakes : the number of pancakes, beginning from the top of the stack, that you want to flip.
	 * @throws InvalidPancakeNumber : in case you ask to flip less than one or more than the total amount of pancakes
	 */
	public void flip(int numberOfPancakes) throws InvalidPancakeNumber {
		if ( numberOfPancakes < 0 || numberOfPancakes > this.getSize()) {
			throw new InvalidPancakeNumber(
					"The number of pancakes must be between 0 and the maximum amount of " +
					"pancakes in the stack " +
					"( "+this.getSize() +" here )\n"+ numberOfPancakes +" (the number of pancakes) isn't !");							
		}
		int firstPancake = 0 ;
		int lastPancake = numberOfPancakes-1;
		while ( firstPancake < lastPancake )
		{
			// Swapping time !
			this.swap(firstPancake, lastPancake);
			// Changing time !
			firstPancake++;
			lastPancake--;
		}
		this.flipped = !this.flipped;
		return;
	}

	/**
	 * Give a specific pancake among others
	 * @param pancakeNumber : the number of the pancake, beginning from the top of the stack, that you want to get.
	 * @return The expected pancake
	 */
	Pancake getPancake(int pancakeNumber) {
		return this.pancakeStack[pancakeNumber];
	}

	/**
	 * Give the size of a specific pancake among others
	 * @param pancakeNumber : the number of the pancake, beginning from the top of the stack, that you want to get.
	 * @return The size of the expected pancake
	 */
	public int getPancakeRadius(int pancakeNumber){
		return this.getPancake(pancakeNumber).getRadius();
	}
	
	/**
	 * Give the size of the stack of pancakes
	 * @return The number of pancakes in the stack
	 */
	public int getSize() {
		return this.pancakeStack.length;
	}
	

	/**
	 * Tell the value of flipped which is used for graphic purpose only
	 * @return the flipped
	 */
	public boolean isFlipped() {
		return flipped;
	}
	
	/**
	 * Tell if the stack of pancakes is correctly sorted according to the control freak pancake seller
	 * @return TRUE if the stack is okay <br>FALSE else
	 */
	public boolean isSorted() {
		boolean stackSorted = true;
		int stackSize = this.getSize();
		for ( int pancakeNumber = 0 ; stackSorted  && pancakeNumber < stackSize ; pancakeNumber++)
		{
			Pancake pancake = this.getPancake(pancakeNumber);
			stackSorted = ( pancake.getRadius() == pancakeNumber+1);
		}
		return stackSorted;
	}
	
	/**
	 * Mix the stack in order to screw the pancake seller
	 */
	public void mix() {
		int stackSize = this.getSize();
		// Just to be sure that the stack is a bit mixed
		while( this.isSorted() )
		{
			for ( int pancakeNumber = 0 ; pancakeNumber < stackSize ; pancakeNumber++)
			{
				// Swapping time !
				if ( Math.random() > 0.5)
				{
					this.swap(pancakeNumber,(int)(Math.random()*stackSize));
				}
			}
		}
		return;
	}
	
	/**
	 * Place the given pancake at the given floor in the stack
	 * @param pancakeNumber : the number of the pancake, beginning from the top of the stack, where you want to place the pancake.
	 * @param pancake :the pancake that you want to place in the stack
	 */
	private void setPancake(int pancakeNumber, Pancake pancake){
		this.pancakeStack[pancakeNumber] = pancake.copy();
		return;
	}
	
	
	/**
	 * Swap two pancakes
	 * @param pancakeOne :the first pancake which will be swapped
	 * @param pancakeTwo :the second one
	 */
	private void swap(int pancakeOne, int pancakeTwo) {
		Pancake flyingPancake = this.getPancake(pancakeOne);
		this.setPancake(pancakeOne,this.getPancake(pancakeTwo));
		this.setPancake(pancakeTwo,flyingPancake);
		return;
	}
	
	/**
	 * Return a string representation of the stack of pancakes
	 * @return A string representation of the stack of pancakes
	 */
	@Override
	public String toString() {
		int amountOfPancakes = this.getSize();
		String s = "< Stack size: "+amountOfPancakes + " >";
		for ( int i =0; i < amountOfPancakes ; i++)
		{
			s += "\n";
			s +=this.getPancake(i).toString();
		}
		return s;
	}

}
