package plm.core.model.lesson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.tree.DefaultMutableTreeNode;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.ui.PlmHtmlEditorKit;
import plm.core.utils.FileUtils;

/** Represents an element of the pedagogic sequence, be it a lecture or 
 * an exercise. A better name would be useful, but I feel limited in 
 * English today. Sorry. */
public abstract class Lecture {
	private String localId; // lecture's identifier WITHIN THE LESSON 
	private String id; // global lecture's identifier 
	
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
	private Lesson lesson;
	
	protected Map<String, String> tips = new HashMap<String, String>();
	
	public Lecture(Lesson lesson,String basename) {
		this.lesson = lesson;
		localId = (basename!=null?basename:getClass().getName());
		id = lesson.getId()+"."+ getLocalId();
		loadHTMLMission();
	}
	public String getId() {
		return id;
	}

	public void setName(String n) {
		name = n;
	}
	public String getName() {
		return PlmHtmlEditorKit.filterHTML(name, false);
	}

	public Lesson getLesson() {
		return this.lesson;
	}


	public String getMission(ProgrammingLanguage lang) {
		String res = "<html><head>"+PlmHtmlEditorKit.getCSS()+"</head><body>"+PlmHtmlEditorKit.filterHTML(this.mission,Game.getInstance().isDebugEnabled())+"</body></html>";
		return res;
	}
	public void setMission(String mission) {
		this.mission=mission;
	}
	public String getTip(String tipsId) {
		return PlmHtmlEditorKit.filterHTML(this.tips.get(tipsId), Game.getInstance().isDebugEnabled());
	}

	public void loadHTMLMission() {
		String filename = getLocalId().replace('.',File.separatorChar);
	
		StringBuffer sb = null;
		try {
			sb = FileUtils.readContentAsText(filename, "html",true);
		} catch (IOException ex) {
			setMission(Game.i18n.tr("File {0}.html not found.",filename));
			return;			
		}
		String str = sb.toString();
	
		/* search the mission name */
		Pattern p =  Pattern.compile("<h[123]>([^<]*)<");
		Matcher m = p.matcher(str);
		if (!m.find())
			System.out.println(Game.i18n.tr("Cannot find the name of mission in {0}.html",filename));
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

	protected Vector<Lecture> dependingLectures = new Vector<Lecture>(); /* To display the graph */
	private DefaultMutableTreeNode myNode;
	public DefaultMutableTreeNode getNode() {
		if (myNode == null) {
			myNode = new DefaultMutableTreeNode(this);
			for (Lecture l : dependingLectures)
				myNode.add(l.getNode());
		}
		return myNode;
	}
	public Vector<Lecture> getNextLectures() {
		if (!dependingLectures.isEmpty())
			return dependingLectures;
		Vector<Lecture> res = new Vector<Lecture>();
		boolean found = false;
		for (Lecture l : Game.getInstance().getCurrentLesson().exercises()) {
			if (found) { // previous loop iteration found the current exo. I'm now seeing the next one
				res.add(l); // Return this next exo.
				return res;
			}
			if (l.equals(this))
				found = true;
		}
		// Being here is OK only if "this" is the last of its lesson
		if (!found) { 
			System.err.println("Current exercise not part of current lesson while searching for the next thing to do.");
		}
		return res;
	}
	public String getLocalId() {
		return localId;
	}
}
