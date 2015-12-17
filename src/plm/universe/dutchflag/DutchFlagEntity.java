package plm.universe.dutchflag;

import java.io.BufferedWriter;
import java.io.IOException;

import plm.universe.Entity;
import plm.universe.World;
import plm.universe.dutchflag.operations.DutchFlagSwap;

public class DutchFlagEntity extends Entity {

	public final static int BLUE = 0;
	public final static int WHITE = 1;
	public final static int RED = 2;
	
	/** Required by the PLM infrastructure */
	public DutchFlagEntity() {
		super("DutchFlag Entity");
	}

	/** Copy constructor (used internally) */
	public DutchFlagEntity(String name) {
		super(name);
	}
	
	/** Instantiation Constructor (used by exercises to setup the world) */ 
	public DutchFlagEntity(World world) {
		super("Flag sorter",world);
	}

	/** Swap two positions */
	public void swap(int from, int to) {
		((DutchFlagWorld) world).swap(from, to);
		addOperation(new DutchFlagSwap(this,from,to));
		stepUI();
	}

	/**
	 * Give the color of of a specific ray in the flag
	 * @param rank : the number of the ray that you want to get.
	 * @return The color of that ray (either 0, 1 or 2)
	 */
	public int getColor(int rank) {
		return ((DutchFlagWorld) world).getColor(rank);
	}
	
	/** Returns the amount of rays in this flag */
	public int getSize() {
		return ((DutchFlagWorld) world).getSize();
	}
		
	/** Returns whether the flag is correctly sorted */
	public boolean isSorted() {
		return ((DutchFlagWorld) world).isSorted();
	}		
			
	/** Must exist so that exercises can instantiate the entity (Entity is abstract) 
	 */
	@Override
	public void run() {
	}
	
	/** Returns a string representation of the world */
	public String toString(){
		return "DutchFlagEntity (" + this.getClass().getName() + ")";
	}
	
	/* BINDINGS TRANSLATION: French */
	public void echange(int i, int j) { swap(i,j); }
	public int getCouleur(int rank)   { return getColor(rank); }
	public int getTaille()            { return getSize(); }
	public boolean estTrie()          { return isSorted(); }
	public boolean estChoisi()        { return isSelected(); }
	public final static int BLEU = BLUE;
	public final static int BLANC = WHITE;
	public final static int ROUGE = RED;

	@Override
	public void command(String command, BufferedWriter out) {
		int num = Integer.parseInt((String) command.subSequence(0, 3));
		int nb,nb2;
		try {
			switch(num){
			case 110:
				nb = Integer.parseInt((command.split(" ")[1]));
				nb2 = Integer.parseInt((command.split(" ")[2]));
				swap(nb, nb2);
				break;
			case 111:
				out.write(Integer.toString(getSize()));
				out.write("\n");
				break;
			case 112:
				nb = Integer.parseInt((command.split(" ")[1]));
				out.write(Integer.toString(getColor(nb)));
				out.write("\n");
				break;
			case 113:
				out.write((isSorted()?"1":"0"));
				out.write("\n");
				break;
			case 114:
				out.write((isSelected()?"1":"0"));
				out.write("\n");
				break;
			case 115:
				((DutchFlagWorld) world).assertSorted();
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
