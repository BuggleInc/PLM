package jlm.universe.sort;

public class Swap extends Operation {
	
	public Swap(int source, int destination){
		super(source,destination);
	}

	@Override
	public int[] run(int[] init) {
		int src = init[source];
		int dest = init[destination];
		init[source] = dest;
		init[destination] = src;
		
		return init;
	}
	
	
}
