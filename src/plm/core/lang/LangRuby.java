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
  @Override
  public void runEntity(Entity ent, RunOutcome progress) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'runEntity'");
  }
}
