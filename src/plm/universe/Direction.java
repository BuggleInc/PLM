package plm.universe;

import java.awt.Point;


// TODO: rewrite using enumeration
public class Direction {
	private int value;

	public static final int NORTH_VALUE = 0;

	public static final int EAST_VALUE = 1;

	public static final int SOUTH_VALUE = 2;

	public static final int WEST_VALUE = 3;

	public static final Direction NORTH = new Direction(NORTH_VALUE);

	public static final Direction EAST = new Direction(EAST_VALUE);

	public static final Direction SOUTH = new Direction(SOUTH_VALUE);

	public static final Direction WEST = new Direction(WEST_VALUE);

	private static final Direction rights[] = { EAST, SOUTH, WEST, NORTH };

	private static final Direction lefts[] = { WEST, NORTH, EAST, SOUTH };

	private static final Direction opposites[] = { SOUTH, WEST, NORTH, EAST };

	private Direction(int d) {
		value = d;
	}

	public boolean equals(Direction d) {
		return value == d.value;
	}
	
	public Direction copy() {
		return new Direction(value);
	}

	public Direction right() {
		return rights[value];
	}

	public Direction left() {
		return lefts[value];
	}

	public Direction opposite() {
		return opposites[value];
	}

	@Override
	public String toString() {
		switch (value) {
		case NORTH_VALUE:
			return "NORTH";
		case EAST_VALUE:
			return "EAST";
		case SOUTH_VALUE:
			return "SOUTH";
		case WEST_VALUE:
			return "WEST";
		default:
			return "Unknown direction";
		}
	}

	public Point toPoint() {
		switch (value) {
		case NORTH_VALUE:
			return new Point(0, -1);
		case EAST_VALUE:
			return new Point(1, 0);
		case SOUTH_VALUE:
			return new Point(0, 1);
		case WEST_VALUE:
			return new Point(-1, 0);
		default:
			return null;
		}
	}
	
	public int intValue() {
		return this.value;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + value;
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
		final Direction other = (Direction) obj;
		if (value != other.value)
			return false;
		return true;
	}
}
