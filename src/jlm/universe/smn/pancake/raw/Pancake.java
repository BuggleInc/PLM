package jlm.universe.smn.pancake.raw;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 */
public class Pancake {

	private int radius; // Radius of the pancake
	
	/**
	 * Constructor of the class Pancake
	 * @param radius :the radius of the pancake
	 */
	public Pancake(int radius) {
		this.radius = radius;
	}
	
	/**
	 * Make a copy of the caller
	 * @return a copy of the method caller
	 */
	public Pancake copy() {
		return new Pancake(this.getRadius());
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
			sw = (this.getRadius()==other.getRadius() );
		}
		return sw;
	}
	
	/**
	 * Give the radius of the pancake
	 * @return The radius of the pancake
	 */
	public int getRadius() {
		return this.radius;
	}
	
	/**
	 * Return a string representation of the pancake.
	 * @return A string representation of the pancake.
	 */
	public String toString() {
		return "< Size: "+this.getRadius()+" >";
	}
	
}
