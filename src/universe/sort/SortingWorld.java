package universe.sort;

import java.awt.Color;
import java.util.Iterator;
import java.util.Random;

import jlm.ui.WorldView;
import jlm.universe.Entity;
import jlm.universe.EntityControlPanel;
import jlm.universe.World;

public class SortingWorld extends World {
	int[] values;
	public int maxValue=-1;
	Color[] color;
	
	public SortingWorld() {
		this("Sorting world");
	}
	public SortingWorld(String name) {
		this(name,200);
	}
	public SortingWorld(String name, int nbValues) {
		super(name);
		setDelay(1);
		boolean[] used = new boolean[nbValues];
		for (int i=0;i<nbValues;i++) 
			used[i] = false;
		
		values = new int[nbValues];
		color = new Color[nbValues];
		Random randomizer = new Random();
		for (int i=0;i<values.length;i++) {
			color[i] = Color.yellow;
			int value = randomizer.nextInt(values.length); 
			while (used[value])
				value = (value+1)%values.length;
			values[i]=value;
			used[value] = true;
			
			if (values[i]>maxValue)
				maxValue = values[i];
		} 
	}
	
	public SortingWorld(SortingWorld world2) {
		super(world2);
		values = world2.values.clone();
		color = world2.color.clone();
		maxValue = world2.maxValue;
		
		Iterator<Entity> it = entities();
		while (it.hasNext()) {
			SortingEntity se = (SortingEntity) it.next();
			se.values = values.clone();
			se.color = color.clone();
			se.maxValue = maxValue;
		}
	}
	
	@Override
	public World copy() {
		return new SortingWorld(this);
	}
	@Override
	public void reset(World w) {
		SortingWorld world2 = (SortingWorld)w;
		values = world2.values.clone();
		color = world2.color.clone();
		maxValue = world2.maxValue;
				
		super.reset(w);		
	}

	@Override
	public EntityControlPanel getEntityControlPanel() {
		return new EntityControlPanel() { // TODO display something
			private static final long serialVersionUID = 1L;
			@Override
			public void setEnabledControl(boolean enabled) {
			}			
		}; 
	}

	SortingWorldView worldView = new SortingWorldView(this); 
	@Override
	public WorldView getView() {
		return worldView;
	}

	// TODO Implement world IO
}
