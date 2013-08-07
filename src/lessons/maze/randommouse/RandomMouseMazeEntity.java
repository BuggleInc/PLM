package lessons.maze.randommouse;

public class RandomMouseMazeEntity extends jlm.universe.bugglequest.SimpleBuggle {
	@Override
	public void setX(int i)  {
		if (isInited())
			throw new RuntimeException("setX(int) forbidden in this exercise");
	}
	@Override
	public void setY(int i)  { 
		if (isInited())
			throw new RuntimeException("setY(int) forbidden in this exercise");
	}
	@Override
	public void setPos(int i,int j)  { 
		if (isInited())
			throw new RuntimeException("setPos(int,int) forbidden in this exercise");
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
