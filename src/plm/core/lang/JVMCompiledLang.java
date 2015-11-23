package plm.core.lang;

import java.util.ArrayList;
import java.util.List;

import org.xnap.commons.i18n.I18n;

import plm.core.PLMCompilerException;
import plm.core.PLMEntityNotFound;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.universe.Entity;

public abstract class JVMCompiledLang extends ProgrammingLanguage {
	public JVMCompiledLang(String lang, String ext, boolean isDebugEnabled) {
		super(lang, ext, isDebugEnabled);
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
	@Override
	public ArrayList<Entity> mutateEntities(Exercise exo, List<Entity> olds, StudentOrCorrection whatToMutate, I18n i18n, int nbError) throws PLMCompilerException {
		//String newClassName = (whatToMutate == StudentOrCorrection.STUDENT ? exo.getTabName() : nameOfCorrectionEntity(exo));
		String newClassName = "";
		switch(whatToMutate) {
		case STUDENT: newClassName = exo.getTabName(); break;
		case CORRECTION: newClassName = nameOfCorrectionEntity(exo); break;
		case ERROR: newClassName = nameOfCommonError(exo, nbError); break;
		}
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
				/* this kind of entity was not written by student. try to get it from default class loader, or complain if it also fails */
				try {
					ent = (Entity)getClass().getClassLoader().loadClass(newClassName).newInstance(); 
				} catch (Exception e2) {
					if (whatToMutate == StudentOrCorrection.STUDENT) {
						/* FIXME: need to pass the current programming language as parameter
						if (getGame().getProgrammingLanguage() == Game.SCALA)
							throw new PLMCompilerException(getGame().i18n.tr(
									  "Your entity failed to start. Did you forgot to put your code within a method?\n\n"
									+ "This problem often arises when the exercise expects you to put all the code within a \n"
									+ "method e.g. run(), but you put some statements (e.g. forward()) outside of any method.\n\n"
									+ "The easiest solution to sort it out is to copy all your code (Ctrl-A Ctrl-C), use the \n"
									+ "'Exercise/Revert' menu to reset the template, and paste (Ctrl-V) your code within the\n"
									+ "provided method."));
						else
						*/
						throw new PLMCompilerException(i18n.tr("Your entity failed to start. Your constructor seems to be broken, but I have no clue."));
					} else {
						throw new PLMEntityNotFound("Cannot find an entity of name "+className(newClassName)+" or "+newClassName+". Broken lesson.", e2);
					}
				}
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Cannot instanciate entity of type "+className(newClassName), e);
			}
			/* change fields of new entity to copy old one */
			ent.copy(old);
			ent.initDone();
			/* Add new entity to the to be returned entities set */
			newEntities.add(ent);

		}
		return newEntities;
	}

	public ArrayList<Entity> mutateEntities(String newClassName, List<Entity> olds) throws PLMCompilerException {
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
	public void runEntity(Entity ent, ExecutionProgress progress, I18n i18n) {
		try {
			ent.run();
		} catch (Exception e) {
			String msg = i18n.tr("The execution of your program raised a {0} exception: {1}\n" + 
					" Please fix your code.\n",e.getClass().getName(),e.getLocalizedMessage());

			for (StackTraceElement elm : e.getStackTrace())
				msg+= "   at "+elm.getClassName()+"."+elm.getMethodName()+" ("+elm.getFileName()+":"+elm.getLineNumber()+")"+"\n";

			System.err.println(msg);
			progress.setExecutionError(msg);
			e.printStackTrace();
		}
	}
}
