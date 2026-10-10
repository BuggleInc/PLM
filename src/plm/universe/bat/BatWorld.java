package plm.universe.bat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import javax.swing.ImageIcon;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.ui.ResourcesCache;
import plm.core.ui.WorldView;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;

public class BatWorld extends World {
  public static class DefaultBatEntity extends BatEntity {
    public DefaultBatEntity() {}
    @Override public void run() throws Exception { throw new UnsupportedOperationException("Unimplemented method 'run'"); }
  }

  public List<BatTest> tests = new Vector<>();
  public BatWorld(String funName, BatEntity ent)
  {
    super(funName);

    addEntity(ent);
  }

  public BatWorld(String funName) { this(funName, new DefaultBatEntity()); }
  public BatWorld(BatWorld w2)
  {
    super(w2);
    this.tests = new Vector<>();
    for (BatTest t : w2.tests)
      tests.add(t.copy());
  }

  @Override public void reset(World w)
  {
    BatWorld anotherWorld = (BatWorld)w;
    this.tests            = new Vector<BatTest>();
    for (BatTest t : anotherWorld.tests)
      tests.add(t.copy());
    super.reset(anotherWorld);
  }
  @Override public boolean equals(Object o)
  {
    if (!(o instanceof BatWorld)) {
      return false;
    }
    BatWorld other = (BatWorld)o;
    return tests.equals(other.tests);
  }
  @Override public WorldView getView() { return new BatWorldView(this); }
  @Override public ImageIcon getIcon() { return ResourcesCache.getIcon("img/world_bat.png"); }
  @Override public boolean haveIO() { return true; }

  /** Reads a world written by writeToFile() */
  @Override protected World parse(BufferedReader reader, String path) throws IOException, BrokenWorldFileException
  {
    BatWorld res = new BatWorld(readField(reader, path, "BatWorld"));
    for (String line = reader.readLine(); line != null; line = reader.readLine())
      res.tests.add(BatTest.fromMapLine(res.getName(), line));
    return res;
  }

  /**
   * Writes the world, then one tab-separated line per test: its visibility, its parameters and its result.
   * Values use the ValueSerializer format. The write fails if a value contains a tab or a line break, as they would corrupt the file.
   * Nothing is written in that case, so that a truncated list of tests is never mistaken for a complete one when reading.
   */
  @Override public void writeToFile(BufferedWriter writer) throws IOException
  {
    StringBuilder sb = new StringBuilder("BatWorld: " + getName() + "\n");
    for (BatTest t : tests)
      sb.append(t.toMapLine() + "\n");
    writer.write(sb.toString());
  }

  /* So that the view can display them */
  protected List<BatTest> getTests() { return tests; }

  /* World logic */
  public void addTest(boolean visible, Object... params) { tests.add(new BatTest(getName(), visible, params)); }
  @Override public String diffTo(World w, ProgrammingLanguage lang)
  {
    BatWorld other     = (BatWorld)w;
    StringBuffer sb    = new StringBuffer();
    boolean foundError = false;
    for (int i = 0; i < tests.size(); i++) {
      if (foundError && !tests.get(i).isVisible() && !Game.getInstance().isDebugEnabled())
        return sb.toString();

      if (!tests.get(i).equals(other.tests.get(i))) {
        sb.append(other.tests.get(i).getName(lang) + " returned " + other.tests.get(i).getResult() + " while " + tests.get(i).getResult() + " was expected.\n");
        foundError = true;
      }
    }
    return sb.toString();
  }
}
