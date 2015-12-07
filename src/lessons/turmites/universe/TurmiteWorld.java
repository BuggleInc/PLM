package lessons.turmites.universe;

import java.awt.Color;
import java.io.IOException;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.xnap.commons.i18n.I18n;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.universe.BrokenWorldFileException;
import plm.universe.Direction;
import plm.universe.World;
import plm.universe.bugglequest.Buggle;
import plm.universe.bugglequest.BuggleWorld;

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
	public TurmiteWorld(Game game, String title, int nbSteps, Object rule, int width, int height, int buggleX, int buggleY) {
		super(game, title,width,height);
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
	 * This is mandatory for the PLM good working. Even if the prototype says that the passed object can be 
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
	
	/** Used to check whether the student code changed the world in the right state */
	@Override 
	public boolean equals(Object o) {
		// hack hack hack: Avoid false negative with answer worlds that were serialized as BuggleWorlds 
		if (o instanceof BuggleWorld && !(o instanceof TurmiteWorld))
			return super.equals(o);
		
		if (o == null || !(o instanceof TurmiteWorld))
			return false;
		if (((TurmiteWorld) o).currStep != currStep && ((TurmiteWorld) o).currStep !=0) // allow other world to be a cache from disk
			return false;
		return super.equals(o);
	}
	@Override
	public String diffTo(World other, I18n i18n) {
		// hack hack hack: Avoid false negative with answer worlds that were serialized as BuggleWorlds 
		if (other instanceof BuggleWorld && !(other instanceof TurmiteWorld))
			return "other is not a turmiteWorld, but that's ok\n"+super.diffTo(other, i18n);
		
		String res = "";
		if (((TurmiteWorld) other).currStep != currStep && ((TurmiteWorld) other).currStep != 0)// allow other world to be a cache from disk
			res += "The amount of steps is wrong: "+((TurmiteWorld) other).currStep +" is not "+ currStep+"\n";
		return res+super.diffTo(other, i18n);
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
	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine engine) throws ScriptException {
		if (lang.equals(Game.PYTHON)) {
			super.setupBindings(lang, engine);
			engine.put("daWorld", this);
			engine.eval(
					"def stepDone():\n"+
					"	daWorld.stepDone()\n"+
					/* BINDINGS TRANSLATION: French */
					"def pasFait():\n"+
					"	daWorld.stepDone()\n"
					);
		}
	}
	
	@Override
	public World readFromFile(String path) throws IOException, BrokenWorldFileException {
		TurmiteWorld res = new TurmiteWorld(getGame(), "toto",1, "",1,1,1,1);
		res.removeEntity(res.getEntity(0));
		return res.readFromFile(path,"TurmiteWorld",res);
	}

}
