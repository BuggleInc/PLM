package plm.core.model.session;

import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import plm.core.log.Logger;
import plm.core.model.ToJSON;
import plm.core.model.lesson.Exercise.StudentOrCorrection;

public class SourceFile implements ToJSON {

	protected String name;
	private String template;
	private String body;
	private int offset;
	private String correction;
	private String error;

	public SourceFile(String name, String initialBody, String template, int _offset, String _correctionCtn, String _errorCtn) {
		this.name = name;
		this.body = initialBody;
		this.offset = _offset;
		this.correction = _correctionCtn;
		this.error = _errorCtn;
		setTemplate( template );
	}

	public SourceFile(JSONObject json) {
		this(
				(String) json.get("name"),
				(String) json.get("body"),
				(String) json.get("template"),
				((Long) json.get("offset")).intValue(),
				(String) json.get("correction"),
				(String) json.get("error")
		);
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();

		json.put("name", name);
		json.put("body", body);
		json.put("offset", offset);
		json.put("correction", correction);
		json.put("error", error);
		json.put("template", template);

		return json;
	}

	public SourceFile clone() {
		return new SourceFile(name, body, template, offset, correction, error);
	}

	public String getName() {
		return this.name;
	}

	public String getBody() {
		return this.body;
	}

	public void setBody(String text) {
		body = text.replaceAll("\\t", "  ");
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
					Logger.log("Damn! I integrated a pattern being more than one line long, line numbers will be wrong."
							+"Please repport this bug (alongside with the following informations) as it will help us fixing our issue #42!");
					Logger.log("pattern key: "+pattern.getKey());
					Logger.log("pattern value: "+pattern.getValue());
					// Logger.log("PLM version: "+Game.getProperty("plm.major.version","internal",false)+" ("+Game.getProperty("plm.major.version","internal",false)+"."+Game.getProperty("plm.minor.version","",false)+")");
					Logger.log("Java version: "+System.getProperty("java.version")+" (VM version: "+ System.getProperty("java.vm.version")+")");
					Logger.log("System: " +System.getProperty("os.name")+" (version: "+System.getProperty("os.version")+"; arch: "+ System.getProperty("os.arch")+")");
				}
			}
		return res.replaceAll("\\xa0", " "); // Kill those damn \160 chars, which are non-breaking spaces (got them from copy/pasting source examples?)
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
}
