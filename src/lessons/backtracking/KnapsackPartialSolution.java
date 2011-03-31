package lessons.backtracking;


public class KnapsackPartialSolution extends BacktrackingPartialSolution {
	/* Data of the problem */
	private int maxSize;
	private int[] values;
	private int[] sizes;
	/* instance data */
	private boolean[] taken;
	private int currSize=0;
	private int currValue=0;
	
	
	protected KnapsackPartialSolution(int maxSize, int[]sizes) {
		this(maxSize,sizes,sizes);
	}
	protected KnapsackPartialSolution(int maxSize, int[] newsizes, int[] newvalues) {
		this.maxSize = maxSize;
		this.values = newvalues;
		this.sizes = newsizes;
		
		taken = new boolean[sizes.length];
		for (int i=0;i<sizes.length;i++)
			taken[i]=false;
	}
	/* setters */
	public void take(int rank) {
		if (taken[rank])
			throw new InvalidBacktrackingActionException("Cannot take object "+rank+" since you already have it");
		taken[rank]=true;
		currSize+=sizes[rank];
		currValue+=values[rank];
	}
	public void leave(int rank) {
		if (!taken[rank])
			throw new InvalidBacktrackingActionException("Cannot leave object "+rank+" since you don't have it");
		taken[rank]=false;
		currSize-=sizes[rank];
		currValue-=values[rank];
	}
	
	/* getters */
	public boolean isValid( ) {
		return currSize<=maxSize;
	}
	public int getValue( ){
		if (currValue>maxSize)
			throw new InvalidSolutionException("You asked the value of an invalid solution (size="+currSize+" where maxsize="+maxSize+")");

		return currValue;
	}
	/** Returns the amount of objects in the cave today */
	public int size() {
		return sizes.length;
	}
	
	@Override
	public KnapsackPartialSolution clone() {
		int[] newsizes = new int[sizes.length];
		int[] newvalues = new int[sizes.length];
		for (int i=0;i<sizes.length;i++) {
			newsizes[i] = sizes[i];
			newvalues[i] = values[i];
		}
		
		KnapsackPartialSolution res = new KnapsackPartialSolution(maxSize, newsizes, newvalues);
		
		for (int i=0;i<sizes.length;i++)
			if (taken[i])
				res.take(i);
		
		return res;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		for (int i=0;i<sizes.length;i++) {
			if (i>0)
				sb.append(", ");
			if (taken[i])
				sb.append(sizes[i]);
			else
				sb.append("--");
		}
		sb.append("]");
		return sb.toString();
	}
	
	@Override
	public String getTitle() {
		StringBuffer sb = new StringBuffer("knapsack(max:"+maxSize+";  ");
		for (int i=0;i<values.length;i++)
			sb.append( (i>0?", ":"") + values[i]);
		sb.append(")");
		return sb.toString();
	}
}
