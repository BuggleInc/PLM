package plm.universe.sort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.ui.ResourcesCache;
import plm.core.ui.WorldView;
import plm.universe.BrokenWorldFileException;
import plm.universe.Entity;
import plm.universe.EntityControlPanel;
import plm.universe.World;

public class SortingWorld extends World {
  private int[] values;       // the values as they are sorted in the array
  private int[] initValues;   // needed by the chronoview
  private boolean someoneHomeOk; // whether a value may be in its sorted position in the initial state
  private int readCount  = 0; // the count of the read made
  private int writeCount = 0; // the count of the write made
  /*
   * It's needed by the chronoview
   * This list contains all the operations ( SetVal, CopyVal or Swap ) made on the world since
   * the moment where it has been shown to the user
   */
  private ArrayList<Operation> operations = new ArrayList<Operation>(1);

  /** Copy constructor used by the PLM internals */
  public SortingWorld(SortingWorld world) { super(world); }

  /** Constructor used in the exercises */
  public SortingWorld(String name, int nbValues) { this(name, nbValues, true); }
  public SortingWorld(String name, int nbValues, boolean someoneHomeOk)
  {
    super(name);
    if (nbValues > 100)
      setDelay(1);
    else
      setDelay(50);
    this.values        = new int[nbValues];
    this.someoneHomeOk = someoneHomeOk;
    shuffle(new Random(name.hashCode())); // Seeded by the name: the initial state must be reproducible to cache the answer worlds
    addEntity(new SortingEntity());
  }

  /** Randomizes the initial state: the answer of this world cannot be cached anymore */
  public SortingWorld randomize()
  {
    shuffle(new Random());
    setRandomized();
    return this;
  }

  /** Sets the initial state to a scrambled version of the sorted values */
  private void shuffle(Random rng)
  {
    for (int i = 0; i < values.length; i++)
      values[i] = i;

    scramble(rng);
    // If instructed so, move every value that is @home. Swapping such a value with any other cell never creates a new one @home.
    if (!someoneHomeOk)
      for (int i = 0; i < values.length; i++)
        if (values[i] == i)
          exchangeValues(i, (i + 1) % values.length);
    this.initValues = this.values;
  }

  private void scramble(Random rng)
  {
    while (this.isSorted())
      for (int caseNumber = 0; caseNumber < this.values.length; caseNumber++)
        // Swapping time !
        if (rng.nextBoolean())
          this.exchangeValues(caseNumber, rng.nextInt(this.values.length));
  }

  public boolean equals(Object o)
  {
    if (o == null || !(o instanceof SortingWorld))
      return false;

    SortingWorld other = (SortingWorld)o;
    if (values.length != other.values.length)
      return false;
    for (int i = 0; i < this.values.length; i++)
      if (this.values[i] != other.values[i])
        return false;

    if (!(this.readCount == other.readCount))
      return false;
    if (!(this.writeCount == other.writeCount))
      return false;

    /* Do not compare the operation order as it's not part of the problem specification
     *
     * If you want to add this again, please make sure that swap(i,j) equals to swap(j,i)

    if (operations.size() != other.operations.size())
            return false;
    for (int i = 0 ; i < this.operations.size() ; i++)
            if (! operations.get(i).equals( other.operations.get(i)) )
                    return false;
    */
    return true;
  }

  /**
   * Make a textual description of the differences between the caller and world
   * @param world : the world with which you want to compare your world
   * @return A textual description of the differences between the caller and world
   */
  @Override public String diffTo(World world, ProgrammingLanguage ignored)
  {
    String s;
    if (world == null || !(world instanceof SortingWorld)) {
      s = "This is not a world of sorting :(";
    } else {
      SortingWorld other = (SortingWorld)world;
      StringBuffer sb    = new StringBuffer();

      if (this.values.length != other.values.length)
        sb.append(
            Game.i18n.tr("This is very weird: There is not the same amount of values! Expected: {0}; Found: {1}\n", this.values.length, other.values.length));

      if (this.readCount != other.readCount)
        sb.append(Game.i18n.tr("Invalid read count. Expected: {0}; Found: {1}\n", this.readCount, other.readCount));

      if (this.writeCount != other.writeCount)
        sb.append(Game.i18n.tr("Invalid write count. Expected: {0}; Found: {1}\n", this.writeCount, other.writeCount));

      for (int i = 0; i < this.values.length; i++)
        if (this.values[i] != other.values[i])
          sb.append(Game.i18n.tr("Value at index {0} differs. Expected {1}; Found {2}\n", i, val2str(values[i], values.length),
                                 val2str(other.values[i], other.values.length)));

      s = sb.toString();
    }
    return s;
  }

  @Override public String getDebugInfo()
  {
    StringBuffer sb = new StringBuffer();
    sb.append(getName() + " [");
    boolean first = true;
    for (Integer i : initValues) {
      if (first)
        first = false;
      else
        sb.append(", ");
      sb.append(i);
    }
    sb.append("]");
    return sb.toString();
  }
  @Override public boolean haveIO() { return true; }

  /** Reads a world written by writeToFile() */
  @Override protected World parse(BufferedReader reader, String path) throws IOException, BrokenWorldFileException
  {
    String name      = readField(reader, path, "SortingWorld");
    int[] values     = Arrays.stream(readField(reader, path, "Values").split(" ")).mapToInt(Integer::parseInt).toArray();
    int[] initValues = Arrays.stream(readField(reader, path, "InitValues").split(" ")).mapToInt(Integer::parseInt).toArray();
    SortingWorld res = new SortingWorld(name, values.length);
    res.values       = values;
    res.initValues   = initValues;
    res.readCount    = Integer.parseInt(readField(reader, path, "Reads"));
    res.writeCount   = Integer.parseInt(readField(reader, path, "Writes"));

    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
      String[] tokens = line.split(" ");
      int first       = Integer.parseInt(tokens[1]);
      res.operations.add(switch (tokens[0]) {
        case "swap" -> new Swap(first, Integer.parseInt(tokens[2]));
        case "copy" -> new CopyVal(first, Integer.parseInt(tokens[2]));
        case "set" -> new SetVal(first, Integer.parseInt(tokens[2]));
        case "get" -> new GetVal(first);
        default -> throw new BrokenWorldFileException(path + ".map: unknown operation '" + line + "'.");
      });
    }
    return res;
  }

  /**
   * Writes the world, one field per line, followed by one line per operation.
   *
   * The operations are serialized, and not only the final values, because the chrono view replays them:
   * without them, a cached world could only be displayed in the spatial view.
   */
  @Override public void writeToFile(BufferedWriter writer) throws IOException
  {
    writer.write("SortingWorld: " + getName() + "\n");
    writer.write("Values: " + Arrays.stream(values).mapToObj(String::valueOf).collect(Collectors.joining(" ")) + "\n");
    writer.write("InitValues: " + Arrays.stream(initValues).mapToObj(String::valueOf).collect(Collectors.joining(" ")) + "\n");
    writer.write("Reads: " + readCount + "\n");
    writer.write("Writes: " + writeCount + "\n");
    for (Operation o : operations)
      writer.write(o + "\n");
  }

  /**
   * Tells if the array is Sorted or not
   */
  private boolean isSorted()
  {
    boolean sw = true;
    for (int i = 0; i < this.values.length && sw; i++) {
      if (this.values[i] != i) {
        sw = false;
      }
    }
    return sw;
  }

  /**
   * Copy the value from the cell of index from into the cell of index to<br>
   * It costs one read and one write.
   * @param from The index of the cell to copy
   * @param to The index of the cell where copy the value
   */
  public void copy(int from, int to)
  {
    if (from < 0)
      throw new RuntimeException("Out of bounds in copy(" + from + "," + to + "): " + from + "<0");
    if (to < 0)
      throw new RuntimeException("Out of bounds in copy(" + from + "," + to + "): " + to + "<0");
    if (from >= getValueCount())
      throw new RuntimeException("Out of bounds in copy(" + from + "," + to + "), " + from + ">= value count");
    if (to >= getValueCount())
      throw new RuntimeException("Out of bounds in copy(" + from + "," + to + "), " + to + ">= value count");

    this.operations.add(new CopyVal(from, to));

    this.readCount++;
    this.writeCount++;
    this.values[to] = this.values[from];
  }

  /** Returns the panel which let the user to interact dynamically with the world */
  @Override public EntityControlPanel getEntityControlPanel() { return new SortingButtonPanel(); }
  /** Returns the icon of the universe */
  @Override public ImageIcon getIcon() { return ResourcesCache.getIcon("img/world_sorting.png"); }

  /**
   * Returns the initial state of the array
   */
  public int[] getInitValues() { return initValues; }

  /**
   * Return the list of all the operations made on the world since its initialization/last reset
   */
  public ArrayList<Operation> getOperations() { return this.operations; }

  /**
   * Return the read counter
   */
  public int getReadCount() { return this.readCount; }

  /** Returns the amount of values in the array */
  public int getValueCount() { return values.length; }

  /** Returns the array of values that need to be sorted */
  public int[] getValues() { return this.values; }

  /** Returns a component able at displaying the world */
  @Override public WorldView getView() { return new SortingWorldView(this); }

  /** Returns the write counter */
  public int getWriteCount() { return this.writeCount; }

  /**
   * Tell if the value at the index i is smaller than the value at index j<br>
   * It costs two reads.
   * @param i the index of the first cell that we want to check
   * @param j the index of the second cell that we want to check
   * @return if the content of the cell of index i is smaller than the content of the cell of index j
   */
  public boolean isSmaller(int i, int j)
  {
    if (i < 0)
      throw new RuntimeException("Out of bounds in isSmaller(" + i + "," + j + "): " + i + "<0");
    if (j < 0)
      throw new RuntimeException("Out of bounds in isSmaller(" + i + "," + j + "): " + j + "<0");
    if (i >= getValueCount())
      throw new RuntimeException("Out of bounds in isSmaller(" + i + "," + j + "), " + i + ">= value count");
    if (j >= getValueCount())
      throw new RuntimeException("Out of bounds in isSmaller(" + i + "," + j + "), " + j + ">= value count");

    this.readCount += 2;
    return this.values[i] < this.values[j];
  }

  /**
   * Tell if the value at the index i is smaller than val<br>
   * It costs one read.
   * @param i the index of case that we want to check
   * @param val the value with which compare the cell
   * @return if the case of index i is smaller than val
   */
  public boolean isSmallerThan(int i, int val)
  {
    if (i < 0)
      throw new RuntimeException("Out of bounds in isSmallerThan(" + i + "," + val + "): " + i + "<0");
    if (i >= getValueCount())
      throw new RuntimeException("Out of bounds in isSmallerThan(" + i + "," + val + "), " + i + ">= value count");

    this.readCount += 1;
    return this.values[i] < val;
  }

  /**
   * Reset the state of the current world to the one passed in argument
   * @param w the world which must be the new start of your current world
   */
  @Override public void reset(World w)
  {
    super.reset(w);
    SortingWorld world = (SortingWorld)w;

    this.values     = world.values.clone();
    this.initValues = world.initValues.clone();
    this.readCount  = world.readCount;
    this.writeCount = world.writeCount;
    this.operations = new ArrayList<Operation>(1);
    for (Operation o : world.operations)
      this.operations.add(o);
  }

  @Override public SortingWorld replaceEntities(Supplier<? extends Entity> newEntitySupplier) { return (SortingWorld)super.replaceEntities(newEntitySupplier); }

  /**
   * Return the value of index i in the array
   * @param i the index wanted in the array
   * @return the value of index i in the array
   */
  public int getValue(int i)
  {
    if (i < 0)
      throw new RuntimeException("Out of bounds in getValue(" + i + "): " + i + "<0");
    if (i >= getValueCount())
      throw new RuntimeException("Out of bounds in getValue(" + i + "), " + i + ">= value count");
    this.operations.add(new GetVal(i));
    readCount++;
    return values[i];
  }

  /**
   * Set the value of the case number i of the array at val<br>
   * It costs only one write.
   * @param i the index of the cell
   * @param val the value that you want to set
   */
  public void setValue(int i, int val)
  {
    if (i < 0)
      throw new RuntimeException("Out of bounds in setValue(" + i + "): " + i + "<0");
    if (i >= getValueCount())
      throw new RuntimeException("Out of bounds in setValue(" + i + "), " + i + ">= value count");

    this.operations.add(new SetVal(i, val));

    this.writeCount++;
    this.values[i] = val;
  }

  /**
   * Swap the value of the case number i with the value of the case number j<br>
   * It costs two reads and two writes
   * @param i the index of the first cell concerned
   * @param j the index of the second cell concerned
   */
  public void swap(int i, int j)
  {
    if (i < 0)
      throw new RuntimeException(Game.i18n.tr("Out of bounds in swap({0},{1}): {2}<0", i, j, i));
    if (j < 0)
      throw new RuntimeException(Game.i18n.tr("Out of bounds in swap({0},{1}): {2}<0", i, j, j));
    if (i >= getValueCount())
      throw new RuntimeException(Game.i18n.tr("Out of bounds in swap({0},{1}): {2}>value count", i, j, i));
    if (j >= getValueCount())
      throw new RuntimeException(Game.i18n.tr("Out of bounds in swap({0},{1}): {2}>value count", i, j, j));

    this.operations.add(new Swap(i, j));

    this.readCount += 2;
    this.writeCount += 2;

    exchangeValues(i, j);
  }

  /**
   * Exchange the value of the case number i with the value of the case number j<br>
   * It doesn't increase the read or the write counters.
   * @param i the index of the first cell concerned
   * @param j the index of the second cell concerned
   */
  private void exchangeValues(int i, int j)
  {
    int tmp        = this.values[i];
    this.values[i] = this.values[j];
    this.values[j] = tmp;
  }

  /**
   * Make a string representation of the value to be sorted
   *
   *  It's a bad idea to display integer values because the students mix the indexes
   *  and the values. It's better to use a string representation for that.
   *
   *  WARNING, this function is duplicated in SortingWorldView. Yes, I fell ashamed.
   */
  protected String val2str(int value, int amountOfValues)
  {
    String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    if (amountOfValues < 26) {
      return letters.substring(value, value + 1);
    }
    if (amountOfValues < 26 * 26) {
      return letters.substring(value / 26, (value / 26) + 1) + letters.substring(value % 26, (value % 26) + 1);
    }
    return "" + value;
  }
}
