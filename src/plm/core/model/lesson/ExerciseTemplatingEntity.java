package plm.core.model.lesson;

import java.util.HashMap;
import java.util.Map;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.session.SourceFile;
import plm.universe.World;

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
 * We would like to reuse the skeleton from Java so that the parameters are properly casted,
 * but the syntax of type casting is very different in Scala and Java, so we provide the types to templateScala and it does the casting on its own. 
 * The user function's template is provided to templateScala just as in Python. 
 *  
 * One limitation is that every such entity must be written in Java, but I can live with it for now.
 */

public abstract class ExerciseTemplatingEntity extends ExerciseTemplated {
	protected Map<ProgrammingLanguage,String> corrections = new HashMap<ProgrammingLanguage, String>();
	private boolean isSetup = false;
	
	public ExerciseTemplatingEntity(Game game, Lesson lesson) {
		super(game, lesson);
	}
	protected void setup(World[] ws, String entName, String template) {
		this.tabName=entName;
		setupWorlds(ws,0);
		
		
		try {
			newSourceFromFile(Game.JAVA, entName, getClass().getCanonicalName());
			addProgLanguage(Game.JAVA);
		} catch (NoSuchEntityException e1) {
			throw new RuntimeException("ExerciseTemplatingEntities must be templated in Java for now, and use langTemplate afterward. Sorry -- patch warmly welcome if you manage to improve that piece of mess.");
		}
		
		SourceFile javaFile = sourceFiles.get(Game.JAVA).get(0);
		
		javaFile.setCorrection("$package "+template+" @SuppressWarnings(\"unchecked\") public void run(BatTest t) {\n"+javaFile.getTemplate()+"}\n"+javaFile.getCorrection()+" }");
		javaFile.setTemplate  ("$package "+template+" @SuppressWarnings(\"unchecked\") public void run(BatTest t) {  "+javaFile.getTemplate()+"}    $body }");
		//getGame().getLogger().log("New template: "+sf.getTemplate());
		
		if (getProgLanguages().contains(Game.SCALA)) {
			SourceFile scalaFile = sourceFiles.get(Game.SCALA).get(0);
			String header = "$package\n"
					+ "import plm.universe.bat.{BatEntity,BatWorld,BatTest}; \n"
					+ "import plm.universe.World; \n"
					+ "import scala.collection.JavaConverters._;\n"
					+ "class "+entName+" extends BatEntity { ";
			
			scalaFile.setCorrection(header+scalaFile.getCorrection()+" }");
			scalaFile.setTemplate  (header+scalaFile.getTemplate()  +" }");
		}
		
		computeAnswer();
		setSetup(true);
	}
	protected void templatePython(String entName, String initialCode, String correction) {
		/* The following test is intended to make sure that this function is called before setup() right above.
		 * This is because setup() needs all programming languages to be declared when it runs */
		if (isSetup())
			throw new RuntimeException("The exercise "+getName()+" is already setup, too late to add a programming language template.");
		if (this.getProgLanguages().contains(Game.PYTHON))
			throw new RuntimeException("The exercise "+getName()+" has two Python templates. Please fix this bug.");
		
		newSource(Game.PYTHON, entName, initialCode, "$body",0,"","");
		corrections.put(Game.PYTHON, initialCode+correction);
		addProgLanguage(Game.PYTHON);
	}
	protected void templateScala(String entName, String[] types, String initialCode, String correction) {
		if (isSetup())
			throw new RuntimeException("The exercise "+getName()+" is already setup, too late to add a programming language template.");
		if (this.getProgLanguages().contains(Game.SCALA))
			throw new RuntimeException("The exercise "+getName()+" has two Scala templates. Please fix this bug.");
		
		StringBuffer skeleton = new StringBuffer(" val res = ");
		skeleton.append(entName);
		skeleton.append("( ");
		for (int i=0;i<types.length;i++) {
			if (i>0)
				skeleton.append(", ");
			if (types[i].equals("List[Int]")) {
				skeleton.append("(if (t.getParameter("+i+") == null) {");
				skeleton.append("  Nil");
				skeleton.append("} else if (t.getParameter("+i+").isInstanceOf[lessons.recursion.cons.universe.RecList]) {");
				skeleton.append("  t.getParameter("+i+").asInstanceOf[lessons.recursion.cons.universe.RecList].toList().asInstanceOf[java.util.List[Int]].asScala.toList");
				skeleton.append("} else {");
				skeleton.append("  t.getParameter("+i+").asInstanceOf[java.util.List[Int]].asScala.toList");
				skeleton.append("}) ");
			} else {
				skeleton.append("t.getParameter(");
				skeleton.append(i);
				skeleton.append(").asInstanceOf[");
				skeleton.append(types[i]);
				skeleton.append("]");
			}
		}
		skeleton.append(");\n");
		skeleton.append("try {\n");
		skeleton.append("   t.setResult(if (res==null||res == Nil) {null} else {res.asInstanceOf[List[Int]].asJava})\n");
		skeleton.append("} catch {\n");
		skeleton.append("  case e:java.lang.ClassCastException => t.setResult(res)\n"); // primitive types cannot be converted to java, but I don't care (and cannot test whether res is a primitive type)
		skeleton.append("}\n");
		
		newSource(Game.SCALA, entName, initialCode, "\n   override def run(t: BatTest) {\n"+skeleton+"\n   }\n$body",14,
				                                    "\n   override def run(t: BatTest) {\n"+skeleton+"\n   }\n"+initialCode+correction,"Error");
		addProgLanguage(Game.SCALA);
	}
	/*protected void templateBlockly(String entName, String initialCode, String correction, String workspace) {
		/* The following test is intended to make sure that this function is called before setup() right above.
		 * This is because setup() needs all programming languages to be declared when it runs */
		/*if (isSetup())
			throw new RuntimeException("The exercise "+getName()+" is already setup, too late to add a programming language template.");
		if (this.getProgLanguages().contains(Game.BLOCKLY))
			throw new RuntimeException("The exercise "+getName()+" has two Blockly templates. Please fix this bug.");
		
		newSource(Game.BLOCKLY, entName, initialCode, "$body",0,"");
		newSource(Game.BLOCKLY, entName, workspace, "",0,"");
		corrections.put(Game.BLOCKLY, initialCode+correction);
		addProgLanguage(Game.BLOCKLY);
	}*/
	public boolean isSetup() {
		return isSetup;
	}
	public void setSetup(boolean isSetup) {
		this.isSetup = isSetup;
	}
}
