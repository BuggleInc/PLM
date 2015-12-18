package plm.core.model.lesson;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.json.simple.JSONObject;
import org.xnap.commons.i18n.I18n;

import plm.core.PLMCompilerException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.LogHandler;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.session.SourceFile;
import plm.core.model.session.TemplatedSourceFileFactory;
import plm.core.utils.FileUtils;

public class ExerciseFactory {

	private ExerciseRunner exerciseRunner;
	private ProgrammingLanguage[] programmingLanguages;
	private Locale[] humanLanguages;
	private TemplatedSourceFileFactory sourceFileFactory;

	private String rootDirectory = "exercises";
	
	public ExerciseFactory(LogHandler logger, I18n i18n, ExerciseRunner exerciseRunner, ProgrammingLanguage[] programmingLanguages, Locale[] humanLanguages) {
		this.exerciseRunner = exerciseRunner;
		this.programmingLanguages = programmingLanguages;
		this.humanLanguages = humanLanguages;
		this.sourceFileFactory = new TemplatedSourceFileFactory(logger, i18n);
	}

	public Exercise cloneExercise(Exercise exo) {
		return new BlankExercise(exo);
	}

	public Exercise exerciseFromJson(JSONObject json) {
		return new BlankExercise(json);
	}

	public void initializeExercise(Exercise exo, ProgrammingLanguage progLang) {
		if(exo instanceof ExerciseTemplatingEntity) {
			ExerciseTemplatingEntity ete = (ExerciseTemplatingEntity) exo;
			ete.initSourceFiles(sourceFileFactory, programmingLanguages);
		}
		else {
			computeSupportedProgrammingLanguages(exo);
		}
		computeMissions(exo);
		computeAnswer(exo, progLang);
	}

	public void computeSupportedProgrammingLanguages(Exercise exo) {
		for(ProgrammingLanguage progLang: programmingLanguages) {
			String entityName = progLang.nameOfCorrectionEntity(exo).replaceAll("\\.", "/") + "." + progLang.getExt();
			String entityPath = rootDirectory + "/" + entityName;
			if(new File(entityPath).exists()) {
				exo.addProgLanguage(progLang);
				SourceFile sourceFile = sourceFileFactory.newSourceFromFile(exo.getId(), progLang, entityPath);
				exo.addDefaultSourceFile(progLang, sourceFile);
			}
		}
	}

	public void computeMissions(Exercise exo) {
		for(Locale humanLanguage: humanLanguages) {
			String baseName = exo.getBaseName().replaceAll("\\.", "/");
			String filename =  rootDirectory + "/" + baseName;
			StringBuffer sb = null;
			try {
				sb = FileUtils.readContentAsText(filename, humanLanguage, "html", true);
				exo.addMission(humanLanguage.getLanguage(), sb.toString());
			} catch (IOException ex) {}			
		}
	}

	public void computeAnswer(Exercise exo, ProgrammingLanguage progLang) {
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