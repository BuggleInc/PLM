package jlm.universe.smn.baseball;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @see BaseballBase
 * @see BaseballPlayer
 */
public class BaseballField {

	/**
	 * Create a new baseball field
	 * @param numberOfBases : number of bases you want in your field
	 * @return a new baseball field
	 */
	public static BaseballField create(int numberOfBases)
	{
		BaseballField field = new BaseballField(numberOfBases);
		field.mix();
		field.setHoleContainer(field.findMissingPlayer());
		return field;
	}
	
	private BaseballBase[] bases; // the bases which composed the field

	private int[] holeContainer; // the current location of the hole

	/**
	 * BaseballField constructor
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
		
		this.bases[numberOfBases-1].setPlayerOne(new BaseballPlayer(0));
		
		// initializing holeContainer
		this.holeContainer = new int[2];
		this.holeContainer[0] = numberOfBases-1;
		this.holeContainer = this.findMissingPlayer();
	}
	
	/**
	 * Make a copy of the given object
	 * @return A copy of the given object
	 */
	public BaseballField copy() {
		BaseballField newField = new BaseballField(this.bases.length);
		newField.holeContainer = this.holeContainer;
		for ( int i = 0 ; i < this.bases.length ; i++ )
		{
			newField.bases[i].setPlayerOne(this.bases[i].getPlayerOne());
			newField.bases[i].setPlayerTwo(this.bases[i].getPlayerTwo());
		}
		return newField;
	}
	
	/**
	 * Make a textual description of the differences between the caller and field
	 * @param field : the field with which you want to compare your current field 
	 * @return A textual description of the differences between the caller and field
	 */
	public String diffTo(BaseballField field) {
		String s="These two fields are identical";
		if ( !this.equals(field))
		{
			int numberOfBases = this.getAmountOfBases();
			int numberOfPlayers = this.getAmountOfBases()*2;
			if ( numberOfBases != field.getAmountOfBases())
			{
				s = "The number of bases are different in the two fields ( "
						+numberOfBases+" vs "+field.getAmountOfBases()+" )";
			}
			else if ( numberOfPlayers != field.getAmountOfBases()*2)
			{
				s = "The number of players are different in the two fields ( "
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
	 * @return If the two objects are equals
	 * @param Object o: the reference object with which to compare
	 */
	public boolean equals(Object o) {
		boolean sw=true;
		if (o == null || !(o instanceof BaseballField) )
		{
			sw = false ;
		}
		else
		{
			BaseballField other = (BaseballField) o;
			if ( this.holeContainer == other.holeContainer && this.bases.length == other.bases.length)
			{
				for ( int i = 0 ; i< this.bases.length && sw ;i++ )
				{
					sw = this.bases[i].equals(other.bases[i]);
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
	 * Give the index of the hole and the index of the base with the hole
	 * @return  the index of the hole and the index of the base with the hole
	 */
	public int[] findMissingPlayer() {
		int hole[] = new int[2];
		hole[0] = this.holeContainer[0];
		if ( this.bases[this.holeContainer[0]].getPlayerOne().getColor()==0)
		{
			hole[1]=1;
		}
		else
		{
			hole[1]=2;
		}
		return hole;
	}
	
	/**
	 * Give the number of bases on your field
	 * @return the number of bases on your field
	 */
	public int getAmountOfBases() {
		return this.bases.length;
	}

	/**
	 * Give a specific base of the field
	 * @return the wanted base
	 */
	public BaseballBase getBase(int i) {
		return this.bases[i];
	}

	/**
	 * Return the index of the base which have only one player on the field
	 * @return the index of the base which has only one player on the field
	 */
	public int indexOfBaseWithOnePlayer()
	{
		return this.bases.length-1;
	}
	
	/**
	 * Tell if every player of the base is on the base
	 * @return if every players of the base is on the base
	 * @param baseIndex the index of the base that you want to check
	 */
	public boolean isBaseSorted(int baseIndex) {
		boolean sw = true;
		if ( baseIndex == this.bases.length-1 )		// it's the base with only one player
		{
			sw = ( this.bases[baseIndex].getPlayerOne().getColor() == 0 
					&& this.bases[baseIndex].getPlayerTwo().getColor() == this.bases[baseIndex].getColor())
				||( this.bases[baseIndex].getPlayerTwo().getColor() == 0 
						&& this.bases[baseIndex].getPlayerOne().getColor() == this.bases[baseIndex].getColor()) ;
		}
		else	// it's a regular base
		{
			sw = ( this.bases[baseIndex].getPlayerOne().getColor() == this.bases[baseIndex].getColor() 
					&& this.bases[baseIndex].getPlayerTwo().getColor() == this.bases[baseIndex].getColor())
				;
		}
		return sw;
	}

	/*
	 * Mix the players between the different bases
	 */
	private void mix() {
		do
		{		
			for (int i = 0 ; i < this.bases.length;i++)
			{
				this.swap(this.bases[i],this.bases[(int) (Math.random()*this.bases.length)]);
			}
		}
		while(this.isSorted());
	}

	/**
	 * Tell if every player of every base is on the right base
	 * @return if every player of every base is on the right base
	 */
	private boolean isSorted() {
		boolean sw = true;
		for ( int i = 0 ; i < this.bases.length && sw ; i++ )
		{
			sw = this.isBaseSorted(i);
		}
		return sw;
	}

	/**
	 * Move a player to the hole
	 * @param base : index of the base where you want to pick a player
	 * @param player : index (1 or 2) of the player in the base you want to move
	 * @param the player player from the base base will be moved to the hole
	 * @throws InvalidMoveException in case baseSrc is not near the hole
	 */
	public void move(int baseSrc, int player) throws InvalidMoveException
	{
		int [] hole = this.findMissingPlayer();;
		
		// must work only if the bases are next to each other
		if(hole[0] == baseSrc+1 || hole[0] == baseSrc-1
				|| (hole[0] == 0 && baseSrc == this.getAmountOfBases()-1)
				|| (hole[0] == this.getAmountOfBases()-1 && baseSrc == 0)) // faire une exception
		{
			if (hole[1] == 1)
			{
				if (player == 1) 
				{
					this.bases[hole[0]].getPlayerOne().setColor(this.bases[baseSrc].getPlayerOne().getColor());
				}
				else
				{
					this.bases[hole[0]].getPlayerOne().setColor(this.bases[baseSrc].getPlayerTwo().getColor());
				}
			}
			else
			{
				if (player == 1)
				{
					this.bases[hole[0]].getPlayerTwo().setColor(this.bases[baseSrc].getPlayerOne().getColor());
				}
				else
				{
				this.bases[hole[0]].getPlayerTwo().setColor(this.bases[baseSrc].getPlayerTwo().getColor());
				}
			}
			if (player == 1) // faire une exception
			{
				this.bases[baseSrc].getPlayerOne().setColor(0);
			}
			else if (player == 2)
			{
				this.bases[baseSrc].getPlayerTwo().setColor(0);
			}
		}
		else
		{
			throw new InvalidMoveException(baseSrc,player,this.holeContainer[0]);
		}
		
		// setting new hole base
		this.setHoleContainer(this.findMissingPlayer());
	}

	/**
	 * Replace the current holeContainer by hole
	 * @param hole : the new holeContainer
	 */
	private void setHoleContainer(int[] hole) {
		this.holeContainer = new int[hole.length];
		for ( int i = 0 ; i < hole.length;i++)
		{
			this.holeContainer[i]=hole[i];
		}
	}
	
	/**
	 * Swap two players from two bases
	 * @param baseOne :the first base where you will pick a player
	 * @param baseTwo :the second one
	 */
	private void swap(BaseballBase baseOne, BaseballBase baseTwo) {
		BaseballPlayer flyingMan = null;
		if ( Math.random()>0.5)
		{
			flyingMan = baseOne.getPlayerOne();
			baseOne.setPlayerOne(baseTwo.getPlayerTwo());
			baseTwo.setPlayerTwo(flyingMan);
		}
		else
		{
			flyingMan = baseOne.getPlayerTwo();
			baseOne.setPlayerTwo(baseTwo.getPlayerOne());
			baseTwo.setPlayerOne(flyingMan);
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
			s+="  Player 1 : "+this.bases[i].getPlayerOne().getColor()+"\n";
			s+="  Player 2 : "+this.bases[i].getPlayerTwo().getColor()+"\n";
		}
		return s;
	}
	

}
