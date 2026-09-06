package plm.universe.bat;

import static plm.core.ValueSerializer.*;

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
  @Override public void setTestResult(int i, String str) { ((BatWorld)world).getTests().get(i).setResult(deserialize(str)); }
}
