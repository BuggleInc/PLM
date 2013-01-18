package jlm.universe.pancake;


/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @version 1.2
 * @see Pancake
 */
public class PancakesStack {

	private Pancake[] pancakeStack; // The stack of pancakes
	
	/**
	 * Create a new stack of pancakes, which can be mixed or not
	 * @version 1.1
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
	
	/**
	 * Constructor of the class PancakesStack
	 * @version 1.1
	 * @param size :the size of the stack of pancakes 
	 */
	private PancakesStack(int size) {
		this.pancakeStack = new Pancake[size];
		for (int i = 0; i < size; i++) 
		{
			this.pancakeStack[i] = new Pancake(i + 1);
		}
	}
	
	/**
	 * Make a copy of the given object
	 * @version 1.2
	 * @return A copy of the given object
	 */
	public PancakesStack copy() {
		PancakesStack stack = new PancakesStack(this.getSize());
		for (int i = 0; i < this.getSize(); i++) 
		{
			stack.pancakeStack[i] = this.getPancake(i);
		}
		return stack;
	}
	
	/**
	 * Make a textual description of the differences between the caller and stack
	 * @version 1.2
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
	 * Indicates whether some other object is "equal to" this one. 
	 * @version 1.2
	 * @return If the two objects are equals
	 * @param Object o: the reference object with which to compare.
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
	 * @version 1.1
	 * @param numberOfPancakes : the number of pancakes, beginning from the top of the stack, that you want to flip.
	 * @throws PancakeNumberException : in case you ask to flip less than one or more than the total amount of pancakes
	 */
	public void flip(int numberOfPancakes) throws PancakeNumberException {
		if ( numberOfPancakes < 0|| numberOfPancakes > this.getSize()) {
			throw new PancakeNumberException(
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
		for (int i = 0 ;i<numberOfPancakes;i++)
		{
			this.getPancake(i).flip();
		}
		return;
	}

	/**
	 * Give a specific pancake among others
	 * @version 1.1
	 * @param pancakeNumber : the number of the pancake, beginning from the top of the stack, that you want to get.
	 * @return The expected pancake
	 */
	Pancake getPancake(int pancakeNumber) {
		return this.pancakeStack[pancakeNumber];
	}

	/**
	 * Give the size of a specific pancake among others
	 * @version 1.1
	 * @param pancakeNumber : the number of the pancake, beginning from the top of the stack, that you want to get.
	 * @return The size of the expected pancake
	 */
	public int getPancakeSize(int pancakeNumber){
		return this.getPancake(pancakeNumber).getSize();
	}

	/**
	 * Give the size of the stack of pancakes
	 * @version 1.1
	 * @return The number of pancakes in the stack
	 */
	public int getSize() {
		return this.pancakeStack.length;
	}
	
	/**
	 * Tell if a specific pancake, among others, is upside down
	 * @version 1.2
	 * @param pancakeNumber : the number of the pancake, beginning from the top of the stack, that you want to get.
	 * @return If the specific pancake is upside down or not
	 */
	public boolean isPancakeUpsideDown(int pancakeNumber) {
		return this.getPancake(pancakeNumber).isUpsideDown();
	}
	
	/**
	 * Tell if the stack of pancakes is correctly sorted according to the control freak pancake seller
	 * @version 1.1
	 * @return TRUE if the stack is okay <br>FALSE else
	 */
	public boolean isSorted() {
		boolean stackSorted = true;
		int stackSize = this.getSize();
		for ( int pancakeNumber = 0 ; stackSorted  && pancakeNumber < stackSize ; pancakeNumber++)
		{
			Pancake pancake = this.getPancake(pancakeNumber);
			stackSorted = ( pancake.getSize() == pancakeNumber+1) && ( !pancake.isUpsideDown());
		}
		return stackSorted;
	}
	
	/**
	 * Mix the stack in order to screw the pancake seller
	 * @version 1.1
	 */
	public void mix() {
		int stackSize = this.getSize();
		// Just to be sure that the stack is a bit mixed
		while( this.isSorted() )
		{
			for ( int pancakeNumber = 0 ; pancakeNumber < stackSize ; pancakeNumber++)
			{
				// Flipping time !
				if ( Math.random() > 0.5)
				{
					this.getPancake(pancakeNumber).flip();
				}
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
	 * @version 1.1
	 * @param pancakeNumber : the number of the pancake, beginning from the top of the stack, where you want to place the pancake.
	 * @param pancake :the pancake that you want to place in the stack
	 */
	private void setPancake(int pancakeNumber, Pancake pancake){
		this.pancakeStack[pancakeNumber] = pancake.copy();
		return;
	}
	
	/**
	 * Sort the stack of pancakes correctly, according to the control freak pancake seller
	 * @version 1.1
	 * @throws PancakeNumberException : in case you ask to flip less than one or more than the total amount of pancakes
	 */
	public void solve() throws PancakeNumberException {
		int stackSize = this.getSize();
		boolean currentPancakeAlreadySorted;
		for ( int pancakesToSort = stackSize ; pancakesToSort != -1 &&!this.isSorted(); pancakesToSort-- )
		{
			currentPancakeAlreadySorted= (this.getPancakeSize(pancakesToSort-1)==pancakesToSort 
					&& !this.isPancakeUpsideDown(pancakesToSort-1) ) ;
			if ( !currentPancakeAlreadySorted)
			{
				int index = this.findBiggestPancake(pancakesToSort);
				this.flip(index+1);	// putting the pancake at the top
				if ( !this.isPancakeUpsideDown(0))
				{
					this.flip(1);	// show your dark side to the world
				}
				this.flip(this.getPancake(0).getSize());	// hit the bottom !
			}	
		}
	}
	
	/*
	 *  We are looking for the next big thing !
	 *  And we know that it is somewhere in the remaining stack !
	 */
	private int findBiggestPancake(int numberOfPancakesNotSorted) {
		int indexBigPancake =-1;
		boolean found = false;
		for ( int j = 0 ; j < numberOfPancakesNotSorted && !found; j++)
		{
			if ( this.getPancake(j).getSize() == numberOfPancakesNotSorted)
			{
				indexBigPancake = j;	// gotcha !
				found = true;
			}
		}
		return indexBigPancake;
	}
	
	/**
	 * Swap two pancakes
	 * @version 1.1
	 * @param pancakeOne :the first pancake which will be swaped
	 * @param pancakeTwo :the second one
	 */
	private void swap(int pancakeOne, int pancakeTwo) {
		Pancake flyingPancake = this.getPancake(pancakeOne);
		this.setPancake(pancakeOne,this.getPancake(pancakeTwo));
		this.setPancake(pancakeTwo,flyingPancake);
		return;
	}
	
	/**
	 * Returns a string representation of the stack of pancakes.
	 * @version 1.2
	 * @return A string representation of the stack of pancakes.
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
