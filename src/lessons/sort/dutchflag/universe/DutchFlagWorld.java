package lessons.sort.dutchflag.universe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;
import java.util.function.Supplier;
import javax.swing.ImageIcon;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.ui.ResourcesCache;
import plm.core.ui.WorldView;
import plm.universe.BrokenWorldFileException;
import plm.universe.Entity;
import plm.universe.World;

public class DutchFlagWorld extends World {

  int[] content;
  int moveCount = 0;
  private int colorRemoved; // the color missing from the flag, or -1 if there is none

  /** Copy constructor */
  public DutchFlagWorld(DutchFlagWorld world) { super(world); }

  /** Returns a component able of displaying the world */
  @Override public WorldView getView() { return new DutchFlagWorldView(this); }
  /** Returns the icon of the universe */
  @Override public ImageIcon getIcon() { return ResourcesCache.getIcon(this, "world_dutchflag.png"); }

  /**
   * Regular DutchFlagWorld constructor that creates a random flag.
   * The randomness is seeded by the name: the initial state must be reproducible to cache the answer worlds.
   * @param name : the name of the world
   * @param size : the amount of elements
   */
  public DutchFlagWorld(String name, int size) { this(name, size, -1); }

  /** Same as above, but with only two colors: the one passed as colorRemoved is missing */
  public DutchFlagWorld(String name, int size, int colorRemoved)
  {
    this(name, new int[size], 0, colorRemoved);
    fill(new Random(name.hashCode()));
  }

  /** Constructor of a world in a given state, used when reading a file */
  private DutchFlagWorld(String name, int[] content, int moveCount, int colorRemoved)
  {
    super(name);
    setDelay(content.length > 100 ? 20 : 200); // Delay (in ms) in default animations
    new DutchFlagEntity(this);

    this.content      = content;
    this.moveCount    = moveCount;
    this.colorRemoved = colorRemoved;
  }

  /** Randomizes the initial state: the answer of this world cannot be cached anymore */
  public DutchFlagWorld randomize()
  {
    fill(new Random());
    setRandomized();
    return this;
  }

  /** Gives a random color to each ray, other than colorRemoved if any */
  private void fill(Random rng)
  {
    for (int i = 0; i < content.length; i++) {
      int color  = rng.nextInt(colorRemoved < 0 ? 3 : 2);
      content[i] = colorRemoved >= 0 && color >= colorRemoved ? color + 1 : color;
    }
  }

  @Override public boolean haveIO() { return true; }

  /** Reads a world written by writeToFile() */
  @Override protected World parse(BufferedReader reader, String path) throws IOException, BrokenWorldFileException
  {
    String name   = readField(reader, path, "DutchFlagWorld");
    int moveCount = Integer.parseInt(readField(reader, path, "Moves"));
    return new DutchFlagWorld(name, readInts(reader, path, "Content"), moveCount, -1);
  }

  /** Writes the world, one field per line. The content gives the color (0, 1 or 2) of each ray. */
  @Override public void writeToFile(BufferedWriter writer) throws IOException
  {
    writeField(writer, "DutchFlagWorld", getName());
    writeField(writer, "Moves", moveCount);
    writeField(writer, "Content", content);
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
