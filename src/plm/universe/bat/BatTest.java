package plm.universe.bat;

import java.util.function.ObjDoubleConsumer;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;

public class BatTest {
  private String funName;
  Object[] parameters;
  Object result;
  private String name = null;
  boolean visible;

  public BatTest(String funName, boolean visible, Object parameters)
  {
    this.funName = funName;
    this.visible = visible;

    /* Cast parameters into an array on need */
    Object param = ValueFormatter.normalize(parameters);
    if (param.getClass().isArray()) {
      this.parameters = (Object[])param;
    } else {
      this.parameters = new Object[] {param};
    }
  }

  public BatTest copy() { return new BatTest(funName, visible, parameters.clone()); }

  @Override public boolean equals(Object o)
  {
    if (!(o instanceof BatTest))
      return false;
    BatTest other = (BatTest)o;
    return ValueFormatter.equals(parameters, other.parameters);
  }

  public Object getParameter(int i)
  {
    if (parameters[i] != null && parameters[i].getClass().isArray()) {
      if (parameters[i].getClass().getComponentType().equals(Integer.TYPE)) {
        int[] orig = (int[])parameters[i];
        int[] res  = new int[orig.length];
        for (int cpt = 0; cpt < orig.length; cpt++)
          res[cpt] = orig[cpt];
        return res;
      } else if (parameters[i].getClass().getComponentType().equals(Integer.class)) {
        Integer[] orig = (Integer[])parameters[i];
        Integer[] res  = new Integer[orig.length];
        for (int cpt = 0; cpt < orig.length; cpt++)
          res[cpt] = orig[cpt];
        return res;
      } else {
        throw new RuntimeException("Unhandled internal type (only Array<int> and Array<Integer> are handled so far)");
      }
    }
    return parameters[i];
  }
  Object getResult() { return result; }
  public void setResult(Object res) { result = res; }

  boolean isVisible() { return visible; }

  public String stringParameter(Object o)
  {
    return ValueFormatter.format(o, Game.getInstance().getProgrammingLanguage());
  }
  public String getName()
  {
    ProgrammingLanguage pl = Game.getInstance().getProgrammingLanguage();
    if (name == null) {
      StringBuffer sb = new StringBuffer(funName + "(");

      for (Object o : parameters) {
        sb.append(ValueFormatter.format(o, pl));
        sb.append(",");
      }
      if (parameters.length > 1)
        sb.deleteCharAt(sb.length() - 1);
      sb.append(") = ");
      sb.append(ValueFormatter.format(result, pl));

      name = sb.toString();
    }
    return name;
  }

  public String toString() { return getName(); }
}
