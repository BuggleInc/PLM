package plm.universe.lander;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.xnap.commons.i18n.I18n;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.universe.World;
import scala.collection.immutable.List;

public class DelegatingLanderWorld extends World {

  final LanderWorld realWorld;

  public DelegatingLanderWorld(Game game, String name, int width, int height,
      List<Point> ground, Point position, Point speed, double angle, int thrust, int fuel) {
    super(game, name);
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
    super(world.getGame(), world.getName());
    realWorld = new LanderWorld(this);
    reset(world);
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
  public String diffTo(World world, I18n i18n) {
    return realWorld.diffTo(world, i18n);
  }

  @Override
  public void reset(World initialWorld) {
    realWorld.reset(((DelegatingLanderWorld) initialWorld).realWorld);
    super.reset(initialWorld);
  }

  @Override
  public String toString() {
    return realWorld.toString();
  }
}
