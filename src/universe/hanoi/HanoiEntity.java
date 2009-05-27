package universe.hanoi;

import jlm.universe.Entity;
import jlm.universe.World;

public class HanoiEntity extends Entity {
	public HanoiEntity(String name,World world) {
		super(name,world);
	}

	public HanoiEntity(String name) {
		super(name);
	}
	public HanoiEntity() {
		this("Hanoi Entity");
	}

	@Override
	public Entity copy() {
		return new HanoiEntity(name);
	}

	@Override
	public void run() throws HanoiInvalidMove {
	}

	public void move(int src, int dst) throws HanoiInvalidMove {
		((HanoiWorld) world).move(src,dst);
		stepUI();
	}
	
	@Override
	public String toString(){
		return "HanoiEntity (" + this.getClass().getName() + ")";
	}
}
