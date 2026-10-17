package lessons.welcome.methods.basics;

import java.awt.Color;
import java.util.regex.Pattern;
import plm.core.PLMCompilerException;
import plm.core.lang.primitives.PrimitivePot;
import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class MethodsDogHouse extends ExerciseTemplated {
  /* Comments (//, slash-star, #) and strings (including triple-quoted ones), in the languages of the PLM */
  private static final Pattern COMMENTS_AND_STRINGS =
      Pattern.compile("\"\"\"[\\s\\S]*?\"\"\"|//[^\\n]*|/\\*[\\s\\S]*?\\*/|#[^\\n]*|\"(?:\\\\.|[^\"\\\\\\n])*\"|'(?:\\\\.|[^'\\\\\\n])*'");

  public MethodsDogHouse(Lesson lesson)
  {
    super(lesson);
    BuggleWorld myWorld = new BuggleWorld("World", 7, 7);
    new SimpleBuggle(myWorld, "Puppy", 0, 6, Direction.EAST, Color.red, Color.red);

    setup(myWorld);
  }

  /**
   * Refuses code that does not call left() exactly once, ignoring the comments and strings. The alias of left() in the current human
   * language counts too, but not the ones of the other languages.
   */
  @Override protected void verifySource(String code) throws PLMCompilerException
  {
    String alias     = Game.i18n.trc(PrimitivePot.NAME_CONTEXT, "left");
    Pattern leftCall = Pattern.compile("(?<![\\w.])(?:left|" + Pattern.quote(alias) + ")\\s*\\(", Pattern.UNICODE_CHARACTER_CLASS);
    long calls       = leftCall.matcher(COMMENTS_AND_STRINGS.matcher(code).replaceAll(" ")).results().count();
    if (calls != 1)
      throw new PLMCompilerException(
          Game.i18n.tr("Your code must call {0}() exactly once, but it contains {1} calls. Factorize it in your method.", alias, calls));
  }
}
