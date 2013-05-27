package jlm.universe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.model.lesson.ExecutionProgress;
import jlm.universe.lightbot.LightBotEntity;

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
				if (script == null) 
					System.err.println("No "+progLang+" script source for entity "+this);
				else 
					engine.eval(script);
			}
		} catch (ScriptException e) {
			String msg = e.getCause().toString();
			
			if (Game.getInstance().isDebugEnabled())
				System.err.println(">>> Original error message\n"+msg+"<<<\n");
			Pattern location = Pattern.compile("File .<script>., line (\\d*), (.*)", Pattern.DOTALL);  
			Matcher locationMatcher = location.matcher(msg);
			
			if (locationMatcher.find()) {
				progress.setCompilationError( "Error in entity "+getName()+" at line "+
						(Integer.parseInt(locationMatcher.group(1)) - getScriptOffset(progLang)+1)+
						", "+locationMatcher.group(2));
			} else {
				progress.setCompilationError( e.getCause().toString() );
			}
		} catch (Exception e) {
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
