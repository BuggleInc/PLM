package plm.universe.hanoi;

import java.awt.Color;
import java.util.Vector;

public class HanoiDisk {
	private int size;
	private Color color;
	
	public HanoiDisk(int size) {
		this(size, Color.yellow);
	}
	
	public HanoiDisk(int size, Color color) {
		this.size = size;
		this.color = color;
	}

	public int getSize() {
		return size;
	}

	public Color getColor() {
		return color;
	}
	
	public void setSize(int size) {
		this.size = size;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public HanoiDisk copy() {
		return new HanoiDisk(size, color);
	}
	
	public String toString() {
		return "("+size+", "+color+")";
	}
	
	public boolean equals(Object o) {
		if (o == null || !(o instanceof HanoiDisk))
			return false;
		HanoiDisk other = (HanoiDisk) o;
		return size == other.size && color.equals(other.color);
	}
	
	public static Vector<HanoiDisk> generateHanoiDisks(Integer[] sizes) {
		return generateHanoiDisks(sizes, Color.yellow);
	}
	
	public static Vector<HanoiDisk> generateHanoiDisks(Integer[] sizes, Color color) {
		Vector<HanoiDisk> hanoiDisks = new Vector<HanoiDisk>();
		for(int i=0; i<sizes.length; i++) {
			hanoiDisks.add(new HanoiDisk(sizes[i], color));
		}
		return hanoiDisks;
	}
	
}
