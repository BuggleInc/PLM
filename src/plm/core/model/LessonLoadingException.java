package plm.core.model;

public class LessonLoadingException extends Exception {
	private static final long serialVersionUID = 1L;

	public LessonLoadingException(String msg) {
		super(msg);
	}

	public LessonLoadingException(String msg, Throwable t) {
		super(msg,t);
	}
}
