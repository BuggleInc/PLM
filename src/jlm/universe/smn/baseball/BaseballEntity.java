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
	
	/** Must exist so that exercises can instantiate the entity (Entity is abstract) 
	 */
	@Override
	public void run(){
	}
	
	/**
	 * Return a string representation of the world
	 * @return A string representation of the world
	 */
	public String toString(){
		return "BaseballEntity (" + this.getClass().getName() + ")";
	}
	
	/**
	 * Move a player to the hole
	 * @param base : index of the base you want to pick a player
	 * @param player : index (1 or 2) of the player in the base you want to move
	 * @param the player player from the base base will be moved to the hole
	 */
	public void move(int base, int player)
	{
		((BaseballWorld) world).move(base,player);
		stepUI();
	}
	
	/**
	 * Give the index of the base with only one player
	 * @return the index of the base with only one player
	 */
	public int getHoleContainer()
	{
		return ((BaseballWorld) world).getHoleContainer();
	}
	
	/**
	 * Give the index of the hole
	 * @return the index of the hole
	 */
	public int findMissingPlayer()
	{	
		return ((BaseballWorld) world).findMissingPlayer();
	}
}
