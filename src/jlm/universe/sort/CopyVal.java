package jlm.universe.sort;

public class CopyVal extends Operation {
	
	public CopyVal(int source, int destination){
		super(source,destination);
	}

	@Override
	public int[] run(int[] init) {
		init[destination] = init[source];
		
		return init;
	}
	
	
}
