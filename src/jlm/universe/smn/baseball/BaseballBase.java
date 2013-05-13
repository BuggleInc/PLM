package jlm.universe.smn.baseball;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @see BaseballPlayer
 */

public class BaseballBase {
	
	private BaseballPlayer[] players; // the players on the base
	private int color; // the color of the base
	
	/**
	 * BaseballBase constructor
	 * @param color : the color of the base you are creating
	 * @param playerLocationAmount the amount of player locations available on the base
	 */
	public BaseballBase(int color,int playerLocationsAmount) {
		this.players=new BaseballPlayer[playerLocationsAmount];
		for ( int i = 0 ; i < playerLocationsAmount ; i++ )
		{
			this.players[i] = new BaseballPlayer(color);
		}
		this.color=color;
	}
	

	/**
	 * Indicate whether some other object is "equal to" this one
	 * @return if the two objects are equals
	 * @param o the reference object with which to compare
	 * @return TRUE if the bases are the same <br>FALSE else
	 */
	public boolean equals(Object o) {
		boolean sw=true;
		if (o == null || !(o instanceof BaseballBase) )
		{
			sw = false ;
		}
		else 
		{
			BaseballBase base = (BaseballBase) o;
			int n = this.getLocationsAmount();
			if (base.getLocationsAmount() == n )
			{		
				for ( int i = 0 ; i < n && sw ; i++)
				{
					sw = base.getPlayer(i).equals(this.getPlayer(i));
				}
			}
			else
			{
				sw = false;
			}
		}
		return sw;
	}
	
	/**
	 * Give the color ( in integer ) of the base
	 * @return The integer corresponding to the color of the base
	 */
	public int getColor() {
		return this.color;
	}
	
	/**
	 * Give the amount of players locations available on the base
	 * @return The amount of players locations available on the base
	 */
	public int getLocationsAmount(){
		return this.players.length;
	}
	
	/**
	 * Give the wanted player of the base
	 * @return The wanted player
	 * @param the index of the wanted player
	 */
	public BaseballPlayer getPlayer(int player) {
		return this.players[player];
	}


	/**
	 * Return the color of the player in base baseIndex at position playerLocation
	 * @param baseIndex the index of the wanted base
	 * @param playerLocation the location ( between 0 and getLocationsAmount()-1 ) of the wanted player
	 * @return the color of the player in base baseIndex at position playerLocation
	 * @throws InvalidPositionException if playerLocation isn't 0 or 1
	 */
	public int getPlayerColor(int playerLocation) throws InvalidPositionException {
		if ( playerLocation < 0 || playerLocation > this.getLocationsAmount()-1 )
		{
			throw new InvalidPositionException("The position of a player must be between 0 and getLocationsAmount()-1.\nIt's sad but "+playerLocation+" isn't one of these values !");
		}
		return this.players[playerLocation].getColor();
	}


	/**
	 * Place the given baseballPlayer at the position player in the base
	 * @param baseballPlayer : the player that you want to place
	 * @param position : the position where you want to place the player
	 */
	public void setPlayer(int position, BaseballPlayer baseballPlayer) {
		this.players[position] = baseballPlayer;
	}


	/**
	 * Return a string representation of the base
	 * @return A string representation of the base
	 */
	public String toString()
	{
		int n = getLocationsAmount();
		String s = "";
		for ( int i = 0 ; i < n ; i++)
		{
			s+="Player "+i+" : "+this.getPlayer(i).getColor()+"\n" ;
		}
		return s;
	}

}
