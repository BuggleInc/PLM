package jlm.lesson;

import java.util.Map;


public class RevertableSourceFile extends SourceFile {

	private String initialBody; 
	
	public RevertableSourceFile(String name, String initialBody) {
		this(name, initialBody, null, null);
	}

	public RevertableSourceFile(String name, String initialBody, String template) {
		this(name, initialBody, template, null);
	}

	public RevertableSourceFile(String name, String initialBody, String template, Map<String, String> patterns) {
		super(name, initialBody, template, patterns);
		this.initialBody = initialBody;
	}
	
	public void revert() {
		setBody(this.initialBody);
	}

	public boolean hasChanged() {
		return (! this.initialBody.equals(getBody()));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((initialBody == null) ? 0 : initialBody.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RevertableSourceFile other = (RevertableSourceFile) obj;
		if (initialBody == null) {
			if (other.initialBody != null)
				return false;
		} else if (!initialBody.equals(other.initialBody))
			return false;
		return true;
	}
}
