package jlm.universe.sort;

public class SetVal extends Operation {
	
	public SetVal(int source, int destination){
		super(source,destination);
	}

	@Override
	public int[] run(int[] init) {
		init[destination] = source;
		
		return init;
	}
	
	
}
