package jlm.universe.smn.baseball;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 */
public class BaseballPlayer
{
	private int color;

	/**
	 * BaseballPlayer constructor
	 * @param color : the color of the player you are creating<br>0 is for no color
	 * @return a new BaseballPlayer of the wanted color
	 */
	public BaseballPlayer(int color)
	{
		this.color=color;
	}
	
	/**
	 * Give the color of the player
	 * @return the color of the player
	 */
	public int getColor()
	{
		return this.color;
	}
	
	/**
	 * Set the player's color at color
	 * @param color : the wanted color
	 */
	public void setColor(int color)
	{
		this.color=color;
	}
}
