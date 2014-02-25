package plm.universe.bugglequest;

import java.awt.Color;

import plm.universe.Direction;


public final class Buggle extends AbstractBuggle {

	public Buggle() {
		super();
	}
	
	public Buggle(AbstractBuggle b){
		super();
		setWorld(b.getWorld());
		setName(b.getName());
		setBodyColor(b.bodyColor);
		setBrushColor(b.brushColor);
		setPosFromLesson(b.getX(), b.getY());
		setDirection(b.direction);
		brushDown = b.brushDown;
	}

	public Buggle(BuggleWorld world, String name, int i, int j, Direction north, Color color, Color brush) {
		super(world, name, i, j, north, color, brush);
	}

	public Buggle(BuggleWorld world) {
		super(world);
	}

	@Override
	public void run() {
		// nothing by default
	}
}
