package lessons.lander.universe;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.ImageIcon;

import plm.core.model.ProgrammingLanguage;
import plm.core.ui.WorldView;
import plm.universe.World;
import scala.collection.immutable.List;

public class DelegatingLanderWorld extends World {

  final LanderWorld realWorld;

  public DelegatingLanderWorld(
      String name, int width, int height, List<Point> ground, Lander lander) {
    super(name);
    realWorld = new LanderWorld(this);
    realWorld.width_$eq(width);
    realWorld.height_$eq(height);
    realWorld.ground_$eq(ground);
    realWorld.lander_$eq(lander);
  }

  public DelegatingLanderWorld(DelegatingLanderWorld world) {
    super(world.getName());
    realWorld = new LanderWorld(this);
    reset(world);
  }

  @Override
  public ImageIcon getIcon() {
    return realWorld.getIcon();
  }

  @Override
  public void setupBindings(ProgrammingLanguage lang, ScriptEngine engine) throws ScriptException {
    realWorld.setupBindings(lang, engine);
  }

  @Override
  public String diffTo(World world) {
    return realWorld.diffTo(world);
  }

  @Override
  public void reset(World initialWorld) {
    realWorld.reset(((DelegatingLanderWorld) initialWorld).realWorld);
    super.reset(initialWorld);
  }

  @Override
  public WorldView getView() {
    return realWorld.getView();
  }

  @Override
  public String toString() {
    return realWorld.toString();
  }
}
