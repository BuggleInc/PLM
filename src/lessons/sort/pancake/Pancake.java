package lessons.sort.pancake;

public class Pancake {

	private int radius; // Radius of the pancake
	private boolean upsideDown; // True if the burned face is facing the sky, else false
	
	/**
	 * Constructor of the class Pancake
	 * @param radius :the radius of the pancake
	 */
	public Pancake(int size) {
		this.radius = size;
		this.upsideDown = false;
	}
	
	/**
	 * Make a copy of the caller
	 * @return a copy of the method caller
	 */
	public Pancake copy() {
		Pancake p = new Pancake(this.getRadius());
		if ( this.isUpsideDown())
		{
			p.flip();
		}
		return p;
	}
	
	/**
	 * Indicate whether some other pancake is "equal to" this one
	 * @param burnedMatter if we take care of the position of the burned part
	 * @param Pancake other: the other pancake with which to compare
	 * @return If the two pancakes are equals
	 */
	public boolean equals(Pancake other, boolean burnedMatter) {
		return(
				this.getRadius()==other.getRadius() )
			&& (this.isUpsideDown()==other.isUpsideDown()
				|| !burnedMatter);
	}
	
	/**
	 * Flip a pancake, which leads to changing upsideDown
	 */
	public void flip() {
		this.upsideDown = !this.upsideDown;
	}
	
	/**
	 * Give the radius of the pancake
	 * @return The radius of the pancake
	 */
	public int getRadius() {
		return this.radius;
	}
	
	/**
	 * Tell if the pancake is upside down
	 * @return If the pancake is upside down or not
	 */
	public boolean isUpsideDown() {
		return this.upsideDown;
	}
	
	/**
	 * Return a string representation of the pancake
	 * @return A string representation of the pancake
	 */
	public String toString() {
		String s = "< Radius: "+this.getRadius();
		if ( this.isUpsideDown())
		{
			s+=" , upside down";
		}
		s+=" >";
		return s;
	}
	
}
