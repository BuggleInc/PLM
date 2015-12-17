package plm.universe.pancake;

public class Pancake {
	private int radius; // Radius of the pancake
	private boolean upsideDown; // True if the burned face is facing the sky, else false
	
	/** Create a new pancake of that radius. If the given radius is negative, the pancake is upside down */
	public Pancake(int radius) {
		this.radius = Math.abs(radius);
		this.upsideDown = radius<0;
	}
	
	/**
	 * Make a copy of the caller
	 * @return a copy of the method caller
	 */
	public Pancake copy() {
		Pancake res = new Pancake(this.getRadius());
		if (this.isUpsideDown())
			res.flip();
		return res;
	}
	
	/**
	 * Indicate whether some other pancake is "equal to" this one
	 * @param burnedMatter if we take care of the position of the burned part
	 * @param Pancake other: the other pancake with which to compare
	 * @return If the two pancakes are equals
	 */
	public boolean equals(Pancake other, boolean burnedMatter) {
		if (getRadius() != other.getRadius())
			return false;
		if (burnedMatter && isUpsideDown() != other.isUpsideDown())
			return false;
				
		return true;
	}
	
	/** Flip a pancake, which leads to changing upsideDown */
	public void flip() {
		upsideDown = !upsideDown;
	}
	
	/** Returns the radius of the pancake */
	public int getRadius() {
		return this.radius;
	}
	
	/** Returns whether the pancake is upside down */
	public boolean isUpsideDown() {
		return this.upsideDown;
	}
	
	/** Returns a string representation of the pancake */
	public String toString() {
		String s = "< Radius: "+this.getRadius();
		if ( this.isUpsideDown())
			s+=" , upside down";
		
		s+=" >";
		return s;
	}
}
