package jlm.universe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;

public abstract class Entity {
	protected String name;
	protected World world;
	
	private Semaphore oneStepSemaphore = new Semaphore(0);
	
	
	public Entity() {
	}
	public Entity(String name) {
		this.name=name;
	}
	public Entity(String name, World w) {
		this.name=name;
		if (w != null) {
			this.world = w;
			world.addEntity(this);
		}
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	/* This is to allow exercise to forbid the use by students of some functions 
	 * which are mandatory for core mechanism. See welcome.ArrayBuggle to see how it forbids setPos(int,int)  
	 */
	private boolean inited = false;
	public boolean isInited() {
		return inited;
	}
	public void initDone() {
		inited = true;		
	}

	public void allowOneStep() {
		this.oneStepSemaphore.release();
	}
	
	protected void stepUI() {		
		// only a trial to see moving steps
		fireStackListener();
		world.notifyWorldUpdatesListeners();
		if (world.isDelayed()) {
			if (Game.getInstance().stepModeEnabled()) {
				this.oneStepSemaphore.acquireUninterruptibly();
			} else {	
				try {
					if (world.getDelay()>0) // seems that sleep(0) takes time (yield thread?)
						Thread.sleep(world.getDelay());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	/** Copy fields of the entity passed in argument */
	public void copy(Entity other) {
		setName(other.getName());
		setWorld(other.getWorld());
	}
	/** Copy constructor */
	public abstract Entity copy();
	public abstract void run() throws Exception;

	
	/* stuff related to tracing mechanism */
	ArrayList<IEntityStackListener> stackListeners = new ArrayList<IEntityStackListener>();
	public void addStackListener(IEntityStackListener l) {
		stackListeners.add(l);
	}
	public void removeStackListener(IEntityStackListener l) {
		stackListeners.remove(l);
	}
	public void fireStackListener() {
		StackTraceElement[] trace = getCurrentStack();
		for (IEntityStackListener l:stackListeners)
			l.entityTraceChanged(this, trace);		
	}
	public StackTraceElement[] getCurrentStack() {
		return Thread.currentThread().getStackTrace();
	}
	
	/** Retrieve one parameter from the world */
	protected Object getParam(int i) {
		return world.parameters[i];
	}	
	protected int getParamsAmount() {
		return world.parameters.length;
	}
	private Map<ProgrammingLanguage,String> script = new HashMap<ProgrammingLanguage, String>(); /* What to execute when running a scripting language */
	public void setScript(ProgrammingLanguage lang, String s) {
		script.put(lang,  s);
	}
	public String getScript(ProgrammingLanguage lang) {
		return script.get(lang);
	}
}
