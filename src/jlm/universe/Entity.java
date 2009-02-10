package jlm.universe;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;



public abstract class Entity {
	protected String name;
	protected World world;
	
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
	
	@Attribute
	public String getName() {
		return this.name;
	}

	@Attribute
	public void setName(String name) {
		this.name = name;
	}	
	
	@Element
	public World getWorld() {
		return world;
	}

	@Element
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

	protected void stepUI() {
		// only a trial to see moving steps
		if (world.isDelayed()) {
			try {
				Thread.sleep(world.getDelay());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			world.notifyWorldUpdatesListeners();
		}		
	}
	
	/** Copy fields of the entity passed in argument */
	public void copy(Entity other) {
		setName(other.getName());
		setWorld(other.getWorld());
	}
	/** Copy constructor */
	public abstract Entity copy();
	public abstract void run();

}
