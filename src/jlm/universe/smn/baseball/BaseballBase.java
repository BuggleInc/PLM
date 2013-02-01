package jlm.universe.smn.baseball;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @see BaseballPlayer
 */

public class BaseballBase
{
	private BaseballPlayer player1;
	private BaseballPlayer player2;
	private int color;
	
	/**
	 * BaseballBase constructor
	 * @param color : the color of the base you are creating
	 */
	public BaseballBase(int color)
	{
		this.player1=new BaseballPlayer(0);
		this.player2=new BaseballPlayer(0);
		this.color=color;
	}
	
	/**
	 * Getters and setters
	 */
	
	public void setPlayer1(BaseballPlayer player)
	{
		this.player1=player;
	}
	
	public void setPlayer2(BaseballPlayer player)
	{
		this.player2=player;
	}
	
	public void setColor(int color)
	{
		this.color=color;
	}
	
	public BaseballPlayer getPlayer1()
	{
		return this.player1;
	}
	
	public BaseballPlayer getPlayer2()
	{
		return this.player2;
	}
	
	public int getColor()
	{
		return this.color;
	}
	
	/**
	 * Indicate whether some other object is "equal to" this one
	 * @return if the two objects are equals
	 * @param base : the reference object with which to compare
	 * @return TRUE if the bases are the same <br>FALSE else
	 */
	public boolean equals(BaseballBase base)
	{
		boolean bool=true;
		
		if (this.getPlayer1().getColor() != base.getPlayer1().getColor()
				|| this.getPlayer2().getColor() != base.getPlayer2().getColor()
				|| this.getColor() != base.getColor())
			bool = false;
		
		return bool;
	}
	
	/**
	 * Return a string representation of the base
	 * @return A string representation of the base
	 */
	public String toString()
	{
		String s = "";
		
		s+="Player 1 : "+this.getPlayer1().getColor()+" ; Player 2 : "+this.getPlayer2().getColor();
		
		return s;
	}
}
