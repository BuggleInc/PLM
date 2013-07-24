package lessons.sort.baseball.universe;

/**
 * @see BaseballPlayer
 */

public class BaseballBase {
	
	private BaseballPlayer[] players; // the players on the base
	private int color; // the color of the base
	
	/**
	 * BaseballBase constructor
	 * @param color the color of the base you are creating
	 * @param playerLocationsAmount the amount of player locations available on the base
	 */
	public BaseballBase(int color,int playerLocationsAmount) {
		this.players=new BaseballPlayer[playerLocationsAmount];
		for ( int i = 0 ; i < playerLocationsAmount ; i++ )
			this.players[i] = new BaseballPlayer(color);
		
		this.color=color;
	}
	

	public boolean equals(Object other) {
		if (other == null || !(other instanceof BaseballBase) )
			return false;
		
		BaseballBase otherBase = (BaseballBase) other;
		if (otherBase.getLocationsAmount() != getLocationsAmount())
			return false;
		
		for (int i=0; i<getLocationsAmount(); i++)
			if (!otherBase.getPlayer(i).equals(getPlayer(i)))
				return false;
		
		return true;
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
	 * Returns the wanted player of this base
	 * @param player the index of the wanted player
	 */
	public BaseballPlayer getPlayer(int player) {
		return this.players[player];
	}


	/**
	 * Return the color of the player in base baseIndex at position playerLocation
	 * @param playerLocation the location ( between 0 and getLocationsAmount()-1 ) of the wanted player
	 * @return the color of the player in base baseIndex at position playerLocation
	 */
	public int getPlayerColor(int playerLocation)  {
		if ( playerLocation < 0 || playerLocation > this.getLocationsAmount()-1 )
		{
			throw new IllegalArgumentException("The position of a player must be between 0 and getLocationsAmount()-1.\nIt's sad but "+playerLocation+" isn't one of these values !");
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
