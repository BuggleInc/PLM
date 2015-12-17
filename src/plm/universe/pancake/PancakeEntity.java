package plm.universe.pancake;

import java.io.BufferedWriter;
import java.io.IOException;

import plm.universe.Entity;
import plm.universe.World;
import plm.universe.pancake.operations.FlipOperation;

public class PancakeEntity extends Entity {

	/**
	 * Must exist. Calling PancakeEntity("dummy name") is ok
	 * Part of the copy process 
	 * Must call super(name)
	 * @return A new instance of PancakeEntity
	 */
	public PancakeEntity() {
		super("Pancake Entity");
	}

	/** Copy constructor (used internally) */
	public PancakeEntity(String name) {
		super(name);
	}
	
	/** Instantiation Constructor (used by exercises to setup the world) */ 
	public PancakeEntity(String name, World world) {
		super(name,world);
	}
	
	/**
	 * Flip a certain amount of pancakes in the stack
	 * @param numberOfPancakes : the number of pancakes, 
	 * 			beginning from the top of the stack, that you want to flip.
	 */
	public void flip(int numberOfPancakes) {
		int old = ((PancakeWorld) world).getLastMove();
		((PancakeWorld) world).flip(numberOfPancakes);
		addOperation(new FlipOperation(this, numberOfPancakes, old));
		stepUI();
	}

	/**
	 * Give the radius of a specific pancake among others
	 * @param pancakeNumber : the number of the pancake, beginning from the top of the stack, that you want to get.
	 * @return The radius of the expected pancake
	 */
	public int getPancakeRadius(int pancakeNumber) {
		return ((PancakeWorld) world).getPancakeRadius(pancakeNumber);
	}

	/** Returns the size of the pancake stack */
	public int getStackSize() {
		return ((PancakeWorld) world).getStackSize();
	}
	
	/**
	 * Returns whether the specific pancake (counting from the stack top) is upside down
	 */
	public boolean isPancakeUpsideDown(int rank) {
		return ((PancakeWorld) world).isPancakeUpsideDown(rank);
	}
	
	/**
	 * Tell if the stack of pancakes is correctly sorted according to the control freak pancake seller
	 */
	public boolean isSorted() {
		return ( (PancakeWorld) this.world).isSorted();
	}
	
	/** Must exist so that exercises can instantiate the entity (Entity is abstract) 
	 */
	@Override
	public void run() {
	}
	
	/** Returns a string representation of the world */
	public String toString(){
		return "PancakeEntity (" + this.getClass().getName() + ")";
	}
	
	/* BINDINGS TRANSLATION: French */
	public void retourne(int numberOfPancakes) { flip(numberOfPancakes); }
	public int getRayonCrepe(int rank) { return getPancakeRadius(rank); }
	public int getTaillePile() { return getStackSize(); }
	public boolean estCrepeRetournee(int rank) { return isPancakeUpsideDown(rank); }
	public boolean estTriee() { return isSorted(); }
	public boolean estChoisi() { return isSelected(); }

	@Override
	public void command(String command, BufferedWriter out) {
		int num = Integer.parseInt((String) command.subSequence(0, 3));
		int nb;
		try {
			switch(num){
			case 110:
				out.write(Integer.toString(getStackSize()));
				out.write("\n");
				break;
			case 111:
				nb = Integer.parseInt((command.split(" ")[1]));
				out.write(Integer.toString(getPancakeRadius(nb)));
				out.write("\n");
				break;
			case 112:
				nb = Integer.parseInt((command.split(" ")[1]));
				out.write((isPancakeUpsideDown(nb)?"1":"0"));
				out.write("\n");
				break;
			case 113:
				nb = Integer.parseInt((command.split(" ")[1]));
				flip(nb);
				break;
			case 114:
				out.write((isSorted()?"1":"0"));
				out.write("\n");
				break;
			case 115:
				out.write((isSelected()?"1":"0"));
				out.write("\n");
				break;
			default:
				getGame().getLogger().log("COMMANDE INCONNUE : "+command);
				break;
			}
			out.flush();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
}
