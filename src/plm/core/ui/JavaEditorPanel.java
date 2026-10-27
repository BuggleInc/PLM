package plm.core.ui;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.universe.Entity;
import plm.universe.IEntityStackListener;

public class JavaEditorPanel extends RTextScrollPane implements IEditorPanel, IEntityStackListener {
  private static final long serialVersionUID = 1L;
  SourceFileDocumentSynchronizer sync;
  RSyntaxTextArea codeEditor;
  Entity tracedEntity;

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

    /* Highlighting stuff, to trace entities */
    tracedEntity = Game.getInstance().getSelectedEntity();
    if (tracedEntity != null)
      tracedEntity.addStackListener(this);
  }
  @Override public void clear()
  {
    sync.clear();
    sync       = null;
    codeEditor = null;
    if (tracedEntity != null)
      tracedEntity.removeStackListener(this);
  }
  @Override public void entityTraceChanged(Entity e, StackTraceElement[] trace)
  {
    /* TODO:  the following code was not working anymore...
    for (StackTraceElement elm:trace) {
            // added defenses against NPE because sometimes (launched from a .jar file, a NullPointerException might happen...)
            if (elm.getFileName() != null && codeEditor != null && (elm.getFileName().equals(srcFile.getName()) ||
    elm.getFileName().equals(srcFile.getName()+".java from JavaFileObjectImpl"))) { SyntaxDocument sd = ((SyntaxDocument) codeEditor.getDocument()); if (sd !=
    null) sd.setCurrentEditedLineNumber(elm.getLineNumber()); codeEditor.repaint();
            }
    }
     */
  }
  @Override public void tracedEntityChanged(Entity e)
  {
    if (tracedEntity != null)
      tracedEntity.removeStackListener(this);

    tracedEntity = e;
    if (tracedEntity != null)
      tracedEntity.addStackListener(this);
  }
}
