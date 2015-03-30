package plm.universe.bugglequest;

import java.awt.Color;

import plm.core.model.Game;
import plm.universe.Direction;
import plm.universe.bugglequest.exception.BuggleInOuterSpaceException;

public class SimpleBuggle extends AbstractBuggle  {
	/* Make it possible to instantiate SimpleBuggles from exercises so that Python also gets graphical window showing */
	public SimpleBuggle() {
		super();
	}
	public SimpleBuggle(Game game, BuggleWorld world, String name, int i, int j, Direction north, Color color, Color brush) {
		super(game, world, name, i, j, north, color, brush);
	}
	@Override
	public void run() {
		// Overridden by children
	}
	
	/* BINDINGS TRANSLATION: French (get/set X/Y/Pos are not translated as they happen to be the same in French) */
	public void avance()          { forward(); }
	public void avance(int steps) { forward(steps); }
	public void recule()          { backward(); }
	public void recule(int steps) { backward(steps); }
	public void prendBiscuit()    { pickupBaggle(); }
	public void poseBiscuit()     { dropBaggle(); }
	/* BINDINGS TRANSLATION: Brazilian Portuguese */
	public void avançar()          { forward(); }
	public void avançar(int steps) { forward(steps); }
	public void recuar()          { backward(); }
	public void recuar(int steps) { backward(steps); }
	public void pegarBaggle()    { pickupBaggle(); }
	public void soltarBaggle()     { dropBaggle(); }

}
