package lessons.sort.baseball.universe;

import java.util.Random;
import java.util.Vector;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.xnap.commons.i18n.I18n;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.universe.World;

public class BaseballWorld extends World {
	public static final int MIX_SORTED = 0;
	public static final int MIX_RANDOM = 1;
	public static final int MIX_NOBODY_HOME = 2;
	public static final int MIX_ALMOST_SORTED = 3;
	
	
	public static final int COLOR_HOLE = -1;

	private int[] field; // the bases which composed the field
	private int baseAmount,posAmount; // field dimensions
	private int holeBase,holePos; // The coordinate of the hole
	protected int[] initialField; 
	private Vector<BaseballMove> moves = new Vector<BaseballMove>(); // all moves made on the field -- used for graphical purpose only

	/** Copy constructor used internally by PLM */
	public BaseballWorld(BaseballWorld other) {
		super(other);
		initialField = new int[other.initialField.length];
		for (int i=0;i<initialField.length;i++)
			initialField[i] = other.initialField[i];
	}


	/** Regular constructor used by exercises */
	public BaseballWorld(Game game, String name, int baseAmount, int positionAmount) {
		this(game, name,baseAmount,positionAmount,MIX_RANDOM);
	}

	public BaseballWorld(Game game, String name, int baseAmount, int posAmount, int mix) {
		super(game, name);
		
		// create the bases
		this.baseAmount = baseAmount;
		this.posAmount = posAmount;
		
		this.field = new int[baseAmount*posAmount];
		for (int base = 0 ; base < baseAmount ; base++)
			for (int pos = 0; pos < posAmount; pos++)
				setPlayerColor(base, pos, base);
		setPlayerColor(baseAmount-1, 0, COLOR_HOLE);
		Random r = new Random(0);
		if (mix == MIX_RANDOM) {
			for (int base = 0 ; base<getBasesAmount();base++)
				for (int pos = 0 ; pos<getPositionsAmount();pos++)
					swap(base, pos, (int) (r.nextDouble()*getBasesAmount()), (int) (r.nextDouble()*getPositionsAmount()));
		
		} else if (mix == MIX_NOBODY_HOME) {
			// Ensure that nobody's home once it's mixed. 
			//   We tested that no situation of 4 bases with that condition exposes the bug of the naive algorithm
			//   We tested it by generating all situations, actually.
			boolean swapped;
			do {
				swapped = false;
				for (int base = 0 ; base<getBasesAmount();base++)
					for (int pos = 0 ; pos<getPositionsAmount();pos++)
						if (getPlayerColor(base, pos) == base) {
							swapped = true;
							int newBase;
							do {
								newBase = (int) (r.nextDouble()*getBasesAmount());
							} while (newBase == base);
							int newPos = (int) (r.nextDouble()*getPositionsAmount());
							swap(base, pos, newBase, newPos);							
						}
			} while (swapped);
		} else if (mix == MIX_SORTED) {
			/* nothing to do here */
		} else if (mix == MIX_ALMOST_SORTED) {
			/* Expose the bug of the naive algorithm */
			swap(0,0,  2,0);
		} else {
			throw new IllegalArgumentException("The mix paramter must be one of the provided constants, not "+mix);
		}

		initialField = new int[field.length];
		for (int i=0;i<field.length;i++)
			initialField[i] = field[i];
		
		// Add an entity
		addEntity(new BaseballEntity());
		
		// Cache the hole position 
		for ( int base = 0 ; base < getBasesAmount(); base++)
			for ( int pos = 0 ; pos < getPositionsAmount(); pos++)
				if ( getPlayerColor(base,pos)== COLOR_HOLE) {
					holeBase = base;
					holePos = pos;
					return;
				}
		
	}


	public BaseballWorld(Game game, String name, int baseAmount, int positionAmount, int[] values) {
		this(game, name, baseAmount, positionAmount, MIX_SORTED);
		if (baseAmount*posAmount != values.length)
			throw new RuntimeException("Your values array is not of the right size");
		field = values;
		
		initialField = new int[field.length];
		for (int i=0;i<field.length;i++)
			initialField[i] = field[i];
		
		// Cache again the hole position 
		for ( int base = 0 ; base < getBasesAmount(); base++)
			for ( int pos = 0 ; pos < getPositionsAmount(); pos++)
				if ( getPlayerColor(base,pos)== COLOR_HOLE) {
					holeBase = base;
					holePos = pos;
					return;
				}
		
	}


	/**
	 * Returns a textual description of the differences between the caller and world
	 * @param o the world with which you want to compare your world
	 */
	@Override
	public String diffTo(World o, I18n i18n) {
		if (o == null || !(o instanceof BaseballWorld))
			return i18n.tr("This is not a baseball world :-(");

		BaseballWorld other = (BaseballWorld) o;
		if (getBasesAmount() != other.getBasesAmount())
			return i18n.tr("Differing amount of bases: {0} vs {1}",getBasesAmount(),other.getBasesAmount());

		if (getPositionsAmount() != ((BaseballWorld) o).getPositionsAmount())
			return i18n.tr("Differing amount of players: {0} vs {1}", getPositionsAmount(), other.getPositionsAmount());

		StringBuffer sb = new StringBuffer();
		for (int base = 0; base< baseAmount; base++)
			for (int pos=0; pos<posAmount; pos++)
				if (getPlayerColor(base, pos) != other.getPlayerColor(base, pos))
					sb.append(i18n.tr("Player at base {0}, pos {1} differs: {2} vs {3}\n",base,pos,getPlayerColor(base, pos), other.getPlayerColor(base, pos)));

		return sb.toString();
	}

	public boolean equals(Object other) {
		if (other == null || !(other instanceof BaseballWorld))
			return false;

		BaseballWorld otherField = (BaseballWorld) other;
		if (   this.holeBase != otherField.holeBase
				|| this.holePos != otherField.holePos
				|| this.getBasesAmount() != otherField.getBasesAmount()
				|| this.getPositionsAmount() != otherField.getPositionsAmount())

			return false;

		for (int base = 0; base< baseAmount; base++)
			for (int pos=0; pos<posAmount; pos++)
				if (getPlayerColor(base, pos) != otherField.getPlayerColor(base, pos))
					return false;

		return true;
	}

	/** Ensures that the provided script engine can run scripts in the specified programming language 
	 * @throws ScriptException */
	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine engine) throws ScriptException {
		if (lang.equals(Game.PYTHON)) {
			engine.eval(
					"def getBasesAmount():\n" +
					"  return entity.getBasesAmount()\n" +
					"def getPositionsAmount():\n" +
					"  return entity.getPositionsAmount()\n" +
					"def getHoleBase():\n" +
					"  return entity.getHoleBase()\n" +
					"def getHolePosition():\n" +
					"  return entity.getHolePosition()\n" +
					"def isSorted():\n" +
					"  return entity.isSorted()\n" +
					"def isBaseSorted():\n" +
					"  return entity.isBaseSorted()\n" +
					"def getPlayerColor(base,pos):\n" +
					"  return entity.getPlayerColor(base,pos)\n" +
					"def move(base,pos):\n" +
					"  entity.move(base,pos)\n" +
					/* BINDINGS TRANSLATION: French */
					"def getNombreBases():\n" +
					"  return entitygetBasesAmount()\n" +
					"def getNombrePositions():\n" +
					"  return entity.getPositionsAmount()\n" +
					"def getTrouBase():\n" +
					"  return entity.getHoleBase()\n" +
					"def getTrouPosition():\n" +
					"  return entity.getHolePosition()\n" +
					"def estTrie():\n" +
					"  return entity.isSorted()\n" +
					"def estBaseTriee():\n" +
					"  return entity.isBaseSorted()\n" +
					"def getCouleurJoueur(base,pos):\n" +
					"  return entity.getPlayerColor(base,pos)\n" +
					"def deplace(base,pos):\n" +
					"  (base,pos)\n" +
					"def estSelectionne():\n"+
					"  return isSelected()\n"+

					""
					);
		} else {
			throw new RuntimeException("No binding of BaseballWorld for "+lang);
		}
	}
	
	/** 
	 * Reset the state of the current world to the one passed in argument
	 * @param the world which must be the new start of your current world
	 */
	public void reset(World world) {
		super.reset(world);		

		BaseballWorld other = (BaseballWorld) world;
		
		moves = new Vector<BaseballMove>();
		for (BaseballMove m : other.moves)
			moves.add(m);
		
		holeBase = other.holeBase;
		holePos = other.holePos;

		baseAmount = other.baseAmount;
		posAmount = other.posAmount;
		field= new int[other.baseAmount*other.posAmount];
		for (int base=0; base<baseAmount; base++)
			for (int pos=0; pos<posAmount; pos++)
				setPlayerColor(base, pos, other.getPlayerColor(base, pos));
	}

	/** Returns a string representation of the world */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("BaseballWorld "+getName()+": {");

		for (int base = 0 ; base < baseAmount ; base++) {
			if (base!=0)
				sb.append(" , ");
			for (int pos = 0 ; pos < posAmount ; pos++) {
				if (pos!=0)
					sb.append(",");
				sb.append(getPlayerColor(base,pos));
			}
		}
		sb.append("}");
		return sb.toString();
	}

	/** Returns the number of bases on your field */
	public int getBasesAmount() {
		return baseAmount;
	}
	/** Returns the amount of players per base on this field */
	public int getPositionsAmount() {
		return posAmount;
	}

	/**
	 * Returns the color of the player in base baseIndex at position playerLocation
	 * @param base the index of the base we are looking for
	 * @param pos  the position within that base (between 0 and getLocationsAmount()-1 )
	 */
	public int getPlayerColor(int base, int pos)  {
		return field[base*posAmount+pos];
	}
	/**
	 * Sets the color of the player in the specified base at the specified position to the specified value 
	 * @param base the index of the base we are looking for
	 * @param pos  the position within that base (between 0 and getLocationsAmount()-1 )
	 * @param color the new value
	 */
	public void setPlayerColor(int base, int pos, int color)  {
		field[base*posAmount+pos] = color;
	}

	/** Returns the index of the base where is hole is located */
	public int getHoleBase() {
		return this.holeBase;
	}

	/** Returns the position in the base where is hole is located */
	public int getHolePosition(){
		return this.holePos;
	}

	/** Returns the last move made on the field */
	public BaseballMove getLastMove() {
		if (moves.size() == 0)
			return null;
		return moves.get(moves.size()-1);
	}
	/** Returns the amount of moves done so far */
	public int getMoveCount() {
		return moves.size();
	}
	/** Returns all moves done so far */
	public Vector<BaseballMove> getMoves() {
		return moves;
	}
	
	public int[] getField()
	{
		return field;
	}

	/** Checks that the world is sorted, and display an helpful error message if not */
	public void assertSorted(String exercise) {
		if (isSorted())
			return;
		
		StringBuffer sb = new StringBuffer("{");

		for (int base = 0 ; base < baseAmount ; base++) {
			if (base!=0)
				sb.append(" , ");
			for (int pos = 0 ; pos < posAmount ; pos++) {
				if (pos!=0)
					sb.append(",");
				sb.append(initialField[base*getPositionsAmount()+ pos]);
			}
		}
		sb.append("}");

		String msg =getGame().i18n.tr("It''s still not sorted!! PLEASE REPORT THIS BUG, along with the following information:\n" +
				"Exercise: {0}; Amount of bases: {1}; Initial situation: {2}", exercise, getBasesAmount(),sb.toString());
		System.err.println(msg);
		throw new RuntimeException(msg);

	}
	/** Returns if every player of the field is on the right base */
	public boolean isSorted() {
		for (int base=0; base<baseAmount; base++)
			for (int pos=0; pos<posAmount; pos++)
				if (base==baseAmount-1) {// last base, may contain the hole
					if (   getPlayerColor(base, pos) != COLOR_HOLE 
					    && getPlayerColor(base, pos) != base)
						return false;
				} else if (getPlayerColor(base, pos) != base)
					return false;
		return true;
	}
	/** Returns if every player of the specified base is on the right base */
	public boolean isBaseSorted(int base) {
		for (int pos=0;pos<posAmount;pos++)
			if (base==baseAmount-1) // last base, may contain the hole
				if (   getPlayerColor(base, pos) != COLOR_HOLE 
				    && getPlayerColor(base, pos) != base)
					return false;
			else if (getPlayerColor(base, pos) != base)
				return false;
		
		return true;
	}

	/**
	 * Moves the specified player into the hole
	 * @throws IllegalArgumentException in case baseSrc is not near the hole
	 */
	public void move(int base, int position) {
		if ( base >= this.getBasesAmount() || base < 0)
			throw new IllegalArgumentException(getGame().i18n.tr("Cannot move from base {0} since it''s not between 0 and {1}",base,(getBasesAmount()-1)));

		if ( position < 0 || position > this.getPositionsAmount()-1 )
			throw new IllegalArgumentException(getGame().i18n.tr("Cannot move from position {0} since it''s not between 0 and {1})",position,(getPositionsAmount()-1)));

		// must work only if the bases are next to each other
		if (	(holeBase != base+1)
			 && (holeBase != base-1)
			 && (holeBase != 0                  || base != getBasesAmount()-1 )
			 && (holeBase != getBasesAmount()-1 || base != 0 )
			 && (holeBase != base ) )
			
			throw new IllegalArgumentException(getGame().i18n.tr("The player {0} from base {1} is too far from the hole (at base {2}) to reach it in one move",
					position,base,holeBase));

		// All clear. Proceed.
		moves.add(new BaseballMove(base, position, holeBase, holePos, getPlayerColor(base, position),this));
		swap(base, position, holeBase,holePos);
		holeBase = base;
		holePos = position;
	}


	/**
	 * Swap two players (no validity check is enforced)
	 * @param baseSrc : the index of the source base
	 * @param posSrc : the position of the player that you want to move from the source base
	 * @param baseDst : the index of the destination base
	 * @param posDst : the position of the player that you want to move from the destination base
	 */
	private void swap(int baseSrc, int posSrc, int baseDst, int posDst) {
		int flyingMan = getPlayerColor(baseSrc,posSrc);

		setPlayerColor(baseSrc, posSrc,   getPlayerColor(baseDst,posDst));
		setPlayerColor(baseDst, posDst,   flyingMan);
	}
}
