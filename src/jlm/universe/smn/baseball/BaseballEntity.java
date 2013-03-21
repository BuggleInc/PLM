package jlm.universe.smn.baseball;

import jlm.universe.Entity;
import jlm.universe.World;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 */
public class BaseballEntity extends Entity
{
	/**
	 * Must exist too. Calling BaseballEntity("dummy name") is ok
	 * Part of the copy process 
	 * Must call super(name)
	 * @return A new instance of BaseballEntity
	 */
	public BaseballEntity() {
		/* BEGIN HIDDEN */
		super("Baseball Entity");
		/* END HIDDEN */
	}
	
	/**
	 * Part of the copy process 
	 * Must call super(name)
	 * @param name : the name of the entity
	 * @return A new instance of BaseballEntity
	 */
	public BaseballEntity(String name) {
		super(name);
	}
	
	
	/** 
	 * Instantiation Constructor (used by exercises to setup the world) 
	 * Must call super(name, world). If you had fields to setup, you'd  have to add more parameters
	 * @param name : the name of the entity
	 * @param world : a world
	 * @return A new instance of BaseballEntity
	 */
	public BaseballEntity(String name, World world) {
		super(name,world);
	}

	/** 
	 * A copy method needed by the JLM
	 * @return a new PancakeEntity with the same name as the caller
	 */
	@Override
	public Entity copy() {
		return new BaseballEntity(this.name);
	}
	
	/**
	 * Give the number of bases on your field
	 * @return the number of bases on your field
	 */
	public int getAmountOfBases()
	{
		return ((BaseballWorld) this.world).getAmountOfBases();
	}
	
	/**
	 * Return the color of the base located at baseIndex
	 * @param baseIndex the index of the wanted base
	 * @return the color of the player in base baseIndex at position playerLocation
	 * @throws InvalidPositionException if you ask for a base which isn't in the range 0 to amountOfBases-1
	 */
	public int getBaseColor( int baseIndex) throws InvalidPositionException
	{
		return  ((BaseballWorld) world).getBaseColor(baseIndex);
	}
	
	/**
	 * Return the index of the base where is hole is located
	 * @return the index of the base where is hole is located
	 */
	public int getHoleBase() {
		return ((BaseballWorld) this.world).getHoleBase();
	}
	
	/**
	 * Return the position in the base where is hole is located
	 * @return the position in the base where is hole is located
	 */
	public int getHolePositionInBase(){
		return ((BaseballWorld) this.world).getHolePositionInBase();
	}
	
	/**
	 * Return the color of the player in base baseIndex at position playerLocation
	 * @param baseIndex the index of the wanted base
	 * @param playerLocation the location ( 0 or 1 ) of the wanted player
	 * @return the color of the player in base baseIndex at position playerLocation
	 * @throws InvalidPositionException if playerLocation isn't 0 or 1
	 */
	public int getPlayerColor(int baseIndex, int playerLocation) throws InvalidPositionException {
		return ((BaseballWorld) world).getPlayerColor(baseIndex,playerLocation);
	}
	
	/**
	 * Tell if the base at baseIndex is sorted
	 * @param baseIndex the index of the base that we want to check
	 * @return TRUE if the field is okay <br>FALSE else
	 */
	public boolean isBaseSorted( int baseIndex)
	{
		return ((BaseballWorld) this.world).isBaseSorted(baseIndex);
	}
	
	/**
	 * Move the player playerSrc of baseSrc to the hole
	 * @param baseSrc : index of the base you want to pick a player
	 * @param playerSrc : index of the player that you want to pick
	 * @throws InvalidMoveException in case baseSrc is not near the hole 
	 */
	public void move(int baseSrc, int playerSrc) throws InvalidMoveException
	{
		((BaseballWorld) this.world).move(baseSrc,playerSrc);
		stepUI();
	}
	
	/** 
	 * Must exist so that exercises can instantiate the entity (Entity is abstract) 
	 */
	@Override
	public void run() {
	}
	
	/**
	 * Return a string representation of the world
	 * @return A string representation of the world
	 */
	public String toString(){
		return "BaseballEntity (" + this.getClass().getName() + ")";
	}
}
