package plm.core.model.lesson;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import org.xnap.commons.i18n.I18n;

import plm.core.PLMCompilerException;
import plm.core.lang.LangJava;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.LogHandler;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.core.model.session.SourceFile;
import plm.core.model.session.TemplatedSourceFileFactory;

public class ExerciseFactory {

	private LogHandler logger;
	private I18n i18n;
	private ExerciseRunner exerciseRunner;
	private ProgrammingLanguage[] programmingLanguages;

	public ExerciseFactory(LogHandler logger, I18n i18n, ExerciseRunner exerciseRunner, ProgrammingLanguage[] programmingLanguages) {
		this.logger = logger;
		this.i18n = i18n;
		this.exerciseRunner = exerciseRunner;
		this.programmingLanguages = programmingLanguages;
	}

	public void initializeExercise(Exercise exo, LangJava progLang) {
		computeSupportedProgrammingLanguages(exo);
		computeAnswer(exo, progLang);
	}

	public void computeSupportedProgrammingLanguages(Exercise exo) {
		for(ProgrammingLanguage progLang: programmingLanguages) {
			String entityName = progLang.nameOfCorrectionEntity(exo);
			String entityPath = "exercises/" + entityName.replaceAll("\\.", "/")  + "." + progLang.getExt();
			if(new File(entityPath).exists()) {
				TemplatedSourceFileFactory sourceFileFactory = new TemplatedSourceFileFactory(logger, i18n);
				SourceFile sourceFile = sourceFileFactory.newSourceFromFile(exo.getId(), progLang, entityPath);
				exo.addDefaultSourceFile(progLang, sourceFile);
			}
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