package plm.universe.baseball;

import java.io.BufferedWriter;
import java.io.IOException;

import plm.universe.Entity;
import plm.universe.baseball.operations.MoveOperation;

public class BaseballEntity extends Entity {
	
	public BaseballEntity() {
		super();
	}
	
	/** Returns the amount of bases on your field */
	public int getBasesAmount() {
		return ((BaseballWorld) this.world).getBasesAmount();
	}
	/** Returns the amount of players locations available on each base of the field */
	public int getPositionsAmount() {
		return ((BaseballWorld) this.world).getPositionsAmount();
	}

	/** Returns the color of the player at the specified coordinate */
	public int getPlayerColor(int base, int position) {
		return ((BaseballWorld) this.world).getPlayerColor(base,position);
	}
	/** Returns whether every players of the specified base are at home */
	public boolean isBaseSorted(int base) {
		return ((BaseballWorld) this.world).isBaseSorted(base);
	}
	/** Returns if every player of the field is on the right base */
	public boolean isSorted() {
		return ((BaseballWorld) this.world).isSorted();
	}

	/** Returns the base in which the hole is located */
	public int getHoleBase() {
		return ((BaseballWorld) this.world).getHoleBase();
	}
	/** Returns the hole position within its base */
	public int getHolePosition(){
		return ((BaseballWorld) this.world).getHolePosition();
	}



	/**
	 * Moves the specified player to the hole
	 * @throws IllegalArgumentException if the specified player is not near the hole (at most one base away) 
	 */
	public void move(int base, int position) {
		((BaseballWorld) this.world).move(base,position);
		addOperation(new MoveOperation(this, base, position));
		stepUI();
	}

	/** Must exist so that exercises can instantiate the entity (Entity is abstract) */ 
	@Override
	public void run() {
	}

	/** Returns a string representation of the world */
	public String toString(){
		return "BaseballEntity (" + this.getClass().getName() + ")";
	}
	
	/* BINDINGS TRANSLATION: French */
	public int getNombreBases()     { return getBasesAmount(); }
	public int getNombrePositions() { return getPositionsAmount(); }
	public int getCouleurJoueur(int base, int position) { return getPlayerColor(base,position); }
	public boolean estBaseTriee(int base) { return isBaseSorted(base); }
	public boolean estTrie()              { return isSorted(); }

	public int getTrouBase()     { return getHoleBase(); }
	public int getTrouPosition() { return getHolePosition(); }
	public void deplace(int base, int position) { move(base, position); }
	
	public boolean estSelectionne() { return isSelected(); }
	@Override
	public void command(String command, BufferedWriter out) {
		int num = Integer.parseInt((String) command.subSequence(0, 3));
		int nb,nb2;
		String str;
		try {
			switch(num){
			case 110:
				out.write(Integer.toString(getBasesAmount()));
				out.write("\n");
				break;
			case 111:
				out.write(Integer.toString(getPositionsAmount()));
				out.write("\n");
				break;
			case 112:
				out.write(Integer.toString(getHoleBase()));
				out.write("\n");
				break;
			case 113:
				out.write(Integer.toString(getHolePosition()));
				out.write("\n");
				break;
			case 114:
				nb = Integer.parseInt((command.split(" ")[1]));
				nb2 = Integer.parseInt((command.split(" ")[2]));
				out.write(Integer.toString(getPlayerColor(nb, nb2)));
				out.write("\n");
				break;
			case 115:
				out.write((isSorted()?"1":"0"));
				out.write("\n");
				break;
			case 116:
				nb = Integer.parseInt((command.split(" ")[1]));
				out.write((isBaseSorted(nb)?"1":"0"));
				out.write("\n");
				break;
			case 117:
				out.write((isSelected()?"1":"0"));
				out.write("\n");
			case 118:
				nb = Integer.parseInt((command.split(" ")[1]));
				nb2 = Integer.parseInt((command.split(" ")[2]));
				move(nb, nb2);
				break;
			case 119:
				str = command.split(" ")[1];
				((BaseballWorld) world).assertSorted(str);
				break;
			default:
				getGame().getLogger().log("COMMANDE INCONNUE : "+command);
				break;
			}
			out.flush();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
}
