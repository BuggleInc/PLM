package lessons.turmites.universe;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.ImageIcon;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.ui.ResourcesCache;
import plm.universe.BrokenWorldFileException;
import plm.universe.Direction;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.ui.BuggleWorldView;

public class TurmiteWorld extends BuggleWorld {
  /**
   * A copy constructor (mandatory for the internal compilation mechanism to work)
   *
   * There is normally no need to change it, but it must be present.
   */
  public TurmiteWorld(TurmiteWorld other) { super(other); }

  /**
   * The constructor that the exercises will use to setup the world.
   *
   * It must begin by super(name), and the rest is free (depending on the state describing your world).
   * It is a good idea to use setDelay to specify the default animation delay, but this is not mandatory.
   *
   * You can perfectly have several such constructor.
   */
  public TurmiteWorld(String title, int nbSteps, Object rule, int width, int height, int buggleX, int buggleY)
  {
    super(title, width, height);
    currStep = 0;
    setDelay(1);
    setVisibleGrid(false);
    setParameter(new Object[] {nbSteps, rule});

    new TurmiteEntity((BuggleWorld)this, "ant", buggleX, buggleY, Direction.NORTH, Color.red, Color.red);
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
  @Override public void reset(World w)
  {
    TurmiteWorld other = (TurmiteWorld)w;
    currStep           = other.currStep;
    super.reset(w);
  }

  /** Returns a component able of displaying the world */
  @Override public BuggleWorldView getView() { return new TurmiteWorldView(this); }
  @Override public ImageIcon getIcon() { return ResourcesCache.getIcon("img/world_buggle.png"); }

  /** Used to check whether the student code changed the world in the right state */
  @Override public boolean equals(Object o)
  {
    if (o == null || !(o instanceof TurmiteWorld))
      return false;
    if (((TurmiteWorld)o).currStep != currStep)
      return false;
    return super.equals(o);
  }
  @Override public String diffTo(World other)
  {
    String res = "";
    if (((TurmiteWorld)other).currStep != currStep)
      res += "The amount of steps is wrong: " + ((TurmiteWorld)other).currStep + " is not " + currStep + "\n";
    return res + super.diffTo(other);
  }

  /* Here comes the world logic */
  public int currStep = 0;

  public void stepDone() { currStep++; }
  @Override public boolean isDelayed() { return super.isDelayed() && ((getDelay() > 0) || (currStep % 1000 == 0)); }
  @Override public void setupBindings(ProgrammingLanguage lang, ScriptEngine engine) throws ScriptException
  {
    if (lang.isPython()) {
      super.setupBindings(lang, engine);
      engine.put("daWorld", this);
      engine.eval("def stepDone():\n"
                  + "	daWorld.stepDone()\n" +
                  /* BINDINGS TRANSLATION: French */
                  "def pasFait():\n"
                  + "	daWorld.stepDone()\n");
    }
  }

  @Override public World readFromFile(String path) throws IOException, BrokenWorldFileException
  {
    TurmiteWorld res = new TurmiteWorld("toto", 1, "", 1, 1, 1, 1);
    res.removeEntity(res.getEntity(0));
    return res.readFromFile(path, "TurmiteWorld", res);
  }

  @Override protected String serializedClassName() { return "TurmiteWorld"; }

  /** Writes the "Step: N" header line right after "Size: WxH", so that currStep round-trips through the .map file. */
  @Override protected void writeExtraHeader(BufferedWriter writer) throws IOException { writer.write("Step: " + currStep + "\n"); }

  /** Reads back the "Step: N" header line written by writeExtraHeader, and returns the following line. */
  @Override protected String readExtraHeader(String path, BufferedReader reader) throws IOException, BrokenWorldFileException
  {
    String line = reader.readLine();
    if (line == null)
      throw new BrokenWorldFileException(Game.i18n.tr("{0}.map: End of file reached before the step count specification.", path));

    Pattern p = Pattern.compile("^Step: (\\d+)$");
    Matcher m = p.matcher(line);
    if (!m.find())
      throw new BrokenWorldFileException(Game.i18n.tr("{0}.map: Expected ''Step: N'' but got ''{1}''.", path, line));
    currStep = Integer.parseInt(m.group(1));

    return reader.readLine();
  }
}
