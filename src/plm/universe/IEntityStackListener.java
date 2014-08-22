package plm.universe;

public interface IEntityStackListener {
	public void entityTraceChanged(Entity e, StackTraceElement[] trace);

	public void tracedEntityChanged(Entity selectedEntity);
}
