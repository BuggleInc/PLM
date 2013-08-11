package jlm.core.model.lesson;

import java.util.HashMap;
import java.util.Map;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.model.session.SourceFile;
import jlm.universe.World;

/** This class of Exercises are useful to merge the entity and world within the same class.
 * #BatExercice is a known implementation 
 *
 * This class is full of crude hacks. All come from the fact that we want to merge the entity 
 * and the exercise in the same class. We do so to avoid having too much files on disk. 
 * Thanks to Java stupid idea preventing to have several public classes in the same file... 
 * 
 * Then we don't have any entity on disk and we build some from scratch when we need to 
 * compile the student code. The exercise must then contain a BEGIN SKEL / END SKEL section 
 * that we will use as entity content.
 *  
 * This injected part is always a method "void run(BatTest t)" that invokes the user provided method (that only the exercise knows) 
 * with the right prototype (likewise). The values of parameters are taken from the test, and the the result is stored back in the test, too.
 * 
 * Example:
 *  
 *  // BEGIN SKEL 
 *  public void run(BatTest t) {
 *		t.setResult( sleepIn((Boolean)t.getParameter(0),(Boolean)t.getParameter(1)) );		
 *	}
 *	// END SKEL 
 *
 * Black magic is then at play to reconstruct the entity around this content. 
 * The classical template (containing the user function prototype, parameters, 
 * and eventually its body) is added to the mixture as initial student content.
 * 
 * The black magic darkens further when compiling the solution, in JUnit tests of Java compilation. 
 * The content of the solution provided in the templating "entity" is then saved 
 * during the parsing, specifically for that case, and readded to the entity we build for 
 * the purpose of testing the compilation. This still constitutes a good test because we want to 
 * see what happen if the student would have typed the correct solution, so here we are. 
 * 
 * Of course, this mess is not used to compute the answers of each tests at initialization. 
 * Instead, we use the run(BatTest) directly in the ExerciseTemplatingEntity.
 * 
 * Python also takes an alternative approach when evaluating the student code: 
 * the parameters of langTemplate are stored somewhere and reused directly in mutateEntity.
 * The trick in this case is that the name of each BatTest is a valid chunk of python code. So, this works:
 * 					engine.put("thetest",test);
 *					engine.eval("thetest.setResult("+test.getName()+")");
 * test.getName() will write the python code to evaluate the user function on the provided parameters, 
 * and its return value is passed to the BatTest that was injected in the scripting world for that. 
 *  
 * This becomes particularly hairy for scala, that is both typed and compiled.  
 * Part of the initial content comes from the langTemplate parameters (what's in the template in Java, ie the user function template) 
 * while the rest comes from the SKEL section of Java (how to invoke this user function from what's in a BatTest).
 * This works because this little code is both valid Java and valid Scala. Ouf! 
 *  
 * One limitation is that every such entity must be written in Java, but I can live with it for now.
 */

public abstract class ExerciseTemplatingEntity extends ExerciseTemplated {
	protected Map<ProgrammingLanguage,String> corrections = new HashMap<ProgrammingLanguage, String>();
	private boolean isSetup = false;
	
	public ExerciseTemplatingEntity(Lesson lesson) {
		super(lesson);
	}
	protected void setup(World[] ws, String entName, String template) {
		this.tabName=entName;
		setupWorlds(ws);
		
		try {
			newSourceFromFile(Game.JAVA, entName, getClass().getCanonicalName());
			addProgLanguage(Game.JAVA);
		} catch (NoSuchEntityException e1) {
			throw new RuntimeException("ExerciseTemplatingEntities must be templated in Java for now, and use langTemplate afterward. Sorry -- patch warmly welcome if you manage to improve that piece of mess.");
		}
		
		SourceFile sf = sourceFiles.get(Game.JAVA).get(0);
		sf.setCorrection("$package "+template+" "+sf.getTemplate()+sf.getCorrection()+" }");
		sf.setTemplate("$package "+template+" "+sf.getTemplate()+" $body }");
		//System.out.println("New template: "+sf.getTemplate());
		
		
		computeAnswer();
		isSetup  = true;
	}
	protected void langTemplate(ProgrammingLanguage pl, String entName, String initialCode, String correction) {
		/* The following test is intended to make sure that this function is called before setup() right above.
		 * This is because setup() needs all programming languages to be declared when it runs */
		if (isSetup)
			throw new RuntimeException("The exercise "+getName()+" is already setup, too late to add a programming language template");
		
		newSource(pl, entName, initialCode, "$body",0,"(templating entity)");
		corrections.put(pl, initialCode+correction);
		addProgLanguage(pl);
	}
}
