package universe.sort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;

import jlm.ui.WorldView;
import jlm.universe.Entity;
import jlm.universe.EntityControlPanel;
import jlm.universe.World;

public class SortingWorld extends World {
	int[] values;
	public int maxValue=-1;
	boolean[] sorted;
	
	public SortingWorld() {
		this("Sorting world");
	}
	public SortingWorld(String name) {
		this(name,200);
	}
	public SortingWorld(String name, int nbValues) {
		super(name);
		boolean[] used = new boolean[nbValues];
		for (int i=0;i<nbValues;i++) 
			used[i] = false;
		
		values = new int[nbValues];
		sorted = new boolean[nbValues];
		for (int i=0;i<values.length;i++) {
			sorted[i] = false;
			
			int value = (int)(values.length * Math.random());
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
		sorted = world2.sorted.clone();
		maxValue = world2.maxValue;
		
		Iterator<Entity> it = entities();
		while (it.hasNext()) {
			SortingEntity se = (SortingEntity) it.next();
			se.values = values.clone();
			se.sorted = sorted.clone();
			se.maxValue = maxValue;
		}
	}

	@Override
	public String getAbout(){
		return "<h1>Méthodes disponibles pour les algorithmes de tri</h1>" +
			   "<table border=1>" +
			   "<tr><td>int getValueCount()<td>Retourne le nombre de valeurs dans le tableau</tr>" +
			   
			   "<tr><td>boolean compare(int i, int j)<td>Retourne vrai ssi le contenu de la case i est inférieur à celui de la case j</tr>" +
			   "<tr><td>boolean compareTo(int i, int val)<td>Retourne vrai ssi le contenu de la case i est inférieur à la valeur val</tr>" +
			   
			   "<tr><td>void swap(int i, int j)<td>Inverse le contenu de la case i et celui de la case j</tr>" +
			   "<tr><td>void copy(int from, int to)<td>Recopie le contenu de la case from dans la case to</tr>" +
			   
			   "<tr><td>int getValue(int idx)<td>Retourne la valeur de la case idx</tr>" +
			   "<tr><td>void setValue(int idx,int val)<td>Affecte la valeur val à la case idx</tr>" +
			   
			   "<tr><td>void sorted(int idx)<td>Indique que la case idx est triée (pour l'affichage)</tr>"+
			   "</table>";
				 
	}
	
	@Override
	public World copy() {
		return new SortingWorld(this);
	}
	@Override
	public void reset(World w) {
		SortingWorld world2 = (SortingWorld)w;
		values = world2.values.clone();
		sorted = world2.sorted.clone();
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

	@Override
	public WorldView getView() {
		return new SortingWorldView(this);
	}

	// TODO Implement world IO
	@Override
	public void readFromFile(BufferedReader br) throws IOException {}
	@Override
	public void writeToFile(BufferedWriter f) throws IOException {}
}
