package lessons.recursion.hanoi.universe;
/* BEGIN TEMPLATE */
import java.io.BufferedWriter;
import java.io.IOException;

import lessons.recursion.hanoi.operations.HanoiMove;
import plm.core.model.Game;
import plm.universe.Entity;
import plm.universe.World;

public class HanoiEntity extends Entity {
	
	public HanoiEntity() {
		super();
	}
	
	/** Instantiation Constructor (used by exercises to setup the world) 
	 * Must call super(name, world). If you had fields to setup, you'd  have to add more parameters
	 */
	public HanoiEntity(String name, World world) {
		/* BEGIN HIDDEN */
		super(name,world);
		/* END HIDDEN */
	}

	/** Part of the copy process 
	 * Must call super(name)
	 */
	public HanoiEntity(String name) {
		/* BEGIN HIDDEN */
		super(name);
		/* END HIDDEN */
	}
	/** Must exist too. Calling HanoiEntity("dummy name") is ok */
	public HanoiEntity(Game game) {
		/* BEGIN HIDDEN */
		this("Hanoi Entity");
		/* END HIDDEN */
	}

	/** Must exist so that exercises can instantiate your entity (Entity is abstract) */
	@Override
	public void run() {
	}

	/** Part of your world logic */
	public void move(int src, int dst) {
		regularMove(src,dst);
	}
	public void regularMove(int src, int dst) {
		/* BEGIN HIDDEN */
		((HanoiWorld) world).move(src,dst);
		addOperation(new HanoiMove(this, src, dst));
		stepUI();
		/* END HIDDEN */
	}
	public void cyclicMove(int from, int to){
		if ((from==0 && to!=1) ||
			(from==1 && to!=2) ||
			(from==2 && to!=0))
			throw new RuntimeException(getGame().i18n.tr(
					"Sorry Dave, I cannot let you use move disks counterclockwise. Move from 0 to 1, from 1 to 2 or from 2 to 0 only, not from {0} to {1}.",from, to));
		regularMove(from,to);
	}
	/** Returns the amount of disks on the given slot */
	public int getSlotSize(int slot) {
		return ((HanoiWorld) world).getSlotSize(slot);
	}
	/** Returns the radius of the topmost disk of the given slot */
	public int getSlotRadius(int slot) {
		return ((HanoiWorld) world).getRadius(slot);
	}

	/* BEGIN HIDDEN */
	@Override
	public String toString(){
		return "HanoiEntity (" + this.getClass().getName() + ")";
	}
	/* END HIDDEN */

	@Override
	public void command(String command, BufferedWriter out) {
		int num = Integer.parseInt((String) command.subSequence(0, 3));
		int nb,nb2;
		try {
			switch(num){
			case 110:
				nb = Integer.parseInt((command.split(" ")[1]));
				nb2 = Integer.parseInt((command.split(" ")[2]));
				move(nb, nb2);
				break;
			case 111:
				nb = Integer.parseInt((command.split(" ")[1]));
				out.write(Integer.toString(getSlotSize(nb)));
				out.write("\n");
				break;
			case 112:
				out.write((isSelected()?"1":"0"));
				out.write("\n");
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
	
	/* BINDINGS TRANSLATION: French */
	public void deplace(int src,int dst) { move(src, dst); }
	public int  getTaillePiquet(int rank) { return getSlotSize(rank); }
	public int  getRayonPiquet(int rank) { return getSlotRadius(rank); }
}
/* END TEMPLATE */
