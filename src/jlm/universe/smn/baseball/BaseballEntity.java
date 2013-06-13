package jlm.universe.smn.baseball;

import jlm.universe.Entity;
import jlm.universe.World;

/**
 * @see BaseballWorld
 */
public class BaseballEntity extends Entity {
	public BaseballEntity() {
		super("Baseball Entity");
	}
	
	public BaseballEntity(String name) {
		super(name);
	}
	
	
	public BaseballEntity(String name, World world) {
		super(name,world);
	}

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
	
	/** Returns the index of the base where is hole is located */
	public int getHoleBase() {
		return ((BaseballWorld) this.world).getHoleBase();
	}
	
	/** Returns the position in the base where is hole is located */
	public int getHolePositionInBase(){
		return ((BaseballWorld) this.world).getHolePositionInBase();
	}
	
	/** Returns the amount of players locations available on each base of the field */
	public int getLocationsAmount() {
		return ((BaseballWorld) this.world).getLocationsAmount();
	}
	
	/**
	 * Returns the color of the player in base baseIndex at position playerLocation
	 * @param baseIndex the index of the wanted base
	 * @param playerLocation the location ( 0 or 1 ) of the wanted player
	 * @return the color of the player in base baseIndex at position playerLocation
	 * @throws InvalidPositionException if playerLocation isn't 0 or 1
	 */
	public int getPlayerColor(int baseIndex, int playerLocation) throws InvalidPositionException {
		return ((BaseballWorld) this.world).getPlayerColor(baseIndex,playerLocation);
	}
	
	/** Returns whether the base at baseIndex is sorted or not */
	public boolean isBaseSorted(int baseIndex) {
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
