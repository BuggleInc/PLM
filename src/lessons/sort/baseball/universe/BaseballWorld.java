package lessons.sort.baseball.universe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import java.util.function.Supplier;
import javax.swing.ImageIcon;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.ui.ResourcesCache;
import plm.core.ui.WorldView;
import plm.universe.BrokenWorldFileException;
import plm.universe.Entity;
import plm.universe.EntityControlPanel;
import plm.universe.World;

public class BaseballWorld extends World {
  /** How the initial field of a world is mixed */
  public enum Mix { SORTED, RANDOM, NOBODY_HOME, ALMOST_SORTED }

  public static final int COLOR_HOLE = -1;

  private int[] field;               // the bases which composed the field
  private Mix mix;                   // how the initial field is mixed
  private int baseAmount, posAmount; // field dimensions
  private int holeBase, holePos;     // The coordinate of the hole
  protected int[] initialField;
  private Vector<BaseballMove> moves = new Vector<BaseballMove>(); // all moves made on the field -- used for graphical purpose only

  /** Copy constructor used internally by PLM */
  public BaseballWorld(BaseballWorld other)
  {
    super(other);
    initialField = new int[other.initialField.length];
    for (int i = 0; i < initialField.length; i++)
      initialField[i] = other.initialField[i];
  }

  /** Regular constructor used by exercises */
  public BaseballWorld(String name, int baseAmount, int positionAmount) { this(name, baseAmount, positionAmount, Mix.RANDOM); }

  public BaseballWorld(String name, int baseAmount, int posAmount, Mix mix)
  {
    super(name);

    // create the bases
    this.baseAmount = baseAmount;
    this.posAmount  = posAmount;

    this.field = new int[baseAmount * posAmount];
    this.mix   = mix;
    sortField();
    shuffle(new Random(name.hashCode())); // Seeded by the name: the initial state must be reproducible to cache the answer worlds

    initialField = field.clone();

    // Add an entity
    addEntity(new BaseballEntity());

    findHole();
  }

  /** Randomizes the initial state, as the mix parameter of the constructor asks: the answer of this world cannot be cached anymore */
  public BaseballWorld randomize()
  {
    sortField();
    shuffle(new Random());
    initialField = field.clone();
    findHole();
    setRandomized();
    return this;
  }

  /** Puts every player on its base, the hole being on the last one */
  private void sortField()
  {
    for (int base = 0; base < baseAmount; base++)
      for (int pos = 0; pos < posAmount; pos++)
        setPlayerColor(base, pos, base);
    setPlayerColor(baseAmount - 1, 0, COLOR_HOLE);
  }

  /** Mixes a sorted field as the mix attribute asks */
  private void shuffle(Random rng)
  {
    switch (mix) {
      case SORTED -> {
      } // nothing to do here
      case RANDOM -> {
        for (int base = 0; base < baseAmount; base++)
          for (int pos = 0; pos < posAmount; pos++)
            swap(base, pos, rng.nextInt(baseAmount), rng.nextInt(posAmount));
      }
      case NOBODY_HOME -> {
        // Ensure that nobody's home once it's mixed.
        //   We tested that no situation of 4 bases with that condition exposes the bug of the naive algorithm
        //   We tested it by generating all situations, actually.
        boolean swapped;
        do {
          swapped = false;
          for (int base = 0; base < baseAmount; base++)
            for (int pos = 0; pos < posAmount; pos++)
              if (getPlayerColor(base, pos) == base) {
                swapped = true;
                int newBase;
                do {
                  newBase = rng.nextInt(baseAmount);
                } while (newBase == base);
                swap(base, pos, newBase, rng.nextInt(posAmount));
              }
        } while (swapped);
      }
      case ALMOST_SORTED -> swap(0, 0, 2, 0); // Expose the bug of the naive algorithm
    }
  }

  public BaseballWorld(String name, int baseAmount, int positionAmount, int[] values)
  {
    this(name, baseAmount, positionAmount, Mix.SORTED);
    if (baseAmount * posAmount != values.length)
      throw new RuntimeException("Your values array is not of the right size");
    field        = values;
    initialField = field.clone();

    findHole();
  }

  /** Caches the position of the hole */
  private void findHole()
  {
    for (int base = 0; base < baseAmount; base++)
      for (int pos = 0; pos < posAmount; pos++)
        if (getPlayerColor(base, pos) == COLOR_HOLE) {
          holeBase = base;
          holePos  = pos;
          return;
        }
  }

  @Override public boolean haveIO() { return true; }

  /** Reads a world written by writeToFile(), by replaying its moves on its initial field */
  @Override protected World parse(BufferedReader reader, String path) throws IOException, BrokenWorldFileException
  {
    String name       = readField(reader, path, "BaseballWorld");
    String[] size     = readField(reader, path, "Size").split("x");
    int[] initial     = readInts(reader, path, "Initial");
    BaseballWorld res = new BaseballWorld(name, Integer.parseInt(size[0]), Integer.parseInt(size[1]), initial);

    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
      String[] tokens = line.split(" ");
      if (!tokens[0].equals("move"))
        throw new BrokenWorldFileException(path + ".map: unknown line '" + line + "'.");
      res.move(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
    }
    return res;
  }

  /**
   * Writes the world as its initial field followed by one line per move (the player moved into the hole).
   *
   * Moves are serialized, and not only the final field, because the history view replays them.
   * The final field, the hole and the moves objects are then rebuilt by replaying the moves when reading.
   */
  @Override public void writeToFile(BufferedWriter writer) throws IOException
  {
    writeField(writer, "BaseballWorld", getName());
    writeField(writer, "Size", baseAmount + "x" + posAmount);
    writeField(writer, "Initial", initialField);
    for (BaseballMove m : moves)
      writer.write("move " + m.getBaseSrc() + " " + m.getPlayerSrc() + "\n");
  }

  /**
   * Returns a textual description of the differences between the caller and world
   * @param o the world with which you want to compare your world
   */
  @Override public String diffTo(World o, ProgrammingLanguage ignored)
  {
    if (o == null || !(o instanceof BaseballWorld))
      return Game.i18n.tr("This is not a baseball world :-(");

    BaseballWorld other = (BaseballWorld)o;
    if (baseAmount != other.baseAmount)
      return Game.i18n.tr("Differing amount of bases: {0} vs {1}", baseAmount, other.baseAmount);

    if (posAmount != other.posAmount)
      return Game.i18n.tr("Differing amount of players: {0} vs {1}", posAmount, other.posAmount);

    StringBuffer sb = new StringBuffer();
    for (int base = 0; base < baseAmount; base++)
      for (int pos = 0; pos < posAmount; pos++)
        if (getPlayerColor(base, pos) != other.getPlayerColor(base, pos))
          sb.append(Game.i18n.tr("Player at base {0}, pos {1} differs: {2} vs {3}\n", base, pos, getPlayerColor(base, pos), other.getPlayerColor(base, pos)));

    return sb.toString();
  }

  @Override public BaseballWorld replaceEntities(Supplier<? extends Entity> newEntitySupplier)
  {
    return (BaseballWorld)super.replaceEntities(newEntitySupplier);
  }

  public boolean equals(Object other)
  {
    if (other == null || !(other instanceof BaseballWorld))
      return false;

    BaseballWorld otherField = (BaseballWorld)other;
    if (this.holeBase != otherField.holeBase || this.holePos != otherField.holePos || this.baseAmount != otherField.baseAmount ||
        this.posAmount != otherField.posAmount)

      return false;

    for (int base = 0; base < baseAmount; base++)
      for (int pos = 0; pos < posAmount; pos++)
        if (getPlayerColor(base, pos) != otherField.getPlayerColor(base, pos))
          return false;

    return true;
  }

  /** Returns the icon of the world */
  @Override public ImageIcon getIcon() { return ResourcesCache.getIcon("img/world_baseball.png"); }
  /** Returns a component able to display the world */
  @Override public WorldView getView() { return new BaseballWorldView(this); }
  BaseballMovePanel panel = null;
  /** Returns a panel allowing to interact dynamically with the world */
  @Override public EntityControlPanel getEntityControlPanel()
  {
    if (panel == null)
      panel = new BaseballMovePanel();
    return panel;
  }
  /** Passes the mouse selection from view to the control panel */
  public void setPlayer(int base, int pos) { panel.setPlayer(base, pos); }
  /** Passes the mouse action from the view to the control panel */
  public void doMove() { panel.doMove(); }

  /**
   * Reset the state of the current world to the one passed in argument
   * @param the world which must be the new start of your current world
   */
  public void reset(World world)
  {
    super.reset(world);

    BaseballWorld other = (BaseballWorld)world;

    moves = new Vector<BaseballMove>();
    for (BaseballMove m : other.moves)
      moves.add(m);

    holeBase = other.holeBase;
    holePos  = other.holePos;

    baseAmount = other.baseAmount;
    posAmount  = other.posAmount;
    field      = new int[other.baseAmount * other.posAmount];
    for (int base = 0; base < baseAmount; base++)
      for (int pos = 0; pos < posAmount; pos++)
        setPlayerColor(base, pos, other.getPlayerColor(base, pos));
  }

  /** Returns a string representation of the world */
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("BaseballWorld " + getName() + ": {");

    for (int base = 0; base < baseAmount; base++) {
      if (base != 0)
        sb.append(" , ");
      for (int pos = 0; pos < posAmount; pos++) {
        if (pos != 0)
          sb.append(",");
        sb.append(getPlayerColor(base, pos));
      }
    }
    sb.append("}");
    return sb.toString();
  }

  /** Returns the number of bases on your field */
  public int getBasesAmount() { return baseAmount; }
  /** Returns the amount of players per base on this field */
  public int getPositionsAmount() { return posAmount; }

  /**
   * Returns the color of the player in base baseIndex at position playerLocation
   * @param base the index of the base we are looking for
   * @param pos  the position within that base (between 0 and getLocationsAmount()-1 )
   */
  public int getPlayerColor(int base, int pos) { return field[base * posAmount + pos]; }
  /**
   * Sets the color of the player in the specified base at the specified position to the specified value
   * @param base the index of the base we are looking for
   * @param pos  the position within that base (between 0 and getLocationsAmount()-1 )
   * @param color the new value
   */
  public void setPlayerColor(int base, int pos, int color) { field[base * posAmount + pos] = color; }

  /** Returns the index of the base where is hole is located */
  public int getHoleBase() { return this.holeBase; }

  /** Returns the position in the base where is hole is located */
  public int getHolePosition() { return this.holePos; }

  /** Returns the last move made on the field */
  protected BaseballMove getLastMove()
  {
    if (moves.size() == 0)
      return null;
    return moves.get(moves.size() - 1);
  }
  /** Returns the amount of moves done so far */
  protected int getMoveCount() { return moves.size(); }
  /** Returns all moves done so far */
  protected Vector<BaseballMove> getMoves() { return moves; }

  /** Checks that the world is sorted, and display an helpful error message if not */
  public void assertSorted(String exercise)
  {
    if (isSorted())
      return;

    StringBuffer sb = new StringBuffer("{");

    for (int base = 0; base < baseAmount; base++) {
      if (base != 0)
        sb.append(" , ");
      for (int pos = 0; pos < posAmount; pos++) {
        if (pos != 0)
          sb.append(",");
        sb.append(initialField[base * posAmount + pos]);
      }
    }
    sb.append("}");

    String msg = Game.i18n.tr("It''s still not sorted!! PLEASE REPORT THIS BUG, along with the following information:\n"
                                  + "Exercise: {0}; Amount of bases: {1}; Initial situation: {2}",
                              exercise, baseAmount, sb.toString());
    System.err.println(msg);
    throw new RuntimeException(msg);
  }
  /** Returns if every player of the field is on the right base */
  public boolean isSorted()
  {
    for (int base = 0; base < baseAmount; base++)
      if (!isBaseSorted(base))
        return false;
    return true;
  }
  /** Returns if every player of the specified base is on the right base */
  public boolean isBaseSorted(int base)
  {
    for (int pos = 0; pos < posAmount; pos++) {
      int color = getPlayerColor(base, pos);
      if (color != base && !(base == baseAmount - 1 && color == COLOR_HOLE)) // last base may contain the hole
        return false;
    }
    return true;
  }

  /**
   * Moves the specified player into the hole
   * @throws IllegalArgumentException in case baseSrc is not near the hole
   */
  public void move(int base, int position)
  {
    if (base >= this.baseAmount || base < 0)
      throw new IllegalArgumentException(Game.i18n.tr("Cannot move from base {0} since it''s not between 0 and {1}", base, (baseAmount - 1)));

    if (position < 0 || position > this.posAmount - 1)
      throw new IllegalArgumentException(Game.i18n.tr("Cannot move from position {0} since it''s not between 0 and {1})", position, (posAmount - 1)));

    // must work only if the bases are next to each other
    if ((holeBase != base + 1) && (holeBase != base - 1) && (holeBase != 0 || base != baseAmount - 1) && (holeBase != baseAmount - 1 || base != 0) &&
        (holeBase != base))

      throw new IllegalArgumentException(
          Game.i18n.tr("The player {0} from base {1} is too far from the hole (at base {2}) to reach it in one move", position, base, holeBase));

    // All clear. Proceed.
    moves.add(new BaseballMove(base, position, holeBase, holePos, getPlayerColor(base, position), this));
    swap(base, position, holeBase, holePos);
    holeBase = base;
    holePos  = position;
  }

  /**
   * Swap two players (no validity check is enforced)
   * @param baseSrc : the index of the source base
   * @param posSrc : the position of the player that you want to move from the source base
   * @param baseDst : the index of the destination base
   * @param posDst : the position of the player that you want to move from the destination base
   */
  private void swap(int baseSrc, int posSrc, int baseDst, int posDst)
  {
    int flyingMan = getPlayerColor(baseSrc, posSrc);

    setPlayerColor(baseSrc, posSrc, getPlayerColor(baseDst, posDst));
    setPlayerColor(baseDst, posDst, flyingMan);
  }
}
