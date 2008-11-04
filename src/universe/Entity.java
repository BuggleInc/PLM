package universe;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import universe.bugglequest.BuggleWorld;


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
		this.world = (BuggleWorld)world;
	}

	public abstract Entity copy();
	public abstract void run();

}
