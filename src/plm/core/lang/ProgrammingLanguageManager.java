package plm.core.lang;

import java.text.MessageFormat;
import plm.core.model.BrokenProgrammingLanguageException;
import plm.core.model.Game;

public class ProgrammingLanguageManager {

  public static final String PROP_PROGRAMING_LANGUAGE = "plm.programingLanguage";

  public final ProgrammingLanguage JAVA   = new LangJava();
  public final ProgrammingLanguage PYTHON = new LangPython();
  public final ProgrammingLanguage SCALA  = new LangScala();
  public final ProgrammingLanguage C      = new LangC();
  // public final ProgrammingLanguage JAVASCRIPT = new
  // ProgrammingLanguage("JavaScript","js",ResourcesCache.getIcon("img/lang_javascript.png"));
  public final ProgrammingLanguage RUBY     = new LangRuby();
  public final ProgrammingLanguage LIGHTBOT = new LangLightbot();

  public final ProgrammingLanguage[] langs = new ProgrammingLanguage[] {
      JAVA, PYTHON, SCALA, RUBY, LIGHTBOT, C // TODO: re-add JAVASCRIPT to this list once it works at least a bit
  };
  private ProgrammingLanguage currentLanguage = JAVA;

  public ProgrammingLanguageManager()
  {
    if (SCALA.isBrokenLanguage())
      System.err.println(Game.i18n.tr("Please install Scala version 2.12 or higher to use it in the PLM."));
    else
      System.err.println(Game.i18n.tr("Scala is usable on your machine. Congratulations."));
    if (PYTHON.isBrokenLanguage())
      System.err.println(Game.i18n.tr("Please install jython to use the python programming language in the PLM."));
    else
      System.err.println(Game.i18n.tr("Jython is usable on your machine. Congratulations."));
    if (C.isBrokenLanguage())
      System.err.println(Game.i18n.tr("Please install gcc to use the C programming language in the PLM."));
    else
      System.err.println(Game.i18n.tr("C is usable on your machine. Congratulations."));

    String defaultProgrammingLanguageName          = Game.getProperty(PROP_PROGRAMING_LANGUAGE, JAVA.getLang(), true);
    ProgrammingLanguage defaultProgrammingLanguage = JAVA;
    if (defaultProgrammingLanguageName.equalsIgnoreCase(PYTHON.getLang()))
      defaultProgrammingLanguage = PYTHON;
    else if (defaultProgrammingLanguageName.equalsIgnoreCase(SCALA.getLang()))
      defaultProgrammingLanguage = SCALA;
    else if (defaultProgrammingLanguageName.equalsIgnoreCase(C.getLang()))
      defaultProgrammingLanguage = C;
    else if (!defaultProgrammingLanguageName.equalsIgnoreCase(JAVA.getLang()))
      System.err.println(
          Game.i18n.tr("Warning, the default programming language is neither ''Java'' nor ''python'' or ''Scala'' or " +
                       "''C'' but {0}.\n"
                       + "   This language will be used to setup the worlds, possibly leading to severe issues for " +
                         "the exercises that don''t expect it.\n"
                       + "   It is safer to change the current language, and restart the PLM before proceeding."));

    if (defaultProgrammingLanguage == SCALA && SCALA.isBrokenLanguage()) {
      System.err.println(Game.i18n.tr("The default programming language is Scala, but your scala installation is not " +
                                      "usable. Switching to Java instead.\n"));
      defaultProgrammingLanguage = JAVA;
    } else if (defaultProgrammingLanguage == PYTHON && PYTHON.isBrokenLanguage()) {
      System.err.println(Game.i18n.tr("The default programming language is python, but your python installation is " +
                                      "not usable. Switching to Java instead.\n"));
      defaultProgrammingLanguage = JAVA;
    } else if (defaultProgrammingLanguage == C && C.isBrokenLanguage()) {
      System.err.println(Game.i18n.tr("The default programming language is C, but your C installation is not "
                                      + "usable. Switching to Java instead.\n"));
      defaultProgrammingLanguage = JAVA;
    }
    currentLanguage = defaultProgrammingLanguage;
  }

  public ProgrammingLanguage current() { return currentLanguage; }
  public boolean isValidProgLanguage(ProgrammingLanguage newL)
  {
    for (ProgrammingLanguage pl : langs)
      if (pl.equals(newL))
        return true;
    return false;
  }
  public void setCurrent(ProgrammingLanguage newLanguage) throws BrokenProgrammingLanguageException
  {
    if (currentLanguage.equals(newLanguage))
      return;

    if (isValidProgLanguage(newLanguage)) {
      // System.out.println("Switch programming language to "+newLanguage);
      if (newLanguage.isScala() && newLanguage.isBrokenLanguage()) {
        throw new BrokenProgrammingLanguageException(
            Game.i18n.tr("Scala is missing"),
            Game.i18n.tr("Please install Scala version 2.12 or higher to use it in the PLM.\n\n") +
                SCALA.getBrokenLanguageMessage());
      }
      if (newLanguage.isPython() && newLanguage.isBrokenLanguage()) {
        throw new BrokenProgrammingLanguageException(
            Game.i18n.tr("Python is missing"),
            MessageFormat.format(Game.i18n.tr("Please install jython and its dependencies to use the python " +
                                              "programming language in the PLM.\nError: {0}\n"),
                                 PYTHON.getBrokenLanguageMessage()));
      }
      if (newLanguage.isC() && newLanguage.isBrokenLanguage()) {
        throw new BrokenProgrammingLanguageException(
            Game.i18n.tr("C is missing"),
            Game.i18n.tr("Please install C and its dependencies to use the C programming language in the PLM.\n\n") +
                C.getBrokenLanguageMessage());
      }
      currentLanguage = newLanguage;
      if (newLanguage.isJava() || newLanguage.isPython() || newLanguage.isScala() ||
          newLanguage.isC()) // Only save it if it's stable enough
        Game.setProperty(PROP_PROGRAMING_LANGUAGE, newLanguage.getLang());

      return;
    }
    throw new BrokenProgrammingLanguageException(
        "Missing " + newLanguage, "Ignoring request to switch the programming language to the unknown " + newLanguage);
  }
}