package plm.universe.sort;

import plm.universe.Entity;
import plm.universe.World;

public class SortingEntity extends Entity {

	public SortingEntity() {
		super("Sorting Entity");
	}

	/** Part of the copy process; Must call super(name) */
	public SortingEntity(String name) {
		super(name);
	}
	
	/** Instantiation Constructor (used by exercises to setup the world) */
	public SortingEntity(String name, World world) {
		super(name,world);
	}

	public void copy(int from,int to) {
		((SortingWorld) this.world).copy(from,to);
		stepUI();
	}

	public int getValue(int i) {
		return ((SortingWorld) this.world).getValue(i);
	}
	
	public int getValueCount() {
		return ((SortingWorld) this.world).getValueCount();
	}
	
	public boolean isSmaller(int i, int j) {
		return ((SortingWorld) this.world).isSmaller(i,j);
	}
	
	public boolean isSmallerThan(int i, int val) {
		return ((SortingWorld) this.world).isSmallerThan(i,val);
	}
	
	public void run() {
		// Child implement this
	}
	
	public void setValue(int i,int val) {
		((SortingWorld) this.world).setValue(i,val);
		stepUI();
	}
	
	public void swap(int i, int j) {
		((SortingWorld) this.world).swap(i,j);
		stepUI();
	}
	
	/* BINDINGS TRANSLATION: French */
	public int getNombreValeurs() { return getValueCount(); }

	public int getValeur(int i)   { return getValue(i);}
	public void setValeur(int i,int val) { setValue(i, val); }
	
	public boolean plusPetit(int i, int j) { return isSmaller(i, j); }	
	public boolean plusPetitQue(int i, int value) { return isSmallerThan(i, value); }
	
	public void echange(int i, int j) { swap(i,j); }
	public void copie(int from,int to) { copy(from,to);}

	
	public boolean estSelectionne() {return isSelected();}
}
