package lessons.sort.dutchflag.universe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.ui.ResourcesCache;
import plm.core.ui.WorldView;
import plm.core.utils.FileUtils;
import plm.universe.BrokenWorldFileException;
import plm.universe.Entity;
import plm.universe.EntityControlPanel;
import plm.universe.World;

public class DutchFlagWorld extends World {

  int[] content;
  int moveCount = 0;

  /** Copy constructor */
  public DutchFlagWorld(DutchFlagWorld world) { super(world); }

  /** Returns a component able of displaying the world */
  @Override public WorldView getView() { return new DutchFlagWorldView(this); }
  /** Returns the icon of the universe */
  @Override public ImageIcon getIcon() { return ResourcesCache.getIcon(this, "world_dutchflag.png"); }
  /** Returns the panel which let the user to interact dynamically with the world */
  @Override public EntityControlPanel getEntityControlPanel()
  {
    return new EntityControlPanel() {
      private static final long serialVersionUID = 1L;

      @Override public void setEnabledControl(boolean enabled)
      {
        // TODO Auto-generated method stub
      }
    };
  }

  /**
   * Regular DutchFlagWorld constructor that creates a random flag.
   * The randomness is seeded by the name: the initial state must be reproducible to cache the answer worlds.
   * @param name : the name of the world
   * @param size : the amount of elements
   */
  public DutchFlagWorld(String name, int size)
  {
    super(name);
    setDelay(size > 100 ? 20 : 200); // Delay (in ms) in default animations
    new DutchFlagEntity(this);

    Random rng = new Random(name.hashCode());
    content    = new int[size];
    for (int i = 0; i < size; i++)
      content[i] = rng.nextInt(3);
  }

  /** Same as above, but with only two colors: the one passed as colorRemoved is missing */
  public DutchFlagWorld(String name, int size, int colorRemoved)
  {
    super(name);
    setDelay(size > 100 ? 20 : 200); // Delay (in ms) in default animations
    new DutchFlagEntity(this);
    int[][] color = {{1, 2}, {0, 2}, {0, 1}};

    Random rng = new Random(name.hashCode());
    content    = new int[size];
    for (int i = 0; i < size; i++)
      content[i] = color[colorRemoved][rng.nextInt(2)];
  }

  @Override public boolean haveIO() { return true; }

  /** Reads a world written by writeToFile() */
  @Override public World readFromFile(String path) throws IOException, BrokenWorldFileException
  {
    try (BufferedReader reader = FileUtils.newFileReader(path, "map", false)) {
      String name        = readField(reader, path, "DutchFlagWorld");
      int moveCount      = Integer.parseInt(readField(reader, path, "Moves"));
      int[] content      = Arrays.stream(readField(reader, path, "Content").split(" ")).mapToInt(Integer::parseInt).toArray();
      DutchFlagWorld res = new DutchFlagWorld(name, content.length);
      res.content        = content;
      res.moveCount      = moveCount;
      return res;
    } catch (NumberFormatException e) {
      throw new BrokenWorldFileException(path + ".map: malformed number (" + e.getMessage() + ").");
    }
  }

  /** Writes the world, one field per line. The content gives the color (0, 1 or 2) of each ray. */
  @Override public void writeToFile(BufferedWriter writer) throws IOException
  {
    writer.write("DutchFlagWorld: " + getName() + "\n");
    writer.write("Moves: " + moveCount + "\n");
    writer.write("Content: " + Arrays.stream(content).mapToObj(String::valueOf).collect(Collectors.joining(" ")) + "\n");
  }

  /** Returns a textual description of the differences between the caller and the parameter */
  @Override public String diffTo(World o, ProgrammingLanguage ignored)
  {
    if (o == null || !(o instanceof DutchFlagWorld))
      return Game.i18n.tr("This world is not a dutch flag");

    DutchFlagWorld other = (DutchFlagWorld)o;
    if (content.length != other.content.length)
      return Game.i18n.tr("The two worlds are of differing size");

    StringBuffer res = new StringBuffer();
    for (int i = 0; i < content.length; i++)
      if (content[i] != other.content[i])
        res.append(Game.i18n.tr(" Ray #{0} differs: color {1} is not color {2}\n", (i + 1), content[i], other.content[i]));

    return res.toString();
  }

  @Override public boolean equals(Object o)
  {
    if (o == null || !(o instanceof DutchFlagWorld))
      return false;
    DutchFlagWorld other = (DutchFlagWorld)o;
    if (content.length != other.content.length)
      return false;
    for (int i = 0; i < content.length; i++)
      if (content[i] != other.content[i])
        return false;
    return true;
  }
  /**
   * Reset the state of the current world to the one passed in argument
   * @param the world which must be the new start of your current world
   */
  @Override public void reset(World world)
  {
    DutchFlagWorld other = (DutchFlagWorld)world;
    this.content         = new int[other.content.length];
    for (int i = 0; i < content.length; i++)
      content[i] = other.content[i];

    this.moveCount = other.moveCount;
    super.reset(world);
  }

  @Override public DutchFlagWorld replaceEntities(Supplier<? extends Entity> newEntitySupplier)
  {
    return (DutchFlagWorld)super.replaceEntities(newEntitySupplier);
  }

  /* --------------------------------------- */
  /** Swap two positions */
  public void swap(int from, int to)
  {
    if (from < 0 || from >= getSize())
      throw new IllegalArgumentException(Game.i18n.tr("Cannot swap {0} and {1} because {2} is not between 0 and {3}", from, to, from, getSize() - 1));
    if (to < 0 || to >= getSize())
      throw new IllegalArgumentException(Game.i18n.tr("Cannot swap {0} and {1} because {2} is not between 0 and {3}", from, to, to, getSize() - 1));

    int temp      = content[from];
    content[from] = content[to];
    content[to]   = temp;

    moveCount++;
  }

  /**
   * Give the color of a specific ray in the flag
   * @param rank : the number of the ray that you want to get.
   * @return The color of that ray (either 0, 1 or 2)
   */
  public int getColor(int rank)
  {
    if (rank < 0 || rank >= getSize())
      throw new IllegalArgumentException(Game.i18n.tr("Cannot get the color of line #{0} because it''s not between 0 and {1}", rank, getSize()));

    return content[rank];
  }

  /** Returns the amount of rays in this flag */
  public int getSize() { return content.length; }

  /** Returns whether the flag is correctly sorted */
  public boolean isSorted()
  {
    int currentColor = 0;
    for (int rank = 0; rank < content.length; rank++) {
      if (content[rank] > currentColor)
        currentColor = content[rank];
      if (content[rank] < currentColor)
        return false;
    }
    return true;
  }
  public void assertSorted()
  {
    if (isSorted())
      return;

    StringBuffer sb = new StringBuffer("{");

    for (int rank = 0; rank < getSize(); rank++) {
      if (rank != 0)
        sb.append(", ");
      sb.append(content[rank]);
    }
    sb.append("}");

    String msg = Game.i18n.tr("It''s still not sorted!! PLEASE REPORT THIS BUG, along with the following information:\n"
                                  + "Initial situation: {0}",
                              sb.toString());
    System.err.println(msg);
    throw new RuntimeException(msg);
  }

  /** Returns a string representation of the world */
  public String toString()
  {
    StringBuffer res = new StringBuffer("<DutchFlagWorld name:" + getName() + " size:" + getSize() + ">");
    for (int i = 0; i < getSize(); i++)
      res.append((i == 0 ? "" : ", ") + content[i]);
    res.append("</DutchFlagWorld>");
    return res.toString();
  }
}
