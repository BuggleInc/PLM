package jlm.universe.smn.pancake.raw;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 */
public class Pancake {

	private int size; // Size of the pancake
	
	/**
	 * Constructor of the class Pancake
	 * @param size :the size of the pancake
	 */
	public Pancake(int size) {
		this.size = size;
	}
	
	/**
	 * Make a copy of the caller
	 * @return a copy of the method caller
	 */
	public Pancake copy() {
		return new Pancake(this.getSize());
	}
	
	/**
	 * Give the size of the pancake
	 * @return The size of the pancake
	 */
	public int getSize() {
		return this.size;
	}
	
	/**
	 * Indicate whether some other object is "equal to" this one. 
	 * @return If the two objects are equals
	 * @param Object o: the reference object with which to compare.
	 */
	public boolean equals(Object o) {
		boolean sw=true;
		if (o == null || !(o instanceof Pancake) )
		{
			sw = false ;
		}
		else
		{
			Pancake other = (Pancake) o;
			sw = (this.getSize()==other.getSize() );
		}
		return sw;
	}
	
	/**
	 * Return a string representation of the pancake.
	 * @return A string representation of the pancake.
	 */
	public String toString() {
		return "< Size: "+this.getSize()+" >";
	}
	
}
