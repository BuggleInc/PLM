package jlm.universe;

public interface IEntityTracable {
	public void addTraceListener(IEntityTraceListener l);
	public void removeTraceListener(IEntityTraceListener l);
	public void fireTraceListener(String location);
	public String getCurrentTrace();
}
