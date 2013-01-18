package jlm.core.model;

/**
 * This enum describes the state of the interface: whether we are running some student code, 
 * loading or whatever. 
 * 
 * It is used extensively in {@link Game} to decide which buttons are enabled and other 
 * such consideration.
 */
public enum GameState {
	IDLE, 
	SAVING, SAVING_DONE,
	LOADING, LOADING_DONE,
	COMPILATION_STARTED, COMPILATION_ENDED, 
	EXECUTION_STARTED, EXECUTION_ENDED,
	DEMO_STARTED, DEMO_ENDED,
}
