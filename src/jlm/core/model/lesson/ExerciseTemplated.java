package jlm.core.model.lesson;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jlm.core.DiscardableGameStateListener;
import jlm.core.model.FileUtils;
import jlm.core.model.Game;
import jlm.core.model.GameState;
import jlm.core.model.ProgrammingLanguage;
import jlm.universe.Entity;
import jlm.universe.World;



public abstract class ExerciseTemplated extends Exercise {

	protected String tabName = getClass().getSimpleName(); /** Name of the tab in editor -- must be a valid java identifier */
	protected String entityName = getClass().getCanonicalName()+"Entity"; /** name of the class of entities being solution of this exercise */

	public ExerciseTemplated(Lesson lesson) {
		super(lesson);
	}

	protected void loadMap(World intoWorld, String path) {
		BufferedReader br = null;
		try {
			br = FileUtils.newFileReader(path, "map", false);
			intoWorld.readFromFile(br);
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("Unable to load "+path+".map");	
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void loadMap(World intoWorld) {
		loadMap(intoWorld, getClass().getCanonicalName());
	}

	public void newSourceFromFile(ProgrammingLanguage lang, String name, String filename) throws NoSuchEntityException {
		newSourceFromFile(lang, name, filename, "");
	}
	public void newSourceFromFile(ProgrammingLanguage lang, String name, String filename,String patternString) throws NoSuchEntityException {

		String shownFilename =  filename.replaceAll("\\.", "/")+"."+lang.getExt();
		StringBuffer sb = null;
		try {
			sb = FileUtils.readContentAsText(filename, lang.getExt(), false);
		} catch (IOException ex) {
			throw new NoSuchEntityException("Source file "+filename+"."+lang.getExt()+" not found.");			
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
		StringBuffer tail = new StringBuffer("\n"); /* after the template (state 4) 
		                                             *   This contains a preliminar \n to help python understanding that the following is not in the same block.
		                                             *   Not doing Without it, we would have issues if the student puts some empty lines with the indentation marker at tail
		                                             */
		StringBuffer skel = new StringBuffer(); /* within BEGIN/END SKEL */

		boolean seenTemplate=false; // whether B/E SOLUTION seems included within B/E TEMPLATE
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
					seenTemplate = true;
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
				} else if (line.contains("BEGIN HIDDEN")) {
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
					System.out.println(shownFilename+": BEGIN SOLUTION is closed with END TEMPLATE. Please fix your entity.");
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
					if (!seenTemplate)
						System.out.println(shownFilename+": END TEMPLATE with no matching BEGIN TEMPLATE. Please fix your entity.");
						
					state = 4;
				} else if (line.contains("BEGIN SOLUTION")) {
					throw new RuntimeException(shownFilename+": Begin solution in template tail. Change it to BEGIN HIDDEN");
				} else if (line.contains("BEGIN SKEL")) {
					savedState = state;
					state = 6; 
				} else if (line.contains("BEGIN HIDDEN")) {
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
		String skelContent;
		String headContent;
		if (lang == Game.PYTHON) { 
			skelContent = skel.toString();
			headContent = head.toString();
		} else {
			skelContent = skel.toString().replaceAll("\n", " ");
			headContent = head.toString().replaceAll("\n", " ");
		}

		String template = (headContent+"$body"+tail);

		/* remove any \n from template to not desynchronize line numbers between compiler and editor */ 
		if (lang != Game.PYTHON) {
			Pattern newLinePattern = Pattern.compile("\n",Pattern.MULTILINE);
			Matcher newLineMatcher = newLinePattern.matcher(template);
			template = newLineMatcher.replaceAll(" ");
		}

		/* Apply all requested rewrites, if any */
		if (patternString != null) {
			Map<String, String> patterns = new HashMap<String, String>();
			for (String pattern: patternString.split(";")) {
				String[] parts = pattern.split("/");
				if (parts.length != 1 || !parts[0].equals("")) {
					if (parts.length != 3 || !parts[0].equals("s")) 
						throw new RuntimeException("Malformed pattern for file "+name+": '"+ pattern+"' (from '"+patterns+"')");

					if (Game.getInstance().isDebugEnabled())
						System.out.println("Replace all "+parts[1]+" to "+parts[2]);
					template = template.replaceAll(parts[1], parts[2]);
					initialContent = initialContent.replaceAll(parts[1], parts[2]);
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

		newSource(lang, name, initialContent,
						skelContent.length()>0?skelContent:template);
	}

	protected final void setup(World w) {
		setup(new World[] {w});
	}
	protected void setup(World[] ws) {
		boolean foundALanguage=false;
		worldDuplicate(ws);

		for (ProgrammingLanguage lang: Game.getProgrammingLanguages()) {
			if (Game.getInstance().isDebugEnabled())
				System.err.println("Look for a templating entity in "+lang);
			boolean foundThisLanguage = false;
			String searchedName = null;
			for (SourceFile sf : getSourceFilesList(lang)) {
				if (searchedName == null) {//lazy initialization if there is any sourcefile to parse
					Pattern p = Pattern.compile(".*?([^.]*)$");
					Matcher m = p.matcher(entityName);
					if (m.matches())
						searchedName = m.group(1);
					p = Pattern.compile("Entity$");
					m = p.matcher(searchedName);
					searchedName = m.replaceAll("");
				}
				if (Game.getInstance().isDebugEnabled())
					System.out.println("Saw "+sf.name+" in "+lang.getLang()+", searched for "+searchedName+" or "+tabName+" while checking for the need of creating a new tab");
				if (sf.name.equals(searchedName)||sf.name.equals(tabName))
					foundThisLanguage=true;
			}
			if (!foundThisLanguage) {
				try {
					newSourceFromFile(lang, tabName, entityName);
					super.addProgLanguage(lang);
					foundALanguage = true;
					if (Game.getInstance().isDebugEnabled())
						System.out.println("Found suitable templating entity "+entityName+" in "+lang);

				} catch (NoSuchEntityException e) {
					if (getProgLanguages().contains(lang)) 
						throw new RuntimeException("Exercise "+getName()+" is said to be compatible with language "+lang+", but I fail to find an entity for this language",e);					
					/* Ok, this language does not work for this exercise but didn't promise anything. I can deal with it */
				}
			} else {
				foundALanguage = true;
			}
		}
		if (!foundALanguage) {
			throw new RuntimeException("Cannot find an entity for this exercise. You should fix your paths and such");
		}
		computeAnswer();
	}
	protected void computeAnswer() {
		Thread t = new Thread() {
			@Override
			public void run() {
				ProgrammingLanguage lang = Game.getProgrammingLanguage();

				if (lang.equals(Game.JAVA) || lang.equals(Game.LIGHTBOT)) {
					mutateEntities(answerWorld,entityName);
				} else {
					for (World aw : answerWorld) {
						aw.setDelay(0);
						for (Entity ent: aw.getEntities()) {
							StringBuffer sb = null;
							try {
								sb = FileUtils.readContentAsText(entityName, lang.getExt(), false);
							} catch (IOException ex) {
								throw new RuntimeException("Cannot compute the answer from file "+entityName+"."+lang.getExt()+" since it does not exist.");			
							}


							ent.setScript(lang, sb.toString());
						}
					}

				}

				for (World aw : answerWorld) 
					for (Entity ent: aw.getEntities()) 
						aw.runEntity(ent);
			}
		};
		t.start();
		Game.addInitThread(t);
	}

	@Override
	public void run(List<Thread> runnerVect){
		for (int i=0; i<currentWorld.length; i++)
			currentWorld[i].doDelay();

		mutateEntities(tabName);

		for (int i=0; i<currentWorld.length; i++)
			currentWorld[i].runEntities(runnerVect);

	}

	@Override
	public void runDemo(List<Thread> runnerVect){
		for (int i=0; i<initialWorld.length; i++) { 
			answerWorld[i].reset(initialWorld[i]);
			answerWorld[i].doDelay();
		}
		final ProgrammingLanguage current = Game.getProgrammingLanguage();
		Game.getInstance().setProgramingLanguage(Game.JAVA);

		mutateEntities(answerWorld, entityName);

		for (int i=0; i<answerWorld.length; i++)
			answerWorld[i].runEntities(runnerVect);


		// as demo is launched in several threads, we can restore the current programming language
		// only when the demo is finished. This is achieved using some kind of hook triggered when DEMO_ENDED.	
		Game.getInstance().addGameStateListener(
				new DiscardableGameStateListener() {			
					private boolean dirty = false;

					@Override
					public void stateChanged(GameState type) {
						if (type == GameState.DEMO_ENDED) {
							Game.getInstance().setProgramingLanguage(current);
							this.dirty = true;
						}
					}

					@Override
					public boolean isDirty() {
						return this.dirty;
					}
				});
	}
}
