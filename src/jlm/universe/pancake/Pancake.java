package jlm.universe.pancake;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @version 1.2
 */
public class Pancake {

	private int size; // Size of the pancake
	private boolean upsideDown; // True if the burned face is facing the sky, else false
	
	/**
	 * Constructor of the class Pancake
	 * @version 1.0
	 * @param size :the size of the pancake
	 */
	public Pancake(int size) {
		this.size = size;
		this.upsideDown = false;
	}
	
	/**
	 * Make a copy of the caller
	 * @version 1.2
	 * @return a copy of the method caller
	 */
	public Pancake copy() {
		Pancake p = new Pancake(this.getSize());
		if ( this.isUpsideDown())
		{
			p.flip();
		}
		return p;
	}
	
	/**
	 * Flip a pancake, which leads to changing upsideDown
	 * @version 1.0
	 */
	public void flip() {
		this.upsideDown = !this.upsideDown;
	}
	
	/**
	 * Give the size of the pancake
	 * @version 1.0
	 * @return The size of the pancake
	 */
	public int getSize() {
		return this.size;
	}
	
	/**
	 * Tell if the pancake is upside down
	 * @version 1.0
	 * @return If the pancake is upside down or not
	 */
	public boolean isUpsideDown() {
		return this.upsideDown;
	}
	
	/**
	 * Indicates whether some other object is "equal to" this one. 
	 * @version 1.2
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
			sw = (this.getSize()==other.getSize() )
					&& (this.isUpsideDown()==other.isUpsideDown());
		}
		return sw;
	}
	
	/**
	 * Returns a string representation of the pancake.
	 * @version 1.2
	 * @return A string representation of the pancake.
	 */
	public String toString() {
		String s = "< Size: "+this.getSize();
		if ( this.isUpsideDown())
		{
			s+=" , upside down";
		}
		s+=" >";
		return s;
	}
	
}
