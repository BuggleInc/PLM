package plm.universe;

public abstract class Operation {
	private String name;
	
	public Operation(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
