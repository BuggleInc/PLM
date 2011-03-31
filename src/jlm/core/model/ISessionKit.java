package jlm.core.model;

/** 
 * This interface is to be implemented by every session kits, aka mecanisms able to 
 * save and restore the state of the code written by the students. 
 * 
 * For now, 2 sessions kits are implemented in JLM: {@link FileSessionKit} and {@link ZipSessionKit}. 
 * The one used is hardcoded in the variable {@link jlm.core.model.Game#sessionKit}. 
 * There is no way to switch from the interface.
 */
public interface ISessionKit {

	public void store() ;
	
	public void load() ;
	
	public void cleanUp() ;
	
}
