package plm.universe.sort;

import java.io.BufferedWriter;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonCreator;

import plm.universe.Entity;
import plm.universe.sort.operations.CopyOperation;
import plm.universe.sort.operations.CountOperation;
import plm.universe.sort.operations.GetValueOperation;
import plm.universe.sort.operations.SetValOperation;
import plm.universe.sort.operations.SwapOperation;

public class SortingEntity extends Entity {

	@JsonCreator
	public SortingEntity() {
		super();
	}

	public void copy(int from,int to) {
		SortingWorld sortingWorld = (SortingWorld) this.world;
		sortingWorld.copy(from,to);
		//addOperation(new CopyOperation(to, from));
		//addOperation(new CountOperation(sortingWorld.getReadCount(), sortingWorld.getWriteCount()));
		stepUI();
	}

	public int getValue(int i) {
		SortingWorld sortingWorld = (SortingWorld) this.world;
		//addOperation(new GetValueOperation(i));
		//addOperation(new CountOperation(sortingWorld.getReadCount(), sortingWorld.getWriteCount()));
		stepUI();
		return sortingWorld.getValue(i);
	}

	public int getValueCount() {
		return ((SortingWorld) this.world).getValueCount();
	}

	public boolean isSmaller(int i, int j) {
		SortingWorld sortingWorld = (SortingWorld) this.world;
		//addOperation(new CountOperation(sortingWorld.getReadCount(), sortingWorld.getWriteCount()));
		return sortingWorld.isSmaller(i,j);
	}

	public boolean isSmallerThan(int i, int val) {
		SortingWorld sortingWorld = (SortingWorld) this.world;
		//addOperation(new CountOperation(sortingWorld.getReadCount(), sortingWorld.getWriteCount()));
		return sortingWorld.isSmallerThan(i,val);
	}

	public void run() {
		// Child implement this
	}

	public void setValue(int i,int val) {
		SortingWorld sortingWorld = (SortingWorld) this.world;
		sortingWorld.setValue(i,val);
		//addOperation(new SetValOperation(val, i));
		//addOperation(new CountOperation(sortingWorld.getReadCount(), sortingWorld.getWriteCount()));
		stepUI();
	}

	public void swap(int i, int j) {
		SortingWorld sortingWorld = (SortingWorld) this.world;
		sortingWorld.swap(j,i);
		//addOperation(new SwapOperation(i, j));
		//addOperation(new CountOperation(sortingWorld.getReadCount(), sortingWorld.getWriteCount()));
		stepUI();
	}

	/* BINDINGS TRANSLATION: French */
	public int getNombreValeurs() { return getValueCount(); }

	public int getValeur(int i)   { return getValue(i);}
	public void setValeur(int i,int val) { setValue(i, val); }

	public boolean plusPetit(int i, int j) { return isSmaller(i, j); }	
	public boolean plusPetitQue(int i, int value) { return isSmallerThan(i, value); }
	public void echange(int i, int j) { swap(i,j); }
	public void copie(int from,int to) { copy(from,to);}


	public boolean estSelectionne() {return isSelected();}

	@Override
	public void command(String command, BufferedWriter out) {
		int num = Integer.parseInt((String) command.subSequence(0, 3));
		int nb,nb2;
		try{
			switch(num){
			case 110:
				out.write(Integer.toString(getValueCount()));
				out.write("\n");
				break;
			case 111:
				nb = Integer.parseInt((command.split(" ")[1]));
				nb2 = Integer.parseInt((command.split(" ")[2]));
				out.write((isSmaller(nb, nb2)?"1":"0"));
				out.write("\n");
				break;
			case 112:
				nb = Integer.parseInt((command.split(" ")[1]));
				nb2 = Integer.parseInt((command.split(" ")[2]));
				out.write((isSmallerThan(nb, nb2)?"1":"0"));
				out.write("\n");
				break;
			case 113:
				nb = Integer.parseInt((command.split(" ")[1]));
				nb2 = Integer.parseInt((command.split(" ")[2]));
				swap(nb, nb2);
				break;
			case 114:
				nb = Integer.parseInt((command.split(" ")[1]));
				nb2 = Integer.parseInt((command.split(" ")[2]));
				copy(nb, nb2);
				break;
			case 115:
				nb = Integer.parseInt((command.split(" ")[1]));
				out.write(Integer.toString(getValue(nb)));
				out.write("\n");
				break;
			case 116:
				nb = Integer.parseInt((command.split(" ")[1]));
				nb2 = Integer.parseInt((command.split(" ")[2]));
				setValue(nb, nb2);
				break;
			case 117:
				out.write((isSelected()?"1":"0"));
				out.write("\n");
				break;
			}
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
