package jlm.lesson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jlm.universe.Entity;
import jlm.universe.World;



public abstract class ExerciseTemplated extends Exercise {

	protected String tabName = getClass().getSimpleName(); /* Name of the tab in editor */
	protected ArrayList<String> tabsNames = null;
	protected String entityName = getClass().getCanonicalName()+"Entity"; /* name of the class of entities being solution of this exercise */
	protected ArrayList<String> entitiesNames = null;

	private static String locale;
	
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

	public static void setLocale(String lang) {
		locale=lang;
	}
	static protected BufferedReader fileReader(String file,String extension,boolean translatable) {
		if (translatable) {
			if (locale==null) 
				throw new RuntimeException("locale is null");
				
			BufferedReader br =  fileReader(file.replace('.','/')+"."+locale+(extension!=null?"."+extension:""));
			if (br != null)
				return br;
		}
		BufferedReader br = fileReader(file.replace('.','/')+(extension!=null?"."+extension:""));
		return br;
	}	
	static protected BufferedReader fileReader(String filename) {
		BufferedReader br = null;
		/* try to find the file */
		try {
			br = new BufferedReader(new FileReader(new File(filename)));
		} catch (FileNotFoundException e) {
			// external HTML file of this exercise not found on file system. Give as resource, in case we are in a jar file
			String resourceName = "/"+filename;
			resourceName = resourceName.replace('\\', '/'); /* just in case we're passed a windows path */

			InputStream s = ExerciseTemplated.class.getResourceAsStream(resourceName);
			if (s == null) {
				return null;	/* file not found, give up */
			}

			try {
				br = new BufferedReader(new InputStreamReader(s,"UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
				System.err.println("File encoding of "+filename+
						" is not supported on this platform (please report this bug)");
				return null;
			}
		}
		return br;
	}	
	static protected StringBuffer fileToStringBuffer(String file, String extension, boolean translatable) {
		BufferedReader br = fileReader(file,extension,translatable);
		String newLine = System.getProperty("line.separator");
		if (br==null) 
			return null;
		
		StringBuffer sb = new StringBuffer();
		try {
			String s;
			s = br.readLine();
			while (s != null) {
				sb.append(s);
				sb.append(newLine);
				s = br.readLine();
			}
			
		} catch (IOException e) {
			e.printStackTrace();			
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb;
	}
	protected void loadHTMLMission() {
		String filename = getClass().getCanonicalName().replace('.',File.separatorChar);

		StringBuffer sb = fileToStringBuffer(filename, "html",true);
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
		
		/* get the mission explanation */
		mission = "<html>\n"+HTMLMissionHeader+"<body>\n"+str+"</body>\n</html>\n";
	}

	protected void loadMap(World intoWorld) {
		BufferedReader br = fileReader( getClass().getCanonicalName(),"map",false);
		if (br==null)
			return;
		try {
			intoWorld.readFromFile(br);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void newSourceFromFile(String name, String filename, String extension) {
		StringBuffer sb = fileToStringBuffer("src"+File.separator+filename,extension,false);
		if (sb==null)
			sb = fileToStringBuffer(filename,extension,false);
		if (sb==null) {
			System.err.println("Source file "+filename+" not found.");
			return;
		}

		/* Extract the template, the initial content and the solution out of the file */
		int state = 0;
		StringBuffer head = new StringBuffer(); /* before the template (state 0) */
		StringBuffer templateHead = new StringBuffer(); /* in template before solution (state 1) */
		StringBuffer solution = new StringBuffer(); /* the solution (state 2) */
		StringBuffer templateTail = new StringBuffer(); /* in template after solution (state 3) */
		StringBuffer tail = new StringBuffer(); /* after the template (state 4) */

		for (String line : sb.toString().split("\n")) {
			if (this.debug)
				System.out.println(state+"->"+line);
			switch (state) {
			case 0: /* initial content */
				if (line.contains("public class ")) {
					head.append(line.replaceAll("public class \\S*", "public class "+name));
				} else if (line.contains("package")) {
					head.append("$package\n");						
				} else if (line.contains("BEGIN TEMPLATE")) {
					state = 1;
				} else if (line.contains("BEGIN SOLUTION")) {
					state = 2; 
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
				} else {
					templateHead.append(line+"\n");
				}
				break;
			case 2: /* solution */
				if (line.contains("END TEMPLATE")) {
					state = 4;
				} else if (line.contains("END SOLUTION")) {
					state = 1; // FIXME: gg: I changed this state from 3 to 1 to enable masking of several solutions, is it wrong? 
				} else {
					solution.append(line+"\n");
				}
				break;
			case 3: /* template tail */
				if (line.contains("END TEMPLATE")) {
					state = 4;
				} else {
					templateTail.append(line+"\n");	
				}
				break;
			case 4: 
				tail.append(line+"\n");
				break;
			default: 	
				throw new RuntimeException("Parser error in "+filename+". This is a parser bug (state="+state+"), please report.");	
			}
		}

		String initialContent = templateHead.toString() + templateTail.toString();
		String debugContent = templateHead.toString() +"/* The solution is displayed because we are in debug mode */\n"+solution+ templateTail.toString();
		/* TODO: remove "//.*\n" before putting everything on one line only */
		/* remove any \n from template to not desynchronize line numbers between compiler and editor */ 
		StringBuffer template = new StringBuffer();
		for (String s: (head+"$body"+tail).split("\n")) 
			template.append(s);			

		if (this.debug) {
			System.out.println("<<<<<<<<template:"+template);
			System.out.println("<<<<<<<<debugCtn:"+debugContent);
			System.out.println("<<<<<<<<initialContent:"+initialContent);
		}
		newSource(name, initialContent, template.toString());
	}
	protected void addEntityKind(World w, Entity se, String name) {
		if (entitiesNames == null)  {
			entitiesNames = new ArrayList<String>();
			tabsNames = new ArrayList<String>();
		}

		w.addEntity(se);
		se.setWorld(w);
		se.setName(name);
		entitiesNames.add(se.getClass().getName());
		tabsNames.add("My"+name);
		newSourceFromFile("My"+name, se.getClass().getName(),"java"); 
	}
	protected void addEntityKind(World[] ws, Entity se, String name) {
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
				newSourceFromFile("My"+name, se.getClass().getName(),"java"); 
			}
		}
	}

	protected final void setup(World w) {
		loadMap(w);
		World[] ws = new World[1];
		ws[0] = w;
		setup(ws);
	}
	protected void setup(World[] ws) {
		worldDuplicate(ws);

		newSourceFromFile(tabName, entityName,"java"); 

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
				it.next().run();
		}		
	}


	@Override
	public void reset(){
		for (int i=0; i<initialWorld.length; i++) 
			currentWorld[i].reset(initialWorld[i]);
	}

	@Override
	public void run(List<Thread> runnerVect){
		reset();

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
