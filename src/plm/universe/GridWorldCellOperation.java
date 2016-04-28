package plm.universe;

public abstract class GridWorldCellOperation extends Operation {

	private int x;
	private int y;
	
	public GridWorldCellOperation(String name, int x, int y) {
		super(name);
		this.setX(x);
		this.setY(y);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
