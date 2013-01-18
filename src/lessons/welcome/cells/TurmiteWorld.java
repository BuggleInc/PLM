package lessons.welcome.cells;

import java.awt.Color;

import jlm.universe.Direction;
import jlm.universe.World;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;
import jlm.universe.bugglequest.ui.BuggleWorldView;

public class TurmiteWorld extends BuggleWorld {	
	/** A copy constructor (mandatory for the internal compilation mechanism to work)
	 * 
	 * There is normally no need to change it, but it must be present. 
	 */ 
	public TurmiteWorld(TurmiteWorld other) {
		super(other);
	}
	
	/** The constructor that the exercises will use to setup the world.
	 *  
	 * It must begin by super(name), and the rest is free (depending on the state describing your world).
	 * It is a good idea to use setDelay to specify the default animation delay, but this is not mandatory.
	 * 
	 * You can perfectly have several such constructor. 
	 */
	public TurmiteWorld(String title, int nbSteps, Object rule, int width, int height, int buggleX, int buggleY) {
		super(title,width,height);
		currStep = 0;
		setDelay(1); 
		setVisibleGrid(false);
		setParameter(new Object[] {
				nbSteps,
				rule
		});

		new Buggle((BuggleWorld)this,"ant",buggleX,buggleY,Direction.NORTH,Color.red,Color.red);
	}
	
	/** Reset the state of the current world to the one passed in argument
	 * 
	 * This is mandatory for the JLM good working. Even if the prototype says that the passed object can be 
	 * any kind of world, you can be sure that it's of the same type than the current world. So, there is 
	 * no need to check before casting your argument.
	 * 
	 * Do not forget to call super.reset(w) afterward, or some internal world fields may not get reset.
	 */
	@Override
	public void reset(World w) {
		TurmiteWorld other = (TurmiteWorld)w;
		currStep = other.currStep;
		super.reset(w);		
	}

	/** Returns a component able of displaying the world -- will be used in third exercise 
	 * You should comment this for the first exercises */
	@Override
	public BuggleWorldView[] getView() {
		return new BuggleWorldView[] { new TurmiteWorldView(this) } ;
	}
	
	/** Used to check whether the student code changed the world in the right state -- see exercise 4 */
	@Override 
	public boolean equals(Object o) {
		if (o == null || !(o instanceof TurmiteWorld))
			return false;
		if (((TurmiteWorld) o).currStep != currStep)
			return false;
		return super.equals(o);
	}
	
	/* Here comes the world logic */
	public int currStep=0;

	public void stepDone() {
		currStep++;
	}
	@Override
	public boolean isDelayed() {
		return super.isDelayed() && ( (getDelay() > 0) || (currStep % 1000 == 0) );
	}
}
