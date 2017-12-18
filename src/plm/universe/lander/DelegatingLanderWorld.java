package plm.universe.lander;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import plm.core.lang.ProgrammingLanguage;
import plm.core.utils.FileUtils;
import plm.universe.SVGOperation;
import plm.universe.World;
import plm.universe.hanoi.HanoiWorldView;
import scala.collection.immutable.List;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class DelegatingLanderWorld extends World {

  final LanderWorld realWorld;

  public DelegatingLanderWorld(FileUtils fileUtils, String name, int width, int height,
                               List<Point> ground, Point position, Point speed, double angle, int thrust, int fuel) {
    super(fileUtils, name);
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
    super(world);
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
  public String diffTo(World world) {
    return realWorld.diffTo(world);
  }

  @Override
  public void reset(World initialWorld) {
    realWorld.reset(((DelegatingLanderWorld) initialWorld).realWorld);
    super.reset(initialWorld);
  }

  @Override
  protected java.util.List<SVGOperation> draw() {
//    String svg = LanderWorldView.draw(this, 400,400);
    java.util.List<SVGOperation> list = new ArrayList<SVGOperation>();
//    SVGOperation operation = new SVGOperation(svg);
//    list.add(operation);
<<<<<<< HEAD
//
//    PrintWriter writer = null;
//    try {
//      writer = new PrintWriter(this.getName()+".svg", "UTF-8");
//    } catch (FileNotFoundException e) {
//      e.printStackTrace();
//    } catch (UnsupportedEncodingException e) {
//      e.printStackTrace();
//    }
//    writer.write(svg);
//    writer.close();
=======

//    PrintWriter writer = null;
//     try {
//       writer = new PrintWriter(this.getName()+".svg", "UTF-8");
//     } catch (FileNotFoundException e) {
//       e.printStackTrace();
//     } catch (UnsupportedEncodingException e) {
//       e.printStackTrace();
//     }
//     writer.write(svg);
//     writer.close();
>>>>>>> 6a4139d430f483d4067d7543e6c5da93a22f2449

    return list;
  }

  @Override
  public String toString() {
    return realWorld.toString();
  }
}
