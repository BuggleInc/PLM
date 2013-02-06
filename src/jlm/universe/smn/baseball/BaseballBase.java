package jlm.universe.smn.baseball;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @see BaseballPlayer
 */

public class BaseballBase
{
	private BaseballPlayer[] players; // the players on the base
	private int color; // the color of the base
	
	/**
	 * BaseballBase constructor
	 * @param color : the color of the base you are creating
	 */
	public BaseballBase(int color)
	{
		this.players=new BaseballPlayer[2];
		this.players[0] = new BaseballPlayer(color);
		this.players[1] = new BaseballPlayer(color);
		this.color=color;
	}
	
	/**
	 * Give the player one of the base
	 * @return The player one
	 */
	public BaseballPlayer getPlayerOne()
	{
		return this.players[0];
	}
	
	/**
	 * Give the player two of the base
	 * @return The player two
	 */
	public BaseballPlayer getPlayerTwo()
	{
		return this.players[1];
	}
	
	/**
	 * Give the color ( in integer ) of the base
	 * @return The integer corresponding to the color of the base
	 */
	public int getColor()
	{
		return this.color;
	}
	
	/**
	 * Indicate whether some other object is "equal to" this one
	 * @return if the two objects are equals
	 * @param o the reference object with which to compare
	 * @return TRUE if the bases are the same <br>FALSE else
	 */
	public boolean equals(Object o)
	{
		boolean sw=true;
		if (o == null || !(o instanceof BaseballBase) )
		{
			sw = false ;
		}
		else 
		{
			BaseballBase base = (BaseballBase) o;
			if (this.getPlayerOne().getColor() != base.getPlayerOne().getColor()
				|| this.getPlayerTwo().getColor() != base.getPlayerTwo().getColor()
				|| this.getColor() != base.getColor())
			{		
				sw = false;
			}
		}
		return sw;
	}
	
	/**
	 * Return a string representation of the base
	 * @return A string representation of the base
	 */
	public String toString()
	{
		String s = "";
		
		s+="Player 1 : "+this.getPlayerOne().getColor()+" ; Player 2 : "+this.getPlayerTwo().getColor();
		
		return s;
	}

	/**
	 * Place the given player at the first position in the base
	 * @param baseballPlayer : the player that you want to place
	 */
	public void setPlayerOne(BaseballPlayer baseballPlayer) {
		this.players[0]=baseballPlayer;
	}

	/**
	 * Place the given player at the second position in the base
	 * @param baseballPlayer : the player that you want to place
	 */
	public void setPlayerTwo(BaseballPlayer baseballPlayer) {
		this.players[1]=baseballPlayer;
	}

}
