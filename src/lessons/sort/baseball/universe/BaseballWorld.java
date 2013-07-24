package jlm.universe.smn.baseball;

import javax.script.ScriptEngine;

import jlm.core.model.ProgrammingLanguage;
import jlm.core.ui.WorldView;
import jlm.universe.EntityControlPanel;
import jlm.universe.World;

/**
 * @see BaseballField
 */

public class BaseballWorld extends World
{
	protected BaseballField field; // the field of the game
	
	/** Copy constructor used internally by JLM */
	public BaseballWorld(BaseballWorld world) {
		super(world);
	}
	
	/**
	 * Returns the panel which let the user to interact dynamically with the world
	 */
	@Override
	public EntityControlPanel getEntityControlPanel() {
		return new BaseballMovePanel();
	}
	
	/** Regular constructor used by exercises */
	public BaseballWorld(String name, int numberOfBases, int playerLocationAmount) {
		super(name);
		setDelay(200); // Delay (in milliseconds) in default animations
		this.field = BaseballField.create(numberOfBases,playerLocationAmount);
	}

	/**
	 * Makes a textual description of the differences between the caller and world
	 * @param world : the world with which you want to compare your world
	 * @return A textual description of the differences between the caller and world
	 */
	public String diffTo(World world)
	{
		String s ;
		if (world == null || !(world instanceof BaseballWorld))
		{
			s="This is not a baseball world ='( ..."; // it's truly sad =(
		}
		else
		{
			BaseballWorld other = (BaseballWorld) world;
			s = this.field.diffTo(other.field);
		}
		return s;
	}
	
	public boolean equals(Object o) {
		boolean sw = true;
		if (o == null || !(o instanceof BaseballWorld))
		{
			sw=false;
		}
		else
		{
			BaseballWorld other = (BaseballWorld) o;
			sw = this.field.equals(other.field);
		}
		return sw;
	}
	
	/** Returns the number of bases on your field */
	public int getAmountOfBases() {
		return this.field.getAmountOfBases();
	}
	
	/** Returns the color of the base located at baseIndex
	 * @throws InvalidPositionException if you ask for a base which isn't in the range 0 to amountOfBases-1
	 */
	public int getBaseColor(int baseIndex) throws InvalidPositionException {
		return this.field.getBaseColor(baseIndex);
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
	
	/**
	 * Return the index of the base where is hole is located
	 * @return the index of the base where is hole is located
	 */
	public int getHoleBase() {
		return this.field.getHoleBase();
	}
	
	/**
	 * Return the position in the base where is hole is located
	 * @return the position in the base where is hole is located
	 */
	public int getHolePositionInBase(){
		return this.field.getHolePositionInBase();
	}
	
	/**
	 * Give the amount of players locations available on each base of the field
	 * @return The amount of players locations available on each base of the field
	 */
	public int getLocationsAmount() {
		return this.field.getLocationsAmount();
	}
	
	/**
	 * Return the last move made on the field
	 * @return the last move made on the field
	 */
	public BaseballMove getLastMove() {
		return this.field.getLastMove();
	}
	
	/**
	 * Return the color of the player in base baseIndex at position playerLocation
	 * @param baseIndex the index of the wanted base
	 * @param playerLocation the location ( between 0 and getLocationsAmount()-1 ) of the wanted player
	 * @return the color of the player in base baseIndex at position playerLocation
	 * @throws InvalidPositionException if playerLocation isn't 0 or 1
	 */
	public int getPlayerColor(int baseIndex, int playerLocation) throws InvalidPositionException {
		return this.field.getPlayerColor(baseIndex,playerLocation);
	}
	
	/** 
	 * Return a component able of displaying the world
	 * @return a component able of displaying the world
	 */
	public WorldView getView()
	{
		return new BaseballWorldView(this);
	}
	
	/**
	 * Tell if everyone is at home
	 * @param baseIndex the index of the base that we want to check
	 * @return TRUE if the field is okay <br>FALSE else
	 */
	public boolean isBaseSorted( int baseIndex)
	{
		return this.field.isBaseSorted(baseIndex);
	}	
	
	/**
	 * Move the player playerSrc of baseSrc to the hole
	 * @param baseSrc : index of the base you want to pick a player
	 * @param PlayerSrc : index of the base you want to pick a player
	 * @throws InvalidMoveException in case baseSrc is not near the hole 
	 */
	public void move(int baseSrc, int playerSrc) throws InvalidMoveException
	{
		this.field.move(baseSrc, playerSrc);
	}

	/** 
	 * Reset the state of the current world to the one passed in argument
	 * @param the world which must be the new start of your current world
	 */
	public void reset(World world)
	{
		BaseballWorld other = (BaseballWorld) world;
		this.field = other.field.copy();
		super.reset(world);		
	}

	/**
	 * Return a string representation of the world
	 * @return A string representation of the world
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("BaseballWorld "+getName()+":\n");
		sb.append(this.field.toString());
		return sb.toString();
	}

}
