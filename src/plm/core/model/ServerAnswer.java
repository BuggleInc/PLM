package plm.core.model;

/**
 * Enum that contains all answers that the server can send (errors or success messages)
 */
public enum ServerAnswer {
	ALL_IS_FINE, WRONG_PASSWORD, WRONG_TEACHER_PASSWORD, COURSE_NAME_ALREADY_USED, DATA_NOT_IN_DATABASE
}
