package plm.universe;

public interface IEntityStackListener {
	void entityTraceChanged(Entity e, StackTraceElement[] trace);

	void tracedEntityChanged(Entity selectedEntity);
}
