package plm.core.ui;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.session.ISourceFileListener;
import plm.core.model.session.SourceFile;

/*
 * Responsibility: synchronize content between Document instance
 * used by a JEditorPane and the body field of a SourceFile instance.
 *
 */
public class SourceFileDocumentSynchronizer implements DocumentListener, ISourceFileListener {

  private Document document;
  private Exercise exo;
  private SourceFile sourceFile;
  private ProgrammingLanguage lang;
  private EditorKit editorKit;
  private boolean propagationInProgress = false, pendingPropagDocToSource = false, pendingPropagSourceToDoc = false;

  public SourceFileDocumentSynchronizer(RSyntaxTextArea rtextarea) { this.editorKit = rtextarea.getUI().getEditorKit(rtextarea); }

  public SourceFileDocumentSynchronizer(EditorKit kit) { this.editorKit = kit; }

  public void clear()
  {
    document.removeDocumentListener(this);
    Game.getInstance().studentWork.removeBodyListener(exo, lang, sourceFile.getName());
    this.document   = null;
    this.sourceFile = null;
    this.exo        = null;
    editorKit       = null;
  }

  public void setDocument(Document doc) { this.document = doc; }

  public void setSourceFile(Exercise exo, SourceFile srcFile)
  {
    this.exo        = exo;
    this.sourceFile = srcFile;
  }

  public void setProgrammingLanguage(ProgrammingLanguage lang) { this.lang = lang; }

  private void copyDocumentContentToSourceFileBody()
  {
    if (this.propagationInProgress) {
      this.pendingPropagDocToSource = true;
      return;
    }

    this.propagationInProgress = true;
    try {
      exo.setEditorContent(lang, sourceFile, this.document.getText(0, this.document.getLength()));
    } catch (BadLocationException e1) {
      e1.printStackTrace();
    } finally {
      this.propagationInProgress = false;
      if (this.pendingPropagDocToSource) {
        this.pendingPropagDocToSource = false;
        copyDocumentContentToSourceFileBody();
      }
    }
  }

  private void copySourceFileBodyToDocumentContent()
  {
    if (this.propagationInProgress) {
      this.pendingPropagSourceToDoc = true;
      return;
    }

    this.propagationInProgress = true;
    try {
      this.document.remove(0, this.document.getLength());
      String editorContent = exo.getEditorContent(lang, sourceFile);
      if (editorContent == null || editorContent.equals("")) {
        return;
      }
      Reader reader = new StringReader(editorContent);
      this.editorKit.read(reader, this.document, 0);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (BadLocationException e) {
      e.printStackTrace();
    } finally {
      this.propagationInProgress = false;
      if (this.pendingPropagSourceToDoc) {
        this.pendingPropagSourceToDoc = false;
        copySourceFileBodyToDocumentContent();
      }
    }
  }

  @Override public void changedUpdate(DocumentEvent e) { copyDocumentContentToSourceFileBody(); }

  @Override public void insertUpdate(DocumentEvent e) { copyDocumentContentToSourceFileBody(); }

  @Override public void removeUpdate(DocumentEvent e) { copyDocumentContentToSourceFileBody(); }

  @Override public void sourceFileContentHasChanged() { copySourceFileBodyToDocumentContent(); }
}
