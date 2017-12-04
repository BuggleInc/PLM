package plm.universe.baseball;

import java.util.List;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.xnap.commons.i18n.I18n;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import plm.core.lang.LangPython;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.I18nManager;
import plm.core.utils.FileUtils;
import plm.universe.SVGOperation;
import plm.universe.World;

public class BaseballWorld extends World {
	public static final int MIX_SORTED = 0;
	public static final int MIX_RANDOM = 1;
	public static final int MIX_NOBODY_HOME = 2;
	public static final int MIX_ALMOST_SORTED = 3;
	
	
	public static final int COLOR_HOLE = -1;

	private int[] field; // the bases which composed the field
	private int basesAmount,positionsAmount; // field dimensions
	private int holeBase,holePosition; // The coordinate of the hole
	protected int[] initialField; 
	private int moveCount = 0;

	/** Copy constructor used internally by PLM */
	public BaseballWorld(BaseballWorld other) {
		super(other);
		initialField = new int[other.initialField.length];
		for (int i=0;i<initialField.length;i++)
			initialField[i] = other.initialField[i];
	}


	/** Regular constructor used by exercises */
	public BaseballWorld(FileUtils fileUtils, String name, int baseAmount, int positionAmount) {
		this(fileUtils, name,baseAmount,positionAmount,MIX_RANDOM);
	}

	public BaseballWorld(FileUtils fileUtils, String name, int basesAmount, int positionsAmount, int mix) {
		super(fileUtils, name);
		
		// create the bases
		this.basesAmount = basesAmount;
		this.positionsAmount = positionsAmount;
		
		this.field = new int[basesAmount*positionsAmount];
		for (int base = 0 ; base < basesAmount ; base++)
			for (int pos = 0; pos < positionsAmount; pos++)
				setPlayerColor(base, pos, base);
		setPlayerColor(basesAmount-1, 0, COLOR_HOLE);
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
					holePosition = pos;
					return;
				}
		
	}


	public BaseballWorld(FileUtils fileUtils, String name, int baseAmount, int positionAmount, int[] values) {
		this(fileUtils, name, baseAmount, positionAmount, MIX_SORTED);
		if (baseAmount*positionsAmount != values.length)
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
					holePosition = pos;
					return;
				}
		
	}

	@JsonCreator
	public BaseballWorld(FileUtils fileUtils, @JsonProperty("name")String name) {
		super(fileUtils, name);
	}

	/**
	 * Returns a textual description of the differences between the caller and world
	 * @param o the world with which you want to compare your world
	 */
	@Override
	public String diffTo(World o) {
		I18n i18n = I18nManager.getI18n(getLocale());
		if (o == null || !(o instanceof BaseballWorld))
			return i18n.tr("This is not a baseball world :-(");

		BaseballWorld other = (BaseballWorld) o;
		if (getBasesAmount() != other.getBasesAmount())
			return i18n.tr("Differing amount of bases: {0} vs {1}",getBasesAmount(),other.getBasesAmount());

		if (getPositionsAmount() != ((BaseballWorld) o).getPositionsAmount())
			return i18n.tr("Differing amount of players: {0} vs {1}", getPositionsAmount(), other.getPositionsAmount());

		StringBuffer sb = new StringBuffer();
		for (int base = 0; base< basesAmount; base++)
			for (int pos=0; pos<positionsAmount; pos++)
				if (getPlayerColor(base, pos) != other.getPlayerColor(base, pos))
					sb.append(i18n.tr("Player at base {0}, pos {1} differs: {2} vs {3}\n",base,pos,getPlayerColor(base, pos), other.getPlayerColor(base, pos)));

		return sb.toString();
	}

	public boolean equals(Object other) {
		if (other == null || !(other instanceof BaseballWorld))
			return false;

		BaseballWorld otherField = (BaseballWorld) other;
		if (   this.holeBase != otherField.holeBase
				|| this.holePosition != otherField.holePosition
				|| this.getBasesAmount() != otherField.getBasesAmount()
				|| this.getPositionsAmount() != otherField.getPositionsAmount())

			return false;

		for (int base = 0; base< basesAmount; base++)
			for (int pos=0; pos<positionsAmount; pos++)
				if (getPlayerColor(base, pos) != otherField.getPlayerColor(base, pos))
					return false;

		return true;
	}

	/** Ensures that the provided script engine can run scripts in the specified programming language 
	 * @throws ScriptException */
	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine engine) throws ScriptException {
		if (lang instanceof LangPython) {
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
	 * @param world the new start of your current world
	 */
	public void reset(World world) {
		super.reset(world);		

		BaseballWorld other = (BaseballWorld) world;
		
		holeBase = other.holeBase;
		holePosition = other.holePosition;

		basesAmount = other.basesAmount;
		positionsAmount = other.positionsAmount;
		field= new int[other.basesAmount*other.positionsAmount];
		for (int base=0; base<basesAmount; base++)
			for (int pos=0; pos<positionsAmount; pos++)
				setPlayerColor(base, pos, other.getPlayerColor(base, pos));
		moveCount = other.moveCount;
	}

	@Override
	protected List<SVGOperation> draw() {

		return null;
	}

	/** Returns a string representation of the world */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("BaseballWorld "+getName()+": {");

		for (int base = 0 ; base < basesAmount ; base++) {
			if (base!=0)
				sb.append(" , ");
			for (int pos = 0 ; pos < positionsAmount ; pos++) {
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
		return basesAmount;
	}
	/** Returns the amount of players per base on this field */
	public int getPositionsAmount() {
		return positionsAmount;
	}

	/** Returns the amount of moves done so far */
	public int getMoveCount() {
		return moveCount;
	}
	
	/**
	 * Returns the color of the player in base baseIndex at position playerLocation
	 * @param base the index of the base we are looking for
	 * @param pos  the position within that base (between 0 and getLocationsAmount()-1 )
	 */
	public int getPlayerColor(int base, int pos)  {
		return field[base*positionsAmount+pos];
	}
	/**
	 * Sets the color of the player in the specified base at the specified position to the specified value 
	 * @param base the index of the base we are looking for
	 * @param pos  the position within that base (between 0 and getLocationsAmount()-1 )
	 * @param color the new value
	 */
	public void setPlayerColor(int base, int pos, int color)  {
		field[base*positionsAmount+pos] = color;
	}

	/** Returns the index of the base where is hole is located */
	public int getHoleBase() {
		return this.holeBase;
	}

	/** Returns the position in the base where is hole is located */
	public int getHolePosition(){
		return this.holePosition;
	}
	
	public int[] getInitialField() {
		return initialField;
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

		for (int base = 0 ; base < basesAmount ; base++) {
			if (base!=0)
				sb.append(" , ");
			for (int pos = 0 ; pos < positionsAmount ; pos++) {
				if (pos!=0)
					sb.append(",");
				sb.append(initialField[base*getPositionsAmount()+ pos]);
			}
		}
		sb.append("}");

		String msg = I18nManager.getI18n(getLocale()).tr("It''s still not sorted!! PLEASE REPORT THIS BUG, along with the following information:\n" +
				"Exercise: {0}; Amount of bases: {1}; Initial situation: {2}", exercise, getBasesAmount(),sb.toString());
		System.err.println(msg);
		throw new RuntimeException(msg);

	}
	/** Returns if every player of the field is on the right base */
	@JsonIgnore
	public boolean isSorted() {
		for (int base=0; base<basesAmount; base++)
			for (int pos=0; pos<positionsAmount; pos++)
				if (base==basesAmount-1) {// last base, may contain the hole
					if (   getPlayerColor(base, pos) != COLOR_HOLE 
					    && getPlayerColor(base, pos) != base)
						return false;
				} else if (getPlayerColor(base, pos) != base)
					return false;
		return true;
	}
	/** Returns if every player of the specified base is on the right base */
	public boolean isBaseSorted(int base) {
		for (int pos=0;pos<positionsAmount;pos++)
			if (base==basesAmount-1) // last base, may contain the hole
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
			throw new IllegalArgumentException(I18nManager.getI18n(getLocale()).tr("Cannot move from base {0} since it''s not between 0 and {1}",base,(getBasesAmount()-1)));

		if ( position < 0 || position > this.getPositionsAmount()-1 )
			throw new IllegalArgumentException(I18nManager.getI18n(getLocale()).tr("Cannot move from position {0} since it''s not between 0 and {1})",position,(getPositionsAmount()-1)));

		// must work only if the bases are next to each other
		if (	(holeBase != base+1)
			 && (holeBase != base-1)
			 && (holeBase != 0                  || base != getBasesAmount()-1 )
			 && (holeBase != getBasesAmount()-1 || base != 0 )
			 && (holeBase != base ) )
			
			throw new IllegalArgumentException(I18nManager.getI18n(getLocale()).tr("The player {0} from base {1} is too far from the hole (at base {2}) to reach it in one move",
					position,base,holeBase));

		// All clear. Proceed.
		swap(base, position, holeBase,holePosition);
		holeBase = base;
		holePosition = position;
		moveCount++;
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
