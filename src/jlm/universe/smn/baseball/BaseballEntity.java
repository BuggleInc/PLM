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
	
	public int getPlayerColor(int baseIndex, int playerLocation) {
		return ((BaseballWorld) world).getPlayerColor(baseIndex,playerLocation);
	}
	
	public int getBaseColor( int baseIndex)
	{
		return  ((BaseballWorld) world).getBaseColor(baseIndex);
	}
	
	/**
	 * Give the index of the hole and the index of the base with the hole
	 * @return  the index of the hole and the index of the base with the hole
	 */
	public int[] getHolePosition()
	{	
		return ((BaseballWorld) world).getHolePosition();
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
	 * Return the index of the base which have only one player on the field
	 * @return the index of the base which has only one player on the field
	 */
	public int indexOfBaseWithOnePlayer()
	{
		return ((BaseballWorld) this.world).indexOfBaseWithOnePlayer();
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
