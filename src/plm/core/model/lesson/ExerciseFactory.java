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
	private final ExerciseRunner exerciseRunner;
	private final ProgrammingLanguage[] programmingLanguages;
	private final Locale[] humanLanguages;
	private final TemplatedSourceFileFactory sourceFileFactory;
	private final AbstractTipFactory tipsFactory;

	public ExerciseFactory(FileUtils fileUtils, Locale locale, ExerciseRunner exerciseRunner, ProgrammingLanguage[] programmingLanguages, Locale[] humanLanguages, AbstractTipFactory tipsFactory) {
		this.fileUtils = fileUtils;
		this.exerciseRunner = exerciseRunner;
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
		computeAnswer(exo, progLang);
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
			StringBuffer sb = null;
			try {
				sb = fileUtils.readContentAsText(filename, humanLanguage, "html", true);
				String mission = tipsFactory.computeTips(exo, humanLanguage, sb.toString());
				exo.addMission(humanLanguage.getLanguage(), mission);
			}
			catch (FileNotFoundException e) {
				Logger.info("No mission found for: " + exo.getId());
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
			StringBuffer sb = null;
			try {
				sb = fileUtils.readContentAsText(filename, humanLanguage, "html", true);
				exo.addHelp(humanLanguage.getLanguage(), sb.toString());
			}
			catch (FileNotFoundException e) {
				Logger.info("No help found for: " + exo.getId());
				exo.addHelp(humanLanguage.getLanguage(), "");
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void computeAnswer(Exercise exo, ProgrammingLanguage progLang) {
		if(exo.isProgLangSupported(progLang)) {
			SourceFile sf = exo.getDefaultSourceFile(progLang);
			try {
				exerciseRunner.mutateEntities(exo, sf, progLang, StudentOrCorrection.CORRECTION, null);
			} catch (PLMCompilerException e) {
				e.printStackTrace();
			}
			exerciseRunner.runDemo(exo, progLang);
		} else {
			// TODO: Handle case if progLang is not supported
			Logger.error("Exercise "+exo.getId()+" does not support "+progLang.getLang()+". Exercise left uninitialized.");
		}
	}
}
