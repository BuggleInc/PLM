package lessons.welcome;

import jlm.universe.bugglequest.SimpleBuggle;

public class TraversalByColumnEntity extends SimpleBuggle {
	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */	
	public void nextStep() {	
		int x=getX();
		int y=getY();

        if (y < getWorldHeight()-1) {
            y++;
        } else {
            y = 0;
            if (x < getWorldWidth()-1) {
                x++;
            } else {
                x = 0; 
            }
        }
        setPos(x,y);
	}

	public boolean endingPosition() {
		return (getX() == getWorldWidth() -1) && (getY() == getWorldHeight()-1);
	}
	
	
	@Override
	public void run() {
		int cpt=0;
		writeMessage(cpt);
		while (!endingPosition()) {
			nextStep();
			cpt++;
			writeMessage(cpt);
		}
	}

	/* END TEMPLATE */	
	
    @Override
	public void forward()  {
        if (isInited())
                throw new RuntimeException("forward() forbidden in this exercise");
    }
    @Override
	public void forward(int step)  {
        if (isInited())
                throw new RuntimeException("forward(int step) forbidden in this exercise");
    }
    @Override
    public void backward()  {
        if (isInited())
                throw new RuntimeException("backward() forbidden in this exercise");
    }
    @Override
    public void backward(int step)  {
        if (isInited())
                throw new RuntimeException("backward(int step) forbidden in this exercise");
    }
    @Override
    public void turnLeft()  {
        if (isInited())
                throw new RuntimeException("turnLeft() forbidden in this exercise");
    }
    @Override
    public void turnRight()  {
        if (isInited())
                throw new RuntimeException("turnRight() forbidden in this exercise");
    }
    @Override
    public void turnBack()  {
        if (isInited())
                throw new RuntimeException("turnBack() forbidden in this exercise");
    }
    @Override
       public boolean isFacingWall()  {
        if (isInited())
                throw new RuntimeException("isFacingWall() forbidden in this exercise");
        return false;
    }
    @Override
    public boolean isBackingWall()  {
        if (isInited())
                throw new RuntimeException("isBackingWall() forbidden in this exercise");
        return false;
    }    

}
