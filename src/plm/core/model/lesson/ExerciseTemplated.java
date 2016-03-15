package plm.core.model.lesson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import plm.core.PLMCompilerException;
import plm.core.PLMEntityNotFound;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.LogHandler;
import plm.core.model.lesson.Lesson.LoadingOutcome;
import plm.core.model.session.SourceFile;
import plm.core.utils.FileUtils;
import plm.universe.BrokenWorldFileException;
import plm.universe.Entity;
import plm.universe.World;



public abstract class ExerciseTemplated extends Exercise {

	protected String worldFileName = getClass().getCanonicalName(); /* Name of the save files */

	public ExerciseTemplated(Game game, Lesson lesson) {
		super(game, lesson,null);
	}
	public ExerciseTemplated(Game game, Lesson lesson, String basename) {
		super(game, lesson,basename);
	}

	public void newSourceFromFile(ProgrammingLanguage lang, String name, String filename) throws NoSuchEntityException {
		newSourceFromFile(lang, name, filename, "");
	}
	public void newSourceFromFile(ProgrammingLanguage lang, String name, String filename,String patternString) throws NoSuchEntityException {

		String shownFilename =  filename.replaceAll("\\.", "/")+"."+lang.getExt();
		StringBuffer sb = null;
		try {
			sb = FileUtils.readContentAsText(filename, null, lang.getExt(), false);
		} catch (IOException ex) {
			throw new NoSuchEntityException(getGame().i18n.tr("Source file {0}.{1} not found.",filename.replaceAll("\\.","/"),lang.getExt()));
		}

		String content;
		if (lang.equals(Game.JAVA)) {
			/* Remove line comments since at some point, we put everything on one line only,
			 * so this would comment the end of the template and break everything */
			Pattern lineCommentPattern = Pattern.compile("//.*$", Pattern.MULTILINE);
			Matcher lineCommentMatcher = lineCommentPattern.matcher(sb.toString());
			content = lineCommentMatcher.replaceAll("");
		} else {
			content = sb.toString();
		}

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
		StringBuffer correction = new StringBuffer(); /* the unchanged content, but the package and className modification */
		boolean containsLinePreprocessor=false;

		boolean seenTemplate=false; // whether B/E SOLUTION seems included within B/E TEMPLATE
		for (String line : content.split("\n")) {
			switch (state) {
			case 0: /* initial content */
				if (line.contains("class ")) {
					String modified = line.replaceAll("class \\S*", "class "+name);
					head.append(modified);
					correction.append(modified+"\n");
				} else if (line.contains("package")) {
					head.append("$package \n");
					correction.append("$package \n");
				} else if(line.contains("#line") && lang.equals(Game.C)){
					containsLinePreprocessor=true;
					head.append(line+"\n");
				}else if (line.contains("BEGIN TEMPLATE")) {
					if(!containsLinePreprocessor && lang.equals(Game.C)){
						head.append("#line 1 \""+name+".c\" \n");
						containsLinePreprocessor=true;
					}
					correction.append(line+"\n");
					seenTemplate = true;
					state = 1;
				} else if (line.contains("BEGIN SOLUTION")) {
					if(!containsLinePreprocessor && lang.equals(Game.C)){
						head.append("#line 1 \""+name+".c\" \n");
						containsLinePreprocessor=true;
					}
					correction.append(line+"\n");
					state = 2;
				} else if (line.contains("BEGIN SKEL")) {
					correction.append(line+"\n");
					savedState = state;
					state = 6;
				} else {
					correction.append(line+"\n");
					head.append(line+"\n");
				}
				break;
			case 1: /* template head */
				correction.append(line+"\n");
				if (line.contains("BEGIN TEMPLATE")) {
					getGame().getLogger().log(LogHandler.ERROR, getGame().i18n.tr("{0}: BEGIN TEMPLATE within the template. Please fix your entity.",shownFilename));
					state = 4;
				} else if (line.contains("public class ")){
					templateHead.append(line.replaceAll("public class \\S*", "public class "+name)+"\n");
				}else if (line.contains("END TEMPLATE")) {
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
				correction.append(line+"\n");
				if (line.contains("END TEMPLATE")) {
					getGame().getLogger().log(LogHandler.ERROR, getGame().i18n.tr("{0}: BEGIN SOLUTION is closed with END TEMPLATE. Please fix your entity.",shownFilename));
					state = 4;
				} else if (line.contains("END SOLUTION")) {
					if (seenTemplate)
						state = 3;
					else
						state = 4; // Jump directly to end of template
				} else if (line.contains("BEGIN SKEL")) {
					savedState = state;
					state = 6;
				} else {
					solution.append(line+"\n");
				}
				break;
			case 3: /* template tail */
				correction.append(line+"\n");
				if (line.contains("END TEMPLATE")) {
					if (!seenTemplate)
						getGame().getLogger().log(LogHandler.ERROR, getGame().i18n.tr("{0}: END TEMPLATE with no matching BEGIN TEMPLATE. Please fix your entity.",shownFilename));
					state = 4;
				} else if (line.contains("BEGIN SOLUTION")) {
					throw new RuntimeException(getGame().i18n.tr("{0}: Begin solution in template tail. Change it to BEGIN HIDDEN",shownFilename));
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
				correction.append(line+"\n");
				if (line.contains("BEGIN SKEL")) {
					savedState = state;
					state = 6;

				} else {
					if (line.contains("END TEMPLATE"))
						if (!seenTemplate)
							getGame().getLogger().log(LogHandler.ERROR, getGame().i18n.tr("{0}: END TEMPLATE with no matching BEGIN TEMPLATE. Please fix your entity.",shownFilename));
					tail.append(line+"\n");
				}
				break;
			case 5: /* Hidden but not bodied */
				correction.append(line+"\n");
				if (line.contains("END HIDDEN")) {
					state = savedState;
				}
				break;
			case 6: /* skeleton */
				correction.append(line+"\n");
				if (line.contains("END SKEL")) {
					state = savedState;
				} else {
					skel.append(line+"\n");
				}
				break;
			default:
				throw new RuntimeException(getGame().i18n.tr("Parser error in file {0}. This is a parser bug (state={1}), please report.",filename,state));
			}
		}
		if (state == 3) {
			if (seenTemplate)
				getGame().getLogger().log(LogHandler.ERROR, getGame().i18n.tr("{0}: End of file unexpected after the solution but within the template. Please fix your entity.",shownFilename,state));
		} else if (state != 4)
			getGame().getLogger().log(LogHandler.ERROR, getGame().i18n.tr("{0}: End of file unexpected (state: {1}). Did you forget to close your template or solution? Please fix your entity.",shownFilename,state));

		String initialContent = templateHead.toString() + templateTail.toString();
		String skelContent;
		String headContent;
		if (lang == Game.PYTHON || lang == Game.SCALA || lang == Game.C || lang == Game.BLOCKLY) {
			skelContent = skel.toString();
			headContent = head.toString();
		} else {
			skelContent = skel.toString().replaceAll("\r\n", " ").replaceAll("\n", " "); // remove Windows and Linux EOF
			headContent = head.toString().replaceAll("\r\n", " ").replaceAll("\n", " "); // remove Windows and Linux EOF
		}

		String template = (headContent+"$body"+tail);
		int offset = headContent.split("\n").length;

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
					if (c == ' ') {
						len ++;
					} else {
						break;
					}
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

		/* remove any \n from template to not desynchronize line numbers between compiler and editor
		 * Python: We should obviously not change blank signs in Python
		 * Scala: no need since our compiler's front-end is aware of these offsets */
		if (lang == Game.JAVA) {
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

					if (getGame().isDebugEnabled())
						getGame().getLogger().log("Replace all "+parts[1]+" to "+parts[2]);
					template = template.replaceAll(parts[1], parts[2]);
					initialContent = initialContent.replaceAll(parts[1], parts[2]);
					skelContent = skelContent.replaceAll(parts[1], parts[2]);
				}
			}

		}

		/*if (this.debug) {
			getGame().getLogger().log("<<<<<<<<template:"+template);
			getGame().getLogger().log("<<<<<<<<debugCtn:"+debugContent);
			getGame().getLogger().log("<<<<<<<<initialContent:"+initialContent);
		    getGame().getLogger().log("<<<<<<<<Skel: "+skelContent);
		}*/

		if (skelContent.length()>0) {
			if (! (this instanceof ExerciseTemplatingEntity))
				throw new RuntimeException(getName()+": You provided an exercise skeleton, but this is not an ExerciseTemplatingEntity. Are you trying to drive me nuts??");

			/*
			 * HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
			 *
			 * A skeleton was provided! this means that we are in a ExerciseTemplating, for sure.
			 * Use the skeleton as a template and correction; ExerciseTemplatingEntity::setup() will do the right thing.
			 *
			 * FIXME: this is a particularly awful design, and I'm ashamed to commit this.
			 *
			 * We should split this function in two parts, one (in charge of the parsing) would remain the same
			 * in ExerciseTemplated and ExerciseTemplatingEntity while the other (in charge of using the result of this parsing)
			 * would be overridden in ExerciseTemplatingEntity.
			 *
			 * As it is now (abusing some fields of the sourceFile structure to pass some values to ExerciseTemplatingEntity::setup),
			 * this's half-way between the uber-crude HACK and a plain old troll to object-orientation defenders...
			 *
			 * Polux would be proud of me. Or maybe not ;)
			 *
			 * HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
			 *
			 * See the comment at the beginning of  ExerciseTemplatingEntities for the motivation of this mess
			 * (TL;DR: entities in ExerciseTemplatingEntities are Frankenstein monsters built manually from scratch)
			 */
			newSource(lang, name, initialContent, skelContent,offset,
					/* correction: */ templateHead.toString()+solution.toString()+templateTail.toString(),"Error");
		} else {
			newSource(lang, name, initialContent, template,offset,
					correction.toString().replaceAll("SimpleBuggle","AbstractBuggle"),"Error"); // We don't want to have little dialogs when testing
		}
	}

	protected final void setup(World w) {
		setup(new World[] {w});
	}
	protected <W extends World> void setup(W[] ws) {
		boolean foundALanguage=false;
		ArrayList<String> files = new ArrayList<String>();
		int k = 0;
		while(getClass().getResourceAsStream("/"+Game.JAVA.nameOfCommonError(this, k).replaceAll("\\.", "/")+".java")!=null) {
			files.add("/"+Game.JAVA.nameOfCommonError(this, k).replaceAll("\\.", "/")+".java");
			k++;
		}
		setupWorlds(ws,files.size());
		for (ProgrammingLanguage lang: Game.getProgrammingLanguages()) {
			boolean foundThisLanguage = false;
			String searchedName = null;
			for (SourceFile sf : getSourceFilesList(lang)) {
				if (searchedName == null) {//lazy initialization if there is any sourcefile to parse
					Pattern p = Pattern.compile(".*?([^.]*)$");
					Matcher m = p.matcher(lang.nameOfCorrectionEntity(this));
					if (m.matches())
						searchedName = m.group(1);
					p = Pattern.compile("Entity$");
					m = p.matcher(searchedName);
					searchedName = m.replaceAll("");
				}
				if (getGame().isDebugEnabled())
					getGame().getLogger().log("Saw "+sf.getName()+" in "+lang.getLang()+", searched for "+searchedName+" or "+tabName+" while checking for the need of creating a new tab");
				if (sf.getName().equals(searchedName)||sf.getName().equals(tabName))
					foundThisLanguage=true;
			}
			if (!foundThisLanguage) {
				try {
					newSourceFromFile(lang, tabName, lang.nameOfCorrectionEntity(this));
					super.addProgLanguage(lang);
					foundALanguage = true;
					if (getGame().isDebugEnabled())
						getGame().getLogger().log("Found suitable templating entity "+lang.nameOfCorrectionEntity(this)+" in "+lang);

				} catch (NoSuchEntityException e) {
					if (lang.equals(Game.PYTHON) || lang.equals(Game.SCALA) || lang.equals(Game.JAVA) || lang.equals(Game.BLOCKLY))
						getGame().getLogger().log("No templating entity found: "+e);

					if (getProgLanguages().contains(lang))
						throw new RuntimeException(
								getGame().i18n.tr("Exercise {0} is said to be compatible with language {1}, but there is no entity for this language: {2}",
										getName(),lang,e.toString()));
					/* Ok, this language does not work for this exercise but didn't promise anything. I can deal with it */
				}
			} else {
				foundALanguage = true;
			}
		}
		if (!foundALanguage)
			throw new RuntimeException(getGame().i18n.tr("{0}: No entity found. You should fix your paths and such",getName()));
		computeAnswer();
		computeError(files);
	}

	protected void computeError(ArrayList<String> files) {
		for(int i = 0 ; i < files.size(); i++) {
			 final int copyOfi = i;
			 if(getClass().getResourceAsStream(files.get(i))!=null) {
				Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {

					@Override
					public void uncaughtException(Thread th, Throwable ex) {
						if(ex instanceof PLMEntityNotFound) {
							getLesson().setLoadingOutcomeState(LoadingOutcome.FAIL);
						}
						System.err.println("Uncaught exception while computing error: " + ex);
					}
				};
				Thread t = new Thread() {
					@Override
					public void run() {
						getGame().statusArgAdd(getClass().getSimpleName());
						ExecutionProgress progress = new ExecutionProgress(getGame().getProgrammingLanguage());
						setNbError(copyOfi);
						mutateEntities(WorldKind.ERROR, StudentOrCorrection.ERROR);
						Vector<World> errorWorld = commonErrors.get(copyOfi);
						for(World ew : errorWorld) {
							for(Entity ent : ew.getEntities()) {
								Game.JAVA.runEntity(ent, progress, getGame().i18n);
							}
							ew.setErrorWorld();
						}
						/* Try to write all files for next time */
						/*if (errorWorld.get(0).haveIO()) { //TODO: Bientôt de retour, un peu de patience...
							int rank = 0;
							for (World ew:errorWorld) {
								String name = "src/"+worldFileName+"-error"+(rank++);
								name = name.replaceAll("\\.", "/") + ".map";
								if (new File(name).getParentFile().canWrite()) {
									try {
										ew.writeToFile(new File(name));
									} catch (Exception e) {
										System.err.println(i18n.tr("Error while writing error world of {0}:",name));
										e.printStackTrace();
									}
								} else {
									System.err.println(i18n.tr("Cannot write error world of {0}. Please check the permissions.",name));
								}
							}
						}*/
						getGame().statusArgRemove(getClass().getSimpleName());
					}
				};
				t.setUncaughtExceptionHandler(h);
				Game.addInitThread(t);
				t.start();
				try {
					t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected void computeAnswer() {
		final String id = this.getId();
		Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread th, Throwable ex) {
				if(ex instanceof PLMEntityNotFound) {
					getLesson().setLoadingOutcomeState(LoadingOutcome.FAIL);
				}
				System.err.println("Uncaught exception while computing answer: " + ex);
			}
		};
		Thread t = new Thread() {
			@Override
			public void run() {
				getGame().statusArgAdd(getClass().getSimpleName());
				boolean allFound = true;
				if (answerWorld.get(0).haveIO()) {
					if (Game.getProperty(Game.PROP_ANSWER_CACHE, "true",true).equalsIgnoreCase("true")) {
						Vector<World> newAnswer = new Vector<World>();
						int rank = 0;
						for (World aw:answerWorld) {
							String name = worldFileName+"-answer"+(rank++);
							try {
								World nw = aw.readFromFile(name);
								nw.setAnswerWorld();
								newAnswer.add(nw);
							} catch (BrokenWorldFileException bwfe) {
								System.err.println(getGame().i18n.tr("World {0} is broken ({1}). Recompute all answer worlds.",name,bwfe.getLocalizedMessage()) );
								allFound = false;
								break;
							} catch (FileNotFoundException fnf) {
								System.err.println(getGame().i18n.tr("Cache file {0} is missing. Recompute all answer worlds.",name,fnf.getLocalizedMessage()));
								allFound = false;
								break;
							} catch (IOException ioe) {
								System.err.println(getGame().i18n.tr("IO exception while reading world {0} ({1}). Recompute all answer worlds.",name,ioe.getLocalizedMessage()));
								allFound = false;
								break;
							}
						}
						if (allFound) {
							answerWorld = newAnswer;
							getGame().statusArgRemove(getClass().getSimpleName());
							return;
						}
					} else {
						getGame().getLogger().log(getGame().i18n.tr("Recompute the answer of {0} despite the cache file, as requested by the property {1}",worldFileName,Game.PROP_ANSWER_CACHE));
					}
				}

				/* I/O didn't work. We have to load the files manually */
				ExecutionProgress progress = new ExecutionProgress(getGame().getProgrammingLanguage());

				// In all language but C, the correction is either directly usable (interpreted) or already compiled in the jarfile
				if(getGame().getProgrammingLanguage().equals(Game.C)){
					try {
						//TODO BAT remove if bat will be implemented in C
						if(!id.contains("bat.string1.lessons.bat") && !id.contains("welcome.lessons.welcome.bat") && ! id.contains("welcome.lessons.welcome.array"))
							compileAll(getGame().getLogger(), StudentOrCorrection.CORRECTION);
					} catch (PLMCompilerException e) {
						System.err.println("Severe error: the correction of exercise "+id+" cannot be compiled in C. Please go fix your PLM.");
						e.printStackTrace();
						getGame().setState(Game.GameState.COMPILATION_ENDED);
						getGame().setState(Game.GameState.EXECUTION_ENDED);
					}
				}
				setNbError(-1);
				mutateEntities(WorldKind.ANSWER, StudentOrCorrection.CORRECTION);

				for (World aw : answerWorld) {
					for (Entity ent: aw.getEntities()) {
						ent.setScript(Game.C, id);
						getGame().getProgrammingLanguage().runEntity(ent,progress, getGame().i18n);
					}
					aw.setAnswerWorld();
				}

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
								System.err.println(getGame().i18n.tr("Error while writing answer world of {0}:",name));
								e.printStackTrace();
							}
						} else {
							System.err.println(getGame().i18n.tr("Cannot write answer world of {0}. Please check the permissions.",name));
						}
					}
				}
				getGame().statusArgRemove(getClass().getSimpleName());
			}
		};
		t.setUncaughtExceptionHandler(h);
		Game.addInitThread(t);
		t.start();
	}

	@Override
	public void run(List<Thread> runnerVect){
		if (lastResult == null)
			lastResult = new ExecutionProgress(getGame().getProgrammingLanguage());
		setNbError(-1);
		mutateEntities(WorldKind.CURRENT, StudentOrCorrection.STUDENT);

		for (World cw: getWorlds(WorldKind.CURRENT)) {
			cw.doDelay();
			cw.runEntities(runnerVect, lastResult);
		}
	}

	@Override
	public void runDemo(List<Thread> runnerVect){
		ExecutionProgress ignored = new ExecutionProgress(getGame().getProgrammingLanguage());

		for (int i=0; i<initialWorld.size(); i++) {
			answerWorld.get(i).reset(initialWorld.get(i));
			answerWorld.get(i).doDelay();
		}
		setNbError(-1);
		mutateEntities(WorldKind.ANSWER, StudentOrCorrection.CORRECTION);

		for (World aw:getWorlds(WorldKind.ANSWER))
			aw.runEntities(runnerVect,ignored);
	}
}
