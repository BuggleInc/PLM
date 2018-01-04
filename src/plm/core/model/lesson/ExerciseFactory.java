package plm.core.model.lesson;

import plm.core.PLMCompilerException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.log.Logger;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.tip.AbstractTipFactory;
import plm.core.model.session.SourceFile;
import plm.core.model.session.TemplatedSourceFileFactory;
import plm.core.utils.FileUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

public class ExerciseFactory {

	private final FileUtils fileUtils;
	private static ExerciseRunner exerciseRunner;
	private final ProgrammingLanguage[] programmingLanguages;
	private final Locale[] humanLanguages;
	private final TemplatedSourceFileFactory sourceFileFactory;
	private final AbstractTipFactory tipsFactory;

	public ExerciseFactory(FileUtils fileUtils, Locale locale, ExerciseRunner exerciseRunner_, ProgrammingLanguage[] programmingLanguages, Locale[] humanLanguages, AbstractTipFactory tipsFactory) {
		this.fileUtils = fileUtils;
		exerciseRunner = exerciseRunner_;
		this.programmingLanguages = programmingLanguages;
		this.humanLanguages = humanLanguages;
		this.sourceFileFactory = new TemplatedSourceFileFactory(fileUtils, locale);
		this.tipsFactory = tipsFactory;
	}

	public static Exercise cloneExercise(Exercise exo) {
		return new BlankExercise(exo);
	}

	public void initializeExercise(Exercise exo, ProgrammingLanguage progLang) {
		computeSupportedProgrammingLanguages(exo);
		computeMissions(exo);
		computeHelps(exo);
		//computeAnswer(exo); This is done lazily, on demand
	}

	private void computeSupportedProgrammingLanguages(Exercise exo) {
		for(ProgrammingLanguage progLang: programmingLanguages) {
			String entityName = progLang.nameOfCorrectionEntity(exo).replaceAll("\\.", "/");
			try {
				SourceFile sourceFile = sourceFileFactory.newSourceFromFile(exo.getId(), progLang, entityName);
				exo.addDefaultSourceFile(progLang, sourceFile);
			} catch (NoSuchEntityException e) {
				// do nothing
			}
		}
	}

	private void computeMissions(Exercise exo) {
		String filename = exo.getId().replaceAll("\\.", "/");
		for(Locale humanLanguage: humanLanguages) {
			try {
				StringBuffer sb = fileUtils.readContentAsText(filename, humanLanguage, "html", true);
				String mission = tipsFactory.computeTips(exo, humanLanguage, sb.toString());
				exo.addMission(humanLanguage.getLanguage(), mission);
			}
			catch (FileNotFoundException e) {
				//Logger.info("No mission found in "+humanLanguage.getDisplayName()+" for: " + exo.getId());
				exo.addMission(humanLanguage.getLanguage(), "");
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void computeHelps(Exercise exo) {
		String baseName = exo.getInitialWorlds().get(0).getClass().getName();
		String filename = baseName.replaceAll("\\.", "/");
		for(Locale humanLanguage: humanLanguages) {
			try {
				StringBuffer sb = fileUtils.readContentAsText(filename, humanLanguage, "html", true);
				exo.addHelp(humanLanguage.getLanguage(), sb.toString());
			}
			catch (FileNotFoundException e) {
				//Logger.info("No help found in "+humanLanguage.getDisplayName()+" for: " + exo.getId());
				exo.addHelp(humanLanguage.getLanguage(), "");
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static Exercise computeAnswer(Exercise exo) {
		if (exo.getAnswerWorlds().get(0).getSteps().size()>1) // We already have the answers
			return exo;
		
		if(exo.isProgLangSupported(ProgrammingLanguage.defaultProgLang)) {
			SourceFile sf = exo.getDefaultSourceFile(ProgrammingLanguage.defaultProgLang);
			try {
				exerciseRunner.mutateEntities(exo, sf, ProgrammingLanguage.defaultProgLang, StudentOrCorrection.CORRECTION, null);
			} catch (PLMCompilerException e) {
				e.printStackTrace();
			}
			exerciseRunner.runDemo(exo, ProgrammingLanguage.defaultProgLang);
		} else {
			// TODO: Handle case if progLang is not supported
			Logger.error("Exercise "+exo.getId()+" does not support "+ProgrammingLanguage.defaultProgLang.getLang()+". Exercise left uninitialized.");
		}
		//Logger.info("Computed answers of "+exo.getName()+" (size:"+exo.getAnswerWorlds().get(0).getSteps().size()+")");
		return exo;
	}
}
