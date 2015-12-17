package plm.universe.baseball;

public class BaseballMove {

	private int baseSrc;
	private int playerSrc;
	private int baseDst;
	private int playerDst;
	private int playerColor;
	private BaseballWorld field;

	public BaseballMove(int baseSrc, int playerSrc, int baseDst, int playerDst, int playerColor, BaseballWorld field){
		this.baseSrc = baseSrc;
		this.playerSrc = playerSrc;
		this.baseDst = baseDst;
		this.playerDst = playerDst;
		this.playerColor = playerColor;
		this.field = field;
	}

	public int getBaseDst() {
		return baseDst;
	}
	public int getBaseSrc() {
		return baseSrc;
	}
	public int getPlayerDst() {
		return playerDst;
	}
	public int getPlayerSrc() {
		return playerSrc;
	}
	public int getPlayerColor() {
		return playerColor;
	}
	
	public int[] run(int[] init) {
		int idxSrc = field.getPositionsAmount()*baseSrc+ playerSrc;
		int idxDst = field.getPositionsAmount()*baseDst+ playerDst;
		int src = init[idxSrc];
		int dest = init[idxDst];
		init[idxSrc] = dest;
		init[idxDst] = src;
		
		return init;
	}

}
