package lessons.lander.universe;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.ImageIcon;

import plm.core.lang.ProgrammingLanguage;
import plm.core.ui.WorldView;
import plm.universe.World;
import scala.collection.immutable.List;

public class DelegatingLanderWorld extends World {

  final LanderWorld realWorld;

  public DelegatingLanderWorld(String name, int width, int height,
      List<Point> ground, Point position, Point speed, double angle, int thrust, int fuel) {
    super(name);
    realWorld = new LanderWorld(this);
    realWorld.width_$eq(width);
    realWorld.height_$eq(height);
    realWorld.ground_$eq(ground);
    realWorld.position_$eq(position);
    realWorld.speed_$eq(speed);
    realWorld.angle_$eq(angle);
    realWorld.thrust_$eq(thrust);
    realWorld.fuel_$eq(fuel);
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
  public boolean winning(World target) {
    return realWorld.winning(target);
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
