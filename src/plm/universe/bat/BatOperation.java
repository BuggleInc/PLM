package plm.universe.bat;

import plm.universe.Operation;

public class BatOperation extends Operation {

	private BatWorld batWorld;
	
	public BatOperation(BatWorld batWorld) {
		super("batOperation");
		this.batWorld = batWorld;
	}

	public BatWorld getBatWorld() {
		return batWorld;
	}
	
}
