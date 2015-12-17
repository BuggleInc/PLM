package lessons.sort.pancake;

import java.io.BufferedWriter;
import java.io.IOException;

import plm.universe.pancake.PancakeEntity;
import plm.universe.pancake.PancakeWorld;



/* This is not exactly the gates algorithm. Here is the original text:
 * <ul><li><b>Case f</b>: <code>t</code> is in a block of length k+1 (the last element is <code>t+ko</code>), <code>t-o</code>
 * is the last element of another block and <code>t+(k+1)o</code> is free (there is two differing situation, depending on the relative order of 
 * <code>t-o</code> and <code>t+(k+1)o</code>. They are merged in 4 flip with the corresponding sequence below.<br/>
 * <div align="center"><img src="lessons/sort/pancake/gates-f1.png"/></div><br/>
 * Other possibility when <code>t-o &gt; t+(k+1)o</code>:<br/>
 * <div align="center"><img src="lessons/sort/pancake/gates-f2.png"/></div></li></ul>

 */


public class GatesPancakeEntity extends PancakeEntity {
	
	// FIXME: killme and merge it to parent class
	@Override
	public void command(String command, BufferedWriter out) {
		int num = Integer.parseInt((String) command.subSequence(0, 3));
		try {
			switch(num){
			case 116:
				out.write(((PancakeWorld)world).wasRandom?"1":"0");
				out.write("\n");
				break;
			default:
				super.command(command, out);
				break;
			}
			out.flush();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public void run() {
		solve();
	}

	/* BEGIN HIDDEN */
	int getRankOf(int size) {
		for (int rank=0;rank<getStackSize();rank++)
			if (getPancakeRadius(rank) == size)
				return rank;
		return -99; // Well, be robust to border cases 
	}
	boolean isFree(int pos) {
		if (pos == -99)
			return false;
		int radius = getPancakeRadius(pos);
		if (pos>0) {
			int nextRadius = getPancakeRadius(pos-1);
			if (nextRadius == radius-1 || nextRadius == radius+1)
				return false;
		}
		if (pos<getStackSize()-1) {
			int nextRadius = getPancakeRadius(pos+1);
			if (nextRadius == radius-1 || nextRadius == radius+1)
				return false;
		}
		return true;
	}
	boolean isFirst(int pos) {
		if (pos == -99)
			return false;
		int radius = getPancakeRadius(pos);
		if (pos>0) {
			int nextRadius = getPancakeRadius(pos-1);
			if (nextRadius == radius-1 || nextRadius == radius+1)
				return false;
		}
		if (pos<getStackSize()-1) {
			int nextRadius = getPancakeRadius(pos+1);
			if (nextRadius == radius-1 || nextRadius == radius+1)
				return true;
		}
		return false;
	}
	boolean isLast(int pos) {
		if (pos == -99)
			return false;
		int radius = getPancakeRadius(pos);
		if (pos<getStackSize()-1) {
			int nextRadius = getPancakeRadius(pos+1);
			if (nextRadius == radius-1 || nextRadius == radius+1)
				return false;
		}
		if (pos>0) {
			int nextRadius = getPancakeRadius(pos-1);
			if (nextRadius == radius-1 || nextRadius == radius+1)
				return true;
		}
		return false;
	}
	int blockLength() {
		int pos = 0;
		int radius = getPancakeRadius(pos);
		int o = getPancakeRadius(pos+1) - radius;
		
		if (o != -1 && o != 1) {
			getGame().getLogger().log("Asked to compute the block length, but the step o is "+o+" instead of +1 or -1. " +
					"The length is then 1, but you are violating a precondition somehow");
			return 1;
		}
		
		while (pos < getStackSize()-1 && getPancakeRadius(pos+1) == radius + o) {
			pos++;
			radius += o;
		}
		return pos+1;
	}
	int debug=0; // 0: silence; 1: which cases; 2: all details
	/* END HIDDEN */
	
	/* BEGIN TEMPLATE */
	public void solve() {
		/* BEGIN SOLUTION */
		/* cruft to search for an instance exercising all transformations */
		boolean doneA=false;
		boolean doneB=false;
		boolean doneC=false;
		boolean doneD=false;
		boolean doneE=false;
		boolean doneF=false;
		boolean doneG=false;
		boolean doneH=false;
		Integer[] origSizes = new Integer[getStackSize()];
		for (int i=0;i<getStackSize();i++)
			origSizes[i] = getPancakeRadius(i);
		/* end of this cruft */
		
		int stackSize = getStackSize();
		
		if (debug>0) {
			System.out.print("{");
			for (int rank=0; rank < stackSize; rank++) 
				System.out.print(""+getPancakeRadius(rank)+", ");
			getGame().getLogger().log("}");
		}
		
		while (true) {
			int tRadius = getPancakeRadius(0);
			int posTPlus  = getRankOf(tRadius+1); // returns -99 if non-existent, that is then ignored
			int posTMinus = getRankOf(tRadius-1); 
			int posT = 0;
			
			if (debug>1) {
				getGame().getLogger().log("t Radius: "+tRadius);
				for (int rank=0; rank < stackSize; rank++) {
					System.out.print("["+rank+"]="+getPancakeRadius(rank)+"; ");

					if (isFree(rank))
						System.out.print("free;");
					else 
						System.out.print("NON-free;");

					if (isFirst(rank))
						System.out.print("first; ");
					else 
						System.out.print("NON-first; ");

					if (isLast(rank))
						System.out.print("last; ");
					else 
						System.out.print("NON-last; ");


					if (rank == posTPlus)
						System.out.print("t+1; ");
					if (rank == posTMinus)
						System.out.print("t-1; ");
					if (rank == posT)
						System.out.print("t;" );

					getGame().getLogger().log("");
				}
			}
						
			if (isFree(posT)) {			
				if (isFree(posTPlus)) { /* CASE A: t and t+o free */
					if (debug>0)
						getGame().getLogger().log("Case A+");
					flip(posTPlus);
					doneA = true;
				} else if (isFree(posTMinus)) { /* CASE A: t and t-o free */
					if (debug>0)
						getGame().getLogger().log("Case A-");
					flip(posTMinus);
					doneA = true;
					
				} else if (isFirst(posTPlus)) { /* CASE B: t free, t+o first element */
					if (debug>0)
						getGame().getLogger().log("Case B+");
					flip(posTPlus);
					doneB = true;
				} else if (isFirst(posTMinus)) { /* CASE B: t free, t-o first element */
					if (debug>0)
						getGame().getLogger().log("Case B-");
					flip(posTMinus);
					doneB = true;

				} else if (Math.min(posTPlus,posTMinus) != -99) { /* CASE C: t free, but both t+o and t-o are last elements */
					if (debug>0)
						getGame().getLogger().log("Case C");
					flip(Math.min(posTPlus,posTMinus) );
					flip(Math.min(posTPlus,posTMinus) - 1);
					flip(Math.max(posTPlus,posTMinus) + 1);
					flip(Math.min(posTPlus,posTMinus) - 1);
					doneC = true;
					
				} else {
					if (debug>0)
						getGame().getLogger().log("Case Cbis");
					flip(Math.max(posTPlus,posTMinus) + 1);
					flip(Math.max(posTPlus,posTMinus) );
					doneC = true;
				}
				
			} else { // t is in a block
				if (blockLength() == stackSize) { // Done!
					if (tRadius != 1) // all reverse 
						flip(stackSize);
					if (doneA && doneB && doneC && doneD && doneE && doneF && doneG && doneH && ((PancakeWorld)world).wasRandom) {
						getGame().getLogger().log("BINGO! This instance is VERY interesting as it experiences every cases of the algorithm.\nPLEASE REPORT IT. PLEASE DONT LOSE IT.");
						System.out.print("{");
						for (int rank=0; rank < stackSize; rank++) 
							System.out.print(""+origSizes[rank]+", ");
						getGame().getLogger().log("}");
					}
					return;
				}
				
				if (isFree(posTPlus)) {          /* CASE D: t in a block, t+1 free */
					if (debug>0)
						getGame().getLogger().log("Case D+");
					flip(posTPlus);
					doneD = true;

				} else if (isFree(posTMinus)) {  /* CASE D: t in a block, t-1 free */
					if (debug>0)
						getGame().getLogger().log("Case D-");
					flip(posTMinus);
					doneD = true;

				} else if (isFirst(posTPlus)) {  /* CASE E: t in a block, t+1 first element */
					if (debug>0)
						getGame().getLogger().log("Case E+");
					flip(posTPlus);
					doneE = true;

				} else if (isFirst(posTMinus)) { /* CASE E: t in a block, t-1 first element */
					if (debug>0)
						getGame().getLogger().log("Case E-");
					flip(posTMinus);
					doneE = true;

				} else if (isLast(posTPlus) && posTPlus != 1) { /* CASE F+: t in a block, t+1 last element */
					doneF = true;
					if (debug>0)
						getGame().getLogger().log("Case F+");
					flip(blockLength());
					flip(posTPlus + 1);
					int newPos = getRankOf(tRadius);
					if (newPos>0)
						flip(newPos);
					
				} else if (isLast(posTMinus) && posTMinus != 1) { /* CASE F-: t in a block, t-1 last element */
					doneF = true;
					if (debug>0)
						getGame().getLogger().log("Case F-");
					flip(blockLength());
					flip(posTMinus + 1);
					int newPos = getRankOf(tRadius);
					if (newPos>0)
						flip(newPos);
				} else {
					int k = blockLength()-1;
					int o = getPancakeRadius(1) - tRadius;
					int pos = getRankOf(tRadius+(k+1)*o);
					if (isFree(pos) || isFirst(pos)) {
						doneG = true;
						if (debug>0)
							getGame().getLogger().log("Case G");
						flip(k+1);
						flip(pos);
					} else {
						doneH = true;
						if (debug>0)
							getGame().getLogger().log("Case H");
						flip(pos+1);
						flip(getRankOf(tRadius+k*o));
					}
				}
			}
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */


}
 