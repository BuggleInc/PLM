package jlm.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

/*
 * based on Edwin Dankert's work
 /
 /**
 * Line number margin for a JTextComponent.
 * 
 * <pre>
 * JEditorPane editor = new JEditorPane();
 * JScrollPane scroller = new JScrollPane(editor);
 *
 * // Add the number margin as a Row Header View
 * LineNumberMargin margin = new LineNumberMargin(editor);
 * scroller.setRowHeaderView(margin);
 * </pre>
 * 
 * @author Edwin Dankert <edankert@gmail.com>
 */

public class LineNumberMarginPanel extends JComponent {

	private static final long serialVersionUID = -8735565514263020503L;

	// Metrics of this LineNumber component
	private FontMetrics fontMetrics = null;
	private JTextComponent editor = null;

	private int lines = 0;

	/**
	 * Convenience constructor for Text Components
	 */
	public LineNumberMarginPanel(JTextComponent editor) {
		this.editor = editor;

		setBackground(UIManager.getColor("control"));
		setForeground(UIManager.getColor("textText"));
		setFont(editor.getFont());

		editor.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent evt) {
				if (getLines() != lines) {
					revalidate();
					repaint();

					lines = getLines();
				}
			}
		});

		setBorder(new CompoundBorder(new MatteBorder(0, 0, 0, 1, UIManager.getColor("controlShadow")), new EmptyBorder(
				0, 2, 0, 2)));

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				LineNumberMarginPanel.this.mouseClicked(e);
			}
		});
	}

	/**
	 * Receives all mouse-click events in the margin.
	 * 
	 * @param event
	 *            the mouse event.
	 */
	protected void mouseClicked(MouseEvent event) {
		selectLineForOffset(event.getY());
		LineNumberMarginPanel.this.editor.requestFocusInWindow();
	}

	/**
	 * @return the preferred dimension.
	 */
	@Override
	public Dimension getPreferredSize() {
		if (isVisible()) {
			return new Dimension(getInsets().left + getMarginwidth() + getInsets().right,
					editor.getPreferredSize().height);
		}

		return null;
	}

	/**
	 * @return the maximum dimension.
	 */
	@Override
	public Dimension getMaximumSize() {
		if (isVisible()) {
			return new Dimension(getInsets().left + getMarginwidth() + getInsets().right,
					editor.getPreferredSize().height);
		}

		return null;
	}

	/**
	 * @return the minimum dimension.
	 */
	@Override
	public Dimension getMinimumSize() {
		if (isVisible()) {
			return new Dimension(getInsets().left + getMarginwidth() + getInsets().right,
					editor.getPreferredSize().height);
		}

		return null;
	}

	private int getMarginwidth() {
		int lines = getLines();
		int width = 0;

		if (fontMetrics != null) {
			if (lines >= 1000000) {
				width = fontMetrics.stringWidth("9999999");
			} else if (lines >= 100000) {
				width = fontMetrics.stringWidth("999999");
			} else if (lines >= 10000) {
				width = fontMetrics.stringWidth("99999");
			} else if (lines >= 1000) {
				width = fontMetrics.stringWidth("9999");
			} else {
				width = fontMetrics.stringWidth("999");
			}
		}

		return width;
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);

		if (font != null) {
			fontMetrics = getFontMetrics(font);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		if (fontMetrics != null) {
			Rectangle bounds = g.getClipBounds();

			// Paint the background
			g.setColor(getBackground());
			g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

			// Determine the number of lines to draw in the foreground.
			g.setColor(getForeground());

			FontMetrics editorMetrics = getFontMetrics(editor.getFont());
			int startLine = getLineNumber(bounds.y);
			int endLine = getLineNumber(bounds.y + bounds.height);

			if (endLine < getLines()) {
				endLine = endLine + 1;
			}

			for (int line = startLine; line < endLine; line++) {
				String lineNumber = String.valueOf(line + 1);

				try {
					int start = getLineStart(line);

					if (start != -1) {
						g.drawString(lineNumber, getInsets().left
								+ (getMarginwidth() - fontMetrics.stringWidth(lineNumber)), start
								+ (editorMetrics.getHeight() - editorMetrics.getMaxDescent()));
					}
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

	private int getLines() {
		return editor.getDocument().getDefaultRootElement().getElementCount();
	}

	private int getLineStart(int i) throws BadLocationException {
		Element line = editor.getDocument().getDefaultRootElement().getElement(i);
		Rectangle result = editor.modelToView(line.getStartOffset());

		if (result != null) {
			return result.y;
		}

		return -1;
	}

	private int getLineNumber(int y) {
		int pos = editor.viewToModel(new Point(0, y));

		return editor.getDocument().getDefaultRootElement().getElementIndex(pos);
	}

	private void selectLineForOffset(int y) {
		int pos = editor.viewToModel(new Point(0, y));

		if (pos >= 0) {
			Element root = editor.getDocument().getDefaultRootElement();
			Element elem = root.getElement(root.getElementIndex(pos));

			if (elem != null) {
				int start = elem.getStartOffset();
				int end = elem.getEndOffset();

				editor.select(start, Math.max(end - 1, 0));
			}
		}
	}
}
