package jlm.universe.smn.baseball;

import java.util.ArrayList;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @see BaseballBase
 * @see BaseballPlayer
 */

public class BaseballField
{
	private ArrayList <BaseballBase> bases;
	private ArrayList <BaseballPlayer> players;
	private int holeContainer;
	
	/**
	 * Baseballfield constructor
	 * @param numberOfBases : number of bases in your field
	 */
	private BaseballField(int numberOfBases)
	{
		this.bases = new ArrayList<BaseballBase>();
		this.players = new ArrayList<BaseballPlayer>();
		
		// creating the colored players
		for (int i = 0 ; i < numberOfBases*2-1 ; i++)
			players.add(new BaseballPlayer((int)i/2+1));
		
		// creating the invisible player
		players.add(new BaseballPlayer(0));
		
		// creating the bases
		for (int j = 0 ; j < numberOfBases ; j++)
			this.bases.add(new BaseballBase(j+1));
		
		int p = 0;
		
		// allocating the players to the bases
		for (int k = 0 ; k < numberOfBases ; k++)
		{
			this.bases.get(k).setPlayer1(this.players.get(p));
			p++;
			this.bases.get(k).setPlayer2(this.players.get(p));
			p++;
		}
		
		// initializing holeContainer
		this.holeContainer = numberOfBases-1;
	}
	
	/**
	 * Give the index of the base with only one player
	 * @return the index of the base with only one player
	 */
	public int getHoleContainer()
	{
		return holeContainer;
	}
	
	/**
	 * Set the index of the base with only one player
	 */
	public void setHoleContainer(int i)
	{
		this.holeContainer = i;
	}
	
	/**
	 * Give a player
	 * @param i : the index of the payer you want
	 * @return the player with the index i
	 */
	public BaseballPlayer getPlayer(int i)
	{
		return players.get(i);
	}
	
	/**
	 * Give a base
	 * @param i : the index of the base you want
	 * @return the base with the index i
	 */
	public BaseballBase getBase(int i)
	{
		return this.bases.get(i);
	}
	
	/**
	 * Give the number of players on your field
	 * @return the number of players on your field
	 */
	public int getNumberOfPlayers()
	{
		return this.players.size();
	}
	
	/**
	 * Give the number of bases on your field
	 * @return the number of bases on your field
	 */
	public int getNumberOfBases()
	{
		return this.bases.size();
	}

	/**
	 * Create a new baseball field
	 * @param numberOfBases : number of bases you want in your field
	 * @return a new baseball field
	 */
	public static BaseballField create(int numberOfBases)
	{
		return new BaseballField(numberOfBases);
	}

	/**
	 * Make a copy of the given object
	 * @return A copy of the given object
	 */
	public BaseballField copy()
	{
		BaseballField field = new BaseballField(this.getNumberOfBases());
		
		for (int i = 0 ; i < this.getNumberOfBases() ; i++)
			field.bases.get(i).setColor(this.bases.get(i).getColor());
			
		for (int j = 0 ; j < this.getNumberOfPlayers() ; j++)
			field.players.get(j).setColor(this.players.get(j).getColor());
		
		field.setHoleContainer(this.getHoleContainer());
		
		return field;
	}
	
	/**
	 * Mix the players between the bases
	 */
	public void mix()
	{
		//mix the players
		int n = this.bases.size();
		do
		{
			for ( int i = 0 ; i < n*10 ; i++ )
				swap(i%n,(int) (Math.random()*n));
		}
		while(this.isSorted());
	  
		// where is the hole now ?
		boolean found = false;
		
		for (int i = 0 ; i < this.bases.size() && found == false ; i++)
		{
			if (this.bases.get(i).getPlayer1().getColor() == 0
					|| this.bases.get(i).getPlayer2().getColor() == 0)
			{
				this.setHoleContainer(i);
				
				found = true;
			}
		}
	}
	 
	/**
	 * Swap two players
	 * @param firstPlayer one of the players you want to swap
	 * @param secondPlayer the other player you want to swap
	 */
	private void swap(int firstPlayer , int secondPlayer ) 
	{
		BaseballPlayer flyingMan = null;
		if ( Math.random()<0.5)
			if ( Math.random()<0.5)
			{
				flyingMan=this.bases.get(firstPlayer).getPlayer1();
				this.bases.get(firstPlayer).setPlayer1(this.bases.get(secondPlayer).getPlayer1());
				this.bases.get(secondPlayer).setPlayer1(flyingMan);
			}
			else
			{
				flyingMan=this.bases.get(firstPlayer).getPlayer2();
				this.bases.get(firstPlayer).setPlayer2(this.bases.get(secondPlayer).getPlayer2());
				this.bases.get(secondPlayer).setPlayer2(flyingMan);
			}
	}
	
	/**
	 * Tell whether the players are on their own bases or not
	 * @return whether the players are on their own bases or not
	 * @return TRUE if the field is sorted <br>FALSE else
	 */
	public boolean isSorted()
	{
	boolean sw = true;
	boolean sw1 = false;
	boolean sw2 = false;
	
	for ( BaseballBase b : this.bases)
	{	  
		if ( b.getPlayer1().getColor() != 0)
			sw1 = b.getPlayer1().getColor() == b.getColor();
		if ( b.getPlayer2().getColor() != 0)
			sw2 = b.getPlayer2().getColor() == b.getColor();
		
		sw = sw1&&sw2;
		if(!sw)
	    return sw;
	}
	  return sw;
	}

	/**
	 * Give the index of the hole
	 * @return the index of the hole
	 */
	public int findMissingPlayer()
	{		
		if (this.bases.get(this.getHoleContainer()).getPlayer1().getColor() == 0)
		{
			return 1;
		}
		else
		{
			return 2;
		}
	}
	
	/**
	 * Move a player to the hole
	 * @param base : index of the base you want to pick a player
	 * @param player : index (1 or 2) of the player in the base you want to move
	 * @param the player player from the base base will be moved to the hole
	 */
	public void move(int base, int player)
	{
		int [] hole = new int[2];
		
		hole[0] = this.getHoleContainer();
		hole[1] = this.findMissingPlayer();
		
		// must work only if the bases are next to each other
		if(hole[0] == base+1 || hole[0] == base-1
				|| (hole[0] == 0 && base == this.getNumberOfBases()-1)
				|| (hole[0] == this.getNumberOfBases()-1 && base == 0)) // faire une exception
		{
			if (hole[1] == 1)
			{
				if (player == 1) 
					this.bases.get(hole[0]).getPlayer1().setColor(this.bases.get(base).getPlayer1().getColor());
				else
					this.bases.get(hole[0]).getPlayer1().setColor(this.bases.get(base).getPlayer2().getColor());
			}
			else
			{
				if (player == 1)
					this.bases.get(hole[0]).getPlayer2().setColor(this.bases.get(base).getPlayer1().getColor());
				else
					this.bases.get(hole[0]).getPlayer2().setColor(this.bases.get(base).getPlayer2().getColor());
			}
			if (player == 1) // faire une exception
				this.bases.get(base).getPlayer1().setColor(0);
			
			else if (player == 2)
				this.bases.get(base).getPlayer2().setColor(0);
		}
		
		// setting new hole base
		this.setHoleContainer(base);
	}
	
	/**
	 * Return a string representation of the field
	 * @return A string representation of the field
	 */
	public String toString()
	{
		String s = "";
		
		for (int i = 0 ; i < this.bases.size() ; i++)
		{
			s+="- Base "+i+"\n";
			s+="  Color : "+this.bases.get(i).getColor()+"\n";
			s+="  Player 1 : "+this.bases.get(i).getPlayer1().getColor()+"\n";
			s+="  Player 2 : "+this.bases.get(i).getPlayer2().getColor()+"\n";
		}
		
		return s;
	}
	
	/**
	 * Indicate whether some other object is "equal to" this one
	 * @return if the two objects are equals
	 * @param field : the reference object with which to compare
	 * @return TRUE if the fieldes are the same <br>FALSE else
	 */
	public boolean equals(BaseballField field)
	{
		boolean bool=true;
		
		if (this.getNumberOfBases()!=field.getNumberOfBases()
				|| this.getNumberOfPlayers()!=field.getNumberOfPlayers())
			return false;
		
		else
		{
			for (int i = 0 ; i < this.getNumberOfBases() && bool==true ; i++)
				bool=this.getBase(i).equals(field.getBase(i));
		}
		return bool;
	}
	
	/**
	 * Make a textual description of the differences between the caller and field
	 * @param field : the field with which you want to compare your current field 
	 * @return A textual description of the differences between the caller and field
	 */
	public String diffTo(BaseballField field) {
		String s="These two fieldes are identical";
		if ( !this.equals(field))
		{
			int numberOfBases = this.getNumberOfBases();
			int numberOfPlayers = this.getNumberOfPlayers();
			if ( numberOfBases != field.getNumberOfBases())
			{
				s = "The number of bases are different in the two fieldes ( "
						+numberOfBases+" vs "+field.getNumberOfBases()+" )";
			}
			else if ( numberOfPlayers != field.getNumberOfPlayers())
			{
				s = "The number of players are different in the two fieldes ( "
						+numberOfPlayers+" vs "+field.getNumberOfPlayers()+" )";
			}
			else
			{
				s="";
				for ( int i = 0 ; i < numberOfBases ; i++)
				{
					if ( !this.getBase(i).equals(field.getBase(i)))
					{
						s+=" Base number "+(i+1)+" : "+this.getBase(i).toString()
								+" vs "+field.getBase(i).toString();
						s+="\n";
					}
				}
			}
		}
		return s;
	}
}
