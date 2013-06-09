package jlm.universe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.model.lesson.ExecutionProgress;
import jlm.universe.lightbot.LightBotEntity;

import org.python.core.PyException;
import org.python.core.PyTraceback;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

public abstract class Entity {
	protected String name;
	protected World world;
	
	private Semaphore oneStepSemaphore = new Semaphore(0);
	
	public I18n i18n = I18nFactory.getI18n(getClass(),"org.jlm.i18n.Messages",Game.getInstance().getLocale(), I18nFactory.FALLBACK);
	
	public Entity() {}
	
	public Entity(String name) {
		this.name=name;
	}
	public Entity(String name, World w) {
		this.name=name;
		if (w != null) {
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
	/** Copy constructor */
	public abstract Entity copy();

	
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
		ScriptEngine engine ;
		try {
			if (progLang.equals(Game.JAVA)||progLang.equals(Game.LIGHTBOT)) {
				run();
			} else {
				/* We could try to optimize here by not starting one engine for each entity but only one per language, in which the entities would be in separate contexts. 
				 * On the other hand, the garbage collection would be harder this way and it works as is... */
				ScriptEngineManager manager = new ScriptEngineManager();       
				engine = manager.getEngineByName(progLang.getLang().toLowerCase());
				if (engine==null) 
					throw new RuntimeException("Failed to start an interpreter for "+progLang.getLang().toLowerCase());

				/* Inject the entity into the scripting world so that it can forward script commands to the world */
				engine.put("entity", this);
				/* Inject commands' wrappers that forward the calls to the entity */
				this.getWorld().setupBindings(progLang,engine);
				
				if (progLang.equals(Game.PYTHON)) 
					engine.eval(
							/* getParam is in every Entity, so put it here to not request the universe to call super.setupBinding() */
							"def getParam(i):\n"+
							"  return entity.getParam(i)\n"+
									
							/* Ugly hack, but print is currently not working! Ugh, taste my axe, bastard! */
							"import java.lang.System.err\n"+
							"def log(a):\n"+
							"  java.lang.System.err.print(\"%s: %s\" %(entity.getName(),a))");									
					
				
				String script = getScript(progLang);
				
				if (Game.getInstance().isDebugEnabled())
					System.err.println("Here is the script >>>>"+script+"<<<<");
				
				if (script == null) 
					System.err.println("No "+progLang+" script source for entity "+this);
				else 
					engine.eval(script);
			}
		} catch (ScriptException e) {
			if (e.getCause() instanceof PyException) { // This seem to be all exceptions raised by python
				PyException cause = (PyException) e.getCause();
				
				StringBuffer msg = new StringBuffer();
	
				if (Game.getInstance().isDebugEnabled()) {
					System.err.println("CAUSE: "+cause.value.toString());
					System.err.println("MSG: "+e.getMessage());
					System.err.println("BT: "+msg);
				}
				
				//if (__builtin__.isinstance(cause.value, Py.SyntaxError)) {
				//	System.err.println("cleanly found a syntaxerror");
				//}
				
				if (cause.type.toString().equals("<type 'exceptions.SyntaxError'>")) {
					msg.append("Syntax error at line "+((cause.value.__findattr__("lineno").asInt())-getScriptOffset(Game.PYTHON))
							+": "+cause.value.__findattr__("msg")+"\n");
					msg.append("In doubt, check your indentation, and that you don't mix tabs and spaces\n");
					
				} else { /* It makes sense to display a backtrace for any errors but syntax ones */
					
					if (cause.type.toString().equals("<type 'exceptions.NameError'>")) {
						msg.append("NameError raised: You seem to use a non-existent identifier; Please check for typos\n");
						msg.append(cause.value+"\n");
					} else if (cause.type.toString().equals("<type 'exceptions.TypeError'>")) {
						msg.append("TypeError raised: you are probably misusing a function or something.\n");
						msg.append(cause.value+"\n");
						
						/* FIXME: how could we factorize the world's error? */ 
					} else if (cause.type.toString().equals("<type 'jlm.universe.bugglequest.exception.NoBaggleUnderBuggleException'>")) {
						msg.append("Error: there is no baggle to pickup under the buggle");
					} else if (cause.type.toString().equals("<type 'jlm.universe.bugglequest.exception.AlreadyHaveBaggleException'>")) {
						msg.append("Error: a buggle cannot carry more than one baggle at the same time");
					} else if (cause.type.toString().equals("<type 'jlm.universe.bugglequest.exception.BuggleInOuterSpaceException'>")) {
						msg.append("Error: your buggle just teleported to the outer space...");
					} else if (cause.type.toString().equals("<type 'jlm.universe.bugglequest.exception.BuggleWallException'>")) {
						msg.append("Error: your buggle just hit a wall. That hurts.");
						
					} else {
						msg.append("Unknown error (please report): "+cause.type.toString()+"\n");
						msg.append("Its value is: "+cause.value+"\n");

					}
				

					/* The following is very inspired from <jython>/src/org/python/core/PyTraceback.java, 
					 * even if we cannot reuse directly this implementation since we want to change all linenos on the fly. 
					 */
					PyTraceback tb = cause.traceback;
					while (tb != null) {
						tb.tb_lineno-= getScriptOffset(Game.PYTHON);
						if (tb.tb_frame == null || tb.tb_frame.f_code == null) {
							msg.append(String.format("  (no code object) at line %s\n", tb.tb_lineno));
						} else {
							msg.append(String.format("  File \"%.500s\", line %d, in %.500s\n",
									tb.tb_frame.f_code.co_filename, tb.tb_lineno, tb.tb_frame.f_code.co_name));
						}
						tb = (PyTraceback) tb.tb_next;
					}
				}				
				progress.setCompilationError(msg.toString());
			}
		} catch (Exception e) {
			String msg = "Script evaluation raised an exception that is not a ScriptEsception but a "+e.getClass()
							+".\n Please report this as a bug against JLM, with all details allowing to reproduce it.";

			System.err.println(msg);
			progress.setCompilationError(msg);
			e.printStackTrace();
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
