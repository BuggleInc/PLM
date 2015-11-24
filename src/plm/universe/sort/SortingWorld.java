package plm.universe.sort;

import java.util.ArrayList;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import plm.core.lang.LangPython;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.universe.World;

public class SortingWorld extends World {
	private int[] values;	// the values as they are sorted in the array
	private int[] initValues;	// needed by the chronoview
	private int readCount = 0;	// the count of the read made
	private int writeCount = 0;	// the count of the write made
	
	/*
	 * It's needed by the chronoview
	 * This list contains all the Actions ( SetVal, CopyVal or Swap ) made on the world since 
	 * the moment where it has been shown to the user
	 */
	private ArrayList<Action> actions = new ArrayList<Action>(1) ;	

	/** Copy constructor used by the PLM internals */
	public SortingWorld(SortingWorld world) {
		super(world);
	}

	/** Constructor used in the exercises */
	public SortingWorld(Game game, String name, int nbValues) {
		this(game, name, nbValues, true);
	}
	public SortingWorld(Game game, String name, int nbValues, boolean someoneHomeOk) {
		super(game, name);
		if (nbValues>100)
			setDelay(1);
		else
			setDelay(50);
		this.values = new int[nbValues];
		for (int i=0 ; i< this.values.length ; i++) 
			this.values[i] = i;
		
		scramble();
		// If instructed so, scramble the values until none is @home
		if (! someoneHomeOk) {	
			boolean someoneHome;
			do {
				someoneHome = false;
				for (int i=0;i<values.length;i++) 
					if (values[i] == i)
						someoneHome = true;
				if (someoneHome)
					scramble();
			} while (!someoneHome);
		}
		this.initValues = this.values;
		addEntity(new SortingEntity());
	}

	private void scramble() {
		Random r = new Random(0);
		while( this.isSorted() )
			for ( int caseNumber = 0 ; caseNumber < this.values.length ; caseNumber++)
				// Swapping time !
				if ( r.nextDouble() > 0.5)
					this.exchangeValues(caseNumber,(int)(r.nextDouble()*this.values.length));
	}

	public boolean equals(Object o) {
		if (o == null || !(o instanceof SortingWorld))
			return false;

		SortingWorld other = (SortingWorld) o;
		if (values.length != other.values.length)
			return false;
		for (int i = 0 ; i < this.values.length ; i++) 
			if ( this.values[i] != other.values[i] )
				return false;
		
		if (! (this.readCount == other.readCount) )
			return false;
		if (! (this.writeCount == other.writeCount) )
			return false;
		
		/* Do not compare the Action order as it's not part of the problem specification
		 * 
		 * If you want to add this again, please make sure that swap(i,j) equals to swap(j,i)
		 
		if (Actions.size() != other.Actions.size())
			return false;
		for (int i = 0 ; i < this.Actions.size() ; i++) 
			if (! Actions.get(i).equals( other.Actions.get(i)) )
				return false;
		*/
		return true;
	}

	/**
	 * Make a textual description of the differences between the caller and world
	 * @param world : the world with which you want to compare your world
	 * @return A textual description of the differences between the caller and world
	 */
	@Override
	public String diffTo(World world) {
		String s ;
		if (world == null || !(world instanceof SortingWorld)) {
			s="This is not a world of sorting :(";
		} else {
			SortingWorld other = (SortingWorld) world;
			StringBuffer sb = new StringBuffer();
			
			if (this.values.length != other.values.length)
				sb.append(getGame().i18n.tr("This is very weird: There is not the same amount of values! Expected: {0}; Found: {1}\n",this.values.length,other.values.length));
			
			if ( this.readCount != other.readCount )
				sb.append(getGame().i18n.tr("Invalid read count. Expected: {0}; Found: {1}\n",this.readCount,other.readCount));
			
			if ( this.writeCount != other.writeCount )
				sb.append(getGame().i18n.tr("Invalid write count. Expected: {0}; Found: {1}\n",this.writeCount,other.writeCount));
			
			for (int i = 0 ; i < this.values.length ; i++) 
				if ( this.values[i] != other.values[i] )
					sb.append(getGame().i18n.tr("Value at index {0} differs. Expected {1}; Found {2}\n",
							i, val2str(values[i],values.length),  val2str(other.values[i],other.values.length)  ));
					
			s = sb.toString();
		}
		return s;
	}

	@Override
	public String getDebugInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append(getName()+" [");
		boolean first = true;
		for (Integer i : initValues) {
			if (first) 
				first = false;
			else 
				sb.append(", ");
			sb.append(i);
		}
		sb.append("]");
		return sb.toString();
	}
	/**
	 * Tells if the array is Sorted or not
	 */
	private boolean isSorted() {
		boolean sw = true;
		for ( int i = 0 ; i < this.values.length && sw ; i++ )
		{
			if ( this.values[i] != i )
			{
				sw = false ;
			}
		}
		return sw;
	}

	/**
	 * Copy the value from the cell of index from into the cell of index to<br>
	 * It costs one read and one write.
	 * @param from The index of the cell to copy
	 * @param to The index of the cell where copy the value
	 */
	public void copy(int from,int to) {
		if (from<0) throw new RuntimeException("Out of bounds in copy("+from+","+to+"): "+from+"<0");
		if (to<0) throw new RuntimeException("Out of bounds in copy("+from+","+to+"): "+to+"<0");
		if (from>=getValueCount()) throw new RuntimeException("Out of bounds in copy("+from+","+to+"), "+from+">= value count");
		if (to>=getValueCount()) throw new RuntimeException("Out of bounds in copy("+from+","+to+"), "+to+">= value count");

		this.actions.add(new CopyVal(from, to));

		this.readCount++;
		this.writeCount++;
		this.values[to] = this.values[from];
	}

	/**
	 * Returns the initial state of the array
	 */
	public int[] getInitValues() {
		return initValues;
	}

	/**
	 * Return the list of all the Actions made on the world since its initialization/last reset
	 */
	public ArrayList<Action> getActions() {
		return this.actions;
	}

	/**
	 * Return the read counter
	 */
	public int getReadCount() {
		return this.readCount;
	}

	/** Returns the amount of values in the array */
	public int getValueCount() {
		return values.length;
	}

	/** Returns the array of values that need to be sorted */
	public int[] getValues() {
		return this.values;
	}
	
	/** Returns the write counter */
	public int getWriteCount() {
		return this.writeCount;
	}

	/**
	 * Tell if the value at the index i is smaller than the value at index j<br>
	 * It costs two reads.
	 * @param i the index of the first cell that we want to check
	 * @param j the index of the second cell that we want to check
	 * @return if the content of the cell of index i is smaller than the content of the cell of index j
	 */
	public boolean isSmaller(int i, int j) {
		if (i<0) throw new RuntimeException("Out of bounds in isSmaller("+i+","+j+"): "+i+"<0");
		if (j<0) throw new RuntimeException("Out of bounds in isSmaller("+i+","+j+"): "+j+"<0");
		if (i>=getValueCount()) throw new RuntimeException("Out of bounds in isSmaller("+i+","+j+"), "+i+">= value count");
		if (j>=getValueCount()) throw new RuntimeException("Out of bounds in isSmaller("+i+","+j+"), "+j+">= value count");

		this.readCount+=2;
		return this.values[i]< this.values[j];
	}
	
	/**
	 * Tell if the value at the index i is smaller than val<br>
	 * It costs one read.
	 * @param i the index of case that we want to check
	 * @param val the value with which compare the cell
	 * @return if the case of index i is smaller than val
	 */
	public boolean isSmallerThan(int i, int val) {
		if (i<0) throw new RuntimeException("Out of bounds in isSmallerThan("+i+","+val+"): "+i+"<0");
		if (i>=getValueCount()) throw new RuntimeException("Out of bounds in isSmallerThan("+i+","+val+"), "+i+">= value count");

		this.readCount+=1;
		return this.values[i]<val;
	}
	
	/** 
	 * Reset the state of the current world to the one passed in argument
	 * @param w the world which must be the new start of your current world
	 */
	@Override
	public void reset(World w) {
		super.reset(w);
		SortingWorld world = (SortingWorld)w;
		
		this.values = world.values.clone();
		this.initValues = world.initValues.clone();
		this.readCount = world.readCount ;
		this.writeCount = world.writeCount;
		this.actions = new ArrayList<Action>(1);
		for ( Action o: world.actions)
			this.actions.add(o);
	}

	/**
	 * Setup the engine so that it's ready to host the user code
	 *
	 * @param lang the programming language used
	 * @throws ScriptException some error reported by the scripting engine itself
	 */
	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine e) throws ScriptException {
		if (lang instanceof LangPython) {
			e.eval(
					"def getValueCount():\n" +
					"  return entity.getValueCount()\n" +
					"def swap(i,j):\n" +
					"  entity.swap(i,j)\n" +
					"def copy(i,j):\n" +
					"  entity.copy(i,j)\n" +
					"def getValue(i):\n" +
					"  return entity.getValue(i)\n" +
					"def setValue(i,j):\n" +
					"  entity.setValue(i,j)\n" +
					"def isSmaller(i,j):\n"+
					"  return entity.isSmaller(i,j)\n"+
					"def isSmallerThan(i,j):\n"+
					"  return entity.isSmallerThan(i,j)\n"+
					/* BINDINGS TRANSLATION: French */
					"def getNombreValeurs():\n" +
					"  return getValueCount()\n" +
					"def echange(i,j):\n" +
					"  swap(i,j)\n" +
					"def copie(i,j):\n" +
					"  copy(i,j)\n" +
					"def getValeur(i):\n" +
					"  return getValue(i)\n" +
					"def setValeur(i,j):\n" +
					"  setValue(i,j)\n" +
					"def plusPetit(i,j):\n"+
					"  return isSmaller(i,j)\n"+
					"def plusPetitQue(i,j):\n"+
					"  return isSmallerThan(i,j)\n"

			);
		} else {
			throw new RuntimeException("No binding of SortingWorld for "+lang);
		}
	}

	/**
	 * Return the value of index i in the array
	 * @param i the index wanted in the array
	 * @return the value of index i in the array
	 */
	public int getValue(int i) {
		if (i<0) throw new RuntimeException("Out of bounds in getValue("+i+"): "+i+"<0");
		if (i>=getValueCount()) throw new RuntimeException("Out of bounds in getValue("+i+"), "+i+">= value count");
		this.actions.add(new GetVal(i));
		readCount++;
		return values[i];
	}

	/**
	 * Set the value of the case number i of the array at val<br>
	 * It costs only one write.
	 * @param i the index of the cell
	 * @param val the value that you want to set
	 */
	public void setValue(int i,int val) {
		if (i<0) throw new RuntimeException("Out of bounds in setValue("+i+"): "+i+"<0");
		if (i>=getValueCount()) throw new RuntimeException("Out of bounds in setValue("+i+"), "+i+">= value count");

		this.actions.add(new SetVal(i, val));

		this.writeCount++;
		this.values[i]=val;
	}

	/**
	 * Swap the value of the case number i with the value of the case number j<br>
	 * It costs two reads and two writes
	 * @param i the index of the first cell concerned
	 * @param j the index of the second cell concerned
	 */
	public void swap(int i, int j) {
		if (i<0) throw new RuntimeException(getGame().i18n.tr("Out of bounds in swap({0},{1}): {2}<0",i,j,i));
		if (j<0) throw new RuntimeException(getGame().i18n.tr("Out of bounds in swap({0},{1}): {2}<0",i,j,j));
		if (i>=getValueCount()) throw new RuntimeException(getGame().i18n.tr("Out of bounds in swap({0},{1}): {2}>value count",i,j,i));
		if (j>=getValueCount()) throw new RuntimeException(getGame().i18n.tr("Out of bounds in swap({0},{1}): {2}>value count",i,j,j));

		this.actions.add(new Swap(i, j));

		this.readCount+=2;
		this.writeCount+=2;

		exchangeValues(i,j);
	}

	/**
	 * Exchange the value of the case number i with the value of the case number j<br>
	 * It doesn't increase the read or the write counters.
	 * @param i the index of the first cell concerned
	 * @param j the index of the second cell concerned
	 */
	private void exchangeValues(int i, int j) {
		int tmp = this.values[i];
		this.values[i] = this.values[j];
		this.values[j] = tmp;
	}

	/** Make a string representation of the value to be sorted
	 * 
	 *  It's a bad idea to display integer values because the students mix the indexes 
	 *  and the values. It's better to use a string representation for that. 
	 *  
	 *  WARNING, this function is duplicated in SortingWorldView. Yes, I fell ashamed.
	 */
	protected String val2str(int value,int amountOfValues) {
		String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		if (amountOfValues<26) {
			return letters.substring(value, value+1);
		} 
		if (amountOfValues < 26*26) {
			return   letters.substring(value/26, (value/26)+1 )
					+letters.substring(value%26, (value%26)+1 );
		}
		return ""+value;
	}


}
