package jlm.universe.smn.baseball;

public class InvalidMoveException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidMoveException(int baseSrc, int player, int i) {
		super("The player "+player+" from base "+baseSrc+" can't move to base "+i+" since it's a lazy guy and he doesn't want to travel more than one base at once");
	}

}
