package plm.universe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.Semaphore;
import plm.core.lang.ProgrammingLanguage;
import plm.core.lang.primitives.EntityPrimitives;
import plm.core.lang.primitives.PrimitiveRegistration;
import plm.core.model.Game;

@EntityPrimitives(EntityPrimitivesBase.class)
public abstract class Entity extends Observable implements EntityPrimitivesBase {
  protected String name = "(noname)";

  protected World world;
  /* Stuff related to tracing mechanism.
   *
   * This is the ability to highlight the current instruction in step-by-step execution.
   *
   * Right now, this is only used for LightBot because I'm not sure of how to retrieve the current point of execution in java or scripting
   */
  ArrayList<IEntityStackListener> stackListeners = new ArrayList<IEntityStackListener>();
  private Semaphore oneStepSemaphore                     = new Semaphore(0);
  private final Map<String, String> forbiddenPrimitives = new HashMap<>(); /* primitive name -> reason shown to the student */
  private Map<ProgrammingLanguage, Integer> scriptOffset = new HashMap<ProgrammingLanguage, Integer>(); /* the offset to apply to error messages */

  public Entity() {}

  public Entity(String name) { this.name = name; }

  public Entity(String name, World w)
  {
    this.name = name;
    if (w != null)
      w.addEntity(this);
  }

  @Override public String getName() { return this.name; }

  public void setName(String name) { this.name = name; }

  public World getWorld() { return world; }

  /**
   * Ideally, this should be used only from world.addEntity()
   */
  protected void setWorld(World world) { this.world = world; }

  /**
   * Forbids the student code to call the primitive of that name in this exercise. The reason (already translated) is appended to the error
   * message. To be called from the constructor of the exercise's entity, so that every copy of the entity forbids it too.
   */
  protected void forbid(String primitive, String reason)
  {
    if (!PrimitiveRegistration.getMinimalPrimitiveForEntity(getClass()).containsKey(primitive))
      throw new IllegalArgumentException("Cannot forbid " + primitive + ": " + getClass().getName() + " has no primitive of that name");
    forbiddenPrimitives.put(primitive, reason);
  }

  /** The primitives that the student code cannot call, with the reason why */
  public Map<String, String> getForbiddenPrimitives() { return Collections.unmodifiableMap(forbiddenPrimitives); }

  public void allowOneStep() { this.oneStepSemaphore.release(); }

  /**
   * Delays the entity to let the user understand what's going on.
   * <p>
   * Calls to this function should be placed in important operation of the entity. There e.g. one such call in BuggleEntity.forward().
   */
  protected void stepUI()
  {
    fireStackListener();
    world.notifyWorldUpdatesListeners();
    if (world.isDelayed()) {
      if (Game.getInstance().stepModeEnabled()) {
        try {
          this.oneStepSemaphore.acquire();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt(); // stopped while waiting for the next step: the caller sees that it was asked to stop
        }
      } else {
        try {
          if (world.getDelay() > 0) // seems that sleep(0) takes time (yield thread?)
            Thread.sleep(world.getDelay());
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt(); // the animation is skipped, and the caller sees that it was asked to stop
        }
      }
    }
  }

  /**
   * Copy fields of the entity passed in argument
   */
  public void copy(Entity other)
  {
    setName(other.getName());
    setWorld(other.getWorld()); // FIXME: killme? I guess that we always reset the world after copy.
  }

  public void addStackListener(IEntityStackListener l) { stackListeners.add(l); }

  public void removeStackListener(IEntityStackListener l) { stackListeners.remove(l); }

  public void fireStackListener()
  {
    if (stackListeners.isEmpty())
      return;
    StackTraceElement[] trace = getCurrentStack();
    for (IEntityStackListener l : stackListeners)
      l.entityTraceChanged(this, trace);
  }

  public StackTraceElement[] getCurrentStack() { return Thread.currentThread().getStackTrace(); }

  /**
   * Retrieve one parameter from the world
   */
  @Override public Object getParam(int i) { return world.parameters[i]; }

  @Override public int getParamCount() { return world.parameters.length; }

  /**
   * Returns whether this is the entity selected in the interface
   */
  @Override public boolean isSelected() { return this == Game.getInstance().getSelectedEntity(); }

  /**
   * Run this specific entity, encoding the student logic to solve a given exercise.
   * <p>
   * This method is redefined by the leafs of the inheritance tree (the entities involved in exercises)
   */
  public abstract void run() throws Exception;

  public void setScriptOffset(ProgrammingLanguage lang, int offset) { scriptOffset.put(lang, offset); }

  public Integer getScriptOffset(ProgrammingLanguage lang)
  {
    Integer res = scriptOffset.get(lang);
    return res == null ? 0 : res;
  }
}
