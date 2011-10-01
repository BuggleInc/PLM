package jlm.core.model;

import java.io.File;

import jlm.core.model.lesson.Lesson;

/** 
 * This interface is to be implemented by every session kits, aka mecanisms able to 
 * save and restore the state of the code written by the students. 
 * 
 * For now, 2 sessions kits are implemented in JLM: {@link FileSessionKit} and {@link ZipSessionKit}. 
 * The one used is hardcoded in the variable {@link jlm.core.model.Game#sessionKit}. 
 * There is no way to switch from the interface.
 */
public interface ISessionKit {

	/** Saves the user content of all loaded lessons
	 * 
	 *  On error, the user is given an opportunity to cancel the procedure through a dialog window.
	 */
	public void storeAll() throws UserAbortException;	

	/** Saves the user content of all loaded lessons
	 * 
	 *  On error, the user is given an opportunity to cancel the procedure through a dialog window.
	 */
	public void storeAll(File path) throws UserAbortException;	
	
	/** Loads saved user content for all loaded lessons */
	public void loadAll();
	/** Loads saved user content for all loaded lessons */
	public void loadAll(File path);
	
	/** Saves the user content of the specified lessons
	 * 
	 *  On error, the user is given an opportunity to cancel the procedure through a dialog window.
	 */
	public void storeLesson(File path, Lesson l) throws UserAbortException;
	
	/** Loads saved user content of the specified lessons */
	public void loadLesson(File path, Lesson l);
	
	/** Removes all user content for all loaded lessons */
	public void cleanAll() ;
	
	/** Removes all user content for all loaded lessons */
	public void cleanLesson(Lesson l);

}
