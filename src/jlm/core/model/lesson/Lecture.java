package jlm.core.model.lesson;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.model.Reader;

/** Represents an element of the pedagogic sequence, be it a lecture or 
 * an exercise. A better name would be useful, but I feel limited in 
 * English today. Sorry. */
public abstract class Lecture {

	public static final String HTMLTipHeader = "<head>\n"+
			"  <meta content=\"text/html; charset=UTF-8\" />\n"+	
			"  <style>\n"+
			"    body { font-family: tahoma, \"Times New Roman\", serif; font-size:10px; margin:10px; }\n"+
			"    code { background:#EEEEEE; }\n"+
			"    pre { background: #EEEEEE;\n"+
			"          margin: 5px;\n"+
			"          padding: 6px;\n"+
			"          border: 1px inset;\n"+
			"          width: 400px;\n"+
			"          overflow: auto;\n"+
			"          text-align: left;\n"+
			"          font-family: \"Courrier New\", \"Courrier\", monospace; }\n"+
			"   .comment { background:#EEEEEE;\n"+
			"              font-family: \"Times New Roman\", serif;\n"+
			"              color:#00AA00;\n"+
			"              font-style: italic; }\n"+
			"  </style>\n"+
			"</head>\n";
	private String name = "<no name>";                     /** indicate whether this Exercise was successfully done or not */
	private String mission = "";                        /** The text to display to present the lesson */
	private static Map<ProgrammingLanguage,String> css; /** The CSS to use for a given language */
	private Lesson lesson;

	protected Map<String, String> tips = new HashMap<String, String>();
	
	public Lecture(Lesson lesson) {
		this.lesson = lesson;
		loadHTMLMission();
	}

	public void setName(String n) {
		name = n;
	}
	public String getName() {
		return this.name;
	}

	public Lesson getLesson() {
		return this.lesson;
	}

	protected static String getCSS(ProgrammingLanguage lang) {
		if (css==null) 
			 css = new HashMap<ProgrammingLanguage, String>();
		String res = css.get(lang);
		if (res == null) {
			res =		"  <style type=\"text/css\">\n"+
				        "    body { font-family: tahoma, \"Times New Roman\", serif; font-size:10px; margin:10px; }\n"+
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
			for (ProgrammingLanguage l2 : Game.programmingLanguages) {
				if (!lang.equals(l2)) {
					res += "."+l2.getLang()+" {display: none; color:#FF0000;}\n";
					res += "."+l2.getLang().toLowerCase()+" {display: none; color:#FF0000;}\n";
				} else {
					/* DEBUG ONLY, to see the specific elements*/ 
					//theCSS += "."+l2.getLang()+" {visibility: visible; color:#00AA00;}\n";
					//theCSS += "."+l2.getLang().toLowerCase()+" {visibility: visible; color:#00AA00;}\n";
				}
			}
			res +=  "  </style>\n";
			css.put(lang, res);
		}
		return res;
	}

	public String getMission(ProgrammingLanguage lang) {
		String res = "<html><head>"+getCSS(lang)+"</head><body>"+this.mission+"</body></html>";
		return res;
	}
	public void setMission(String mission) {
		this.mission=mission;
	}
	public String getTip(String tipsId) {
		return this.tips.get(tipsId);
	}

	public boolean isSuccessfullyPassed() {
		return false;//FIXME
	}

	public void loadHTMLMission() {
		String filename = getClass().getCanonicalName().replace('.',File.separatorChar);
	
		StringBuffer sb = Reader.fileToStringBuffer(filename, "html",true);
		if (sb==null) {
			setMission("File "+filename+" not found.");
			return;
		}		
		String str = sb.toString();
	
		/* search the mission name */
		Pattern p =  Pattern.compile("<h[123]>([^<]*)<");
		Matcher m = p.matcher(str);
		if (!m.find())
			System.out.println("Cannot find the name of mission in "+filename+".html");
		setName( m.group(1) );
	
		/* prepare the tips, if any */
		Pattern p3 =  Pattern.compile("<div class=\"tip\" id=\"(tip-\\d+?)\" alt=\"([^\"]+?)\">(.*?)</div>",Pattern.MULTILINE|Pattern.DOTALL);
		Matcher m3 = p3.matcher(str);
		while (m3.find()) {	
			tips.put("#"+m3.group(1), m3.group(3));
		}
		str = m3.replaceAll("<a href=\"#$1\">$2</a>");
	
		Pattern p4 =  Pattern.compile("<div class=\"tip\" id=\"(tip-\\d+?)\">(.*?)</div>",Pattern.MULTILINE|Pattern.DOTALL);
		Matcher m4 = p4.matcher(str);
		while (m4.find()) {	
			tips.put("#"+m4.group(1), m4.group(2));
		}		
		str=m4.replaceAll("<a href=\"#$1\">Show Tip</a>");				
	
	
		/* get the mission explanation */
		setMission(str);
	}

	
}
