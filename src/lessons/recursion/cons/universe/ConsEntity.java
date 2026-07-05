package lessons.recursion.cons.universe;

import plm.universe.bat.BatEntity;

public class ConsEntity extends BatEntity {

  public RecList cons(int head, RecList tail) { return new RecList(head, tail); }
  @Override protected String pythonArgExpression() { return "toRecListIfArray(t.getParameter(i))"; }
}