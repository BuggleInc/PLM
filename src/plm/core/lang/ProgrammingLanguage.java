package plm.core.lang;

import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.ImageIcon;
import lessons.lightbot.universe.LightBotEntity;
import plm.core.PLMCompilerException;
import plm.core.model.Game;
import plm.core.model.LogWriter;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.RunOutcome;
import plm.universe.Entity;

/**
 * Captures the whole logic of a given programming language (compiling the user code, running it).
 *
 * If you want to add a new programming language to the PLM, then you probably want to read that page:
 * https://github.com/oster/PLM/wiki/Adding-a-new-programming-language
 *
 */

public abstract class ProgrammingLanguage implements Comparable<ProgrammingLanguage> {
  String lang;
  String ext;
  ImageIcon icon;
  public ProgrammingLanguage(String l, String ext, ImageIcon i)
  {
    lang      = l;
    this.ext  = ext;
    this.icon = i;
  }
  public boolean equals(Object o)
  {
    if (!super.equals(o))
      return false;
    if (getClass() != o.getClass())
      return false;
    return lang.equals(((ProgrammingLanguage)o).lang);
  }
  public String getLang() { return lang; }
  public String getExt() { return ext; }
  @Override public String toString() { return lang; }
  @Override public int hashCode() { return lang.hashCode(); }
  @Override public int compareTo(ProgrammingLanguage o)
  {
    if (o == null)
      return 1;
    int res = lang.compareTo(o.lang);
    if (res != 0)
      return res;
    return ext.compareTo(o.ext);
  }
  public ImageIcon getIcon() { return icon; }
  public boolean isJava() { return false; }
  public boolean isScala() { return false; }
  public boolean isPython() { return false; }
  public boolean isC() { return false; }
  public boolean isLightBot() { return false; }

  // internal tool used to detect whether a given language is usable
  protected String canResolve(String resource, String hint)
  {
    try {
      URL path = getClass().getResource(resource + ".class");
      if (path != null)
        return ""; // Cool, found it.

      path = ClassLoader.getSystemResource(resource + ".class");
      if (path != null)
        return ""; // Cool, found it.

      resource = resource.replaceAll("/", ".");
      resource = resource.substring(1);
      Class.forName(resource).newInstance();
      return ""; // That's cool if I manage to create one such object

    } catch (ClassNotFoundException ce) {
      return Game.i18n.tr("Resource {0} not found in the classpath.\nIs {1} in your classpath?", resource, hint);
    } catch (Exception e) {
      return Game.i18n.tr("{0} received while searching for resource {1}: {2}", e.getClass().getName(), resource, e.getLocalizedMessage());
    }
  }

  protected Map<String, String> runtimePatterns = new TreeMap<String, String>();
  public abstract void compileExo(Exercise exercise, LogWriter out, StudentOrCorrection whatToCompile) throws PLMCompilerException;

  /**
   * Make the entity run, according to the used universe and programming language.
   *
   * This task is not trivial given that it depends on the universe and the programming language:
   *  * In most universes, the active part is the entity itself. But in the Bat universe, the
   *    student-provided method (that is not a real entity but part of the world directly)
   *    is run against all testcase, that are not real worlds either.
   *
   *  * Java, Scala, Python and C entities are launched in an external program, with some threads
   *    to deal with the pipes that are connected to the external process.
   *  * LightBot entities are launched by executing the {@link LightBotEntity#run()} method,
   *    that is NOT defined by the student, but interprets the code of the students.
   *
   *  @see #run() that encodes the student logic in Java
   */
  public abstract void runEntity(Entity ent, RunOutcome progress);

  public enum BrokenLanguageState { Usable, NotUsable, Unitialized }
  ;
  public abstract boolean isBrokenLanguage();
  public abstract String getBrokenLanguageMessage();
}
