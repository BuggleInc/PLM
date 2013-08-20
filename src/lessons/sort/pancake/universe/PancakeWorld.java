package lessons.sort.pancake.universe;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.ImageIcon;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.ui.ResourcesCache;
import jlm.core.ui.WorldView;
import jlm.universe.EntityControlPanel;
import jlm.universe.World;

public class PancakeWorld extends World {

	private int lastMove = -1;
	protected int moveCount = 0;
	private int selected = -1;
	private boolean burnedWorld ;
	public boolean wasRandom = false;
	
	/** Copy constructor */ 
	public PancakeWorld(PancakeWorld world) {
		super(world);
	}
	
	/** Returns a component able of displaying the world */
	@Override
	public WorldView getView() {
		return new PancakeWorldView(this);
	}
	/** Returns the icon of the universe */
	// http://omgwtflols.deviantart.com/
	// http://fc06.deviantart.net/fs71/f/2012/118/5/7/pixel_art__pancakes_with_s236rup_b236_omgwtflols-d4xu72c.gif
	// http://omgwtflols.deviantart.com/art/Pixel-Art-Pancakes-with-syrup-298700868
	@Override
	public ImageIcon getIcon() {
		return ResourcesCache.getIcon(this,"../img/world_pancake.png");
	}
	PancakeFlipButtonPanel panel = null;
	/** Returns the panel which let the user to interact dynamically with the world */
	@Override
	public EntityControlPanel getEntityControlPanel() {
		if (panel == null)
			panel = new PancakeFlipButtonPanel();
		return panel;
	}
	/** Passes the mouse selection from view to the control panel */ 
	public void setSelectedPancake(int rank) {
		if (rank < 1 || rank > getStackSize() || (rank==1&&!burnedWorld) ) {
			this.selected = -1;
			panel.setSelectedPancake(selected);
		} else {
			this.selected = rank;
			panel.setSelectedPancake(selected);
			notifyWorldUpdatesListeners();
		}
		
	}
	/** Passes the mouse action from the view to the control panel */
	public void doMove() {
		panel.doMove();
	}
	
	
	/** 
	 * Regular PancakeWorld constructor that creates a random plate
	 * @param name : the name of the world
	 * @param amountOfPancakes : the amount of pancakes in the stack
	 * @param burnedPancake : if we take care of the fact that the pancake is burned on one side
	 */
	public PancakeWorld(String name, int size, boolean burnedPancake) {
		super(name);
		setDelay(200); // Delay (in ms) in default animations
		
		/* Create the pancakes */
		this.pancakeStack =  new Pancake[size];
		for (int i = 0; i < size; i++) 
			pancakeStack[i] = new Pancake(i + 1);
		
		/* Mix them */
		wasRandom = true;
		while (isSorted()) 
			for ( int rank = 0 ; rank < size ; rank++) {			
				if ( Math.random() > 0.5) // Flipping time !
					pancakeStack[rank].flip(); 

				if ( Math.random() > 0.5) // Swapping time !
					swap(rank, (int)(Math.random()*size));
			}
		
		this.burnedWorld = burnedPancake;
	}
	/** 
	 * Regular PancakeWorld constructor that takes the pancake sizes from the provided parameter
	 * @param name the name of the world
	 * @param sizes the size of each pancake
	 * @param burnedPancake if we take care of the fact that the pancake is burned on one side
	 */
	public PancakeWorld(String name, int[] sizes, boolean burnedPancake) {
		super(name);
		setDelay(200); // Delay (in ms) in default animations
		
		/* Create the pancakes */
		this.pancakeStack =  new Pancake[sizes.length];
		for (int i = 0; i < sizes.length; i++) 
			pancakeStack[i] = new Pancake(sizes[i]);
		
		this.burnedWorld = burnedPancake;
	}
	
	/** Returns a textual description of the differences between the caller and the parameter */
	@Override
	public String diffTo(World o) {
		if (o == null || !(o instanceof PancakeWorld))
			return Game.i18n.tr("This is not a world of pancakes :-(");

		PancakeWorld other = (PancakeWorld) o;
		if (pancakeStack.length != other.pancakeStack.length)
			return Game.i18n.tr("The two worlds are of differing size");

		StringBuffer res = new StringBuffer();
		for ( int i = 0;i< pancakeStack.length;i++) 
			if ( !pancakeStack[i].equals(other.pancakeStack[i], burnedWorld)) 
				res.append(" Pancake #"+(i+1)+" differs: "+pancakeStack[i].toString() +" vs "+other.pancakeStack[i].toString()+"\n");

		return res.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof PancakeWorld))
			return false;
		PancakeWorld other = (PancakeWorld) o;
		if (burnedWorld != other.burnedWorld)
			return false;
		if (pancakeStack.length != other.pancakeStack.length)
			return false;
		for ( int i = 0;i< pancakeStack.length;i++) 
			if ( !pancakeStack[i].equals(other.pancakeStack[i], burnedWorld))
				return false;
		return true;
	}
	/** 
	 * Reset the state of the current world to the one passed in argument
	 * @param the world which must be the new start of your current world
	 */
	@Override
	public void reset(World world) {
		PancakeWorld other = (PancakeWorld) world;
		this.pancakeStack = new Pancake[other.pancakeStack.length];
		for (int i=0;i<pancakeStack.length;i++)
			pancakeStack[i] = other.pancakeStack[i].copy();

		this.burnedWorld = other.burnedWorld;
		this.lastMove = other.lastMove;
		super.reset(world);		
	}

	/** Ensures that the provided engine can be used to solve Pancake exercises */ 
	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine e) throws ScriptException {
		if (lang.equals(Game.PYTHON)) {
			e.eval(
				"def getStackSize():\n" +
				"  return entity.getStackSize()\n" +
				"def getPancakeRadius(rank):\n" +
				"  return entity.getPancakeRadius(rank)\n" +
				"def isPancakeUpsideDown(pancakeNumber):\n"+
				"  return entity.isPancakeUpsideDown(pancakeNumber)\n" +
				"def flip(numberOfPancakes):\n" +
				"  entity.flip(numberOfPancakes)\n"	+
				/* BINDINGS TRANSLATION: French */
				"def getTaillePile():\n"+
				"  return getStackSize()\n"+
				"def getRayonCrepe(rank):\n"+
				"  return getPancakeRadius(rank)\n"+
				"def estCrepeRetournee(rank):\n"+
				"  return isPancakeUpsideDown(rank)\n"+
				"def retourne(nb):\n"+
				"  return flip(nb)\n"
				);
		} else {
			throw new RuntimeException("No binding of PancakeWorld for "+lang);
		}
	}

	/* --------------------------------------- */
	private Pancake[] pancakeStack; // The stack of pancakes

	private void swap(int from, int to) {
		Pancake temp = pancakeStack[from];
		pancakeStack[from] = pancakeStack[to];
		pancakeStack[to] = temp;
	}
	/**
	 * Flip a certain amount of pancakes in the stack
	 * @param numberOfPancakes : the number of pancakes, beginning from the top of the stack, that you want to flip.
	 */
	public void flip(int numberOfPancakes) {
		if (numberOfPancakes < 1) 
			throw new IllegalArgumentException(Game.i18n.tr("Asked to flip {0} pancakes, but you must flip at least one", numberOfPancakes));
		if (numberOfPancakes > this.getStackSize()) 
			throw new IllegalArgumentException(Game.i18n.tr("Asked to flip {0} pancakes, but there is only {1} pancakes on this stack", numberOfPancakes,getStackSize()));
		
		//System.err.println("Flip("+numberOfPancakes+")");
		/* Invert the pancakes' position */
		int firstPancake = 0 ;
		int lastPancake = numberOfPancakes-1;
		while ( firstPancake < lastPancake )
			this.swap(firstPancake++, lastPancake--);
		
		/* Change their orientation */
		for (int i = 0 ;i<numberOfPancakes;i++)
			pancakeStack[i].flip();
		
		this.lastMove = numberOfPancakes ;
		this.selected = -1;
		this.moveCount++;
	}
	
	
	/** Returns the index of the last flipped pancake */
	protected int getLastMove() {
		return lastMove;
	}
	protected int getSelectedPancake() {
		return selected;
	}
	
	/**
	 * Give the size of a specific pancake among others
	 * @param rank : the number of the pancake, beginning from the top of the stack, that you want to get.
	 * @return The radius of the expected pancake
	 */
	public int getPancakeRadius(int rank) {
		if ( rank < 0 || rank >= getStackSize())
			throw new IllegalArgumentException(Game.i18n.tr("Cannot get the radius of pancake #{0} because it''s not between 0 and {1}",rank, getStackSize()));

		return pancakeStack[rank].getRadius();
	}
	
	/**
	 * Give the size of the stack of pancakes
	 * @return The number of pancakes in the stack
	 */
	public int getStackSize() {
		return pancakeStack.length;
	}
	

	/**
	 * Returns if the specified pancake (counting from the stack top) is upside down
	 */
	public boolean isPancakeUpsideDown(int rank) {
		if ( rank < 0 || rank >= getStackSize())
			throw new IllegalArgumentException(Game.i18n.tr("Cannot get the orientation of pancake #{0} because it''s not between 0 and {1}",rank, getStackSize()));

		return pancakeStack[rank].isUpsideDown();
	}
	
	/** Returns whether the stack of pancakes is correctly sorted according to the control freak pancake seller */
	public boolean isSorted() {
		for ( int rank = 0 ; rank < pancakeStack.length ; rank++) {
			Pancake pancake = pancakeStack[rank];
			if (pancake.getRadius() != rank+1)
				return false;
			if (burnedWorld && pancake.isUpsideDown())
				return false;
		}
		return true;
	}
	
	/** Returns a string representation of the world */
	public String toString(){
		StringBuffer res = new StringBuffer("<PancakeWorld name:"+getName()+" size:"+getStackSize()+">");
		for (Pancake p : pancakeStack) 
			res.append(p.toString());
		res.append("</PancakeWorld>");
		return res.toString();
	}

	/** Returns whether we pay attention to the burned side or not */
	public boolean isBurnedPancake() {
		return burnedWorld;
	}
}

class Pancake {
	private int radius; // Radius of the pancake
	private boolean upsideDown; // True if the burned face is facing the sky, else false
	
	public Pancake(int radius) {
		this.radius = radius;
		this.upsideDown = false;
	}
	
	/**
	 * Make a copy of the caller
	 * @return a copy of the method caller
	 */
	public Pancake copy() {
		Pancake res = new Pancake(this.getRadius());
		if (this.isUpsideDown())
			res.flip();
		return res;
	}
	
	/**
	 * Indicate whether some other pancake is "equal to" this one
	 * @param burnedMatter if we take care of the position of the burned part
	 * @param Pancake other: the other pancake with which to compare
	 * @return If the two pancakes are equals
	 */
	public boolean equals(Pancake other, boolean burnedMatter) {
		if (getRadius() != other.getRadius())
			return false;
		if (burnedMatter && isUpsideDown() != other.isUpsideDown())
			return false;
				
		return true;
	}
	
	/** Flip a pancake, which leads to changing upsideDown */
	public void flip() {
		upsideDown = !upsideDown;
	}
	
	/** Returns the radius of the pancake */
	public int getRadius() {
		return this.radius;
	}
	
	/** Returns whether the pancake is upside down */
	public boolean isUpsideDown() {
		return this.upsideDown;
	}
	
	/** Returns a string representation of the pancake */
	public String toString() {
		String s = "< Radius: "+this.getRadius();
		if ( this.isUpsideDown())
			s+=" , upside down";
		
		s+=" >";
		return s;
	}
	
}
