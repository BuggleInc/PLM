package plm.core.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.text.JTextComponent;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.CompletionCellRenderer;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.FunctionCompletion;
import org.fife.ui.autocomplete.ParameterizedCompletion;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Token;
import plm.core.lang.RemoteExecutionLang;
import plm.core.lang.primitives.ExternalPrimitiveLanguage;
import plm.core.lang.primitives.PrimitiveMethod;
import plm.core.lang.primitives.PrimitivePot;
import plm.core.model.Game;
import plm.universe.World;

/**
 * Completion in an editor, with Ctrl+Space or after 2 seconds without a new letter. It proposes the primitives that the
 * exercise documents and allows, under their name and their alias in the current human language, with their documentation and
 * their parameters, then the names of the student's code. They are collected at each request, as the code and the human language
 * change.
 */
public class CodeCompletion extends DefaultCompletionProvider {
  /** A name in all the languages, which may be accented */
  private static final String NAME = "[\\p{L}_][\\p{L}\\p{Nd}_]*";
  private final World world;
  private final ExternalPrimitiveLanguage generator;
  /** The popup and its settings, to uninstall with the editor */
  public final AutoCompletion autoCompletion;

  public CodeCompletion(RSyntaxTextArea editor, World world, RemoteExecutionLang lang)
  {
    this.world     = world;
    this.generator = lang.getPrimitiveGenerator();
    setAutoActivationRules(true, null);
    setParameterizedCompletionParams('(', ", ", ')'); // the calls to insert, with tab stops on the parameters

    autoCompletion = new AutoCompletion(this); // Ctrl+Space by default
    autoCompletion.setAutoActivationEnabled(true);
    autoCompletion.setAutoActivationDelay(2000);        // milliseconds after the last letter
    autoCompletion.setAutoCompleteSingleChoices(false); // the popup shows the documentation of the choice
    autoCompletion.setShowDescWindow(true);
    autoCompletion.setListCellRenderer(new CompletionCellRenderer()); // the parameters of the functions
    autoCompletion.setParameterAssistanceEnabled(true);
    autoCompletion.install(editor);
  }

  /** The primitives, then the identifiers of the code that are not primitives nor the word typed, starting with the word typed */
  @Override protected List<Completion> getCompletionsImpl(JTextComponent editor)
  {
    List<Completion> completions = new ArrayList<>();
    Set<String> names            = new HashSet<>();
    world.getDocumentedPrimitives().forEach((group, primitives) -> {
      for (PrimitiveMethod primitive : primitives)
        for (String name : List.of(primitive.name(), Game.i18n.trc(PrimitivePot.NAME_CONTEXT, primitive.name())))
          if (names.add(name))
            completions.add(function(primitive, name, group));
    });

    names.add(getAlreadyEnteredText(editor));
    RSyntaxDocument document = (RSyntaxDocument)editor.getDocument();
    for (int line = 0; line < document.getDefaultRootElement().getElementCount(); line++)
      for (Token token = document.getTokenListForLine(line); token != null && token.isPaintable(); token = token.getNextToken())
        if (token.isIdentifier() && token.getLexeme().matches(NAME) && names.add(token.getLexeme())) // some separators are identifiers
          completions.add(new BasicCompletion(this, token.getLexeme()));

    clear();
    addCompletions(completions);
    return super.getCompletionsImpl(editor);
  }

  /** The primitive under that name, documented by its signature in the programming language, its description and its group */
  private FunctionCompletion function(PrimitiveMethod primitive, String name, String group)
  {
    String summary                = "<html><b><code>" + generator.signature(primitive, name) + "</code></b><p>" + Game.i18n.tr(primitive.doc()) +
                                    (group.isEmpty() ? "" : "<p><i>" + Game.i18n.tr(group) + "</i>");
    FunctionCompletion completion = new FunctionCompletion(this, name, null) {
      @Override public String getSummary()
      {
        return summary;
      }
    };
    completion.setParams(primitive.parameters().stream().map(parameter -> new ParameterizedCompletion.Parameter(null, parameter.name())).toList());
    return completion;
  }
}
