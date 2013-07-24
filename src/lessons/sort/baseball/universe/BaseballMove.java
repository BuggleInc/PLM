package jlm.universe.smn.baseball;

public class BaseballMove {

	private int baseSrc;
	private int playerSrc;
	private int baseDst;
	private int playerDst;
	private int playerColor;
	
	public BaseballMove(int baseSrc, int playerSrc, int baseDst, int playerDst, int playerColor){
		this.setBaseSrc(baseSrc);
		this.setPlayerSrc(playerSrc);
		this.setBaseDst(baseDst);
		this.setPlayerDst(playerDst);
		this.setPlayerColor(playerColor);
	}


	/**
	 * @return the baseDst
	 */
	public int getBaseDst() {
		return baseDst;
	}

	/**
	 * @return the baseSrc
	 */
	public int getBaseSrc() {
		return baseSrc;
	}

	/**
	 * @return the playerColor
	 */
	public int getPlayerColor() {
		return playerColor;
	}

	/**
	 * @return the playerDst
	 */
	public int getPlayerDst() {
		return playerDst;
	}

	/**
	 * @return the playerSrc
	 */
	public int getPlayerSrc() {
		return playerSrc;
	}

	/**
	 * @param baseDst the baseDst to set
	 */
	public void setBaseDst(int baseDst) {
		this.baseDst = baseDst;
	}

	/**
	 * @param baseSrc the baseSrc to set
	 */
	public void setBaseSrc(int baseSrc) {
		this.baseSrc = baseSrc;
	}

	/**
	 * @param playerColor the playerColor to set
	 */
	public void setPlayerColor(int playerColor) {
		this.playerColor = playerColor;
	}


	/**
	 * @param playerDst the playerDst to set
	 */
	public void setPlayerDst(int playerDst) {
		this.playerDst = playerDst;
	}


	/**
	 * @param playerSrc the playerSrc to set
	 */
	public void setPlayerSrc(int playerSrc) {
		this.playerSrc = playerSrc;
	}
	
	
	
}
