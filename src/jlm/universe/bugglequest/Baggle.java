package jlm.universe.bugglequest;

import java.awt.Color;

public class Baggle {

	private Color color;
	private BuggleWorldCell cell;

	public static final Color DEFAULT_COLOR = new Color(0.82f,0.41f,0.12f);
	
	
	public Baggle(BuggleWorldCell c) {
		this(c, DEFAULT_COLOR);
	}

	public Baggle(BuggleWorldCell cell, Color c) {
		this.cell = cell;
		this.color = c;
	}
	
	public Baggle(Baggle b) {
		this.color = b.color;
		this.cell = b.cell;
	}
	
	public void setCell(BuggleWorldCell c) {
		this.cell = c;
	}
		
	@Override
	public String toString() {
		return "Baggle ("+this.getClass().getName()+"): Color:" + color;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		//result = prime * result + ((cell == null) ? 0 : cell.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Baggle other = (Baggle) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
//		if (cell == null) {
//			if (other.cell != null)
//				return false;
//		} //else if (!cell.equals(other.cell))
		//	return false;
		return true;
	}

	

}
