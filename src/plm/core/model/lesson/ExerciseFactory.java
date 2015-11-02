package plm.core.model.lesson;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.xnap.commons.i18n.I18n;

import plm.core.PLMCompilerException;
import plm.core.lang.LangJava;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.LogHandler;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.session.SourceFile;
import plm.core.model.session.TemplatedSourceFileFactory;
import plm.core.utils.FileUtils;

public class ExerciseFactory {

	private LogHandler logger;
	private I18n i18n;
	private ExerciseRunner exerciseRunner;
	private ProgrammingLanguage[] programmingLanguages;
	private Locale[] humanLanguages;

	public ExerciseFactory(LogHandler logger, I18n i18n, ExerciseRunner exerciseRunner, ProgrammingLanguage[] programmingLanguages, Locale[] humanLanguages) {
		this.logger = logger;
		this.i18n = i18n;
		this.exerciseRunner = exerciseRunner;
		this.programmingLanguages = programmingLanguages;
		this.humanLanguages = humanLanguages;
	}

	public void initializeExercise(Exercise exo, LangJava progLang) {
		computeSupportedProgrammingLanguages(exo);
		computeMissions(exo);
		computeAnswer(exo, progLang);
	}

	public void computeSupportedProgrammingLanguages(Exercise exo) {
		for(ProgrammingLanguage progLang: programmingLanguages) {
			String entityName = progLang.nameOfCorrectionEntity(exo);
			String entityPath = "exercises/" + entityName.replaceAll("\\.", "/")  + "." + progLang.getExt();
			if(new File(entityPath).exists()) {
				exo.addProgLanguage(progLang);
				TemplatedSourceFileFactory sourceFileFactory = new TemplatedSourceFileFactory(logger, i18n);
				SourceFile sourceFile = sourceFileFactory.newSourceFromFile(exo.getId(), progLang, entityPath);
				exo.addDefaultSourceFile(progLang, sourceFile);
			}
		}
	}

	public void computeMissions(Exercise exo) {
		for(Locale humanLanguage: humanLanguages) {
			String filename = "exercises/environment/" + exo.getId().replace('.',File.separatorChar);
			StringBuffer sb = null;
			try {
				sb = FileUtils.readContentAsText(filename, humanLanguage, "html",true);
				exo.addMission(humanLanguage.getLanguage(), sb.toString());
			} catch (IOException ex) {}			
		}
	}

	public void computeAnswer(Exercise exo, LangJava progLang) {
		// TODO: Handle case if progLang is not supported
		if(exo.isProgLangSupported(progLang)) {
			SourceFile sf = exo.getDefaultSourceFile(progLang);
			try {
				exerciseRunner.mutateEntities(exo, sf, progLang, StudentOrCorrection.CORRECTION);
			} catch (PLMCompilerException e) {
				e.printStackTrace();
			}
			exerciseRunner.runDemo(exo, progLang);
		}
	}
}