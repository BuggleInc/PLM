package jlm.universe.sort;

import java.awt.Color;
import java.util.ArrayList;

import jlm.universe.Entity;
import jlm.universe.World;

public class SortingEntity extends Entity {
	Color[] color;	
	int[] values;
	ArrayList<Integer> initValues;
	boolean init = false;
	int maxValue;
	private int readCount = 0;
	private int writeCount = 0;

	ArrayList<Operation> operations;
	
	public SortingEntity(){
		super();
		
		initValues = new ArrayList<Integer>();
		operations = new ArrayList<Operation>();
	}
	public SortingEntity(String name, World w) {
		super(name,w);
		
		initValues = new ArrayList<Integer>();
		operations = new ArrayList<Operation>();
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

	@SuppressWarnings("unchecked")
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
		
		for(int k=0 ; k<other.initValues.size() ; k++){
			initValues.set(k, other.initValues.get(k));
		}
		
		if(other.initValues!=null)initValues = (ArrayList<Integer>)other.initValues.clone();
		if(other.operations!=null)operations = (ArrayList<Operation>)other.operations.clone();
	}

	@Override
	public void run() {
		// Child implement this
	}
	public void checkme() {
		for (int i=0;i<values.length;i++) {
			if (values[i] == i)
				color[i] = Color.green;
			else
				color[i] = Color.red;
		}
	}

	public final boolean isSmaller(int i, int j) {
		if (i<0) throw new RuntimeException("Out of bounds in isSmaller("+i+","+j+"): "+i+"<0");
		if (j<0) throw new RuntimeException("Out of bounds in isSmaller("+i+","+j+"): "+j+"<0");
		if (i>=getValueCount()) throw new RuntimeException("Out of bounds in isSmaller("+i+","+j+"), "+i+">= value count");
		if (j>=getValueCount()) throw new RuntimeException("Out of bounds in isSmaller("+i+","+j+"), "+j+">= value count");

		readCount+=2;
		return values[i]<values[j];
	}
	public final boolean isSmallerThan(int i, int val) {
		if (i<0) throw new RuntimeException("Out of bounds in isSmallerThan("+i+","+val+"): "+i+"<0");
		if (i>=getValueCount()) throw new RuntimeException("Out of bounds in isSmallerThan("+i+","+val+"), "+i+">= value count");
		readCount+=1;
		return values[i]<val;
	}
	public final void swap(int i, int j) {
		if (i<0) throw new RuntimeException("Out of bounds in swap("+i+","+j+"): "+i+"<0");
		if (j<0) throw new RuntimeException("Out of bounds in swap("+i+","+j+"): "+j+"<0");
		if (i>=getValueCount()) throw new RuntimeException("Out of bounds in swap("+i+","+j+"), "+i+">= value count");
		if (j>=getValueCount()) throw new RuntimeException("Out of bounds in swap("+i+","+j+"), "+j+">= value count");
		
		if(!init){
			initValues.clear();
			for(int k=0 ; k<values.length ; k++){
				initValues.add(new Integer(values[k]));
			}
			init = true;
		}
		operations.add(new Swap(i, j));
		
		readCount+=2;
		writeCount+=2;
		synchronized (values) {
			int tmp = values[i];
			values[i] = values[j];
			values[j] = tmp;
		}
		stepUI();
	}
	public final int getValueCount() {
		return values.length;
	}
	public final int getValue(int i) {
		if (i<0) throw new RuntimeException("Out of bounds in getValue("+i+"): "+i+"<0");
		if (i>=getValueCount()) throw new RuntimeException("Out of bounds in getValue("+i+"), "+i+">= value count");
		readCount++;
		return values[i];
	}
	public final void setValue(int i,int val) {
		if (i<0) throw new RuntimeException("Out of bounds in setValue("+i+"): "+i+"<0");
		if (i>=getValueCount()) throw new RuntimeException("Out of bounds in setValue("+i+"), "+i+">= value count");
		
		if(!init){
			initValues.clear();
			for(int k=0 ; k<values.length ; k++){
				initValues.add(new Integer(values[k]));
			}
			init = true;
		}
		operations.add(new SetVal(i, val));
		
		writeCount++;
		values[i]=val;
		stepUI();
	}
	public final void copy(int from,int to) {
		if (from<0) throw new RuntimeException("Out of bounds in copy("+from+","+to+"): "+from+"<0");
		if (to<0) throw new RuntimeException("Out of bounds in copy("+from+","+to+"): "+to+"<0");
		if (from>=getValueCount()) throw new RuntimeException("Out of bounds in copy("+from+","+to+"), "+from+">= value count");
		if (to>=getValueCount()) throw new RuntimeException("Out of bounds in copy("+from+","+to+"), "+to+">= value count");
		
		if(!init){
			initValues.clear();
			for(int k=0 ; k<values.length ; k++){
				initValues.add(new Integer(values[k]));
			}
			init = true;
		}
		operations.add(new CopyVal(from, to));
		
		readCount++;
		writeCount++;
		values[to] = values[from];
		stepUI();
	}
	public final void sorted(int i){
		color[i] = Color.green;
	}
	
	public int getWriteCount() {
		return writeCount ;
	}
	public int getReadCount() {
		return readCount;
	}

	/*@ get the color of the value passed as argument */
	protected int getValueColor(int value) {
		return (int) ((((float) value) / ((float) getValueCount())) * 255.);
	}

}
