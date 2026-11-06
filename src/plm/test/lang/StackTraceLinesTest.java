package plm.test.lang;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import plm.core.lang.LangJava;
import plm.core.lang.ProgrammingLanguage.Compiled;
import plm.core.model.Game;

/**
 * The JVM names the generated file alone in its stack traces: the frames of the code of the editor get its lines, and the frames of
 * the template keep their line but get the full path of the generated file, next to the jar.
 */
public class StackTraceLinesTest {

  /** A Java language giving access to how it displays the stack traces of the student process */
  private static class TracedJava extends LangJava {
    String shown(String line, Compiled compiled) { return shiftTrace(line, compiled); }
  }

  @Test public void theFramesOfTheTemplateGetTheFullPathOfTheGeneratedFile()
  {
    Game game     = Game.getInstance();
    boolean debug = game.isDebugEnabled();
    if (debug)
      game.switchDebug(); // debugging keeps the stack traces as is
    try {
      Compiled compiled = new Compiled(Path.of("/tmp", "plm", "java", "exo", "Code.jar").toString(), "", 40);
      String generated  = Path.of("/tmp", "plm", "java", "exo", "Entity.java").toString();
      TracedJava java   = new TracedJava();
      assertEquals("\tat generated.Entity.forward(" + generated + ":12)", java.shown("\tat generated.Entity.forward(Entity.java:12)", compiled));
      assertEquals("\tat generated.Entity.run(Entity.java:5)", java.shown("\tat generated.Entity.run(Entity.java:45)", compiled));
      assertEquals("\tat generated.Main.main(Main.java:7)", java.shown("\tat generated.Main.main(Main.java:7)", compiled));
    } finally {
      if (debug)
        game.switchDebug();
    }
  }
}
