package plm.core.ui;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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

import plm.core.model.Game;
import plm.core.model.ProgrammingLanguage;
import plm.core.model.lesson.Lecture;


public class PlmHtmlEditorKit extends HTMLEditorKit {
	private static final long serialVersionUID = 1L;

	private static Map<String,String> langColors = null;
	/** Filters out the part that should not be displayed in the current programming language
	 * 
	 *  If current language is java, then we will:
	 *    - keep parts such as [!java] ... [/!]  (that is, replace the whole group by what's within the tag)
	 *    - remove parts such as [!python] ... [/!] 
	 *    - remove parts such as [!scala] ... [/!]
	 *     
	 *    - keep parts such as [!java|python] ... [/!] 
	 *    - keep parts such as [!python|java] ... [/!] 
	 *    - keep parts such as [!java|scala] ... [/!] 
	 *    - keep parts such as [!scala|java] ... [/!] 
	 *    - remove parts such as [!scala|python] ... [/!] 
	 *    - remove parts such as [!python|scala] ... [/!] 
	 * 
	 * Other automagic conversions:
	 *  [!thelang] displays the name of the current programming language.
	 *  [!configfile] displays where the PLM config file is stored on disk.
	 * 
	 */

	public static String filterHTML(String in, boolean showAll){

		return filterHTML(in, showAll,false, null);
	}

	public static String filterHTML(String in, boolean showAll, boolean showMulti, ProgrammingLanguage[] currLang) {
		if (langColors == null) {
			langColors = new HashMap<String, String>();
			langColors.put("java", "FF0000");
			langColors.put("python", "008000");
			langColors.put("scala",  "0000FF");
			langColors.put("c",  "00FF00");

			langColors.put("java|python", "FF8000");
			langColors.put("java|scala",  "FF00FF");
			langColors.put("java|c",  "FFFF00");
			langColors.put("python|java", "FF8000");
			langColors.put("python|scala", "0080FF");
			langColors.put("python|c", "00AF00");
			langColors.put("scala|java",  "FF00FF");
			langColors.put("scala|python", "0080FF");
			langColors.put("scala|c", "00FFFF");

			// sclala python c
			langColors.put("scala|python|c", "00AFFF");
			langColors.put("scala|c|python", "00AFFF");
			langColors.put("python|c|scala", "00AFFF");
			langColors.put("python|scala|c", "00AFFF");
			langColors.put("c|python|scala", "00AFFF");
			langColors.put("c|scala|python", "00AFFF");

			//scala python java
			langColors.put("scala|python|java", "FF80FF");
			langColors.put("scala|java|python", "FF80FF");
			langColors.put("python|java|scala", "FF80FF");
			langColors.put("python|scala|java", "FF80FF");
			langColors.put("java|python|scala", "FF80FF");
			langColors.put("java|scala|python", "FF80FF");

			//scala c java
			langColors.put("scala|c|java", "EEEEEE");
			langColors.put("scala|java|c", "EEEEEE");
			langColors.put("c|java|scala", "EEEEEE");
			langColors.put("c|scala|java", "EEEEEE");
			langColors.put("java|c|scala", "EEEEEE");
			langColors.put("java|scala|c", "EEEEEE");

			//python java c
			langColors.put("python|c|java", "333333");
			langColors.put("python|java|c", "333333");
			langColors.put("c|java|python", "333333");
			langColors.put("c|python|java", "333333");
			langColors.put("java|python|c", "333333");
			langColors.put("java|c|python", "333333");
		}

		String res = in.replaceAll("\\[!thelang/?\\]", "[!java]Java[/!][!python]python[/!][!scala]Scala[/!][!c]C[/!]");
		res = res.replaceAll("\\[!configfile/?\\]", Game.getSavingLocation()+File.separator+"plm.properties".replaceAll("\\\\", "\\\\"));

		/* Display everything when in debug mode, with shiny colors */
		if (showAll) {
			// Process any block with one language first so that they can be nested in blocks with more than one language.
			for (ProgrammingLanguage lang : Game.getProgrammingLanguages()) {
				String l = lang.getLang().toLowerCase();
				res = res.replaceAll("(?s)\\[!"+l+"\\](.*?)\\[/!\\]",
						"<font color=\""+langColors.get(l)+"\">$1</font>");
			}
			for (ProgrammingLanguage lang : Game.getProgrammingLanguages()) {
				String l = lang.getLang().toLowerCase();
				for (ProgrammingLanguage lang2 : Game.getProgrammingLanguages()) {
					if (!lang2.equals(lang)) {
						String l2 = lang2.getLang().toLowerCase();
						res = res.replaceAll("(?s)\\[!"+l+"\\|"+l2+"\\](.*?)\\[/!\\]","<font color=\""+langColors.get(l+"|"+l2)+"\">$1</font>");
						res = res.replaceAll("(?s)\\[!"+l2+"\\|"+l+"\\](.*?)\\[/!\\]","<font color=\""+langColors.get(l+"|"+l2)+"\">$1</font>");
					}
				}
			}

			for (ProgrammingLanguage lang : Game.getProgrammingLanguages()) {
				String l = lang.getLang().toLowerCase();
				for (ProgrammingLanguage lang2 : Game.getProgrammingLanguages()) {
					if (!lang2.equals(lang)) {
						String l2 = lang2.getLang().toLowerCase();
						for (ProgrammingLanguage lang3 : Game.getProgrammingLanguages()) {
							if (!lang3.equals(lang) && !lang3.equals(lang2)) {
								String l3 = lang3.getLang().toLowerCase();
								res = res.replaceAll("(?s)\\[!"+l+"\\|"+l2+"\\|"+l3+"\\](.*?)\\[/!\\]","<font color=\""+langColors.get(l+"|"+l2+"|"+l3)+"\">$1</font>");
								res = res.replaceAll("(?s)\\[!"+l+"\\|"+l3+"\\|"+l2+"\\](.*?)\\[/!\\]","<font color=\""+langColors.get(l+"|"+l2+"|"+l3)+"\">$1</font>");
								res = res.replaceAll("(?s)\\[!"+l2+"\\|"+l+"\\|"+l3+"\\](.*?)\\[/!\\]","<font color=\""+langColors.get(l+"|"+l2+"|"+l3)+"\">$1</font>");
								res = res.replaceAll("(?s)\\[!"+l2+"\\|"+l3+"\\|"+l+"\\](.*?)\\[/!\\]","<font color=\""+langColors.get(l+"|"+l2+"|"+l3)+"\">$1</font>");
								res = res.replaceAll("(?s)\\[!"+l3+"\\|"+l2+"\\|"+l+"\\](.*?)\\[/!\\]","<font color=\""+langColors.get(l+"|"+l2+"|"+l3)+"\">$1</font>");
								res = res.replaceAll("(?s)\\[!"+l3+"\\|"+l+"\\|"+l2+"\\](.*?)\\[/!\\]","<font color=\""+langColors.get(l+"|"+l2+"|"+l3)+"\">$1</font>");
							}
						}
					}
				}
			}
			return res;
		}

		/* filter out irrelevant stuff when not in debug */
		/*if(currLang==null){
			currLang = new ProgrammingLanguage[1]; 
			currLang[1]=Game.getProgrammingLanguage();
		}*/


		if(showMulti){
			String replace="<font color=\"FF0000\">$7</font>";
			
			String strtemp="";
			String cls[] = new String[currLang.length];
			for(int i=0;i<currLang.length;i++){
				cls[i]=currLang[i].getLang().toLowerCase();
			}
			while(!strtemp.equals(res)){
				strtemp=res.toString();
				for (String cl : cls) {
					res = res.replaceAll("(?s)\\[!((([a-z]*\\|)*)?)"+cl+"(((\\|[a-z]*)*)?)\\]([^\\[]*?)\\[/!\\]",   "\\[!#\\]"+replace+"\\[/!\\]");
					//System.out.println("\t"+res);
				}
				res = res.replaceAll("(?s)\\[!((([a-z]*\\|)*)?)"+"[a-z]*"+"(((\\|[a-z]*)*)?)\\]([^\\[]*?)\\[/!\\]",   "");
				//System.out.println(res);
				res = res.replaceAll(      "(?s)\\[![a-z]*\\](.*?)\\[/!\\]",   "");
				//System.out.println(res);
				res = res.replaceAll(      "(?s)\\[!#\\](.*?)\\[/!\\]",   "$1");
				//System.out.println(res);

			}
		}else{

			
			String strtemp="";
			//String cls[] = new String[currLang.length];
			String cls[] = {Game.getProgrammingLanguage().getLang().toLowerCase()};
			
			while(!strtemp.equals(res)){
				strtemp=res.toString();
				for (String cl : cls) {
					res = res.replaceAll("(?s)\\[!((([a-z]*\\|)*)?)"+cl+"(((\\|[a-z]*)*)?)\\]([^\\[]*?)\\[/!\\]",   "\\[!#\\]$7\\[/!\\]");
					//System.out.println("\t"+res);
				}
				res = res.replaceAll("(?s)\\[!((([a-z]*\\|)*)?)"+"[a-z]*"+"(((\\|[a-z]*)*)?)\\]([^\\[]*?)\\[/!\\]",   "");
				//System.out.println(res);
				res = res.replaceAll(      "(?s)\\[![a-z]*\\](.*?)\\[/!\\]",   "");
				//System.out.println(res);
				res = res.replaceAll(      "(?s)\\[!#\\](.*?)\\[/!\\]",   "$1");
				//System.out.println(res);

			}
			/*
			String cl = Game.getProgrammingLanguage().getLang().toLowerCase();
			// Process any block with one language first so that they can be nested in blocks with more than one language.
			res = res.replaceAll(      "(?s)\\[!"+cl+"\\](.*?)\\[/!\\]","$1");
			//System.out.println("Keep "+"(?s)\\[!"+cl+"\\](.*?)\\[/!\\]");
			for (ProgrammingLanguage lang : Game.getProgrammingLanguages()) {
				if (!lang.equals(Game.getProgrammingLanguage())) {
					String l = lang.getLang().toLowerCase();
					res = res.replaceAll(      "(?s)\\[!"+l+"\\](.*?)\\[/!\\]",   "");
					//System.out.println("Kill "+"(?s)\\[!"+l+"\\](.*?)\\[/!\\]");
				}
			}
			
			for (ProgrammingLanguage lang : Game.getProgrammingLanguages()) {
				if (!lang.equals(Game.getProgrammingLanguage())) {
					String l = lang.getLang().toLowerCase();

					res = res.replaceAll(      "(?s)\\[!"+l +"\\|"+cl+"\\](.*?)\\[/!\\]",   "$1");
					//System.out.println("Keep "+"(?s)\\[!"+l +"\\|"+cl+"\\](.*?)\\[/!\\]");
					res = res.replaceAll(      "(?s)\\[!"+cl+"\\|"+l +"\\](.*?)\\[/!\\]",   "$1");
					//System.out.println("Keep "+"(?s)\\[!"+cl+"\\|"+l +"\\](.*?)\\[/!\\]");

					for (ProgrammingLanguage lang2 : Game.getProgrammingLanguages()) {
						if (!lang2.equals(Game.getProgrammingLanguage()) && !lang2.equals(lang)) {
							String l2 = lang2.getLang().toLowerCase();
							res = res.replaceAll(   "(?s)\\[!"+l+"\\|"+l2+"\\](.*?)\\[/!\\]",    "");
							res = res.replaceAll(   "(?s)\\[!"+l2+"\\|"+l+"\\](.*?)\\[/!\\]",    "");
							//System.out.println("Kill (?s)\\[!"+l+"\\|"+l2+"\\](.*?)\\[/!\\]");


							for (ProgrammingLanguage lang3 : Game.getProgrammingLanguages()) {
								if (lang3.equals(Game.getProgrammingLanguage()) && !lang3.equals(lang) && !lang3.equals(lang2)) {
									String l3 = lang3.getLang().toLowerCase();
									res = res.replaceAll("(?s)\\[!"+cl+"\\|"+l2+"\\|"+l3+"\\](.*?)\\[/!\\]","$1");
									res = res.replaceAll("(?s)\\[!"+cl+"\\|"+l3+"\\|"+l2+"\\](.*?)\\[/!\\]","$1");
									res = res.replaceAll("(?s)\\[!"+l2+"\\|"+cl+"\\|"+l3+"\\](.*?)\\[/!\\]","$1");
									res = res.replaceAll("(?s)\\[!"+l2+"\\|"+l3+"\\|"+cl+"\\](.*?)\\[/!\\]","$1");
									res = res.replaceAll("(?s)\\[!"+l3+"\\|"+l2+"\\|"+cl+"\\](.*?)\\[/!\\]","$1");
									res = res.replaceAll("(?s)\\[!"+l3+"\\|"+cl+"\\|"+l2+"\\](.*?)\\[/!\\]","$1");
								}else{
									String l3 = lang3.getLang().toLowerCase();
									res = res.replaceAll("(?s)\\[!"+l+"\\|"+l2+"\\|"+l3+"\\](.*?)\\[/!\\]","");
									res = res.replaceAll("(?s)\\[!"+l+"\\|"+l3+"\\|"+l2+"\\](.*?)\\[/!\\]","");
									res = res.replaceAll("(?s)\\[!"+l2+"\\|"+l+"\\|"+l3+"\\](.*?)\\[/!\\]","");
									res = res.replaceAll("(?s)\\[!"+l2+"\\|"+l3+"\\|"+l+"\\](.*?)\\[/!\\]","");
									res = res.replaceAll("(?s)\\[!"+l3+"\\|"+l2+"\\|"+l+"\\](.*?)\\[/!\\]","");
									res = res.replaceAll("(?s)\\[!"+l3+"\\|"+l+"\\|"+l2+"\\](.*?)\\[/!\\]","");
								}
							}
						}
					}

				}

			}
			*/
		}

		return res;
	}

	private static boolean hideLang(String cssClass) {
		if (cssClass == null)
			return false;
		if (cssClass.toLowerCase().equals(Game.getProgrammingLanguage().getLang().toLowerCase())) 
			return false;
		return true;
	}
	
	public static class HTMLFactoryX extends HTMLEditorKit.HTMLFactory implements ViewFactory {
		boolean visible=false;
		@Override
		public View create(Element element) {
			Element iterElem = element;
			String theCSSClass = (String) iterElem.getAttributes().getAttribute(HTML.Attribute.CLASS);
			/*
			while (theCSSClass == null && iterElem != null) {
				iterElem = iterElem.getParentElement();
				if (iterElem != null)
					theCSSClass = (String) iterElem.getAttributes().getAttribute(HTML.Attribute.CLASS);
			}
			*/
			if (!Game.getInstance().isDebugEnabled() // Display everything when in debug mode 
				&& hideLang(theCSSClass)) {
				return new EmptyView(element);
			}
			
			Object tagName = element.getAttributes().getAttribute(StyleConstants.NameAttribute);
			if (tagName instanceof HTML.Tag) {
				HTML.Tag tag = (HTML.Tag) tagName;
				if (tag == HTML.Tag.IMG)
					return new MyIconView(element, baseExercise);
			}
			return super.create(element);
		}
	}

	protected static Lecture baseExercise = null;
	public PlmHtmlEditorKit() {
		baseExercise = null;
	}

	public PlmHtmlEditorKit(Lecture _baseExercise) {
		baseExercise = _baseExercise;
	}

	@Override
	public ViewFactory getViewFactory() {
		return new HTMLFactoryX();
	}
	public static String getCSS() {
		String header = "  <style type=\"text/css\">\n"+
		        "    body { font-family: tahoma, \"Times New Roman\", serif; font-size:"+Game.getProperty(Game.PROP_FONT_SIZE, "10px", true)+"; margin:10px; }\n"+
		        "    code { background:#EEEEEE; }\n"+
		        "    pre { background: #EEEEEE;\n"+
		        "          margin: 5px;\n"+
		        "          padding: 6px;\n"+
		        "          border: 1px inset;\n"+
		        "          width: 640px;\n"+
		        "          overflow: auto;\n"+
		        "          text-align: left;\n"+
		        "          font-family: \"Courrier New\", \"Courrier\", monospace; }\n"+
		        "   .comment { background:#EEEEEE;\n"+
		        "              font-family: \"Times New Roman\", serif;\n"+
		        "              color:#00AA00;\n"+
		        "              font-style: italic; }\n";
		String res;
		if (Game.getInstance().isDebugEnabled()) {
			/* In debugging mode, all languages are displayed, with differing colors */
			res = header;
			res += ".Java   {visibility: visible; color:#FF0000}\n";
			res += ".java   {visibility: visible; color:#FF0000}\n";
			res += ".python {visibility: visible; color:#008000}\n";
			res += ".Python {visibility: visible; color:#008000}\n";
			res += ".scala  {visibility: visible; color:#0000FF}\n";
			res += ".Scala  {visibility: visible; color:#0000FF}\n";
			res +=  "  </style>\n";
			return res;
		}
		return header+"  </style>\n";
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
	 * @param baseExercise 
	 * @throws FileNotFoundException 
	 */
	public MyIconView(Element elem, Lecture baseExercise) {
		super(elem);
		String filename = (String) elem.getAttributes().getAttribute(HTML.Attribute.SRC);
		if (filename == null) {
			System.err.println(Game.i18n.tr("<img> tag without src attribute in exercise {0}",baseExercise.getName()));
			c = (Icon) UIManager.getLookAndFeelDefaults().get("html.missingImage");
		} else {
			c = ResourcesCache.getIcon(filename,true);
			if (c != null)
				return;
			if (baseExercise != null) 
				c = ResourcesCache.getIcon(baseExercise, filename);
			if (c != null)
				return;
			
			String resourceName = "/"+filename.replace('.','/');
			resourceName = resourceName.replaceAll("/png$", ".png").replaceAll("/jpg$", ".jpg");
			resourceName = resourceName.replaceAll("/jpeg$", ".jpeg").replaceAll("/gif$", ".gif");

			InputStream s = getClass().getResourceAsStream(resourceName);
			
			try {
				if (s == null) {
					c = (Icon) UIManager.getLookAndFeelDefaults().get("html.missingImage");
					System.out.println("Broken image link: "+resourceName);
				} else
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

