package universe.bat;

import jlm.ui.WorldView;
import jlm.universe.World;

public class BatWorld extends World {
	public Object result;
	public boolean visible;
	public BatWorld(boolean visible, Object...params) {
		super("");
		this.visible = visible;
		parameters=params;
	}
	public BatWorld(BatWorld w2) {
		super(w2);
	}
	@Override
	public void reset(World w) {
		BatWorld anotherWorld = (BatWorld) w;
		this.result = anotherWorld.result;
		this.visible = anotherWorld.visible;
		super.reset(anotherWorld);		
	}
	@Override
	public boolean equals(Object o){
		if (!(o instanceof BatWorld)) {
			return false;
		}
		BatWorld other = (BatWorld) o;
//		System.out.println(this+"  =?=  "+o);
		if (other.result==null && this.result != null)
			return false;
		if (other.result!=null && !other.result.equals(this.result))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return getName()+"="+result;
	}
	@Override
	public WorldView[] getView() {
		return new WorldView[] { new BatWorldView(this) };
	}
}
