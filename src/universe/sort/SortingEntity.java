package universe.sort;

import java.awt.Color;

import jlm.universe.Entity;
import jlm.universe.World;

public class SortingEntity extends Entity {
	Color[] color;	
	int[] values;
	int maxValue;
	private int readCount = 0;
	private int writeCount = 0;

	public SortingEntity(){
		super();
	}
	public SortingEntity(String name, World w) {
		super(name,w);
	}
	public SortingEntity(SortingEntity other) {
		super();
		copy(other);
	}
	@Override
	public void setWorld(World w) {
		super.setWorld(w);
		if (w!=null && ((SortingWorld)w).values != null) {
			values = ((SortingWorld)w).values.clone();
			maxValue=((SortingWorld)w).maxValue;
			color = ((SortingWorld)w).color.clone();
		}	
	}
	@Override
	public Entity copy() {
		return new SortingEntity(this);
	}
	@Override
	public void copy(Entity o) {
		super.copy(o);
		SortingEntity other = (SortingEntity) o;
		if (other.values!=null) {
			values = other.values.clone();
			color = other.color.clone();
			maxValue = other.maxValue;
		} else if (other.world !=null && ((SortingWorld)other.world).values != null) {
			color = ((SortingWorld)other.world).color.clone();
			values = ((SortingWorld)other.world).values.clone();
			maxValue = ((SortingWorld)other.world).maxValue;
		}
	}

	@Override
	public void run() {
		// Child implement this
	}
	public void checkme() {
		for (int i=0;i<values.length;i++) {
			if (values[i] == i)
				color[i] = Color.blue;
			else
				color[i] = Color.red;
		}
	}

	public final boolean compare(int i, int j) {
		readCount+=2;
		return values[i]<values[j];
	}
	public final boolean compareTo(int i, int val) {
		readCount+=1;
		return values[i]<val;
	}
	public final void swap(int i, int j) {
		readCount+=2;
		writeCount+=2;
		synchronized (values) {
			int tmp = values[i];
			values[i] = values[j];
			values[j] = tmp;
		}
		stepUI();
	}
	protected final int getValueCount() {
		return values.length;
	}
	public final int getValue(int i) {
		readCount++;
		return values[i];
	}
	public final void setValue(int idx,int val) {
		writeCount++;
		values[idx]=val;
		stepUI();
	}
	public final void copy(int from,int to) {
		readCount++;
		writeCount++;
		values[to] = values[from];
		stepUI();
	}
	public final void sorted(int i){
		color[i] = Color.blue;
	}

	private void stepUI() {
		try {
			if (world.getDelay()>0) {
				world.notifyWorldUpdatesListeners();
				Thread.sleep(1);
			}
		} catch (InterruptedException e) {}
	}
	
	public int getWriteCount() {
		return writeCount ;
	}
	public int getReadCount() {
		return readCount;
	}
}
