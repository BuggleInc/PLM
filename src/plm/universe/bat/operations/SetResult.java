package plm.universe.bat.operations;

import plm.universe.bat.BatTest;

public class SetResult extends BatOperation {
	
	private int index;
	private BatTest batTest;
	
	public SetResult(int index, BatTest batTest) {
		super("setResult");
		this.index = index;
		this.batTest = batTest;
	}

	public int getIndex() {
		return index;
	}

	public BatTest getBatTest() {
		return batTest;
	}
}
