package lessons.maze.randommouse;

public class RandomMouseMazeEntity extends plm.universe.bugglequest.SimpleBuggle {
	@Override
	public void setX(int i)  {
		if (isInited())
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setX(x) in this exercise. Walk to your goal instead."));
	}
	@Override
	public void setY(int i)  { 
		if (isInited())
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setY(y) in this exercise. Walk to your goal instead."));
	}
	@Override
	public void setPos(int i,int j)  { 
		if (isInited())
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setPos(x,y) in this exercise. Walk to your goal instead."));
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
