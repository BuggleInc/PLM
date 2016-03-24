package plm.universe.bat.operations;

import plm.universe.bat.BatTest;

public class SetResult extends BatOperation {
	
	private int index;
	private Object result;
	
	public SetResult(int index, BatTest batTest) {
		super("setResult");
		this.index = index;
		this.result = batTest.getResult();
	}

	public int getIndex() {
		return index;
	}

	public Object getResult() {
		return result;
	}
}
