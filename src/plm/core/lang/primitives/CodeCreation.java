package plm.core.lang.primitives;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import lessons.lander.universe.LanderEntity;
import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.hanoi.universe.HanoiEntity;
import lessons.sort.baseball.universe.BaseballEntity;
import lessons.sort.dutchflag.universe.DutchFlagEntity;
import lessons.sort.pancake.universe.PancakeEntity;
import lessons.turmites.universe.TurmiteEntity;
import plm.core.lang.LangC;
import plm.core.lang.LangJava;
import plm.core.lang.LangPython;
import plm.core.lang.LangScala;
import plm.test.simple.SimpleExerciseEntity;
import plm.universe.Entity;
import plm.universe.bat.BatEntity;
import plm.universe.bugglequest.AbstractBuggle;
import plm.universe.sort.SortingEntity;
import plm.universe.turtles.Turtle;

public class CodeCreation {
  public static void main(String[] args) throws IOException
  {
    File folder = new File("target/classes/resources/langages/");

    Map<String, Class<? extends Entity>> remoteMap =
        Map.ofEntries(Map.entry("RemoteBat", BatEntity.class), Map.entry("RemoteCons", ConsEntity.class), Map.entry("RemoteBuggle", AbstractBuggle.class),
                      Map.entry("RemoteTurmite", TurmiteEntity.class), Map.entry("RemoteSort", SortingEntity.class), Map.entry("RemoteTurtle", Turtle.class),
                      Map.entry("RemotePancake", PancakeEntity.class), Map.entry("RemoteHanoi", HanoiEntity.class),
                      Map.entry("RemoteBaseball", BaseballEntity.class), Map.entry("RemoteDutchFlag", DutchFlagEntity.class),
                      Map.entry("RemoteSimple", SimpleExerciseEntity.class), Map.entry("RemoteLander", LanderEntity.class));

    // Hand-written code to splice into some generated remote stubs, for manual helpers
    Map<String, String> remoteExtraJavaCode  = Map.of("RemoteCons", ConsEntity.JAVA_REMOTE_EXTRA_CODE);
    Map<String, String> remoteExtraScalaCode = Map.of("RemoteCons", ConsEntity.SCALA_REMOTE_EXTRA_CODE);
    Map<String, String> remoteExtraPythonCode = Map.of("RemoteCons", ConsEntity.PYTHON_REMOTE_EXTRA_CODE);

    LangJava.LangJavaExternalPrimitiveGenerator langJavaGenerator = new LangJava.LangJavaExternalPrimitiveGenerator();
    File javaFolder                                               = new File(folder, "java");
    System.err.print("Generating Java entities in " + javaFolder + ":");
    for (Map.Entry<String, Class<? extends Entity>> entry : remoteMap.entrySet()) {
      String name                   = entry.getKey();
      Class<? extends Entity> clazz = entry.getValue();

      Map<Integer, PrimitiveMethod> list = null;
      try {
        list = PrimitiveRegistration.getMaximalPrimitiveForEntity(clazz);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      System.err.print(" " + name);
      langJavaGenerator.generate(javaFolder, name, list.values().stream().toList(), remoteExtraJavaCode.getOrDefault(name, ""));
    }
    System.err.println(".");

    LangScala.LangScalaExternalPrimitiveGenerator langScalaGenerator = new LangScala.LangScalaExternalPrimitiveGenerator();
    File scalaFolder                                                 = new File(folder, "scala");
    System.err.print("Generating Scala entities in " + scalaFolder + ":");
    for (Map.Entry<String, Class<? extends Entity>> entry : remoteMap.entrySet()) {
      String name                   = entry.getKey();
      Class<? extends Entity> clazz = entry.getValue();

      Map<Integer, PrimitiveMethod> list = null;
      try {
        list = PrimitiveRegistration.getMaximalPrimitiveForEntity(clazz);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      System.err.print(" " + name);
      langScalaGenerator.generate(scalaFolder, name, list.values().stream().toList(), remoteExtraScalaCode.getOrDefault(name, ""));
    }
    System.err.println(".");

    LangPython.LangPythonExternalPrimitiveGenerator langPythonGenerator = new LangPython.LangPythonExternalPrimitiveGenerator();
    File pythonFolder                                                   = new File(folder, "python");
    System.err.print("Generating Python entities in " + pythonFolder + ":");
    for (Map.Entry<String, Class<? extends Entity>> entry : remoteMap.entrySet()) {
      String name                   = entry.getKey();
      Class<? extends Entity> clazz = entry.getValue();

      Map<Integer, PrimitiveMethod> list = null;
      try {
        list = PrimitiveRegistration.getMaximalPrimitiveForEntity(clazz);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      System.err.print(" " + name);
      langPythonGenerator.generate(pythonFolder, name, list.values().stream().toList(), remoteExtraPythonCode.getOrDefault(name, ""));
    }
    System.err.println(".");

    LangC.LangCExternalPrimitiveGenerator langCGenerator = new LangC.LangCExternalPrimitiveGenerator();
    File cFolder                                         = new File(folder, "c");
    System.err.print("Generating C entities in " + cFolder + ":");
    for (Map.Entry<String, Class<? extends Entity>> entry : remoteMap.entrySet()) {
      String name                   = entry.getKey();
      Class<? extends Entity> clazz = entry.getValue();

      Map<Integer, PrimitiveMethod> list = null;
      try {
        list = PrimitiveRegistration.getMaximalPrimitiveForEntity(clazz);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      System.err.print(" " + name);
      langCGenerator.generate(cFolder, name, list.values().stream().toList());
    }
    System.err.println(".");
  }
}
