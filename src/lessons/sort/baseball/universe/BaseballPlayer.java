package lessons.sort.baseball.universe;

public class BaseballPlayer
{
	private int color;

	/**
	 * Builds a player of the passed color
	 * @param color : the color of the player you are creating (0 means no color)
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
