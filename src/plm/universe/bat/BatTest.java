package plm.universe.bat;

import java.util.StringJoiner;
import plm.core.lang.ProgrammingLanguage;

public class BatTest {
  private String funName;
  Object[] parameters;
  Object result;
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
    return ValueFormatter.equals(parameters, other.parameters) && ValueFormatter.equals(result, other.result);
  }

  public Object getParameter(int i) { return ValueFormatter.normalize(parameters[i]); }
  Object getResult() { return result; }
  public void setResult(Object res) { result = res; }

  public String getFunName() { return funName; }
  public int getParameterCount() { return parameters.length; }

  boolean isVisible() { return visible; }

  public String stringParameter(Object o, ProgrammingLanguage lang) { return ValueFormatter.format(o, lang); }
  public String getName(ProgrammingLanguage lang)
  {
    StringJoiner name = new StringJoiner(",", funName + "(", ")");
    for (Object o : parameters)
      name.add(ValueFormatter.format(o, lang));
    return name.toString();
  }

  public String toString(ProgrammingLanguage lang) { return getName(lang); }
}
