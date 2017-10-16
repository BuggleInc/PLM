package plm.core.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import plm.core.PLMCompilerException;
import plm.core.PLMEntityNotFound;
import plm.core.model.I18nManager;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.session.SourceFile;
import plm.universe.Entity;

public abstract class JVMCompiledLang extends ProgrammingLanguage {
	protected final ClassLoader applicationClassLoader;

	public JVMCompiledLang(ClassLoader applicationClassLoader, String lang, String ext, boolean isDebugEnabled) {
		super(lang, ext, isDebugEnabled);
		this.applicationClassLoader = applicationClassLoader;
	}
	/* to make sure that the subsequent version of the same class have different names, in order to bypass the cache of the class loader */
	protected static final String packageNamePrefix = "plm.runtime";
	protected int packageNameSuffix = 0;
	protected String packageName(){
		return packageNamePrefix + packageNameSuffix;
	}
	protected String className(String name) {
		return packageName() + "." + name;
	}

	protected abstract Entity mutateEntity(String newClassName) throws InstantiationException, IllegalAccessException, ClassNotFoundException;

	public ArrayList<Entity> mutateEntities(String newClassName, SourceFile sourceFile, StudentOrCorrection whatToMutate, List<Entity> olds) throws PLMCompilerException {
		ArrayList<Entity> newEntities = new ArrayList<Entity>();
		for (Entity old : olds) {
			/* Instantiate a new entity of the new type */
			Entity ent = null;
			try {
				ent = mutateEntity(newClassName);
			} catch (InstantiationException e) {
				throw new RuntimeException("Cannot instanciate entity of type "+className(newClassName), e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Illegal access while instanciating entity of type "+className(newClassName), e);
			} catch (NullPointerException e) {
				throw new PLMEntityNotFound("Cannot find an entity of name "+className(newClassName)+" or "+newClassName+". Broken lesson.", e);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			/* change fields of new entity to copy old one */
			ent.copy(old);
			ent.initDone();
			/* Add new entity to the to be returned entities set */
			newEntities.add(ent);
		}
		return newEntities;
	}

	@Override
	public void runEntity(Entity ent, ExecutionProgress progress, Locale locale) {
		try {
			ent.run();
		} catch (Exception e) {
			String msg = I18nManager.getI18n(locale).tr("The execution of your program raised a {0} exception: {1}\n" + 
					" Please fix your code.\n",e.getClass().getName(),e.getLocalizedMessage());

			for (StackTraceElement elm : e.getStackTrace())
				msg+= "   at "+elm.getClassName()+"."+elm.getMethodName()+" ("+elm.getFileName()+":"+elm.getLineNumber()+")"+"\n";

			System.err.println(msg);
			progress.setExecutionError(msg);
			e.printStackTrace();
		}
	}
}
