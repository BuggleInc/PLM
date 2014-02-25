package plm.universe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import plm.core.PythonExceptionDecipher;
import plm.core.model.Game;
import plm.core.model.ProgrammingLanguage;
import plm.core.model.lesson.ExecutionProgress;
import plm.universe.lightbot.LightBotEntity;

/* Entities cannot have their own org.xnap.commons.i18n.I18n, use the static Game.i18n instead.
 * 
 * This is because we have to pass the classname to the I18nFactory, but it seems to break 
 * stuff that our code generate new package names. This later case being forced by our use 
 * of the compiler, we cannot initialize an I18n stuff. 
 * 
 * Instead, the solution is to use the static field Game.i18n, as it is done in AbstractBuggle::diffTo().
 */

public abstract class Entity {
	protected String name;
	protected World world;

	private Semaphore oneStepSemaphore = new Semaphore(0);

	public Entity() {}

	public Entity(String name) {
		this.name=name;
	}
	public Entity(String name, World w) {
		this.name=name;
		if (w != null) { /* FIXME: why isn't this symmetric with setWorld? */
			this.world = w;
			world.addEntity(this);
		}
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}	

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	/* This is to allow exercise to forbid the use by students of some functions 
	 * which are mandatory for core mechanism. See welcome.ArrayBuggle to see how it forbids setPos(int,int)  
	 */
	private boolean inited = false;
	public boolean isInited() {
		return inited;
	}
	public void initDone() {
		inited = true;		
	}

	public void allowOneStep() {
		this.oneStepSemaphore.release();
	}

	/** Delays the entity to let the user understand what's going on.
	 *  
	 * Calls to this function should be placed in important operation of the entity. There e.g. one such call in BuggleEntity.forward().  
	 */
	protected void stepUI() {		
		fireStackListener();
		world.notifyWorldUpdatesListeners();
		if (world.isDelayed()) {
			if (Game.getInstance().stepModeEnabled()) {
				this.oneStepSemaphore.acquireUninterruptibly();
			} else {	
				try {
					if (world.getDelay()>0) // seems that sleep(0) takes time (yield thread?)
						Thread.sleep(world.getDelay());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}		
	}

	/** Copy fields of the entity passed in argument */
	public void copy(Entity other) {
		setName(other.getName());
		setWorld(other.getWorld());
	}

	/* Stuff related to tracing mechanism.
	 * 
	 * This is the ability to highlight the current instruction in step-by-step execution. 
	 * 
	 * Right now, this is only used for LightBot because I'm not sure of how to retrieve the current point of execution in java or scripting
	 */
	ArrayList<IEntityStackListener> stackListeners = new ArrayList<IEntityStackListener>();
	public void addStackListener(IEntityStackListener l) {
		stackListeners.add(l);
	}
	public void removeStackListener(IEntityStackListener l) {
		stackListeners.remove(l);
	}
	public void fireStackListener() {
		StackTraceElement[] trace = getCurrentStack();
		for (IEntityStackListener l:stackListeners)
			l.entityTraceChanged(this, trace);		
	}
	public StackTraceElement[] getCurrentStack() {
		return Thread.currentThread().getStackTrace();
	}

	/** Retrieve one parameter from the world */
	public Object getParam(int i) {
		return world.parameters[i];
	}	
	protected int getParamsAmount() {
		return world.parameters.length;
	}
	
	/** Returns whether this is the entity selected in the interface */
	public boolean isSelected() {
		return this == Game.getInstance().getSelectedEntity();
	}

	/** Run this specific entity, encoding the student logic to solve a given exercise. 
	 * 
	 *  This method is redefined by the leafs of the inheritance tree (the entities involved in exercises)   
	 *   
	 *  @see #runIt() that execute an entity depending on the universe and programming language
	 *    
	 */
	protected abstract void run() throws Exception;

	/** Make the entity run, according to the used universe and programming language.
	 * 
	 * This task is not trivial given that it depends on the universe and the programming language:
	 *  * In most universes, the active part is the entity itself. But in the Bat universe, the 
	 *    student-provided method (that is not a real entity but part of the world directly) 
	 *    is run against all testcase, that are not real worlds either.
	 *    
	 *  * Java entities are launched by just executing their {@link #run()} method that 
	 *    was redefined by the student (possibly with some templating)
	 *  * LightBot entities are launched by executing the {@link LightBotEntity#run()} method, 
	 *    that is NOT defined by the student, but interprets the code of the students.
	 *  * Python (and other scripting language) entities are launched by injecting the 
	 *    student-provided code within a {@link ScriptEngine}. 
	 *    In this later case, the java entity is injected within the scripting world so that it 
	 *    can forward the student commands to the world. 
	 * 
	 *  @see #run() that encodes the student logic in Java
	 */
	public void runIt(ExecutionProgress progress) {
		ProgrammingLanguage progLang = Game.getProgrammingLanguage();
		ScriptEngine engine = null;
		if (progLang.equals(Game.JAVA)||progLang.equals(Game.SCALA)||progLang.equals(Game.LIGHTBOT)) {
			try {
				run();
			} catch (Exception e) {
				String msg = Game.i18n.tr("The execution of your program raised a {0} exception: {1}\n" + 
						" Please fix your code.\n",e.getClass().getName(),e.getLocalizedMessage());
				
				for (StackTraceElement elm : e.getStackTrace())
					msg+= "   at "+elm.getClassName()+"."+elm.getMethodName()+" ("+elm.getFileName()+":"+elm.getLineNumber()+")"+"\n";
				
				System.err.println(msg);
				progress.setCompilationError(msg);
				e.printStackTrace();
			}
		} else {
			try {
				ScriptEngineManager manager = new ScriptEngineManager();       
				engine = manager.getEngineByName(progLang.getLang().toLowerCase());
				if (engine==null)
					throw new RuntimeException(Game.i18n.tr("No ScriptEngine for {0}. Please check your classpath and similar settings.",progLang.getLang()));

				/* Inject the entity into the scripting world so that it can forward script commands to the world */
				engine.put("entity", this);
				
				
				/* Inject commands' wrappers that forward the calls to the entity */
				this.getWorld().setupBindings(progLang,engine);

				/* getParam is in every Entity, so put it here to not request the universe to call super.setupBinding() */
				if (progLang.equals(Game.PYTHON)) 
					engine.eval(
							"def getParam(i):\n"+
							"  return entity.getParam(i)\n" +
							"def isSelected():\n" +
							"  return entity.isSelected()\n");		

				String script = getScript(progLang);

				if (script == null) { 
					System.err.println(Game.i18n.tr("No {0} script source for entity {1}. Please report that bug against PLM.",progLang,this));
					return;
				}
				if (progLang.equals(Game.PYTHON)) {
					/* that's not really clean to get the output working when we 
					 * redirect to the graphical console, but it works. */
					setScriptOffset(progLang, getScriptOffset(progLang)+7);
					script= "import sys;\n" +
							"import java.lang;\n" +
							"class PLMOut:\n" +
							"  def write(obj,msg):\n" +
							"    java.lang.System.out.print(str(msg))\n" +
							"sys.stdout = PLMOut()\n" +
							"sys.stderr = PLMOut()\n" +
							script;
				}
				engine.eval(script);
				
			} catch (ScriptException e) {
				if (Game.getInstance().isDebugEnabled()) 
					System.err.println("Here is the script in "+progLang.getLang()+" >>>>"+script+"<<<<");
				if (Game.getInstance().canPython && PythonExceptionDecipher.isPythonException(e))
					PythonExceptionDecipher.handlePythonException(e,this,progress);
				else {
					System.err.println(Game.i18n.tr("Received a ScriptException that does not come from Python.\n")+e);
					e.printStackTrace();
				}

			} catch (Exception e) {
				String msg = Game.i18n.tr("Script evaluation raised an exception that is not a ScriptException but a {0}.\n"+
						" Please report this as a bug against PLM, with all details allowing to reproduce it.\n" +
						"Exception message: {1}\n",e.getClass(),e.getLocalizedMessage());
				System.err.println(msg);
				for (StackTraceElement elm : e.getStackTrace()) 
					msg += elm.toString()+"\n";
				
				progress.setCompilationError(msg);
				e.printStackTrace();
			}
		}
	}

	private Map<ProgrammingLanguage,String> script = new HashMap<ProgrammingLanguage, String>(); /* What to execute when running a scripting language */
	public void setScript(ProgrammingLanguage lang, String s) {
		script.put(lang,  s);
	}
	public String getScript(ProgrammingLanguage lang) {
		return script.get(lang);
	}

	private Map<ProgrammingLanguage,Integer> scriptOffset = new HashMap<ProgrammingLanguage, Integer>(); /* the offset to apply to error messages */
	public void setScriptOffset(ProgrammingLanguage lang, int offset) {
		scriptOffset.put(lang,  offset);
	}
	public Integer getScriptOffset(ProgrammingLanguage lang) {
		Integer res = scriptOffset.get(lang);
		return res == null ? 0:res;
	}
}
