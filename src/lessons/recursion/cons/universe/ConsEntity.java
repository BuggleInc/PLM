package lessons.recursion.cons.universe;

import plm.universe.bat.BatEntity;

public abstract class ConsEntity extends BatEntity {

  // Code to be injected into the generated RemoteCons.java (see plm.core.lang.primitives.CodeCreation).
  public static final String JAVA_REMOTE_EXTRA_CODE = "public static RecList cons(int head, RecList tail) { return new RecList(head, tail); }";
  // Same, for the generated RemoteCons.scala.
  public static final String SCALA_REMOTE_EXTRA_CODE = "def cons(head: Int, tail: RecList): RecList = new RecList(head, tail)";

  public RecList cons(int head, RecList tail) { return new RecList(head, tail); }
  @Override protected String pythonArgExpression() { return "toRecListIfArray(t.getParameter(i))"; }
}