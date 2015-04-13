package plm.universe;

public abstract class Operation {
	private String name;
	private String msg;
	
	public Operation(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}
}
