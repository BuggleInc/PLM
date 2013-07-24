package lessons.sort.baseball.universe;

import javax.script.ScriptEngine;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.ui.WorldView;
import jlm.universe.EntityControlPanel;
import jlm.universe.World;

public class BaseballWorld extends World {
	public int COLOR_HOLE = -1;

	private BaseballBase[] bases; // the bases which composed the field
	private int holeBase,holePos; // The coordinate of the hole
	private BaseballMove lastMove; // the last move made on the field -- used for graphical purpose only

	/** Copy constructor used internally by JLM */
	public BaseballWorld(BaseballWorld world) {
		super(world);
	}


	/** Regular constructor used by exercises */
	public BaseballWorld(String name, int numberOfBases, int playerLocationAmount) {
		super(name);
		setDelay(200); // Delay (in milliseconds) in default animations

		// create the bases
		this.bases = new BaseballBase[numberOfBases];
		for (int color = 0 ; color < numberOfBases ; color++)
			this.bases[color] = new BaseballBase(color,playerLocationAmount, color!=numberOfBases-1);

		holeBase = bases.length-1;
		holePos = 0;
		this.bases[this.bases.length-1].setPlayerColor(0, COLOR_HOLE);

		lastMove = null;
		mix();
	}

	/**
	 * Returns a textual description of the differences between the caller and world
	 * @param o the world with which you want to compare your world
	 */
	public String diffTo(World o) {
		if (o == null || !(o instanceof BaseballWorld))
			return Game.i18n.tr("This is not a baseball world :-(");

		BaseballWorld other = (BaseballWorld) o;
		if (getBasesAmount() != other.getBasesAmount())
			return Game.i18n.tr("Differing amount of bases: {0} vs {1}",getBasesAmount(),other.getBasesAmount());

		if (getLocationsAmount() != ((BaseballWorld) o).getLocationsAmount())
			return Game.i18n.tr("Differing amount of players: {0} vs {1}", getLocationsAmount(), other.getLocationsAmount());

		StringBuffer sb = new StringBuffer();
		for ( int i = 0 ; i < getBasesAmount() ; i++)
			if ( !this.getBase(i).equals(other.getBase(i)))
				sb.append(Game.i18n.tr("Base #{0} differs: {1} vs {2}",this.getBase(i).toString(),other.getBase(i).toString()));

		return sb.toString();
	}

	public boolean equals(Object other) {
		if (other == null || !(other instanceof BaseballWorld))
			return false;

		BaseballWorld otherField = (BaseballWorld) other;
		if (   this.holeBase != otherField.holeBase
				|| this.holePos != otherField.holePos
				|| this.getBasesAmount() != otherField.getBasesAmount()
				|| this.getLocationsAmount() == otherField.getLocationsAmount())

			return false;

		for ( int i = 0 ; i< this.bases.length ;i++ )
			if (! this.bases[i].equals(otherField.bases[i]))
				return false;

		return true;
	}

	/**
	 * Return the script except that must be injected within the environment before running user code 
	 * It should pass all order to the java entity, which were injected independently  
	 * @return  the script except that must be injected within the environment before running user code 
	 * @param the programming language used
	 */
	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine e) {
		throw new RuntimeException("No binding of BaseballWorld for "+lang);
	}

	/** Returns a component able to display the world */
	public WorldView getView() {
		return new BaseballWorldView(this);
	}

	/** Returns a panel allowing to interact dynamically with the world */
	@Override
	public EntityControlPanel getEntityControlPanel() {
		return new BaseballMovePanel();
	}

	/** 
	 * Reset the state of the current world to the one passed in argument
	 * @param the world which must be the new start of your current world
	 */
	public void reset(World world) {
		super.reset(world);		

		BaseballWorld other = (BaseballWorld) world;

		this.bases = new BaseballBase[other.bases.length];
		for (int i = 0 ; i < bases.length ; i++) {
			this.bases[i] = new BaseballBase(i,other.getLocationsAmount(), i!=bases.length-1);
			for ( int j = 0 ; j < getLocationsAmount();j++)
				bases[i].setPlayerColor(j,other.bases[i].getPlayerColor(j));
		}

		holeBase = other.holeBase;
		holePos = other.holePos;

		lastMove = other.lastMove;
	}

	/**
	 * Return a string representation of the world
	 * @return A string representation of the world
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("BaseballWorld "+getName()+":\n");

		for (int i = 0 ; i < this.bases.length ; i++) {
			sb.append("- Base "+i+"\n");
			sb.append("  Color : "+this.bases[i].getColor()+"\n");
			for ( int j = 0 ; j < getLocationsAmount() ; j++)
				sb.append("  Player "+j+" : "+this.bases[i].getPlayerColor(j)+"\n");
		}
		return sb.toString();
	}

	/** Returns the number of bases on your field */
	public int getBasesAmount() {
		return this.bases.length;
	}
	/** Returns the amount of players per base on this field */
	public int getLocationsAmount() {
		return this.bases[0].getLocationsAmount();
	}

	/** Returns a specific base of the field */
	public BaseballBase getBase(int i) {
		return this.bases[i];
	}

	/**
	 * Returns the color of the base located at baseIndex
	 * @param baseIndex the index of the wanted base
	 * @return the color of the player in base baseIndex at position playerLocation
	 * @throws IllegalArgumentException if you ask for a base which isn't in the range 0 to amountOfBases-1
	 */
	public int getBaseColor(int baseIndex) {
		if ( baseIndex < 0 || baseIndex >= this.bases.length)
			throw new IllegalArgumentException("getBaseColor: you ask for a base "+baseIndex+" which isn't in the range 0 to getAmountOfBases()-1.");

		return this.bases[baseIndex].getColor();
	}
	/**
	 * Returns the color of the player in base baseIndex at position playerLocation
	 * @param base the index of the base we are looking for
	 * @param pos  the position within that base (between 0 and getLocationsAmount()-1 )
	 */
	public int getPlayerColor(int base, int pos)  {
		return this.bases[base].getPlayerColor(pos);
	}
	/**
	 * Sets the color of the player in the specified base at the specified position to the specified value 
	 * @param base the index of the base we are looking for
	 * @param pos  the position within that base (between 0 and getLocationsAmount()-1 )
	 * @param color the new value
	 */
	public void setPlayerColor(int base, int pos, int color)  {
		bases[base].setPlayerColor(pos, color);
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
		return lastMove;
	}

	/** Returns if every player of every base is on the right base */
	private boolean isSorted() {
		for ( int base = 0 ; base < bases.length ; base++ )
			if (!(bases[base].isSorted()))
				return false;
		return true;
	}

	/** Mix the players between the different bases */
	private void mix() {
		do {
			for (int base = 0 ; base<getBasesAmount();base++)
				for (int pos = 0 ; pos<getLocationsAmount();pos++)
					this.swap(base, pos,
							(int) (Math.random()*getBasesAmount()), (int) (Math.random()*getLocationsAmount()));
		} while(this.isSorted());

		// Cache again the hole position 
		for ( int base = 0 ; base < getBasesAmount(); base++)
			for ( int pos = 0 ; pos < getLocationsAmount(); pos++)
				if ( this.bases[base].getPlayerColor(pos)==-1) {
					this.holeBase = base;
					this.holePos = pos;
					return;
				}	
	}

	/**
	 * Moves the specified player into the hole
	 * @throws IllegalArgumentException in case baseSrc is not near the hole
	 */
	public void move(int indexBaseSrc, int playerLocation) {
		if ( indexBaseSrc >= this.getBasesAmount() || indexBaseSrc < 0)
			throw new IllegalArgumentException("The base index must be between 0 and "+(this.getBasesAmount()-1)+".\nUnfortunatly, "+indexBaseSrc+" isn't");

		if ( playerLocation < 0 || playerLocation > this.getLocationsAmount()-1 )
			throw new IllegalArgumentException("There isn't a position "+playerLocation+".\nIt must be between 0 and getLocationsAmount()-1.");

		// must work only if the bases are next to each other
		if (		( this.holeBase == indexBaseSrc+1 )
				||  ( this.holeBase == indexBaseSrc-1 )
				||  ( this.holeBase == 0 && indexBaseSrc == this.getBasesAmount()-1 )
				||  ( this.holeBase == this.getBasesAmount()-1 && indexBaseSrc == 0 )
				||  ( this.holeBase == indexBaseSrc )
				)
		{
			lastMove  = new BaseballMove(indexBaseSrc, playerLocation, 
					this.holeBase, this.holePos, 
					this.getPlayerColor(indexBaseSrc, playerLocation));
			swap(indexBaseSrc, playerLocation, this.holeBase,this.holePos);
			this.holeBase= indexBaseSrc;
			this.holePos= playerLocation;

		}
		else
		{
			throw new IllegalArgumentException("The player "+playerLocation+" from base "+indexBaseSrc+" can't move to base "+this.holeBase+" since it's a lazy guy and he doesn't want to travel more than one base length at once");
		}
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
