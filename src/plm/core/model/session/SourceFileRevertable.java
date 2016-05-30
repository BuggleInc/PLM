package plm.core.model.session;

import plm.core.model.Game;

public class SourceFileRevertable extends SourceFile {

	private String initialBody; 
	
	public SourceFileRevertable(Game game, String name) {
		this(game, name, "", null, 0,"","");
	}

	public SourceFileRevertable(Game game, String name, String initialBody, String template, int offset, String correctionCtn, String errorCtn) {
		super(game, name, initialBody, template, offset,correctionCtn, errorCtn);
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
		SourceFileRevertable other = (SourceFileRevertable) obj;
		if (initialBody == null) {
			if (other.initialBody != null)
				return false;
		} else if (!initialBody.equals(other.initialBody))
			return false;
		return true;
	}
}
