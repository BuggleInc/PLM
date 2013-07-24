package lessons.sort.baseball.universe;

import jlm.core.model.Game;

public class BaseballBase {

	private int[] players;  // the players on the base
	private int color;      // the color of the base
	private boolean isLast; // Whether we should contain the hole once everything is sorted

	/**
	 * BaseballBase constructor
	 * @param color the color of the base you are creating
	 * @param playerLocationsAmount the amount of player locations available on the base
	 */
	public BaseballBase(int color,int playerLocationsAmount, boolean isLast) {
		this.players=new int[playerLocationsAmount];
		for ( int i = 0 ; i < playerLocationsAmount ; i++ )
			this.players[i] = color;

		this.color=color;
		this.isLast = isLast;
	}


	public boolean equals(Object other) {
		if (other == null || !(other instanceof BaseballBase) )
			return false;

		BaseballBase otherBase = (BaseballBase) other;
		if (otherBase.getPositionsAmount() != getPositionsAmount())
			return false;

		for (int i=0; i<getPositionsAmount(); i++)
			if (otherBase.getPlayerColor(i) != getPlayerColor(i))
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

	/** Returns the amount of players locations available on the base */
	public int getPositionsAmount(){
		return this.players.length;
	}

	/**
	 * Returns the color of the player in this base at the specified location
	 * @param location the location ( between 0 and getLocationsAmount()-1 ) of the wanted player
	 */
	public int getPlayerColor(int location)  {
		if ( location < 0 || location > this.getPositionsAmount()-1 )
			throw new IllegalArgumentException(Game.i18n.tr("Cannot retrieve the color of player {0} as it is not a valid location (between 0 and {1})",location,getPositionsAmount()));

		return this.players[location];
	}


	/**
	 * Place the given baseballPlayer at the position player in the base
	 * @param baseballPlayer : the player that you want to place
	 * @param position : the position where you want to place the player
	 */
	public void setPlayerColor(int position, int baseballPlayer) {
		this.players[position] = baseballPlayer;
	}


	/**
	 * Return a string representation of the base
	 * @return A string representation of the base
	 */
	public String toString()
	{
		int n = getPositionsAmount();
		String s = "";
		for ( int i = 0 ; i < n ; i++)
			s+="Player "+i+" : "+this.getPlayerColor(i)+"\n" ;

		return s;
	}

	/** Tells if everyone is at home on the specified base */
	public boolean isSorted() {
		boolean sw = true;
		int n = this.getPositionsAmount();
		if (isLast) {
			for ( int pos = 0 ; pos < n && sw; pos++) 
				if (getPlayerColor(pos) != 0 && getPlayerColor(pos) != getColor())
					return false;
		} else {
			for ( int pos = 0 ; pos < n && sw; pos++) 
				if (getPlayerColor(pos) != getColor())
					return false;
		}

		return true;
	}

}
