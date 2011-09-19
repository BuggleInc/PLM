package jlm.core.model.lesson;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.model.Reader;
import jlm.universe.Entity;
import jlm.universe.World;



public abstract class ExerciseTemplated extends Exercise {

	protected String tabName = getClass().getSimpleName(); /** Name of the tab in editor */
	protected ArrayList<String> tabsNames = null;
	protected String entityName = getClass().getCanonicalName()+"Entity"; /** name of the class of entities being solution of this exercise */
	protected ArrayList<String> entitiesNames = null;

	public ExerciseTemplated(Lesson lesson) {
		super(lesson);
		loadHTMLMission();
	}
	
	final static String HTMLMissionHeader = 
		"<head>\n"+
		"  <meta content=\"text/html; charset=UTF-8\" />\n"+	
		"  <style>\n"+
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
        "              font-style: italic; }\n"+
        "  </style>\n"+
        "</head>\n";

	final public static String HTMLTipHeader = 
		"<head>\n"+
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

	
	public void loadHTMLMission() {
		String filename = getClass().getCanonicalName().replace('.',File.separatorChar);

		StringBuffer sb = Reader.fileToStringBuffer(filename, "html",true);
		if (sb==null) {
			mission = "File "+filename+" not found.";
			return;
		}		
		String str = sb.toString();
		
		/* search the mission name */
		Pattern p =  Pattern.compile("<h[123]>([^<]*)<");
		Matcher m = p.matcher(str);
		if (!m.find())
			System.out.println("Cannot find the name of mission in "+filename+".html");
		if (!name.equals("<no name>"))
			System.out.print(" "+filename+".java ");
		name = m.group(1);

		/* extract the hint, if any */
		Pattern p2 =  Pattern.compile("<div class=\"hint\">(.*?)</div>",Pattern.MULTILINE|Pattern.DOTALL);
		Matcher m2 = p2.matcher(str);
		if (m2.find()) {
			hint="<html>\n"+HTMLMissionHeader+"<body>\n"+m2.group(1)+"</body>\n</html>\n";
			str=m2.replaceAll("");
		}

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
		mission = "<html>\n"+HTMLMissionHeader+"<body>\n"+str+"</body>\n</html>\n";
	}

	protected void loadMap(World w,String path) {
		BufferedReader br = Reader.fileReader( path,"map",false);
		if (br==null)
			throw new RuntimeException("Unable to load "+path+".map");
		try {
			w.readFromFile(br);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	protected void loadMap(World intoWorld) {
		BufferedReader br = Reader.fileReader( getClass().getCanonicalName(),"map",false);
		if (br==null)
			// TODO: not very nice, particularly when we were expecting to load a map and it is not included in the .jar file ;)
			return;
		try {
			intoWorld.readFromFile(br);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void newSourceFromFile(ProgrammingLanguage lang, String name, String filename, String extension) {
		newSourceFromFile(lang, name, filename, extension,"");
	}
	public void newSourceFromFile(ProgrammingLanguage lang, String name, String filename, String extension,String patternString) {
		
		StringBuffer sb = Reader.fileToStringBuffer("src"+File.separator+filename,extension,false);

		if (sb==null)
			sb = Reader.fileToStringBuffer(filename,extension,false);
		if (sb==null) {
			System.err.println("Source file "+filename+"."+extension+" not found.");
			return;
		}
		/* Remove line comments since at some point, we put everything on one line only, 
		 * so this would comment the end of the template and break everything */
		Pattern lineCommentPattern = Pattern.compile("//.*$");
		Matcher lineCommentMatcher = lineCommentPattern.matcher(sb.toString());
		String content = lineCommentMatcher.replaceAll("");

		/* Extract the template, the initial content and the solution out of the file */
		int state = 0;
		int savedState = 0;
		StringBuffer head = new StringBuffer(); /* before the template (state 0) */
		StringBuffer templateHead = new StringBuffer(); /* in template before solution (state 1) */
		StringBuffer solution = new StringBuffer(); /* the solution (state 2) */
		StringBuffer templateTail = new StringBuffer(); /* in template after solution (state 3) */
		StringBuffer tail = new StringBuffer(); /* after the template (state 4) */
		StringBuffer skel = new StringBuffer(); /* within BEGIN/END SKEL */

		for (String line : content.split("\n")) {
			//if (this.debug)
			//	System.out.println(state+"->"+line);
			switch (state) {
			case 0: /* initial content */
				if (line.contains("public class ")) {
					head.append(line.replaceAll("public class \\S*", "public class "+name));
				} else if (line.contains("package")) {
					head.append("$package \n");						
				} else if (line.contains("BEGIN TEMPLATE")) {
					state = 1;
				} else if (line.contains("BEGIN SOLUTION")) {
					state = 2; 
				} else if (line.contains("BEGIN SKEL")) {
					savedState = state;
					state = 6; 
				} else {
					head.append(line+"\n");
				}
				break;
			case 1: /* template head */
				if (line.contains("public class "))
					templateHead.append(line.replaceAll("public class \\S*", "public class "+name)+"\n");
				else if (line.contains("END TEMPLATE")) {
					state = 4;
				} else if (line.contains("BEGIN SOLUTION")) {
					state = 2; 
				} else if (line.contains("BEGIN HIDDEN") && !debug) {
					savedState = 1;
					state = 5; 
				} else if (line.contains("BEGIN SKEL")) {
					savedState = state;
					state = 6; 
				} else {
					templateHead.append(line+"\n");
				}
				break;
			case 2: /* solution */
				if (line.contains("END TEMPLATE")) {
					state = 4;
				} else if (line.contains("END SOLUTION")) {
					state = 3;  
				} else if (line.contains("BEGIN SKEL")) {
					savedState = state;
					state = 6; 
				} else {
					solution.append(line+"\n");
				}
				break;
			case 3: /* template tail */
				if (line.contains("END TEMPLATE")) {
					state = 4;
				} else if (line.contains("BEGIN SOLUTION")) {
					throw new RuntimeException("Begin solution in template tail in file "+name+" of "+getName()+". Change it to BEGIN HIDDEN");
				} else if (line.contains("BEGIN SKEL")) {
					savedState = state;
					state = 6; 
				} else if (line.contains("BEGIN HIDDEN") && !debug) {
					savedState = 4;
					state = 2; 
				} else {
					templateTail.append(line+"\n");	
				}
				break;
			case 4: /* end of file */
				tail.append(line+"\n");
				break;
			case 5: /* Hidden but not bodied */
				if (line.contains("END HIDDEN")) {
					state = savedState;
				} 
				break;
			case 6: /* skeleton */
				if (line.contains("END SKEL")) {
					state = savedState;
				} else {
					skel.append(line+"\n");					
				}
				break;
			default: 	
				throw new RuntimeException("Parser error in "+filename+". This is a parser bug (state="+state+"), please report.");	
			}
		}

		String initialContent = templateHead.toString() + templateTail.toString();
		String debugContent = "/* The solution is displayed because we are in debug mode */\n"+
			templateHead.toString() +"/* The solution is displayed because we are in debug mode */\n"+solution+ 
			templateTail.toString();
		String skelContent = skel.toString().replaceAll("\n", " ");
		String headContent = head.toString().replaceAll("\n", " ");
		
		String template = (headContent+"$body"+tail);
		
		/* remove any \n from template to not desynchronize line numbers between compiler and editor */ 
		Pattern newLinePattern = Pattern.compile("\n",Pattern.MULTILINE);
		Matcher newLineMatcher = newLinePattern.matcher(template);
		template = newLineMatcher.replaceAll(" ");
		
		/* Apply all requested rewrites, if any */
		if (patternString != null) {
		Map<String, String> patterns = new HashMap<String, String>();
			for (String pattern: patternString.split(";")) {
				String[] parts = pattern.split("/");
				if (parts.length != 1 || !parts[0].equals("")) {
					if (parts.length != 3 || !parts[0].equals("s")) 
						throw new RuntimeException("Malformed pattern for file "+name+": '"+ pattern+"' (from '"+patterns+"')");

					if (this.debug)
						System.out.println("Replace all "+parts[1]+" to "+parts[2]);
					template = template.replaceAll(parts[1], parts[2]);
					initialContent = initialContent.replaceAll(parts[1], parts[2]);
					debugContent = debugContent.replaceAll(parts[1], parts[2]);
					skelContent = skelContent.replaceAll(parts[1], parts[2]);
				}
			}

		}

		 /*if (this.debug) {
			System.out.println("<<<<<<<<template:"+template);
			System.out.println("<<<<<<<<debugCtn:"+debugContent);
			System.out.println("<<<<<<<<initialContent:"+initialContent);
		    System.out.println("<<<<<<<<Skel: "+skelContent);
		}*/
		
		newSource(lang, name, 
				debug?debugContent:initialContent,
				skelContent.length()>0?skelContent:template);
	}
	protected void addEntityKind(World w, Entity se, String name) {
		addEntityKind(Game.JAVA, w, se, name);
	}
	protected void addEntityKind(ProgrammingLanguage lang, World w, Entity se, String name) {
		if (entitiesNames == null)  {
			entitiesNames = new ArrayList<String>();
			tabsNames = new ArrayList<String>();
		}

		w.addEntity(se);
		se.setWorld(w);
		se.setName(name);
		entitiesNames.add(se.getClass().getName());
		tabsNames.add("My"+name);
		newSourceFromFile(lang, "My"+name, se.getClass().getName(),"java"); 
	}
	protected void addEntityKind(World[] ws, Entity se, String name) {
		addEntityKind(Game.JAVA, ws, se, name);
	}
	protected void addEntityKind(ProgrammingLanguage lang, World[] ws, Entity se, String name) {
		if (entitiesNames == null)  {
			entitiesNames = new ArrayList<String>();
			tabsNames = new ArrayList<String>();
		}
		for (int i=0;i<ws.length;i++) {
			ws[i].addEntity(se);
			se.setWorld(ws[i]);
			se.setName(name);
			if (i==0) {
				entitiesNames.add(se.getClass().getName());
				tabsNames.add("My"+name);
				newSourceFromFile(lang, "My"+name, se.getClass().getName(),"java"); 
			}
		}
	}

	protected final void setup(World w) {
		World[] ws = new World[1];
		ws[0] = w;
		setup(ws);
	}
	protected void setup(World[] ws) {
		worldDuplicate(ws);
	
		for (ProgrammingLanguage lang: getProgLanguages()) {
			boolean found = false;
			String searchedName = null;
			for (SourceFile sf : getSourceFiles(lang)) {
				if (searchedName == null) {//lazy initialization
					Pattern p = Pattern.compile(".*?([^.]*)$");
					Matcher m = p.matcher(entityName);
					if (m.matches())
						searchedName = m.group(1);
					p = Pattern.compile("Entity$");
					m = p.matcher(searchedName);
					searchedName = m.replaceAll("");

				}
				//	System.out.println("Saw "+sf.name+", searched for "+searchedName+" or "+tabName+" while checking for the need of creating a new tab");
				if (sf.name.equals(searchedName)||sf.name.equals(tabName))
					found=true;
			}
			if (!found) {
				newSourceFromFile(lang, tabName, entityName, Game.getProgrammingFileExtension(lang));
			} else if (!lang.equals(Game.JAVA))
				System.out.println("Found it, no need to create a new source file");
		}
		computeAnswer();
	}
	protected void computeAnswer() {
		if (entitiesNames != null)
			mutateEntities(answerWorld,entitiesNames);
		else 
			mutateEntity(answerWorld,entityName);
		for (World aw : answerWorld) {
			Iterator<Entity> it = aw.entities();
			while (it.hasNext())
				try {
					it.next().run();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}		
	}

	@Override
	public void run(List<Thread> runnerVect){
		for (int i=0; i<currentWorld.length; i++)
			currentWorld[i].doDelay();

		if (tabsNames == null)
			mutateEntity(tabName);
		else
			mutateEntities(tabsNames);
		
		for (int i=0; i<currentWorld.length; i++)
			currentWorld[i].runEntities(runnerVect);

	}

	@Override
	public void runDemo(List<Thread> runnerVect){
		for (int i=0; i<initialWorld.length; i++) { 
			answerWorld[i].reset(initialWorld[i]);
			answerWorld[i].doDelay();
		}
		
		if (entitiesNames == null)
			mutateEntity(answerWorld, entityName);
		else
			mutateEntities(answerWorld, entitiesNames);
		
		for (int i=0; i<answerWorld.length; i++)
			answerWorld[i].runEntities(runnerVect);
	}
}
