package plm.universe;

import java.util.Locale;

import plm.core.lang.ProgrammingLanguage;
import plm.core.log.Logger;
import plm.core.model.lesson.ExecutionProgress;

class EntityRunner extends Thread {
	public Entity entity;
	private Locale locale;
	private ExecutionProgress progress;
	private ProgrammingLanguage progLang;
	private boolean executing = true;
	
	public EntityRunner(Entity e, ExecutionProgress progress, ProgrammingLanguage progLang, Locale locale) {
		this.entity = e;
		this.progress = progress;
		this.locale = locale;
		this.progLang = progLang;
	}
	public boolean isExecuting() {
		return executing;
	}
	public void run() {
		this.entity.stepBegin.acquireUninterruptibly();
		progLang.runEntity(entity, progress, locale);
		//Logger.info("Entity "+entity.getName()+"@"+entity.getWorld().getName()+" in "+progLang+" terminating.");
		this.executing = false;
		entity.stepEnd.release();
	}

}
