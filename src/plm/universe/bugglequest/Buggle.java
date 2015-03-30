package plm.universe.bugglequest;

import java.awt.Color;

import plm.core.model.Game;
import plm.universe.Direction;


public final class Buggle extends AbstractBuggle {

	public Buggle() {
		super();
	}
	
	public Buggle(Game game, BuggleWorld world, String name, int i, int j, Direction north, Color color, Color brush) {
		super(game, world, name, i, j, north, color, brush);
	}

	@Override
	public void run() {
		// nothing by default
	}
}
