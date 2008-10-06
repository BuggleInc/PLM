package lessons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Vector;

import bugglequest.core.AbstractBuggle;
import bugglequest.core.World;
import bugglequest.io.Utils;

public abstract class ExerciseTemplated extends Exercise {

	protected String tabName = getClass().getSimpleName(); /* Name of the tab in editor */
	protected String buggleName = getClass().getCanonicalName()+"Buggle"; /* name of the class of buggles being solution of this exercise */
	protected int UIDelay = 100;

	public ExerciseTemplated(Lesson lesson) {
		super(lesson);
		loadHTMLMission();
	}
	
	protected void loadHTMLMission() {
		String filename = getClass().getCanonicalName().replace('.',File.separatorChar)+".html";

		String newLine = System.getProperty("line.separator");

		BufferedReader br = null;
		/* try to find the file */
		try {
			br = new BufferedReader(new FileReader(new File(filename)));
		} catch (FileNotFoundException e) {
			// external HTML file of this exercise not found on file system. Give as resource, in case we are in a jar file
			String resourceName = "/"+getClass().getCanonicalName().replace('.','/')+".html";

			InputStream s = getClass().getResourceAsStream(resourceName);
			if (s == null) {
				mission = "File "+filename+" and resource "+resourceName+" not found.";
				return;	/* file not found, give up */
			}

			try {
				br = new BufferedReader(new InputStreamReader(s,"UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
				mission = "Mission file encoding is not supported on this platform (please report this bug)";
				return;
			}
		}

		/* read it */
		try {
			StringBuffer sb = new StringBuffer();
			String s;
			s = br.readLine();
			while (s != null) {
				sb.append(s);
				sb.append(newLine);
				s = br.readLine();
			}
			mission = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();			
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void loadMap(World intoWorld) {
		String filename = getClass().getCanonicalName().replace('.',File.separatorChar)+".map";

		BufferedReader br = null;
		/* try to find the file */
		try {
			br = new BufferedReader(new FileReader(new File(filename)));
		} catch (FileNotFoundException e) {
			// external HTML file of this exercise not found on file system. Give as resource, in case we are in a jar file
			String resourceName = "/"+getClass().getCanonicalName().replace('.','/')+".map";
			InputStream s = getClass().getResourceAsStream(resourceName);
			if (s == null) {
				return;	/* file not found, give up */
			}

			try {
				br = new BufferedReader(new InputStreamReader(s,"UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		
		/* read it */
		try {
			Utils.readWorldFromFile(intoWorld, br);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void newSourceFromFile(String name, String filename) {		
		String fullfilename = "src"+File.separator+filename;
		String newLine = System.getProperty("line.separator");

		BufferedReader br = null;
		/* try to find the file */

		try {
			br = new BufferedReader(new FileReader(new File(fullfilename)));
		} catch (FileNotFoundException e) {
			// file not found on file system. Give as resource, in case we are in a jar file
			String resourceName = File.separator+filename;
			resourceName = resourceName.replace('\\', '/'); // dirty hack
			InputStream is = getClass().getResourceAsStream(resourceName);
			if (is == null) {
				/* Really not found. Give up, the exercise may have other ways to define its answer source */
				return;
			}

			br = new BufferedReader(new InputStreamReader(is));
		}


		/* read it */
		StringBuffer sbFullContent=null;
		try {
			sbFullContent = new StringBuffer();
			String s;
			s = br.readLine();
			while (s != null) {
				sbFullContent.append(s);
				sbFullContent.append(newLine);
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

		if (sbFullContent != null) {
			/* Extract the template, the initial content and the solution out of the file */
			int state = 0;
			StringBuffer head = new StringBuffer(); /* before the template (state 0) */
			StringBuffer templateHead = new StringBuffer(); /* in template before solution (state 1) */
			StringBuffer solution = new StringBuffer(); /* the solution (state 2) */
			StringBuffer templateTail = new StringBuffer(); /* in template after solution (state 3) */
			StringBuffer tail = new StringBuffer(); /* after the template (state 4) */

			for (String line : sbFullContent.toString().split("\n")) {
				if (debugThis)
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
					if (line.contains("END TEMPLATE")) {
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
						state = 3; 
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
			/* remove any \n from template to not desynchronize line numbers between compiler and editor */ 
			StringBuffer template = new StringBuffer();
			for (String s: (head+"$body"+tail).split("\n")) 
				template.append(s);			

			if (debugThis) {
				System.out.println("<<<<<<<<template:"+template);
				System.out.println("<<<<<<<<debugCtn:"+debugContent);
				System.out.println("<<<<<<<<initialContent:"+initialContent);
			}
			//newSource(name, (Exercise.debug?debugContent:initialContent), template.toString());
			if (getLesson().solutionShown()) {
				newSource(name, debugContent, template.toString());
			} else {
				newSource(name, initialContent, template.toString());				
			}

		}
	}

	protected final void setup(World w) {
		loadMap(w);
		World[] ws = new World[1];
		ws[0] = w;
		setup(ws);
	}

	protected void computeAnswer() {
		mutateBuggle(answerWorld,buggleName);
		for (World aw : answerWorld) {
			Iterator<AbstractBuggle> it = aw.buggles();
			while (it.hasNext())
				it.next().run();
		}		
	}

	protected void setup(World[] ws) {		
		worldDuplicate(ws);

		newSourceFromFile(tabName, buggleName.replace('.',File.separatorChar)+".java"); //FIXME: potential bug under windows

		computeAnswer();
	}

	@Override
	public void reset(){
		for (int i=0; i<initialWorld.length; i++) 
			currentWorld[i].reset(initialWorld[i]);
	}

	@Override
	public void run(Vector<Thread> runnerVect){
		reset();

		for (int i=0; i<currentWorld.length; i++) 
			currentWorld[i].setDelay(this.UIDelay );

		mutateBuggle(tabName);
		for (int i=0; i<currentWorld.length; i++)
			currentWorld[i].runBuggles(runnerVect);

	}

	@Override
	public void runDemo(Vector<Thread> runnerVect){
		for (int i=0; i<initialWorld.length; i++) { 
			answerWorld[i].reset(initialWorld[i]);
			answerWorld[i].setDelay(this.UIDelay);
		}		
		mutateBuggle(answerWorld, buggleName);
		for (int i=0; i<answerWorld.length; i++)
			answerWorld[i].runBuggles(runnerVect);
	}
}
