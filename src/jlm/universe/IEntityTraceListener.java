package jlm.universe;

public interface IEntityTraceListener {
	public void entityTraceChanged(Entity e,String location);

	public void tracedEntityChanged(Entity selectedEntity);
}
