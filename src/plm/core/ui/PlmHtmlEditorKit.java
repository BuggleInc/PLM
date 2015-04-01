package plm.core.ui;
import java.awt.Graphics;
import java.awt.Shape;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
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

	public static String filterHTML(String in, boolean showAll, ProgrammingLanguage language){

		return filterHTML(in, showAll,false, null, language);
	}

	public static String filterHTML(String in, boolean showAll, boolean showMulti, ProgrammingLanguage[] currLang, ProgrammingLanguage language) {
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
			currLang[1]=getGame().getProgrammingLanguage();
		}*/

		
		/* if showMulti, all of selected language will be appear on editor, but in the same color*/
		//FIXME add colors
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
					res = res.replaceAll("(?s)\\[!"+cl+"\\](.*?)\\[/!\\]",   "\\[!#\\]<font color=\"FF0000\">$1</font>\\[/!\\]");
					res = res.replaceAll("(?s)\\[!((([a-z]*\\|)?)?)"+cl+"(((\\|[a-z]*)?)?)\\](.*?)\\[/!\\]",   "\\[!#\\]"+replace+"\\[/!\\]");
					res = res.replaceAll("(?s)\\[!((([a-z]*\\|)*)?)"+cl+"(((\\|[a-z]*)*)?)\\](.*?)\\[/!\\]",   "\\[!#\\]"+replace+"\\[/!\\]");
				}

				res = res.replaceAll("(?s)\\[![a-z]*\\](.*?)\\[/!\\]",   "");
				res = res.replaceAll("(?s)\\[!((([a-z]*\\|)?)?)"+"[a-z]*"+"(((\\|[a-z]*)?)?)\\](.*?)\\[/!\\]",   "");
				res = res.replaceAll("(?s)\\[!((([a-z]*\\|)*)?)"+"[a-z]*"+"(((\\|[a-z]*)*)?)\\](.*?)\\[/!\\]",   "");
				res = res.replaceAll(      "(?s)\\[![a-z]*\\](.*?)\\[/!\\]",   "");
				res = res.replaceAll(      "(?s)\\[!#\\](.*?)\\[/!\\]",   "$1");

			}
		}else{

			
			String strtemp="";
			String cls[] = {language.getLang().toLowerCase()};
			
			while(!strtemp.equals(res)){
				strtemp=res.toString();
				for (String cl : cls) {
					res = res.replaceAll("(?s)\\[!"+cl+"\\](.*?)\\[/!\\]",   "\\[!#\\]$1\\[/!\\]");
					res = res.replaceAll("(?s)\\[!((([a-z]*\\|)?)?)"+cl+"(((\\|[a-z]*)?)?)\\](.*?)\\[/!\\]",   "\\[!#\\]$7\\[/!\\]");
					res = res.replaceAll("(?s)\\[!((([a-z]*\\|)*)?)"+cl+"(((\\|[a-z]*)*)?)\\](.*?)\\[/!\\]",   "\\[!#\\]$7\\[/!\\]");
				}
				res = res.replaceAll("(?s)\\[![a-z]*\\](.*?)\\[/!\\]",   "");
				res = res.replaceAll("(?s)\\[!((([a-z]*\\|)?)?)"+"[a-z]*"+"(((\\|[a-z]*)?)?)\\](.*?)\\[/!\\]",   "");
				res = res.replaceAll("(?s)\\[!((([a-z]*\\|)*)?)"+"[a-z]*"+"(((\\|[a-z]*)*)?)\\](.*?)\\[/!\\]",   "");
				res = res.replaceAll(      "(?s)\\[![a-z]*\\](.*?)\\[/!\\]",   "");
				res = res.replaceAll(      "(?s)\\[!#\\](.*?)\\[/!\\]",   "$1");

			}
		}

		return res;
	}

	private static boolean hideLang(String cssClass, ProgrammingLanguage language) {
		if (cssClass == null)
			return false;
		if (cssClass.toLowerCase().equals(language.getLang().toLowerCase())) 
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
			/*
			if (!game.isDebugEnabled() // Display everything when in debug mode 
					&& hideLang(theCSSClass)) {
				return new EmptyView(element);
			}
			*/
			Object tagName = element.getAttributes().getAttribute(StyleConstants.NameAttribute);
			if (tagName instanceof HTML.Tag) {
				HTML.Tag tag = (HTML.Tag) tagName;
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
		/*
		String res;
		if (game.isDebugEnabled()) {
			/* In debugging mode, all languages are displayed, with differing colors */
			/*
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
		*/
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