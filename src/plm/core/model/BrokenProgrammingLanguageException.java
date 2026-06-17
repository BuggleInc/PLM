package plm.core.model;

public class BrokenProgrammingLanguageException extends Exception {
	private static final long serialVersionUID = 1L;
	public String title;

	public BrokenProgrammingLanguageException(String title, String msg) {
		super(msg);
		title = title;
	}
}
