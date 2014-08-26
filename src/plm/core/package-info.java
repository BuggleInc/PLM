/**
 * Basic PLM infrastructure: Data Model, User Interface, basic exercise building bricks.
 * 
 * <p>
 * The classes defined directly in this package are stuff that were difficult 
 * to sort properly somewhere. The main sub-packages are
 * <ul>
 *   <li>{@link plm.core.model} Model of the PLM core: Game singleton, ability to save student 
 *                              files, log stuff, etc.</li>
 *   <li>{@link plm.core.model.lesson} Building bricks for exercises and lessons: storing students'
 *       code in memory and compiling it; grouping exercises together, providing templates to 
 *       students</li>                            
 *   <li>{@link plm.core.ui} User interface.</li>
 *   <li>{@link plm.core.ui.action} The action listeners of the UI, calling the right functions 
 *        of the game singleton.</li>
 * </ul>
 */
package plm.core;
