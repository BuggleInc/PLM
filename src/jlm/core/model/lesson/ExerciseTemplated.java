package jlm.core.model.lesson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.utils.FileUtils;
import jlm.universe.BrokenWorldFileException;
import jlm.universe.Entity;
import jlm.universe.World;



public abstract class ExerciseTemplated extends Exercise {

	protected String tabName = getClass().getSimpleName(); /** Name of the tab in editor -- must be a valid java identifier */
	protected String worldFileName = getClass().getCanonicalName(); /** Name of the save files */
	protected String nameOfCorrectionEntity = getClass().getCanonicalName()+"Entity"; /** name of the entity class computing the answer. Usually no need to redefine this */

	public ExerciseTemplated(Lesson lesson) {
		super(lesson);
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
			throw new NoSuchEntityException(Game.i18n.tr("Source file {0}.{1} not found.",filename,lang.getExt()));			
		}


		/* Remove line comments since at some point, we put everything on one line only, 
		 * so this would comment the end of the template and break everything */
		Pattern lineCommentPattern = Pattern.compile("//.*$", Pattern.MULTILINE);
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
					System.out.println(i18n.tr("{0}: BEGIN SOLUTION is closed with END TEMPLATE. Please fix your entity.",shownFilename));
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
						System.out.println(i18n.tr("{0}: END TEMPLATE with no matching BEGIN TEMPLATE. Please fix your entity.",shownFilename));
						
					state = 4;
				} else if (line.contains("BEGIN SOLUTION")) {
					throw new RuntimeException(i18n.tr("{0}: Begin solution in template tail. Change it to BEGIN HIDDEN",shownFilename));
				} else if (line.contains("BEGIN SKEL")) {
					savedState = state;
					state = 6; 
				} else if (line.contains("BEGIN HIDDEN")) {
					savedState = 3;
					state = 5; 
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
				throw new RuntimeException(i18n.tr("Parser error in file {0}. This is a parser bug (state={1}), please report.",filename,state));	
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
		
		/* Remove the unnecessary leading spaces from the initial content */
		Pattern newLinePattern = Pattern.compile("\n",Pattern.MULTILINE);
		if (lang != Game.PYTHON) {
			initialContent = initialContent.replaceAll("\t","    ");
			String[] ctn = newLinePattern.split(initialContent);
			/* Compute the minimal amount of leading spaces on all lines */
			int minAmountOfLeadingSpace = -1;
			for (String line:ctn) {
				if (line.equals(""))
					continue;
				int len = 0;
				for (char c:line.toCharArray())
					if (c == ' ')
						len ++;
					else
						if (minAmountOfLeadingSpace == -1 || len<minAmountOfLeadingSpace)
							minAmountOfLeadingSpace = len;
			}
			if (minAmountOfLeadingSpace > 0) {
				/* Remove that amount of leading spaces on all lines, and rebuilds initialContent */
				StringBuffer sbCtn = new StringBuffer();
				for (String line : ctn) 
					if (line.equals(""))
						sbCtn.append("\n");
					else
						sbCtn.append(line.substring(minAmountOfLeadingSpace)+"\n");
				/* Rebuild the initial content */
				initialContent = sbCtn.toString();
			}
		}

		/* remove any \n from template to not desynchronize line numbers between compiler and editor */ 
		if (lang != Game.PYTHON) {
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
		setupWorlds(ws);

		for (ProgrammingLanguage lang: Game.getProgrammingLanguages()) {
			boolean foundThisLanguage = false;
			String searchedName = null;
			for (SourceFile sf : getSourceFilesList(lang)) {
				if (searchedName == null) {//lazy initialization if there is any sourcefile to parse
					Pattern p = Pattern.compile(".*?([^.]*)$");
					Matcher m = p.matcher(nameOfCorrectionEntity);
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
					newSourceFromFile(lang, tabName, nameOfCorrectionEntity);
					super.addProgLanguage(lang);
					foundALanguage = true;
					if (Game.getInstance().isDebugEnabled())
						System.out.println("Found suitable templating entity "+nameOfCorrectionEntity+" in "+lang);

				} catch (NoSuchEntityException e) {
					if (lang.equals(Game.PYTHON))
						System.out.println("Cannnot find any suitable templating entity "+nameOfCorrectionEntity+" in "+lang+":\n  "+e);
						
					if (getProgLanguages().contains(lang)) 
						throw new RuntimeException("Exercise "+getName()+" is said to be compatible with language "+lang+", but I fail to find an entity for this language",e);
					/* Ok, this language does not work for this exercise but didn't promise anything. I can deal with it */
				}
			} else {
				foundALanguage = true;
			}
		}
		if (!foundALanguage) {
			throw new RuntimeException("Cannot find an entity for exercise "+getName()+". You should fix your paths and such");
		}
		computeAnswer();
	}
	
	protected void computeAnswer() {
		Thread t = new Thread() {
			@Override
			public void run() {
				Game.getInstance().statusArgAdd(getClass().getSimpleName());
				boolean allFound = true;
				if (answerWorld.get(0).haveIO()) {
					if (Game.getProperty(Game.PROP_ANSWER_CACHE, "true",true).equalsIgnoreCase("true")) {
						Vector<World> newAnswer = new Vector<World>();
						int rank = 0;
						for (World aw:answerWorld) {
							String name = worldFileName+"-answer"+(rank++);
							try {
								World nw = aw.readFromFile(name);
								newAnswer.add(nw);
							} catch (BrokenWorldFileException bwfe) {
								System.err.println(i18n.tr("World {0} is broken ({1}). Recompute all answer worlds.",name,bwfe.getLocalizedMessage()) );
								allFound = false;
								break;
							} catch (IOException ioe) {
								System.err.println(i18n.tr("IO exception while reading world {1} ({1}). Recompute all answer worlds.",name,ioe.getLocalizedMessage()));
								allFound = false;
								break;
							}
						}
						if (allFound) {
							answerWorld = newAnswer;
							Game.getInstance().statusArgRemove(getClass().getSimpleName());
							return;
						}
					} else {
						System.out.println(i18n.tr("Recompute the answer of {0} despite the cache file, as requested by the property {1}",worldFileName,Game.PROP_ANSWER_CACHE));
					}
				}
				
				/* I/O didn't work. We have to load the files manually */
				ExecutionProgress progress = new ExecutionProgress();
				
				mutateCorrection(WorldKind.ANSWER);

				for (World aw : answerWorld) 
					for (Entity ent: aw.getEntities()) 
						ent.runIt(progress);
				
				/* Try to write all files for next time */
				if (answerWorld.get(0).haveIO()) {
					int rank = 0;
					for (World aw:answerWorld) {
						String name = "src/"+worldFileName+"-answer"+(rank++);
						name = name.replaceAll("\\.", "/") + ".map";
						if (new File(name).getParentFile().canWrite()) {
							try {
								aw.writeToFile(new File(name));
							} catch (Exception e) {
								System.err.println(i18n.tr("Error while writing answer world of {0}:",name));
								e.printStackTrace();
							}
						} else {
							System.err.println(i18n.tr("Cannot write answer world of {0}. Please check the permissions.",name));
						}
					}
				}
				Game.getInstance().statusArgRemove(getClass().getSimpleName());
			}
		};
		t.start();
		Game.addInitThread(t);
	}

	@Override
	public void run(List<Thread> runnerVect){
		if (lastResult == null)
			lastResult = new ExecutionProgress();
		
		mutateEntities(currentWorld, tabName);

		for (World cw: getWorlds(WorldKind.CURRENT)) {
			cw.doDelay();
			cw.runEntities(runnerVect, lastResult);
		}
	}

	@Override
	public void runDemo(List<Thread> runnerVect){
		ExecutionProgress ignored = new ExecutionProgress();
		
		for (int i=0; i<initialWorld.size(); i++) { 
			answerWorld.get(i).reset(initialWorld.get(i));
			answerWorld.get(i).doDelay();
		}
		mutateCorrection(WorldKind.ANSWER);

		for (World aw:getWorlds(WorldKind.ANSWER))
			aw.runEntities(runnerVect,ignored);
	}
	
	public void mutateCorrection(WorldKind kind) {
		ProgrammingLanguage lang = Game.getProgrammingLanguage();
		Vector<World> worlds;
		switch (kind) {
		case INITIAL: worlds = initialWorld; break;
		case CURRENT: worlds = currentWorld; break;
		case ANSWER:  worlds = answerWorld;  break;
		default: throw new RuntimeException("kind is invalid: "+kind);
		}

		/* No need to deal with lightbot here: this method is redefined in LightBotExercise from scratch */
		if (lang.equals(Game.JAVA)) {
			mutateEntities(worlds,nameOfCorrectionEntity);
		} else {
			for (World aw : worlds) {
				for (Entity ent: aw.getEntities()) {
					StringBuffer sb = null;
					try {
						sb = FileUtils.readContentAsText(nameOfCorrectionEntity, lang.getExt(), false);
					} catch (IOException ex) {
						throw new RuntimeException("Cannot compute the answer from file "+nameOfCorrectionEntity+"."+lang.getExt()+" since it does not exist.");			
					}


					ent.setScript(lang, sb.toString());
				}
			}
		}		
	}
}
