package jlm.universe.smn.pancake.burned;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 */
public class Pancake {

	private int size; // Size of the pancake
	private boolean upsideDown; // True if the burned face is facing the sky, else false
	
	/**
	 * Constructor of the class Pancake
	 * @param size :the size of the pancake
	 */
	public Pancake(int size) {
		this.size = size;
		this.upsideDown = false;
	}
	
	/**
	 * Make a copy of the caller
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
	 */
	public void flip() {
		this.upsideDown = !this.upsideDown;
	}
	
	/**
	 * Give the size of the pancake
	 * @return The size of the pancake
	 */
	public int getSize() {
		return this.size;
	}
	
	/**
	 * Tell if the pancake is upside down
	 * @return If the pancake is upside down or not
	 */
	public boolean isUpsideDown() {
		return this.upsideDown;
	}
	
	/**
	 * Indicate whether some other object is "equal to" this one
	 * @return If the two objects are equals
	 * @param Object o: the reference object with which to compare
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
	 * Return a string representation of the pancake
	 * @return A string representation of the pancake
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
