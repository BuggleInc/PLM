package plm.core.model.lesson;

import java.util.HashMap;
import java.util.Map;

import plm.core.lang.LangJava;
import plm.core.lang.LangPython;
import plm.core.lang.LangScala;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.session.SourceFile;
import plm.core.model.session.TemplatedSourceFileFactory;
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
	
	private String javaInitialCode = "";
	private String javaTemplate = "";
	private int javaOffset = 0;
	private String javaCorrection = "";
	
	private String scalaInitialCode = "";
	private String scalaTemplate = "";
	private int scalaOffset = 0;
	private String scalaCorrection = "";
	
	private String pythonInitialCode = "";
	protected String pythonTemplate = "";
	private int pythonOffset = 0;
	protected String pythonCorrection = "";
	
	public ExerciseTemplatingEntity(Game game, Lesson lesson) {
		super(game, lesson);
	}
	
	public ExerciseTemplatingEntity(String id, String name) {
		super(id, name);
		tabName = Character.toLowerCase(id.charAt(0)) + id.substring(1);
	}
	
	protected void setup(World[] ws, String extraImport, String extraBody) {
		setupWorlds(ws,0);
	}
	
	public void initSourceFiles(TemplatedSourceFileFactory sourceFileFactory, ProgrammingLanguage[] programmingLanguages)  {
		for(ProgrammingLanguage progLang: programmingLanguages) {
			if(progLang instanceof LangJava) {
				generateJavaSourceFile(sourceFileFactory, (LangJava) progLang);
			}
			if(progLang instanceof LangScala) {
				generateScalaSourceFile(sourceFileFactory, (LangScala) progLang);
			}
			if(progLang instanceof LangPython) {
				generatePythonSourceFile(sourceFileFactory, (LangPython) progLang);
			}
		}
	}
	
	protected void templateJava(String entName, String extraImport, String[] types, String initialCode, String correction) {
		String header = "$package\n"
				+ "import plm.universe.bat.BatEntity;\n"
				+ "import plm.universe.bat.BatWorld;\n"
				+ "import plm.universe.bat.BatTest;\n"
				+ "import plm.universe.World; \n"
				+ extraImport
				+ "public class "+entName+" extends BatEntity { ";
		
		StringBuffer skeleton = new StringBuffer("t.setResult( ");
		skeleton.append(tabName);
		skeleton.append("( ");
		for (int i=0;i<types.length;i++) {
			if (i>0)
				skeleton.append(", ");
			skeleton.append("(" + types[i] + ")");
			skeleton.append("t.getParameter("+i+")");
		}
		skeleton.append("));");

		javaInitialCode = initialCode;
		javaTemplate = header+" @SuppressWarnings(\"unchecked\") public void run(BatTest t) {  "+skeleton+"}\n$body }";
		javaOffset = 10;
		javaCorrection = header+" @SuppressWarnings(\"unchecked\") public void run(BatTest t) {\n"+skeleton+"}\n"+initialCode + correction+" }";
	}
	
	public void generateJavaSourceFile(TemplatedSourceFileFactory sourceFileFactory, LangJava java) {
		SourceFile sourceFile = sourceFileFactory.newSourceFromParams(getId(), javaInitialCode, javaTemplate, javaOffset, javaCorrection, "Error");
		addDefaultSourceFile(java, sourceFile);
		addProgLanguage(java);
	}
	
	protected void templatePython(String entName, String initialCode, String correction) {
		pythonInitialCode = initialCode;
		pythonCorrection =  correction;
	}
	
	public void generatePythonSourceFile(TemplatedSourceFileFactory sourceFileFactory, LangPython python) {
		SourceFile sourceFile = sourceFileFactory.newSourceFromParams(getId(), pythonInitialCode, "$body\n" + pythonTemplate, pythonOffset, "", "");
		corrections.put(python, pythonInitialCode+pythonCorrection+"\n"+pythonTemplate);
		addDefaultSourceFile(python, sourceFile);
		addProgLanguage(python);
	}
	
	protected void templateScala(String entName, String[] types, String initialCode, String correction) {
		StringBuffer skeleton = new StringBuffer(" val res = ");
		skeleton.append(tabName);
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
		
		
		String header = "$package\n"
				+ "import plm.universe.bat.{BatEntity,BatWorld,BatTest}; \n"
				+ "import plm.universe.World; \n"
				+ "import scala.collection.JavaConverters._;\n"
				+ "class "+tabName+" extends BatEntity { ";
		
		scalaInitialCode = initialCode;
		scalaTemplate = header + "\n   override def run(t: BatTest) {\n"+skeleton+"\n   }\n$body }";
		scalaOffset = 14;
		scalaCorrection =  header + "\n   override def run(t: BatTest) {\n"+skeleton+"\n   }\n"+initialCode+correction+" }";
	}
	
	public void generateScalaSourceFile(TemplatedSourceFileFactory sourceFileFactory, LangScala scala) {
		SourceFile sourceFile = sourceFileFactory.newSourceFromParams(getId(), scalaInitialCode, scalaTemplate, scalaOffset, scalaCorrection, "Error");
		addDefaultSourceFile(scala, sourceFile);
		addProgLanguage(scala);
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
