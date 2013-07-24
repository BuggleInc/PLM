package lessons.sort.baseball.universe;

public class BaseballMove {

	private int baseSrc;
	private int playerSrc;
	private int baseDst;
	private int playerDst;
	private int playerColor;

	public BaseballMove(int baseSrc, int playerSrc, int baseDst, int playerDst, int playerColor){
		this.baseSrc = baseSrc;
		this.playerSrc = playerSrc;
		this.baseDst = baseDst;
		this.playerDst = playerDst;
		this.playerColor = playerColor;
	}

	public int getBaseDst() {
		return baseDst;
	}
	public int getBaseSrc() {
		return baseSrc;
	}
	public int getPlayerColor() {
		return playerColor;
	}
	public int getPlayerDst() {
		return playerDst;
	}
	public int getPlayerSrc() {
		return playerSrc;
	}
}
