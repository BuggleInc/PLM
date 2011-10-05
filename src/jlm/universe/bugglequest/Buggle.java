package jlm.universe.bugglequest;

import java.awt.Color;

import jlm.universe.Direction;


public final class Buggle extends AbstractBuggle {

	public Buggle(AbstractBuggle b){
		super();
		setWorld(b.getWorld());
		setName(b.getName());
		setColor(b.color);
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
