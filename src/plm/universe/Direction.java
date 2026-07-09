package plm.universe;

import java.awt.Point;



public enum Direction {
	NORTH(),
	EAST(),
	SOUTH(),
	WEST();

	public Direction right() {
		return values()[(ordinal()+1)%values().length];
	}

	public Direction opposite() {
		return values()[(ordinal()+2)%values().length];
	}

	public Direction left() {
		return values()[(ordinal()+3)%values().length];
	}

	public Point toPoint() {
        return switch (this) {
            case NORTH -> new Point(0, -1);
            case EAST -> new Point(1, 0);
            case SOUTH -> new Point(0, 1);
            case WEST -> new Point(-1, 0);
        };
	}
	
	/* BINDINGS TRANSLATION: French */
	public static final Direction NORD = NORTH;
	public static final Direction EST = EAST;
	public static final Direction SUD = SOUTH;
	public static final Direction OUEST = WEST;
}
