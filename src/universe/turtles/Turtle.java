package universe.turtles;

import universe.Entity;

public class Turtle extends AbstractTurtle {

	public Turtle() {
		super();
	}
	public Turtle(AbstractTurtle t) {
		super(t);
	}
	
	public Turtle(TurtleWorld world, String name) {
		super(world,name);
	}

	@Override
	public Entity copy() {
		// TODO Raccord de méthode auto-généré
		return null;
	}

	@Override
	public void run() {
		// TODO Raccord de méthode auto-généré

	}

}
