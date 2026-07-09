package plm.universe.bat;

import static plm.core.ValueSerializer.*;

import java.io.BufferedWriter;
import plm.core.lang.ProgrammingLanguage;
import plm.core.lang.primitives.EntityPrimitives;
import plm.universe.Entity;
import plm.universe.World;

@EntityPrimitives(BatEntityPrimitives.class)
public abstract class BatEntity extends Entity implements BatEntityPrimitives {

  public BatEntity() { super(); }

  public BatEntity(String name, World w) { super(name, w); }

  public BatEntity(BatEntity other)
  {
    super();
    copy(other);
  }

  @Override public boolean equals(Object o)
  {
    if (!(o instanceof BatEntity)) {
      return false;
    }
    return (super.equals(o));
  }

  @Override public int getTestCount() { return ((BatWorld)world).getTests().size(); }
  @Override public String getTest(int i) { return serialize(((BatWorld)world).getTests().get(i).parameters); }
  @Override public void setTestResult(int i, String str)
  {
    ((BatWorld)world).getTests().get(i).setResult(deserialize(str));
    System.err.println("Got the serialized(" + i + "," + str + "), of type " + ((BatWorld)world).getTests().get(i).getResult().getClass());
  }

  @Override public String getScript(ProgrammingLanguage lang)
  {
    String script = super.getScript(lang);
    if (lang.isPython() && script != null)
      script = script + "\n" + pythonDispatchLoop();
    return script;
  }
  /** Calls the student's function once per test case and stores its result. */
  protected String pythonDispatchLoop()
  {
    return "for t in batTests:\n"
        + "    args = [" + pythonArgExpression() + " for i in range(t.getParameterCount())]\n"
        + "    t.setResult(globals()[t.getFunName()](*args))\n";
  }
  protected String pythonArgExpression() { return "t.getParameter(i)"; }
  @Override public void command(String command, BufferedWriter out)
  {
    // TODO if use
  }
}
