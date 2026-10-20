package lessons.recursion.cons.universe;

import plm.core.lang.primitives.Glue;
import plm.universe.bat.BatEntity;

@Glue("RemoteCons")
public abstract class ConsEntity extends BatEntity {

  public RecList cons(int head, RecList tail) { return new RecList(head, tail); }
}
