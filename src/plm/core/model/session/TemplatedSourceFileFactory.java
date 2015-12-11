package plm.core.model.session;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xnap.commons.i18n.I18n;

import plm.core.lang.LangBlockly;
import plm.core.lang.LangC;
import plm.core.lang.LangPython;
import plm.core.lang.LangScala;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.LogHandler;
import plm.core.model.lesson.NoSuchEntityException;
import plm.core.utils.FileUtils;

public class TemplatedSourceFileFactory {

	private LogHandler logger;
	private I18n i18n;
	
	public TemplatedSourceFileFactory(LogHandler logger, I18n i18n) {
		this.logger = logger;
		this.i18n = i18n;
	}
	
	public SourceFile newSourceFromParams(String name, String initialBody, String template, int _offset, String _correctionCtn, String _errorCtn) {
		return new SourceFile(null, name, initialBody, template, _offset, _correctionCtn, _errorCtn);
	}

	public SourceFile newSourceFromFile(String name, ProgrammingLanguage lang, String filename) throws NoSuchEntityException {
		String patternString = "";
		String shownFilename =  filename.replaceAll("\\.", "/")+"."+lang.getExt();
		StringBuffer sb = null;
		try {
			sb = FileUtils.readContentAsText(filename, null, lang.getExt(), false);
		} catch (IOException ex) {
			throw new NoSuchEntityException(i18n.tr("Source file {0}.{1} not found.",filename.replaceAll("\\.","/"),lang.getExt()));			
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
					logger.log(i18n.tr("{0}: BEGIN TEMPLATE within the template. Please fix your entity.",shownFilename));
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
					logger.log(i18n.tr("{0}: BEGIN SOLUTION is closed with END TEMPLATE. Please fix your entity.",shownFilename));
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
						logger.log(i18n.tr("{0}: END TEMPLATE with no matching BEGIN TEMPLATE. Please fix your entity.",shownFilename));
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
				correction.append(line+"\n");
				if (line.contains("BEGIN SKEL")) {
					savedState = state;
					state = 6; 

				} else {
					if (line.contains("END TEMPLATE"))  
						if (!seenTemplate)
							logger.log(i18n.tr("{0}: END TEMPLATE with no matching BEGIN TEMPLATE. Please fix your entity.",shownFilename));
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
				throw new RuntimeException(i18n.tr("Parser error in file {0}. This is a parser bug (state={1}), please report.",filename,state));	
			}
		}
		if (state == 3) {
			if (seenTemplate)
				logger.log(i18n.tr("{0}: End of file unexpected after the solution but within the template. Please fix your entity.",shownFilename,state));
		} else if (state != 4)
			logger.log(i18n.tr("{0}: End of file unexpected (state: {1}). Did you forget to close your template or solution? Please fix your entity.",shownFilename,state));

		String initialContent = templateHead.toString() + templateTail.toString();
		String skelContent;
		String headContent;
		if (lang instanceof LangPython || lang instanceof LangScala || lang instanceof LangC || lang instanceof LangBlockly) { 
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

					//if (getGame().isDebugEnabled())
					//	logger.log("Replace all "+parts[1]+" to "+parts[2]);
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
			return new SourceFile(null, name, initialContent, skelContent,offset,
					/* correction: */ templateHead.toString()+solution.toString()+templateTail.toString(),"Error");
		} else {
			return new SourceFile(null, name, initialContent, template,offset,
					correction.toString().replaceAll("SimpleBuggle","AbstractBuggle"),"Error"); // We don't want to have little dialogs when testing
		}
	}
	
}
