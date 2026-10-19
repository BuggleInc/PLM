package lessons.recursion.hanoi.universe;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.StringJoiner;
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

public class HanoiWorld extends World {
  /**
   * A copy constructor (mandatory for the internal compilation mechanism to work)
   *
   * There is normally no need to change it, but it must be present.
   */
  public HanoiWorld(HanoiWorld other) { super(other); }

  /** The constructor that the exercises use: one array of disks per slot, from the bottom to the top. All the disks are yellow. */
  @SuppressWarnings("unchecked") public HanoiWorld(String name, Integer[]... slots)
  {
    this(name, 0, new Vector[slots.length], new Vector[slots.length]);
    for (int slot = 0; slot < slots.length; slot++) {
      slotsVal[slot]   = new Vector<Integer>(Arrays.asList(slots[slot]));
      slotsColor[slot] = new Vector<Color>(Collections.nCopies(slots[slot].length, Color.yellow));
    }
  }

  /** Constructor of a world in a given state, used by the other constructors and when reading a file */
  private HanoiWorld(String name, int moveCount, Vector<Integer>[] slotsVal, Vector<Color>[] slotsColor)
  {
    super(name);
    setDelay(200); /* Delay (in ms) in default animations */
    this.moveCount  = moveCount;
    this.slotsVal   = slotsVal;
    this.slotsColor = slotsColor;
  }

  /**
   * Reset the state of the current world to the one passed in argument
   *
   * This is mandatory for the PLM good working. Even if the prototype says that the passed object can be
   * any kind of world, you can be sure that it's of the same type than the current world. So, there is
   * no need to check before casting your argument.
   *
   * Do not forget to call super.reset(w) afterward, or some internal world fields may not get reset.
   */
  @SuppressWarnings("unchecked") @Override public void reset(World w)
  {
    HanoiWorld other = (HanoiWorld)w;
    slotsVal         = new Vector[other.slotsVal.length];
    slotsColor       = new Vector[other.slotsColor.length];
    for (int slot = 0; slot < other.slotsVal.length; slot++) {
      slotsVal[slot]   = new Vector<Integer>(other.slotsVal[slot]);
      slotsColor[slot] = new Vector<Color>(other.slotsColor[slot]);
    }
    moveCount = other.moveCount;
    super.reset(w);
  }

  @Override public HanoiWorld replaceEntities(Supplier<? extends Entity> newEntitySupplier) { return (HanoiWorld)super.replaceEntities(newEntitySupplier); }

  /**
   * Returns a component able of displaying the world -- will be used in third exercise
   * You should comment this for the first exercises
   */
  @Override public WorldView getView() { return new HanoiWorldView(this); }
  @Override public ImageIcon getIcon() { return ResourcesCache.getIcon("img/world_hanoi.png"); }

  @Override public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("HanoiWorld " + getName() + ": ");
    for (int s = 0; s < slotsVal.length; s++) {
      sb.append("slot " + s + " [");
      for (Object i : slotsVal[s].toArray())
        sb.append(i + " ");
      sb.append("]");
    }
    return sb.toString();
  }

  /** Used to check whether the student code changed the world in the right state -- see exercise 4 */
  @Override public boolean equals(Object o)
  {
    if (o == null || !(o instanceof HanoiWorld))
      return false;
    HanoiWorld other = (HanoiWorld)o;
    if (this.moveCount != other.moveCount)
      return false;
    if (this.slotsVal.length != other.slotsVal.length)
      return false;
    for (int i = 0; i < slotsVal.length; i++) {
      if (!(this.slotsVal[i].equals(other.slotsVal[i])) || !(this.slotsColor[i].equals(other.slotsColor[i])))
        return false;
    }
    return getName().equals(other.getName());
  }

  @Override public String diffTo(World o, ProgrammingLanguage ignored)
  {
    StringBuffer res = new StringBuffer();
    if (o == null || !(o instanceof HanoiWorld))
      return "This is not a world of Hanoi";

    HanoiWorld other = (HanoiWorld)o;
    if (slotsVal.length != other.slotsVal.length)
      return "The worlds don't have the same amount of pegs";

    if (other.moveCount != moveCount)
      res.append(Game.i18n.tr("The disks were not moved the same amount of time: {0} vs. {1}.\n", moveCount, other.moveCount));

    for (int slot = 0; slot < slotsVal.length; slot++)
      for (int pos = 0; pos < Math.max(slotsVal[slot].size(), other.slotsVal[slot].size()); pos++) {
        String thisVal  = pos >= this.slotsVal[slot].size() ? "--" : this.slotsVal[slot].get(pos).toString();
        String otherVal = pos >= other.slotsVal[slot].size() ? "--" : other.slotsVal[slot].get(pos).toString();
        if (!thisVal.equals(otherVal)) {
          res.append(Game.i18n.tr(" Disk #{0} of slot #{1} differs: {2} vs. {3}.\n", (pos + 1), slot, thisVal, otherVal));
        } else {
          Color thisColor  = pos >= this.slotsColor[slot].size() ? null : this.slotsColor[slot].get(pos);
          Color otherColor = pos >= other.slotsColor[slot].size() ? null : other.slotsColor[slot].get(pos);
          if (!thisColor.equals(otherColor))
            res.append(Game.i18n.tr(" Color of disk #{0} of slot #{1} differs: {2} vs. {3}.\n", (pos + 1), slot, thisColor, otherColor));
        }
      }
    return res.toString();
  }

  /* Here comes the world logic */
  private Vector<Integer> slotsVal[];
  private Vector<Color> slotsColor[];
  public int moveCount = 0;

  /** This function is used by the view to retrieve the data to display */
  protected Integer[] values(Integer i) { return slotsVal[i].toArray(new Integer[slotsVal[i].size()]); }

  /**
   * This is the main function of the public interface
   * @throws IllegalArgumentException if your move is not valid
   */
  public void move(Integer src, Integer dst)
  {
    if (src < 0 || src >= slotsVal.length || dst < 0 || dst >= slotsVal.length)
      throw new IllegalArgumentException(
          Game.i18n.tr("Cannot move from slot {0} to {1}: the only existing slots are numbered from 0 to {2}.", src, dst, getSlotAmount() - 1));
    if (src.equals(dst))
      throw new IllegalArgumentException(Game.i18n.tr("Cannot move from slot {0} to itself.", src));
    if (slotsVal[src].size() == 0)
      throw new IllegalArgumentException(Game.i18n.tr("No disc to move from slot {0}.", src));

    if (slotsVal[dst].size() > 0 && slotsVal[src].lastElement() > slotsVal[dst].lastElement())
      throw new IllegalArgumentException(Game.i18n.tr("Cannot move disc from slot {0} to {1} small disk must remain over large ones but {2} > {3}.", src, dst,
                                                      slotsVal[src].lastElement(), slotsVal[dst].lastElement()));

    moveCount++;
    slotsVal[dst].add(slotsVal[src].remove(slotsVal[src].size() - 1));
    slotsColor[dst].add(slotsColor[src].remove(slotsColor[src].size() - 1));
  }
  public int getSlotAmount() { return slotsVal.length; }
  public int getSlotSize(int slot) { return slotsVal[slot].size(); }
  public int getRadius(int slot) { return slotsVal[slot].isEmpty() ? 99 : slotsVal[slot].lastElement(); }
  public Color getColor(int slot, int pos)
  {
    if (slot >= getSlotAmount() || slot < 0)
      throw new RuntimeException(Game.i18n.tr("Invalid slot: {0}.", slot));
    if (pos >= slotsColor[slot].size())
      throw new RuntimeException(Game.i18n.tr("Slot {0} is only {1} high; Cannot get the color of the {2}th disk.", slot, slotsColor[slot].size(), pos));
    return slotsColor[slot].get(pos);
  }
  public void setColor(int slot, int pos, Color c) { slotsColor[slot].set(pos, c); }

  @Override public boolean haveIO() { return true; }

  /** Reads a world written by writeToFile() */
  @SuppressWarnings("unchecked") @Override protected World parse(BufferedReader reader, String path) throws IOException, BrokenWorldFileException
  {
    String name            = readField(reader, path, "HanoiWorld");
    int moveCount          = Integer.parseInt(readField(reader, path, "Moves"));
    int slotAmount         = Integer.parseInt(readField(reader, path, "Slots"));
    Vector<Integer>[] vals = new Vector[slotAmount];
    Vector<Color>[] colors = new Vector[slotAmount];
    for (int slot = 0; slot < slotAmount; slot++) {
      vals[slot]   = new Vector<Integer>();
      colors[slot] = new Vector<Color>();
      for (String disk : readField(reader, path, "Slot").split(" ")) {
        if (disk.isEmpty())
          continue;
        String[] parts = disk.split(":");
        vals[slot].add(Integer.parseInt(parts[0]));
        colors[slot].add(new Color(Integer.parseInt(parts[1], 16)));
      }
    }
    HanoiWorld res = new HanoiWorld(name, moveCount, vals, colors);
    new HanoiEntity("worker", res);
    return res;
  }

  /** Writes the world, one line per slot. Each disk of a slot is written as "radius:RRGGBB", from the bottom to the top. */
  @Override public void writeToFile(BufferedWriter writer) throws IOException
  {
    writeField(writer, "HanoiWorld", getName());
    writeField(writer, "Moves", moveCount);
    writeField(writer, "Slots", slotsVal.length);
    for (int slot = 0; slot < slotsVal.length; slot++) {
      StringJoiner disks = new StringJoiner(" ");
      for (int i = 0; i < slotsVal[slot].size(); i++)
        disks.add(slotsVal[slot].get(i) + ":" + Integer.toHexString(slotsColor[slot].get(i).getRGB() & 0xFFFFFF));
      writeField(writer, "Slot", disks);
    }
  }

  /**
   * Return the panel which let the user to interact dynamically with the world
   */
  @Override public EntityControlPanel getEntityControlPanel() { return new HanoiMovePanel(); }
}
