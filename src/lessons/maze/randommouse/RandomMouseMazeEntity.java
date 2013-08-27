package lessons.maze.randommouse;

import plm.core.model.Game;

public class RandomMouseMazeEntity extends plm.universe.bugglequest.SimpleBuggle {
	@Override
	public void setX(int i)  {
		if (isInited())
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setX() in this exercise."));
	}
	@Override
	public void setY(int i)  { 
		if (isInited())
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setY() in this exercise."));
	}
	@Override
	public void setPos(int i,int j)  { 
		if (isInited())
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setPos() in this exercise."));
	}

	/* BEGIN TEMPLATE */ 
	public void run() {
		// Your code here =)
		/* BEGIN SOLUTION */ 
		while (!isOverBaggle()) 
		{
			switch(random3()) 
			{
			case 0:
				if (!isFacingWall())
				{
					forward();
				}
				break;
			case 1:
				left();
				break;
			case 2:
				right();
				break;
			}
		}
		pickupBaggle();
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	public int random3() {
		double n = Math.random();
		if (n < 0.33) {
			return 0;
		} else if (n < 0.66) {
			return 1;
		} else {
			return 2;
		}
	}

}
