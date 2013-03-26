package jlm.core.ui;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.Position;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

import jlm.core.model.Game;


public class JlmHtmlEditorKit extends HTMLEditorKit {
	private static final long serialVersionUID = 1L;

	public static class HTMLFactoryX extends HTMLEditorKit.HTMLFactory implements ViewFactory {
		boolean visible=false;
		@Override
		public View create(Element element) {
			Element iterElem = element;
			String theCSSClass = (String) iterElem.getAttributes().getAttribute(HTML.Attribute.CLASS);
			while (theCSSClass == null && iterElem != null) {
				iterElem = iterElem.getParentElement();
				if (iterElem != null)
					theCSSClass = (String) iterElem.getAttributes().getAttribute(HTML.Attribute.CLASS);
			}
			if (theCSSClass != null && 
				!theCSSClass.toLowerCase().equals(Game.getProgrammingLanguage().getLang().toLowerCase())) {
				return new EmptyView(element);
			}
			
			Object tagName = element.getAttributes().getAttribute(StyleConstants.NameAttribute);
			if (tagName instanceof HTML.Tag) {
				HTML.Tag tag = (HTML.Tag) tagName;
				if (tag == HTML.Tag.IMG)
					return new MyIconView(element);
			}
			return super.create(element);
		}
	}

	@Override
	public ViewFactory getViewFactory() {
		return new HTMLFactoryX();
	}
}

class EmptyView extends IconView {

	public EmptyView(Element elem) {
		super(elem);
	}
	@Override
	public float getPreferredSpan(int axis) {
		return 0;
	}
	@Override
	public void paint(Graphics g, Shape a) {
	}
}

/* This is a crude copy/paste of IconView where all I changed is the constructor. 
 * The day where swing authors won't fight against subclassing by for example putting the c field as 
 * private here, I will stop doing such nasty thing. 
 * 
 * But I have the feeling that we will move to SWT before that day happens :-/ [Mt] */

class MyIconView extends View {
	/**
	 * Creates a new icon view that represents an element.
	 *
	 * @param elem the element to create a view for
	 * @throws FileNotFoundException 
	 */
	public MyIconView(Element elem) {
		super(elem);
		String filename = (String) elem.getAttributes().getAttribute(HTML.Attribute.SRC);
		if (filename == null) {
			System.err.println("<img> tag without src attribute");
			c = (Icon) UIManager.getLookAndFeelDefaults().get("html.missingImage");
		} else {
			String resourceName = "/"+filename.replace('.','/');
			resourceName = resourceName.replaceAll("/png$", ".png").replaceAll("/jpg$", ".jpg");
			resourceName = resourceName.replaceAll("/jpeg$", ".jpeg").replaceAll("/gif$", ".gif");

			InputStream s = getClass().getResourceAsStream(resourceName);


			try {
				if (s == null)
					c = (Icon) UIManager.getLookAndFeelDefaults().get("html.missingImage");
				else
					c = new ImageIcon(ImageIO.read(s));
			} catch (IOException e) {
				e.printStackTrace();
				c = (Icon) UIManager.getLookAndFeelDefaults().get("html.missingImage");
			}
		}
	}

	// --- View methods ---------------------------------------------

	/**
	 * Paints the icon.
	 * The real paint behavior occurs naturally from the association
	 * that the icon has with its parent container (the same
	 * container hosting this view), so this simply allows us to
	 * position the icon properly relative to the view.  Since
	 * the coordinate system for the view is simply the parent
	 * containers, positioning the child icon is easy.
	 *
	 * @param g the rendering surface to use
	 * @param a the allocated region to render into
	 * @see View#paint
	 */
	@Override
	public void paint(Graphics g, Shape a) {
		Rectangle alloc = a.getBounds();
		c.paintIcon(getContainer(), g, alloc.x, alloc.y);
	}

	/**
	 * Determines the preferred span for this view along an
	 * axis.
	 *
	 * @param axis may be either View.X_AXIS or View.Y_AXIS
	 * @return  the span the view would like to be rendered into
	 *           Typically the view is told to render into the span
	 *           that is returned, although there is no guarantee.
	 *           The parent may choose to resize or break the view.
	 * @exception IllegalArgumentException for an invalid axis
	 */
	@Override
	public float getPreferredSpan(int axis) {
		switch (axis) {
		case View.X_AXIS:
			return c.getIconWidth();
		case View.Y_AXIS:
			return c.getIconHeight();
		default:
			throw new IllegalArgumentException("Invalid axis: " + axis);
		}
	}

	/**
	 * Determines the desired alignment for this view along an
	 * axis.  This is implemented to give the alignment to the
	 * bottom of the icon along the y axis, and the default
	 * along the x axis.
	 *
	 * @param axis may be either View.X_AXIS or View.Y_AXIS
	 * @return the desired alignment >= 0.0f && <= 1.0f.  This should be
	 *   a value between 0.0 and 1.0 where 0 indicates alignment at the
	 *   origin and 1.0 indicates alignment to the full span
	 *   away from the origin.  An alignment of 0.5 would be the
	 *   center of the view.
	 */
	@Override
	public float getAlignment(int axis) {
		switch (axis) {
		case View.Y_AXIS:
			return 1;
		default:
			return super.getAlignment(axis);
		}
	}

	/**
	 * Provides a mapping from the document model coordinate space
	 * to the coordinate space of the view mapped to it.
	 *
	 * @param pos the position to convert >= 0
	 * @param a the allocated region to render into
	 * @return the bounding box of the given position
	 * @exception BadLocationException  if the given position does not
	 *   represent a valid location in the associated document
	 * @see View#modelToView
	 */
	@Override
	public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
		int p0 = getStartOffset();
		int p1 = getEndOffset();
		if ((pos >= p0) && (pos <= p1)) {
			Rectangle r = a.getBounds();
			if (pos == p1) {
				r.x += r.width;
			}
			r.width = 0;
			return r;
		}
		throw new BadLocationException(pos + " not in range " + p0 + "," + p1, pos);
	}

	/**
	 * Provides a mapping from the view coordinate space to the logical
	 * coordinate space of the model.
	 *
	 * @param x the X coordinate >= 0
	 * @param y the Y coordinate >= 0
	 * @param a the allocated region to render into
	 * @return the location within the model that best represents the
	 *  given point of view >= 0
	 * @see View#viewToModel
	 */
	@Override
	public int viewToModel(float x, float y, Shape a, Position.Bias[] bias) {
		Rectangle alloc = (Rectangle) a;
		if (x < alloc.x + (alloc.width / 2)) {
			bias[0] = Position.Bias.Forward;
			return getStartOffset();
		}
		bias[0] = Position.Bias.Backward;
		return getEndOffset();
	}

	// --- member variables ------------------------------------------------

	private Icon c;
}

