package lessons.lightbot;




public class LightBotWorldCell {
	private boolean light=false; /* whether we have a light */
	private boolean lightOn=false; /* if we have a light, whether it's on or off */

	private LightBotWorld world;

	private int x;
	private int y;
	private int height;

	public LightBotWorldCell(LightBotWorld w, int x, int y) {
		this(w, x, y, false, false, 0);
	}

	public LightBotWorldCell(LightBotWorldCell c) {
		this(c.world, c.x, c.y, c.light, c.lightOn, c.height);
	}

	public LightBotWorldCell(LightBotWorld w, int x, int y, boolean light, boolean lightOn, int height) {
		this.world = w;
		this.x = x;
		this.y = y;
		this.light = light;
		this.lightOn = lightOn;
		this.height = height;
	}

	public void addLight() {
		this.light = true;
		this.lightOn = false;
		world.notifyWorldUpdatesListeners();
	}
	public void removeLight() {
		this.light = false;
		world.notifyWorldUpdatesListeners();
	}
	public void lightSwitch() {
		lightOn = !lightOn;
		world.notifyWorldUpdatesListeners();
	}
	public void setLightOn() {
		lightOn = true;
		world.notifyWorldUpdatesListeners();
	}
	public void setLightOff() {
		lightOn = false;
		world.notifyWorldUpdatesListeners();
	}
	/** Returns true if the light is on, or if there is no light */
	public boolean getLightOnOrNone(){
		return (!light) || lightOn;
	}

	public LightBotWorld getWorld() {
		return this.world;
	}
	
	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
		
	public void setWorld(LightBotWorld w) {
		this.world = w;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (light ? 1231 : 1237);
		result = prime * result + (lightOn ? 1231 : 1237);
		result = prime * result + ((world == null) ? 0 : world.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LightBotWorldCell other = (LightBotWorldCell) obj;
		if (light != other.light)
			return false;
		if (lightOn != other.lightOn)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int h) {
		height = h;
	}

	public boolean isLight() {
		return light;
	}

	public boolean isLightOn() {
		return lightOn;
	}
}
