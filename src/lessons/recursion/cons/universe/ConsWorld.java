package lessons.recursion.cons.universe;

import javax.swing.ImageIcon;
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
}
