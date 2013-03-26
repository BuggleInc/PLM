package jlm.universe.sort;

import java.util.ArrayList;

public abstract class Operation {
	
	int type, source, destination;
	
	public Operation(int source, int destination){
		this.source = source;
		this.destination = destination;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}
	
	public static int[] compute(int[] init, ArrayList<Operation> ops, int rang){
		int[] current = new int[init.length];
		for(int i=0;i<init.length ; i++){
			current[i] = init[i];
		}
		
		int cmpt=1;
		for(Operation op : ops){
			
			current = op.run(current);
			if(cmpt==rang){
				break;
			}
			cmpt++;
		}
		
		return current;
	}
	
	public abstract int[] run(int[] init);
	
}
