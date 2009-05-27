package universe.hanoi;

import jlm.ui.WorldView;
import jlm.universe.EntityControlPanel;
import jlm.universe.World;

public class HanoiWorld extends World {
	private HanoiSlot slots[];
	
	protected Integer[] values(int i) {
		return slots[i].values();
	}
	public int getSlotSize(int slot) {
		return slots[slot].size();
	}
	public void move(int src, int dst) throws HanoiInvalidMove {
		if (src < 0 || src > 2 || dst < 0 || dst > 2)
			throw new HanoiInvalidMove("Cannot move from slot #"+src+" to #"+dst+": invalid parameters");
		if (src == dst)
			throw new HanoiInvalidMove("Cannot move from slot #"+src+" itself: invalid parameters");
		
		if (slots[src].size() == 0)
			throw new HanoiInvalidMove("No disc to move from slot #"+src);
		if (slots[dst].size() > 0 &&
				slots[src].top() > slots[dst].top())
			throw new HanoiInvalidMove("Cannot move disc from slot #"+src+" to #"+dst+": "+
					slots[src].top()+" > "+slots[dst].top());
		slots[dst].push(slots[src].pop());
	}
	
	public HanoiWorld(String name, int size) {
		super(name);
		setDelay(200);
		slots = new HanoiSlot[3];
		for (int i=0;i<3;i++)
			slots[i] = new HanoiSlot();
		for (int i=size; i>0; i--) 
			slots[0].push(new Integer(i));
	}

	

	public HanoiWorld(String name, int size1, int size2, int size3) {
		super(name);
		slots = new HanoiSlot[3];
		for (int i=0;i<3;i++)
			slots[i] = new HanoiSlot();
		for (int i=size1; i>0; i--) 
			slots[0].push(new Integer(i));
		for (int i=size2; i>0; i--) 
			slots[1].push(new Integer(i));
		for (int i=size3; i>0; i--) 
			slots[2].push(new Integer(i));
	}
	public HanoiWorld(HanoiWorld other) {
		super(other);
	}
	@Override
	public void reset(World w) {
		HanoiWorld other = (HanoiWorld)w;
		slots = new HanoiSlot[3];
		for (int i=0;i<3;i++)
			slots[i] = other.slots[i].copy(); 		
		super.reset(w);		
	}

	@Override
	public EntityControlPanel getEntityControlPanel() {
		return new EntityControlPanel() {
			private static final long serialVersionUID = 1L;
			@Override
			public void setEnabledControl(boolean enabled) {
			}
		};
	}

	@Override
	public WorldView[] getView() {
		WorldView res[] = new WorldView[1];
		res[0] = new HanoiWorldView(this);
		return res;
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("HanoiWorld "+getName()+": ");
		sb.append("A: [");
		for (int i:slots[0].values()) 
			sb.append(i+" ");
		sb.append("] B: [");
		for (int i:slots[1].values()) 
			sb.append(i+" ");
		sb.append("] C: [");
		for (int i:slots[2].values()) 
			sb.append(i+" ");
		sb.append("]");
		return sb.toString();
	}

	@Override 
	public boolean equals(Object o) {
		if (o == null || !(o instanceof HanoiWorld))
			return false;
		HanoiWorld other = (HanoiWorld) o;
		for (int i=0;i<3;i++) {
			if (!(this.slots[i].equals(other.slots[i])))
				return false;
		}
		return true;
	}
}
