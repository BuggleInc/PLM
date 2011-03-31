package lessons.race;

public interface IClock {

	/**
	 * reset the clock to 00 h 00mn 00s.
	 */
	public void reset();

	/** 
	 * Returns the hours of the clock
	 * 
	 * @return the value of the hour counter of the clock
	 */
	public int getHours();

	/**
	 * Return the minutes of the clocl
	 * 
	 * @return the value of the minute counter of the clock
	 */
	public int getMinutes();

	/**
	 * Return the seconds of the clock
	 * 
	 * @return the value of the second counter of the clock
	 */
	public int getSeconds();

	/**
	 * Retourne la valeur totale de l'horloge en secondes.
	 * 
	 * @return la valeur de l'horloge en secondes
	 */
	public int inSeconds();

	/**
	 * increment the value of the clock
	 * 
	 * @param nbh the amount of hours to add
	 * @param nbm the amount of minutes to add
	 * @param nbs the amount of seconds to add
	 */
	public void increment(int nbh, int nbm, int nbs);

	/**
	 * increment the value of the clock
	 * 
	 * @param nbs the amount of seconds to add
	 */
	public void incrementSeconds(int nbs);

	/**
	 * increment the value of the clock
	 * 
	 * @param nbm the amount of minutes to add
	 */
	public void incrementMinutes(int nbm);

	/**
	 * increment the value of the clock
	 * 
	 * @param nbh the amount of hours to add
	 */
	public void incrementHours(int nbh);

	/**
	 * Computes the difference between the current clock and the clock passed as parameter
	 * 
	 * @param other
	 *            represents the clock with which we want to compute the timing difference
	 * @return a new clock representing the difference
	 */
	public IClock differenceBetween(IClock other);

	/**
	 * Returns whether an object is equal to the current clock
	 * 
	 * @param other
	 *            represents the clock with which we want to compare the current clock
	 * @return true if both objects are equal
	 */
	public boolean equals(Object other);

	/**
	 * Computes an integer value (named hashcode) representing the object
	 * 
	 * @return the hashcode
	 */
	public int hashCode();

	/**
	 * Returns a string representation of the clock
	 * 
	 * @return a string of chars of the form 'Xh Ymn Zs'.
	 */
	public String toString();

}
