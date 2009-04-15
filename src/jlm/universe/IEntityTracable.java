package jlm.universe;

public interface IEntityTracable {
	public void addStackListener(IEntityStackListener l);
	public void removeStackListener(IEntityStackListener l);
	public void fireStackListener(StackTraceElement[] trace);
	public StackTraceElement[] getCurrentStack();
}
