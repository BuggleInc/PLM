package plm.core.model.session;

import java.util.Map;
import java.util.Map.Entry;

import plm.core.model.Game;
import plm.core.model.LogHandler;
import plm.core.model.lesson.Exercise.StudentOrCorrection;

public class SourceFile {

	protected String name;
	private String template;
	private String body;
	private int offset;
	private String correction;
	private String error;
	private ISourceFileListener listener = null;
	private Game game;

	public SourceFile(Game game, String name, String initialBody, String template, int _offset, String _correctionCtn, String _errorCtn) {
		this.game = game;
		this.name = name;
		this.body = initialBody;
		this.offset = _offset;
		this.correction = _correctionCtn;
		this.error = _errorCtn;
		setTemplate( template );
	}

	public String getName() {
		return this.name;
	}

	public String getBody() {
		return this.body;
	}

	public void setBody(String text) { 
		if (getGame().getProgrammingLanguage().equals(Game.PYTHON))
			body = text.replaceAll("\\t", "    ");
		else
			body = text;
		notifyListener();
	}
	public void setTemplate(String string) {
		this.template = string;
	}
	public String getTemplate() {
		return template;
	}
	public void setCorrection(String c) {
		this.correction = c;
	}
	public String getCorrection() {
		return this.correction;
	}
	public void setError(String e) {
		this.error = e;
	}
	public String getError() {
		return this.error;
	}

	public String getCompilableContent(StudentOrCorrection whatToRetrieve) {
		return getCompilableContent(null,whatToRetrieve);
	}

	/**
	 * Returns the source text that we should compile
	 * @param runtimePatterns 
	 * 			some last-minute replacement to do (such as package name adjustment)
	 * @param whatKind
	 * 			whether we want to retrieve the student-provided content or the correction
	 * @return
	 */
	public String getCompilableContent(Map<String, String> runtimePatterns, StudentOrCorrection whatToRetrieve) {
		String res;

		if (whatToRetrieve == StudentOrCorrection.CORRECTION) {
			res = correction;
		} else if(whatToRetrieve == StudentOrCorrection.ERROR) {
			res = error;
		} else if (template != null) {
			res = template.replaceAll("\\$body", this.body+" \n");;			
		} else {
			res = this.body;
		}
		if (runtimePatterns != null)
			for (Entry<String, String> pattern : runtimePatterns.entrySet()) {
				res = res.replaceAll(pattern.getKey(), pattern.getValue());
				// This is a trap to find issue #42 that I fail to reproduce
				if (pattern.getValue().contains("\n")) { 
					getGame().getLogger().log(LogHandler.ERROR, "Damn! I integrated a pattern being more than one line long, line numbers will be wrong."
							+"Please repport this bug (alongside with the following informations) as it will help us fixing our issue #42!");
					getGame().getLogger().log(LogHandler.ERROR, "pattern key: "+pattern.getKey());
					getGame().getLogger().log(LogHandler.ERROR, "pattern value: "+pattern.getValue());
					getGame().getLogger().log(LogHandler.ERROR, "Exercise: "+game.getCurrentLesson().getCurrentExercise().getName());
					getGame().getLogger().log(LogHandler.ERROR, "PLM version: "+Game.getProperty("plm.major.version","internal",false)+" ("+Game.getProperty("plm.major.version","internal",false)+"."+Game.getProperty("plm.minor.version","",false)+")");
					getGame().getLogger().log(LogHandler.ERROR, "Java version: "+System.getProperty("java.version")+" (VM version: "+ System.getProperty("java.vm.version")+")");
					getGame().getLogger().log(LogHandler.ERROR, "System: " +System.getProperty("os.name")+" (version: "+System.getProperty("os.version")+"; arch: "+ System.getProperty("os.arch")+")");
				}
			}
		return res.replaceAll("\\xa0", " "); // Kill those damn \160 chars, which are non-breaking spaces (got them from copy/pasting source examples?)
	}

	public void setListener(ISourceFileListener l) {
		this.listener = l;
	}

	public void removeListener() {
		this.listener = null;
	}

	public void notifyListener() {
		if (this.listener != null)
			this.listener.sourceFileContentHasChanged();
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((body == null) ? 0 : body.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SourceFile other = (SourceFile) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		return true;
	}

	public int getOffset() {
		return offset;
	}
	
	public Game getGame() {
		return game;
	}
}
