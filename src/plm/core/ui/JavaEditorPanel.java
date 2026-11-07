package plm.core.ui;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import plm.core.lang.ProgrammingLanguage;
import plm.core.lang.RemoteExecutionLang;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.WorldKind;

public class JavaEditorPanel extends RTextScrollPane implements IEditorPanel {
  private static final long serialVersionUID = 1L;
  SourceFileDocumentSynchronizer sync;
  RSyntaxTextArea codeEditor;
  /** The messages of the last compilation, in the margin and in the code */
  final CompilerMarkers markers;
  final CodeCompletion completion;

  public JavaEditorPanel(Exercise exo, ProgrammingLanguage lang)
  {
    super();

    codeEditor = new RSyntaxTextArea();
    setViewportView(codeEditor);

    setLineNumbersEnabled(true);
    setFoldIndicatorEnabled(true);

    if (lang.isJava()) {
      codeEditor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
    } else if (lang.isPython()) {
      codeEditor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
    } else if (lang.isScala()) {
      codeEditor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SCALA);
    } else if (lang.isC()) {
      codeEditor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
    } else {
      System.err.println("WARNING: Unsupported programming language for syntax highlighting module. Please fix that PLM bug.");
    }

    codeEditor.setAnimateBracketMatching(true);
    codeEditor.setAntiAliasingEnabled(true);
    codeEditor.setAutoIndentEnabled(true);
    codeEditor.setBracketMatchingEnabled(true);
    codeEditor.setCloseCurlyBraces(true);
    codeEditor.setCodeFoldingEnabled(true);
    codeEditor.setMarkOccurrences(true); // the other uses of the name under the caret
    codeEditor.setPaintTabLines(true);   // the guides of the indentation levels
    // The tab key inserts 4 spaces, as tabulations display differently elsewhere and Python refuses to mix them with spaces
    codeEditor.setTabSize(4);
    codeEditor.setTabsEmulated(true);

    codeEditor.setCaretPosition(0);

    /* Create a synchronization element, and connect it to the editor */
    sync = new SourceFileDocumentSynchronizer(codeEditor);
    sync.setDocument(codeEditor.getDocument());
    codeEditor.getDocument().addDocumentListener(sync);

    /* Connect the synchronization element to the student's code */
    Game.getInstance().studentWork.setBodyListener(exo, lang, sync);
    sync.setExercise(exo);
    sync.setProgrammingLanguage(lang);

    codeEditor.setText(exo.getEditorContent(lang));
    codeEditor.convertTabsToSpaces(); // the code saved or given with tabulations
    codeEditor.discardAllEdits();
    Zoom.apply(this);
    markers    = new CompilerMarkers(this);
    completion =
        new CodeCompletion(codeEditor, exo.getWorlds(WorldKind.INITIAL).get(0), (RemoteExecutionLang)lang, () -> Game.i18n); // Lightbot has another editor
  }
  @Override public void clear()
  {
    sync.clear();
    completion.autoCompletion.uninstall(); // it listens to the look and feel, which would keep this editor
    sync       = null;
    codeEditor = null;
  }
}
