package jlm.universe.robozzle;

import jlm.universe.Direction;


public final class Robozzle extends AbstractRobozzle {

	public Robozzle(AbstractRobozzle b){
		super();
		setWorld(b.getWorld());
		setName(b.getName());
		setPos(b.getX(), b.getY());
		setDirection(b.direction);
	}

	public Robozzle(RobozzleWorld world, String name, int i, int j, Direction north) {
		super(world, name, i, j, north);
	}

	public Robozzle(RobozzleWorld world) {
		super(world);
	}

	@Override
	public void run() {
		// nothing by default
	}
	
}
