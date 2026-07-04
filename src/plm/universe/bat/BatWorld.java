package plm.universe.bat;

import java.util.List;
import java.util.Vector;
import javax.script.ScriptEngine;
import javax.swing.ImageIcon;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.ui.ResourcesCache;
import plm.core.ui.WorldView;
import plm.universe.World;

public class BatWorld extends World {
  public List<BatTest> tests = new Vector<>();
  public BatWorld(String funName, BatEntity ent)
  {
    super(funName);

    addEntity(ent);
  }

  public BatWorld(String funName) { this(funName, new BatEntity()); }
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

  /* So that the view can display them */
  protected List<BatTest> getTests() { return tests; }

  /* World logic */
  public void addTest(boolean visible, Object... params) { tests.add(new BatTest(getName(), visible, params)); }
  @Override public void setupBindings(ProgrammingLanguage lang, ScriptEngine e)
  {
    if (lang.isPython()) {
      e.put("batTests", tests);
    }
  }
  @Override public String diffTo(World w)
  {
    BatWorld other     = (BatWorld)w;
    StringBuffer sb    = new StringBuffer();
    boolean foundError = false;
    for (int i = 0; i < tests.size(); i++) {
      if (foundError && !tests.get(i).isVisible() && !Game.getInstance().isDebugEnabled())
        return sb.toString();

      if (!tests.get(i).equals(other.tests.get(i))) {
        sb.append(other.tests.get(i).getName() + " returned " + other.tests.get(i).getResult() + " while " +
                  tests.get(i).getResult() + " was expected.\n");
        foundError = true;
      }
    }
    return sb.toString();
  }
}
