package plm.universe.dutchflag;

import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.xnap.commons.i18n.I18n;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.universe.World;

public class DutchFlagWorld extends World {

	int[] content;
	int moveCount = 0;
	
	/** Copy constructor */ 
	public DutchFlagWorld(DutchFlagWorld world) {
		super(world);
	}
	
	private int initialContent[];
	/** 
	 * Regular PancakeWorld constructor that creates a random flag
	 * @param name : the name of the world
	 * @param size : the amount of elements 
	 */
	public DutchFlagWorld(Game game, String name, int size) {
		super(game, name);
		setDelay(size>100?20:200); // Delay (in ms) in default animations
		new DutchFlagEntity(this);
		
		content = new int[size];
		initialContent = new int[size];
		Random rand = new Random(0);
		for (int i=0; i<size; i++) {
			double r = rand.nextDouble();
			if (r<1./3.)
				content[i] = 0;
			else if (r < 2./3.)
				content[i] = 1;
			else
				content[i] = 2;
			
			initialContent[i] = content[i];
		}
	}
	
	public DutchFlagWorld(Game game, String name, int size, int colorRemoved) {
		super(game, name);
		setDelay(size>100?20:200); // Delay (in ms) in default animations
		new DutchFlagEntity(this);
		int [][]color = {
				{1,2},
				{0,2},
				{0,1}
		};
		
		content = new int[size];
		initialContent = new int[size];
		Random rand = new Random(0);
		for (int i=0; i<size; i++) {
			double r = rand.nextDouble();
			if (r<1./2.)
				content[i] = color[colorRemoved][0];
			else
				content[i] = color[colorRemoved][1];
			
			initialContent[i] = content[i];
		}
	}

	/** Returns a textual description of the differences between the caller and the parameter */
	@Override
	public String diffTo(World o, I18n i18n) {
		if (o == null || !(o instanceof DutchFlagWorld))
			return i18n.tr("This world is not a dutch flag");

		DutchFlagWorld other = (DutchFlagWorld) o;
		if (content.length != other.content.length)
			return i18n.tr("The two worlds are of differing size");

		StringBuffer res = new StringBuffer();
		for ( int i = 0; i<content.length; i++) 
			if ( content[i] != other.content[i] ) 
				res.append(i18n.tr(" Ray #{0} differs: color {1} is not color {2}\n",(i+1), content[i],other.content[i]));

		return res.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof DutchFlagWorld))
			return false;
		DutchFlagWorld other = (DutchFlagWorld) o;
		if (content.length != other.content.length)
			return false;
		for ( int i = 0; i<content.length; i++) 
			if ( content[i] != other.content[i] ) 
				return false;
		return true;
	}
	/** 
	 * Reset the state of the current world to the one passed in argument
	 * @param the world which must be the new start of your current world
	 */
	@Override
	public void reset(World world) {
		DutchFlagWorld other = (DutchFlagWorld) world;
		this.content = new int[other.content.length];
		for (int i=0;i<content.length;i++)
			content[i] = other.content[i];

		this.moveCount = other.moveCount;
		super.reset(world);		
	}

	/** Ensures that the provided engine can be used to solve Pancake exercises */ 
	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine e) throws ScriptException {
		if (lang.equals(Game.PYTHON)) {
			e.eval(
				"def getSize():\n" +
				"  return entity.getSize()\n" +
				"def getColor(rank):\n" +
				"  return entity.getColor(rank)\n" +
				"def swap(i,j):\n" +
				"  entity.swap(i,j)\n"	+
				"BLUE = entity.BLUE\n"	+
				"WHITE = entity.WHITE\n"	+
				"RED = entity.RED\n"	+
				/* BINDINGS TRANSLATION: French */
				"def getTaille():\n"+
				"  return getSize()\n"+
				"def getCouleur(rank):\n"+
				"  return getColor(rank)\n"+
				"def echange(i,j):\n"+
				"  return swap(i,j)\n"+
				"BLEU = entity.BLUE\n"	+
				"BLANC = entity.WHITE\n"	+
				"ROUGE = entity.RED\n"	
				);
		} else {
			throw new RuntimeException("No binding of PancakeWorld for "+lang);
		}
	}

	/* --------------------------------------- */
	/** Swap two positions */
	public void swap(int from, int to) {
		if ( from < 0 || from >= getSize())
			throw new IllegalArgumentException(getGame().i18n.tr("Cannot swap {0} and {1} because {2} is not between 0 and {3}",
					from, to, from, getSize()-1));
		if ( to < 0 || to >= getSize())
			throw new IllegalArgumentException(getGame().i18n.tr("Cannot swap {0} and {1} because {2} is not between 0 and {3}",
					from, to, to, getSize()-1));
		
		int temp = content[from];
		content[from] = content[to];
		content[to] = temp;
		
		moveCount++;
	}	
	
	/**
	 * Give the color of a specific ray in the flag
	 * @param rank : the number of the ray that you want to get.
	 * @return The color of that ray (either 0, 1 or 2)
	 */
	public int getColor(int rank) {
		if ( rank < 0 || rank >= getSize())
			throw new IllegalArgumentException(getGame().i18n.tr("Cannot get the color of line #{0} because it''s not between 0 and {1}",rank, getSize()));

		return content[rank];
	}
	
	/** Returns the amount of rays in this flag */
	public int getSize() {
		return content.length;
	}
	
	
	/** Returns whether the flag is correctly sorted */
	public boolean isSorted() {
		int currentColor = 0;
		for ( int rank = 0 ; rank < content.length ; rank++) {
			if (content[rank] > currentColor)
				currentColor = content[rank];
			if (content[rank] < currentColor)
				return false;
		}
		return true;
	}
	public void assertSorted() {
		if (isSorted())
			return;
		
		StringBuffer sb = new StringBuffer("{");

		for (int rank = 0 ; rank < getSize() ; rank++) {
			if (rank!=0)
				sb.append(", ");
			sb.append(content[rank]);
		}
		sb.append("}");

		String msg =getGame().i18n.tr("It''s still not sorted!! PLEASE REPORT THIS BUG, along with the following information:\n" +
				"Initial situation: {0}", sb.toString());
		System.err.println(msg);
		throw new RuntimeException(msg);

	}
	
	/** Returns a string representation of the world */
	public String toString(){
		StringBuffer res = new StringBuffer("<DutchFlagWorld name:"+getName()+" size:"+getSize()+">");
		for (int i=0;i<getSize();i++) 
			res.append( (i==0?"":", ")+content[i]);
		res.append("</DutchFlagWorld>");
		return res.toString();
	}
	
	public int[] getContent()
	{
		return content;
	}
	
	public int getMove()
	{
		return moveCount;
	}
}
