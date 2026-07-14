package lessons.recursion.cons.universe;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.ImageIcon;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.ui.ResourcesCache;
import plm.universe.bat.BatWorld;

public class ConsWorld extends BatWorld {
  public static class DefaultConsEntity extends ConsEntity {
    public DefaultConsEntity() {}
    @Override public void run() throws Exception { throw new UnsupportedOperationException("Unimplemented method 'run'"); }
  }
  public ConsWorld(ConsWorld other) { super(other); }
  public ConsWorld(String funName) { super(funName, new DefaultConsEntity()); }
  @Override public ImageIcon getIcon() { return ResourcesCache.getIcon("img/world_cons.png"); }

  @Override public void setupBindings(ProgrammingLanguage lang, ScriptEngine e)
  {
    super.setupBindings(lang, e);
    if (lang.isPython()) {
      try {
        e.put("RecList", RecList.class);
        String script = "import array\n"
                        + "def cons(a,b):\n"
                        + "  return RecList(a,b)\n"
                        + "def RecListFromArray(a, rank=0):\n"
                        + "  if len(a) <= rank:\n"
                        + "    return None\n"
                        + "  return RecList(a[rank], RecListFromArray(a, rank+1))\n"
                        + "def toRecListIfArray(a):\n"
                        + "  if isinstance(a, array.array):\n"
                        + "    return RecListFromArray(a)\n"
                        + "  return a\n";
        if (Game.getInstance().isDebugEnabled() && !Game.getInstance().isBatchExecution())
          System.out.println("Extra script chunk added by " + getClass() + ":\n" + script);
        e.eval(script);
      } catch (ScriptException e1) {
        e1.printStackTrace();
      }
    }
  }
}
