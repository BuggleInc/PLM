package plm.test.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Locale;
import javax.swing.KeyStroke;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.FunctionCompletion;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import plm.core.lang.RemoteExecutionLang;
import plm.core.model.Game;
import plm.core.ui.CodeCompletion;
import plm.test.universe.ApiGatingTest.TeleportingBuggle;
import plm.universe.bugglequest.BuggleWorld;

/** The completion in a Java editor of buggles, in French, through the API of its provider, without showing its popup */
public class CodeCompletionTest {
  private final RSyntaxTextArea code = new RSyntaxTextArea();
  private final CodeCompletion completion;

  @BeforeAll static void inFrench() { Game.getInstance().setLocale(new Locale("fr")); }
  @AfterAll static void inEnglish() { Game.getInstance().setLocale(new Locale("en")); }

  public CodeCompletionTest()
  {
    code.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
    BuggleWorld world = new BuggleWorld("completion", 3, 3);
    world.addEntity(new TeleportingBuggle()); // forbids setX(), setY() and setPos()
    completion = new CodeCompletion(code, world, (RemoteExecutionLang)Game.getInstance().programmingLanguageManager.JAVA);
  }

  /** The completions proposed at the end of that code */
  private List<Completion> completions(String text)
  {
    code.setText(text);
    code.setCaretPosition(text.length());
    return completion.getCompletions(code);
  }

  /** The texts proposed at the end of that code, followed by () for the functions */
  private List<String> proposed(String text)
  {
    return completions(text).stream().map(c -> c.getReplacementText() + (c instanceof FunctionCompletion ? "()" : "")).toList();
  }

  @Test public void thePrimitivesAreProposedUnderTheirNameAndTheirAlias()
  {
    assertEquals(List.of("avance()"), proposed("av"));
    assertEquals(List.of("forward()"), proposed("forw"));
    List<String> set = proposed("set");
    assertTrue(set.containsAll(List.of("setBrushColor()", "setCouleurBrosse()")), set.toString());
  }

  @Test public void theForbiddenAndTheUndocumentedPrimitivesAreNotProposed()
  {
    List<String> set = proposed("set");
    for (String hidden : new String[] {"setX()", "setY()", "setPos()"})
      assertFalse(set.contains(hidden), hidden + " is forbidden: " + set);
    List<String> le = proposed("Le");
    assertTrue(le.contains("leveBrosse()"), le.toString());
    assertFalse(le.contains("Left()"), le.toString());
  }

  @Test public void aPrimitiveIsDocumentedInTheProgrammingLanguage()
  {
    String summary = completions("avan").get(0).getSummary();
    assertTrue(summary.contains("void avance(int steps)"), summary);
    assertTrue(summary.contains(Game.i18n.tr("Moves the given amount of cells forward.")), summary);
  }

  @Test public void theNamesOfTheCodeAreProposed()
  {
    String source = "int compteur = 0; // commentaire\nString s = \"chaine\";\nvoid avancer() { compteur++; }\n";
    assertEquals(List.of("compteur"), proposed(source + "co"));
    assertEquals(List.of("avance()", "avancer"), proposed(source + "av"));
    assertEquals(List.of(), proposed(source + "ch"));
    assertEquals(List.of(), proposed(source + "vo"));
  }

  @Test public void theWordTypedIsNotProposed() { assertEquals(List.of("compteur"), proposed("int compteur;\ncomp")); }

  @Test public void theCompletionOpensOnCtrlSpaceOrAutomaticallyWithoutInsertingAlone()
  {
    AutoCompletion popup = completion.autoCompletion;
    assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.CTRL_DOWN_MASK), popup.getTriggerKey());
    assertTrue(popup.isAutoActivationEnabled());
    // The delay cannot be checked: getAutoActivationDelay() of AutoComplete 2.5.8 returns another delay than the one set
    assertFalse(popup.getAutoCompleteSingleChoices());
    assertTrue(popup.getShowDescWindow());
    assertTrue(popup.isParameterAssistanceEnabled());
  }
}
