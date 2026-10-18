package plm.universe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import plm.core.lang.ProgrammingLanguage;
import plm.core.lang.RemoteExecutionLang;
import plm.core.lang.primitives.ExternalPrimitiveLanguage;
import plm.core.lang.primitives.PrimitiveMethod;
import plm.core.lang.primitives.PrimitivePot;
import plm.core.lang.primitives.PrimitiveRegistration;
import plm.core.model.Game;
import plm.core.model.Logger;
import plm.core.model.lesson.RunOutcome;
import plm.core.ui.PlmHtmlEditorKit;
import plm.core.ui.WorldView;
import plm.core.utils.FileUtils;

public abstract class World {
  private boolean isDelayed = false; // whether we display interactively or not
  private boolean isAnswer  = false;
  private boolean randomized = false; // whether the initial state is random, so that the answer cannot be cached
  private int delay         = 100; // delay between two instruction executions of an entity.

  protected List<Entity> entities = new ArrayList<Entity>();

  private String name;

  public World(String name) { this.name = name; }

  public World(World w2)
  {
    this(w2.getName());
    reset(w2);
  }

  public World copy()
  {
    World res = null;
    try {
      res = this.getClass().getConstructor(this.getClass()).newInstance(this);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    return res;
  }
  public String getDebugInfo() { return ""; }

  /**
   * Reset the content of a world to be the same than the one passed as
   * argument
   *
   * @param initialWorld
   */
  public void reset(World initialWorld)
  {
    entities = new ArrayList<Entity>();
    for (Entity oldEntity : initialWorld.entities) {
      try {
        Entity newEntity = oldEntity.getClass().getDeclaredConstructor().newInstance();
        newEntity.copy(oldEntity);
        addEntity(newEntity);
      } catch (Exception e) {
        throw new RuntimeException("Cannot copy entity of class " + oldEntity.getClass().getName(), e);
      }
    }
    this.isDelayed  = initialWorld.isDelayed;
    this.randomized = initialWorld.randomized;
    this.delay      = initialWorld.delay;
    this.parameters = (initialWorld.parameters != null ? initialWorld.parameters.clone() : null);
    notifyEntityUpdateListeners();
    notifyWorldUpdatesListeners();
  }

  /**
   * Replace entities inside the world with a new entity type
   * Principally used when parsing to override the parsed entity type
   *
   * @param newEntitySupplier The supplier creating a new instance of the new entity type
   *
   * @return for chaining purposes
   */
  public World replaceEntities(Supplier<? extends Entity> newEntitySupplier)
  {
    setEntities(getEntities()
                    .stream()
                    .map(entity -> {
                      Entity buggle = newEntitySupplier.get();
                      buggle.copy(entity);
                      return buggle;
                    })
                    .toList());

    return this;
  }

  public String getName() { return this.name; }

  public void setName(String n) { name = n; }

  public boolean isDelayed() { return isDelayed; }
  /** returns the delay to apply */
  public int getDelay() { return this.delay; }
  /**
   * set the value of the UI delay which will be used on doDelay()
   *
   * Default value: 100ms
   */
  public void setDelay(int d)
  {
    this.delay = d;
    notifyWorldUpdatesListeners(); // notify the speed slider model
  }
  /** set current UI delay to what was defined as max UI delay with setDelayUI() */
  public void doDelay() { isDelayed = true; }
  /** set current UI delay to 0 */
  public void doneDelay() { isDelayed = false; }
  public void setAnswerWorld() { isAnswer = true; }
  public boolean isAnswerWorld() { return isAnswer; }
  public boolean isRandomized() { return randomized; }

  /** To be called by the universes when they randomize the initial state of the world */
  protected void setRandomized() { randomized = true; }

  public void addEntity(Entity b)
  {
    if (b.getWorld() != this)
      b.setWorld(this);
    entities.add(b);
    notifyEntityUpdateListeners();
  }
  public void removeEntity(Entity b)
  {
    if (!entities.remove(b))
      System.out.println("Ignoring a request to remove an unknown entity");
    notifyEntityUpdateListeners();
  }

  public void emptyEntities()
  {
    entities = new ArrayList<Entity>();
    notifyEntityUpdateListeners();
  }

  public void setEntities(List<Entity> l)
  {
    entities = l;
    notifyEntityUpdateListeners();
  }

  public int getEntityCount() { return entities.size(); }

  public Entity getEntity(int i) { return entities.get(i); }
  public List<Entity> getEntities() { return entities; }

  public void runEntities(List<Future<?>> runnerVect, final RunOutcome progress, final ProgrammingLanguage pl, final String executable)
  {
    for (final Entity b : entities) {
      Future<?> future = Game.submitEntityTask(new Runnable() {
        public void run()
        {
          Game.getInstance().statusArgAdd(getName());
          pl.runEntity(b, progress, executable);
          Game.getInstance().statusArgRemove(getName());
        }
      });
      runnerVect.add(future);
    }
  }

  /* who's interested in every details of the world changes */
  private ArrayList<IWorldView> worldUpdatesListeners = new ArrayList<IWorldView>();

  /* who's only interested in entities creation and destructions */
  private ArrayList<IWorldView> entitiesUpdateListeners = new ArrayList<IWorldView>();

  public void addWorldUpdatesListener(IWorldView v)
  {
    synchronized (this.worldUpdatesListeners) {
      this.worldUpdatesListeners.add(v);
    }
  }

  public void removeWorldUpdatesListener(IWorldView v)
  {
    synchronized (this.worldUpdatesListeners) {
      this.worldUpdatesListeners.remove(v);
    }
  }

  public void notifyWorldUpdatesListeners()
  {
    if (worldUpdatesListeners.isEmpty())
      return;
    synchronized (this.worldUpdatesListeners) {
      for (IWorldView v : this.worldUpdatesListeners) {
        v.worldHasMoved();
      }
    }
  }

  public void addEntityUpdateListener(IWorldView v)
  {
    synchronized (this.entitiesUpdateListeners) {
      this.entitiesUpdateListeners.add(v);
    }
  }

  public void removeEntityUpdateListener(IWorldView v)
  {
    synchronized (this.entitiesUpdateListeners) {
      this.entitiesUpdateListeners.remove(v);
    }
  }

  public void notifyEntityUpdateListeners()
  {
    synchronized (this.entitiesUpdateListeners) {
      for (IWorldView v : this.entitiesUpdateListeners) {
        v.worldHasChanged();
      }
    }
  }

  /* IO related */
  /** Returns whether this universe implements world I/O */
  public boolean haveIO() { return false; }

  /**
   * Reads a world from the .map file at path, by delegating to parse() and reporting malformed content as a BrokenWorldFileException.
   * The universes that need another way of reading may override this method instead.
   */
  public World readFromFile(String path) throws IOException, BrokenWorldFileException
  {
    try (BufferedReader reader = FileUtils.newFileReader(path, "map", false)) {
      return parse(reader, path);
    } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) { // Includes NumberFormatException
      throw new BrokenWorldFileException(path + ".map: malformed content (" + e.getMessage() + ").");
    }
  }

  /** Builds a world from the content of a .map file, as written by writeToFile(). The path is only used in error messages. */
  protected World parse(BufferedReader reader, String path) throws IOException, BrokenWorldFileException
  {
    throw new RuntimeException("This universe does not implement world I/O");
  }

  public void writeToFile(BufferedWriter f) throws IOException {}

  /** Returns the value of the next line of a .map file, that must be of the form "key: value" */
  protected static String readField(BufferedReader reader, String path, String key) throws IOException, BrokenWorldFileException
  {
    String line = reader.readLine();
    if (line == null || !line.startsWith(key + ": "))
      throw new BrokenWorldFileException(path + ".map: expected a line starting with '" + key + ": ' but got '" + line + "'.");
    return line.substring(key.length() + 2);
  }

  /** Returns the integers of the next line of a .map file, that must be of the form "key: 1 2 3" */
  protected static int[] readInts(BufferedReader reader, String path, String key) throws IOException, BrokenWorldFileException
  {
    String line = readField(reader, path, key);
    return line.isEmpty() ? new int[0] : Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
  }

  /** Writes a line of a .map file in the form "key: value" */
  protected static void writeField(BufferedWriter writer, String key, Object value) throws IOException { writer.write(key + ": " + value + "\n"); }

  /** Writes a line of a .map file in the form "key: 1 2 3" */
  protected static void writeField(BufferedWriter writer, String key, int[] values) throws IOException
  {
    writeField(writer, key, Arrays.stream(values).mapToObj(String::valueOf).collect(Collectors.joining(" ")));
  }

  /** Writes the world into the file. The content goes to a temporary file first, so that a failure never leaves a partial file behind. */
  public void writeToFile(File outputFile) throws IOException
  {
    Path temporary = Paths.get(outputFile.getPath() + ".tmp");
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(temporary.toFile()))) {
      writeToFile(bw);
    } catch (IOException | RuntimeException e) {
      Files.deleteIfExists(temporary);
      throw e;
    }
    Files.move(temporary, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
  }

  /* Find my UI */
  public WorldView getView()
  {
    return new WorldView(this) {
      private static final long serialVersionUID = 1L;
      @Override public boolean isWorldCompatible(World world)
      {
        return false;
      }
    };
  }
  public EntityControlPanel getEntityControlPanel()
  {
    return new EntityControlPanel() {
      private static final long serialVersionUID = 1L;
      @Override public void setEnabledControl(boolean enabled) {}
    };
  }
  public abstract ImageIcon getIcon();

  @Override public int hashCode()
  {
    final int prime = 31;
    int result      = 1;
    result          = prime * result + ((entities == null) ? 0 : entities.hashCode());
    result          = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  /**
   * Checks whether the receiver world wins the exercise
   *
   * https://www.youtube.com/watch?v=9QS0q3mGPGg
   *
   * @param standard a correction world that should be used to compare the receiver state
   */
  public boolean winning(World standard) { return this.equals(standard); }

  @Override public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof World))
      return false;
    World other = (World)obj;
    if (entities == null) {
      if (other.entities != null)
        return false;
    } else if (!entities.equals(other.entities))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

  String about = null;

  /** The documentation of the world (Ctrl-H): its html file, in the current human and programming languages. */
  public String getAbout()
  {
    if (about == null) {
      String filename = getClass().getCanonicalName().replace('.', File.separatorChar);
      try {
        about = FileUtils.readContentAsText(filename, "html", true).toString();
      } catch (IOException ex) {
        about = "File " + filename + ".html not found.";
      }
    }
    String html = about.replace("[!primitives/]", getPrimitivesDoc());
    return "<html>\n" + PlmHtmlEditorKit.getCSS() + "<body>\n" + PlmHtmlEditorKit.filterHTML(html, Game.getInstance().isDebugEnabled()) + "</body>\n</html>\n";
  }

  /**
   * HTML table documenting the primitives that the first entity may use, in the current programming language. The groups are
   * ordered as declared by @PrimitiveGroups (undeclared ones last) and the primitives by name. The undocumented and the forbidden
   * primitives are not shown.
   */
  private String getPrimitivesDoc()
  {
    if (entities.isEmpty() || !(Game.getInstance().getProgrammingLanguage() instanceof RemoteExecutionLang lang))
      return "";
    Entity entity = entities.get(0);

    Map<String, List<PrimitiveMethod>> groups = new LinkedHashMap<>();
    PrimitiveRegistration.getPrimitiveGroupsForEntity(entity.getClass()).forEach(group -> groups.put(group, new ArrayList<>()));
    for (PrimitiveMethod primitive : PrimitiveRegistration.getMinimalPrimitiveForEntity(entity.getClass()).values())
      if (!primitive.doc().isEmpty() && !entity.getForbiddenPrimitives().containsKey(primitive.name()))
        groups.computeIfAbsent(primitive.group(), group -> new ArrayList<>()).add(primitive);

    ExternalPrimitiveLanguage generator = lang.getPrimitiveGenerator();
    StringBuilder sb                    = new StringBuilder("<table border=1>\n");
    groups.forEach((group, primitives) -> {
      if (!group.isEmpty() && !primitives.isEmpty())
        sb.append("<tr><td colspan=2 align=center><b>").append(Game.i18n.tr(group)).append("</b></td></tr>\n");
      for (PrimitiveMethod primitive : primitives)
        sb.append("<tr><td><code>")
            .append(generator.signature(primitive, Game.i18n.trc(PrimitivePot.NAME_CONTEXT, primitive.name())))
            .append("</code></td><td>")
            .append(Game.i18n.tr(primitive.doc()))
            .append("</td></tr>\n");
    });
    return sb.append("</table>\n").toString();
  }

  /**
   * Set about to null in order to allows it to be reloaded in the right language
   */
  public void resetAbout() { this.about = null; }

  protected Object[] parameters = null;
  public void setParameter(Object[] parameters) { this.parameters = parameters; }
  public Object[] getParameters() { return parameters; }
  public Object getParameter(int i) { return parameters[i]; }

  public void setSelectedEntity(Entity e)
  {
    notifyWorldUpdatesListeners(); // EntityUpdateListeners();
  }

  /**
   * Returns a textual representation of the differences from the receiver world to the one in parameter
   *
   * The BatWorld needs to know the ProgrammingLanguage to adapt its output, so give that parameter to all worlds.
   */
  public abstract String diffTo(World world, ProgrammingLanguage lang);
}
