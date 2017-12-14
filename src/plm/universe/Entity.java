package plm.universe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import plm.core.lang.ProgrammingLanguage;
import plm.core.log.Logger;

import java.io.BufferedWriter;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/* Entities cannot have their own org.xnap.commons.i18n.I18n, use the static getGame().i18n instead.
 * 
 * This is because we have to pass the classname to the I18nFactory, but it seems to break 
 * stuff that our code generate new package names. This later case being forced by our use 
 * of the compiler, we cannot initialize an I18n stuff. 
 * 
 * Instead, the solution is to use the static field getGame().i18n, as it is done in AbstractBuggle::diffTo().
 */

@JsonTypeInfo(use = Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Entity extends Observable {
    protected String name = "(noname)";

    protected World world;

    private List<Operation> operations = new ArrayList<Operation>();
    Semaphore stepBegin = new Semaphore(0);
    Semaphore stepEnd = new Semaphore(0);



    public Entity() {
    }

    public Entity(String name) {
        this.name = name;
    }

    public Entity(String name, World w) {
        this.name = name;
        if (w != null) {
            w.addEntity(this);
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

    /**
     * Ideally, this should be used only from world.addEntity()
     */
    protected void setWorld(World world) {
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

    /**
     * Called from the maestro to let the entity do one step
     */
    public void allowOneStep() {
        //Logger.info("(maestro): Start step for "+getName()+"@"+getWorld().getName());
        this.stepBegin.release();
    }

    /**
     * Called from the maestro to wait until the entity is done doing its step, or until the timeout occurs.
     * The entity signals being done with its step using stepUI()
     * <p>
     * If the calling thread gets interrupted, it will still go down to its timeout value before stopping.
     *
     * @return true if the step ended properly, false on timeout
     */
    public boolean waitStepEnd(long timeoutMilli) {
        boolean interrupted = true;
        long startTime = System.currentTimeMillis();
        while (interrupted) {
            try {
                long now = System.currentTimeMillis();
                return stepEnd.tryAcquire(timeoutMilli - (now - startTime), TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
            }
        }
        return false; // Will never happen
    }

    /**
     * Delays the entity to let the user understand what's going on.
     * <p>
     * Calls to this function should be placed in important operation of the entity. There e.g. one such call in BuggleEntity.forward().
     */
    protected void stepUI() {
//        if (operations.size() > 0) {
//            world.addStep(operations);
//            operations = new ArrayList<Operation>();
//        }

        fireStackListener();
//      Logger.info(getName()+"@"+getWorld().getName()+" done with this step.");
        if (inited) {
            stepEnd.release();
            stepBegin.acquireUninterruptibly();
        }
    }

    /**
     * Copy fields of the entity passed in argument
     */
    public void copy(Entity other) {
        setName(other.getName());
        setWorld(other.getWorld()); // FIXME: killme? I guess that we always reset the world after copy.
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
        if (stackListeners.isEmpty())
            return;
        StackTraceElement[] trace = getCurrentStack();
        for (IEntityStackListener l : stackListeners)
            l.entityTraceChanged(this, trace);
    }

    @JsonIgnore
    public StackTraceElement[] getCurrentStack() {
        return Thread.currentThread().getStackTrace();
    }

    /**
     * Retrieve one parameter from the world
     */
    public Object getParam(int i) {
        return world.parameters[i];
    }

    protected int getParamsAmount() {
        return world.parameters.length;
    }

    /**
     * Returns whether this is the entity selected in the interface
     */
    @JsonIgnore
    public boolean isSelected() {
        // FIXME: Re-implement me or not needed anymore?
        return false;
    }

    /**
     * Run this specific entity, encoding the student logic to solve a given exercise.
     * <p>
     * This method is redefined by the leafs of the inheritance tree (the entities involved in exercises)
     */
    public abstract void run() throws Exception;

    /**
     * Allows Entity to communicate with external programs, as needed to execute C programs
     */
    public abstract void command(String command, BufferedWriter out);

    private Map<ProgrammingLanguage, String> script = new HashMap<ProgrammingLanguage, String>(); /* What to execute when running a scripting language */

    public void setScript(ProgrammingLanguage lang, String s) {
        script.put(lang, s);
    }

    public String getScript(ProgrammingLanguage lang) {
        return script.get(lang);
    }

    private Map<ProgrammingLanguage, Integer> scriptOffset = new HashMap<ProgrammingLanguage, Integer>(); /* the offset to apply to error messages */

    public void setScriptOffset(ProgrammingLanguage lang, int offset) {
        scriptOffset.put(lang, offset);
    }

    public Integer getScriptOffset(ProgrammingLanguage lang) {
        Integer res = scriptOffset.get(lang);
        return res == null ? 0 : res;
    }

    @JsonIgnore
    public List<Operation> getOperations() {
        return operations;
    }

//    public void addOperation(Operation operation) {
//        operations.add(operation);
//    }
}
