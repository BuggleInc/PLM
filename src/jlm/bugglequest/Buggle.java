package jlm.bugglequest;

import java.awt.Color;

public final class Buggle extends AbstractBuggle {

	public Buggle(AbstractBuggle b){
		super();
		setWorld(b.world);
		setName(b.name);
		setColor(b.color);
		setBrushColor(b.brushColor);
		setPos(b.getX(), b.getY());
		setDirection(b.direction);
		brushDown = b.brushDown;
	}


	public Buggle(World world, String name, int i, int j, Direction north, Color color, Color brush) {
		super(world, name, i, j, north, color, brush);
	}

	public Buggle(World world) {
		super(world);
	}

	@Override
	public void run() {
		// nothing by default
	}
	
}
