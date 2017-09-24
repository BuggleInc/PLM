package plm.judge;

import java.io.IOException;
import java.util.Locale;

import com.fasterxml.jackson.databind.JsonNode;

import plm.core.model.json.JSONUtils;
import plm.core.model.lesson.Exercise;

/**
 * Parses a JSON request message (in the form of a {@link String}) into 
 * @author Tanguy
 *
 */
public class RequestMsg {

	private Exercise exercise;
	private Locale locale;
	private String language;
	private String code;
	private String replyQueue;
	private String clientQueue;

	public RequestMsg(String message) {
		try {
			JsonNode json = JSONUtils.mapper.readTree(message);
			exercise = JSONUtils.jsonToExercise(json.get("exercise"));
			locale = new Locale(json.path("localization").asText());
			language = json.path("language").asText();
			code = json.path("code").asText();
			replyQueue = json.path("replyQueue").asText();
			clientQueue = json.path("clientQueue").asText();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Exercise getExercise() {
		return exercise;
	}

	/**
	 * Retrieves the messages' localization.
	 * @return the PLM-compliant natural language.
	 */
	public Locale getLocalization() {
		return locale;
	}

	/**
	 * Retrieves the messages' language.
	 * @return the PLM-compliant programming language.
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Retrieves the messages' code.
	 * @return the PLM-compliant code.
	 */
	public String getCode() {
		return code;
	}
	
	public String getReplyQueue() {
		return replyQueue;
	}

	public String getClientQueue() {
		return clientQueue;
	}
}
