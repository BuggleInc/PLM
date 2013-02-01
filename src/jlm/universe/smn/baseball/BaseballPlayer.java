package jlm.universe.smn.baseball;


/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 */

public class BaseballPlayer
{
	private int color;

	/**
	 * BaseballPlayer constructor
	 * @param color : the color of the player you are creating
	 * @param 0 is for no color
	 */
	public BaseballPlayer(int color)
	{
		this.color=color;
	}
	
	/**
	 * Getters and setters
	 */
	
	public void setColor(int color)
	{
		this.color=color;
	}
	
	public int getColor()
	{
		return this.color;
	}
}
