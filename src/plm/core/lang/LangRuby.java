package plm.core.lang;

import javax.script.ScriptException;
import plm.core.model.lesson.RunOutcome;
import plm.core.ui.ResourcesCache;
import plm.universe.Entity;

public class LangRuby extends ScriptingLanguage {

  public LangRuby() { super("Ruby", "rb", ResourcesCache.getIcon("img/lang_ruby.png")); }
  @Override public boolean isRuby() { return true; }

  /* Language detection logic */
  private static String brokenLanguageMessage;
  @Override public String getBrokenLanguageMessage() { return brokenLanguageMessage; }
  private static BrokenLanguageState brokenLanguageState = BrokenLanguageState.Unitialized;
  @Override public boolean isBrokenLanguage()
  {

    if (brokenLanguageState == BrokenLanguageState.Unitialized) {
      throw new RuntimeException("Unimplemented");
    }
    return brokenLanguageState != BrokenLanguageState.Usable;
  }

  @Override protected void setupEntityBindings(Entity ent)
  {
    // Nothing to do for now
  }

  @Override protected boolean handleLangException(ScriptException e, Entity ent, RunOutcome progress)
  {
    // For now, we don't know how to decipher Ruby exceptions.
    return false;
  }
}
