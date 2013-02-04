package jlm.universe.smn.baseball;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @see BaseballBase
 * @see BaseballPlayer
 */

public class BaseballField
{
	/**
	 * Create a new baseball field
	 * @param numberOfBases : number of bases you want in your field
	 * @return a new baseball field
	 */
	public static BaseballField create(int numberOfBases)
	{
		return new BaseballField(numberOfBases);
	}
	private BaseballBase[] bases;
	
	private int holeContainer;
	
	/**
	 * Baseballfield constructor
	 * @param numberOfBases : number of bases in your field
	 */
	private BaseballField(int numberOfBases)
	{
		BaseballPlayer[] players;
		
		this.bases = new BaseballBase[numberOfBases];
		players = new BaseballPlayer[2*numberOfBases];
		
		// creating the colored players
		for (int i = 0 ; i < numberOfBases*2-1 ; i++)
		{
			players[i]= new BaseballPlayer((int)i/2+1);
		}
		
		// creating the invisible player
		players[2*numberOfBases-1]=new BaseballPlayer(0);
		
		// creating the bases
		for (int j = 0 ; j < numberOfBases ; j++)
		{
			this.bases[j]=new BaseballBase(j+1);
		}
		
		int p = 0;
		
		// allocating the players to the bases
		for (int k = 0 ; k < numberOfBases ; k++)
		{
			this.bases[k].setPlayer1(players[p]);
			p++;
			this.bases[k].setPlayer2(players[p]);
			p++;
		}
		
		// initializing holeContainer
		this.holeContainer = numberOfBases-1;
	}
	
	/**
	 * Make a copy of the given object
	 * @return A copy of the given object
	 */
	public BaseballField copy()
	{
		BaseballField field = new BaseballField(this.getAmountOfBases());
		
		for (int i = 0 ; i < this.getAmountOfBases() ; i++)
		{
			field.bases[i].setColor(this.bases[i].getColor());
		}	
		field.setHoleContainer(this.getHoleContainer());
		
		return field;
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
			int numberOfBases = this.getAmountOfBases();
			int numberOfPlayers = this.getAmountOfBases()*2;
			if ( numberOfBases != field.getAmountOfBases())
			{
				s = "The number of bases are different in the two fieldes ( "
						+numberOfBases+" vs "+field.getAmountOfBases()+" )";
			}
			else if ( numberOfPlayers != field.getAmountOfBases()*2)
			{
				s = "The number of players are different in the two fieldes ( "
						+numberOfPlayers+" vs "+field.getAmountOfBases()*2+" )";
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
	
	/**
	 * Indicate whether some other object is "equal to" this one
	 * @return if the two objects are equals
	 * @param field : the reference object with which to compare
	 * @return TRUE if the fields are the same <br>FALSE else
	 */
	public boolean equals(BaseballField field)
	{
		boolean sw=true;
		
		if (this.getAmountOfBases()!=field.getAmountOfBases()
				|| this.bases.length*2!=field.bases.length*2 )
		{
			sw= false;
		}
		else
		{
			for (int i = 0 ; i < this.getAmountOfBases() && sw ; i++)
			{
				sw=this.getBase(i).equals(field.getBase(i));
			}
		}
		return sw;
	}
	
	/**
	 * Give the index of the hole
	 * @return the index of the hole
	 */
	public int findMissingPlayer()
	{		
		if (this.bases[this.getHoleContainer()].getPlayer1().getColor() == 0)
		{
			return 1;
		}
		else
		{
			return 2;
		}
	}
	
	/**
	 * Give the number of bases on your field
	 * @return the number of bases on your field
	 */
	public int getAmountOfBases()
	{
		return this.bases.length;
	}

	/**
	 * Give a base
	 * @param i : the index of the base you want
	 * @return the base with the index i
	 */
	public BaseballBase getBase(int i)
	{
		return this.bases[i];
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
	 * Give a player
	 * @param playerNumber : the index of the player that you want
	 * @param baseIndex : the index of the base wanted
	 * @return the player with the index i
	 */
	public BaseballPlayer getPlayer(int playerNumber, int baseIndex)
	{
		if ( playerNumber == 1 )
		{
			return bases[baseIndex].getPlayer1();
		}
		else
		{
			return bases[baseIndex].getPlayer2();
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
	 * Mix the players between the bases
	 */
	public void mix()
	{
		//mix the players
		do
		{
			for ( int i = 0 ; i < this.bases.length*10 ; i++ )
			{
				swap(i%this.bases.length,(int) (Math.random()*this.bases.length));
			}
		}
		while(this.isSorted());
	  
		// where is the hole now ?
		boolean found = false;
		
		for (int i = 0 ; i <this.bases.length && found == false ; i++)
		{
			if (this.bases[i].getPlayer1().getColor() == 0
					|| this.bases[i].getPlayer2().getColor() == 0)
			{
				this.setHoleContainer(i);
				found = true;
			}
		}
	}
	
	/**
	 * Move a player to the hole
	 * @param base : index of the base where you want to pick a player
	 * @param player : index (1 or 2) of the player in the base you want to move
	 * @param the player player from the base base will be moved to the hole
	 */
	public void move(int baseSrc, int player)
	{
		int [] hole = new int[2];
		
		hole[0] = this.getHoleContainer();
		hole[1] = this.findMissingPlayer();
		
		// must work only if the bases are next to each other
		if(hole[0] == baseSrc+1 || hole[0] == baseSrc-1
				|| (hole[0] == 0 && baseSrc == this.getAmountOfBases()-1)
				|| (hole[0] == this.getAmountOfBases()-1 && baseSrc == 0)) // faire une exception
		{
			if (hole[1] == 1)
			{
				if (player == 1) 
				{
					this.bases[hole[0]].getPlayer1().setColor(this.bases[baseSrc].getPlayer1().getColor());
				}
				else
				{
					this.bases[hole[0]].getPlayer1().setColor(this.bases[baseSrc].getPlayer2().getColor());
				}
			}
			else
			{
				if (player == 1)
				{
					this.bases[hole[0]].getPlayer2().setColor(this.bases[baseSrc].getPlayer1().getColor());
				}
				else
				{
				this.bases[hole[0]].getPlayer2().setColor(this.bases[baseSrc].getPlayer2().getColor());
				}
			}
			if (player == 1) // faire une exception
			{
				this.bases[baseSrc].getPlayer1().setColor(0);
			}
			else if (player == 2)
			{
				this.bases[baseSrc].getPlayer2().setColor(0);
			}
		}
		
		// setting new hole base
		this.setHoleContainer(baseSrc);
	}
	
	/**
	 * Set the index of the base with only one player
	 */
	public void setHoleContainer(int i)
	{
		this.holeContainer = i;
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
				flyingMan=this.bases[firstPlayer].getPlayer1();
				this.bases[firstPlayer].setPlayer1(this.bases[secondPlayer].getPlayer1());
				this.bases[secondPlayer].setPlayer1(flyingMan);
			}
			else
			{
				flyingMan=this.bases[firstPlayer].getPlayer2();
				this.bases[firstPlayer].setPlayer2(this.bases[secondPlayer].getPlayer2());
				this.bases[secondPlayer].setPlayer2(flyingMan);
			}
	}
	
	/**
	 * Return a string representation of the field
	 * @return A string representation of the field
	 */
	public String toString()
	{
		String s = "";
		
		for (int i = 0 ; i < this.bases.length ; i++)
		{
			s+="- Base "+i+"\n";
			s+="  Color : "+this.bases[i].getColor()+"\n";
			s+="  Player 1 : "+this.bases[i].getPlayer1().getColor()+"\n";
			s+="  Player 2 : "+this.bases[i].getPlayer2().getColor()+"\n";
		}
		
		return s;
	}
}
