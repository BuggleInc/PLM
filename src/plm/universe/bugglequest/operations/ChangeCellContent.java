package plm.universe.bugglequest.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeCellContent extends BuggleWorldCellOperation{

	private String oldContent;
	private String newContent;

	@JsonCreator
	public ChangeCellContent(@JsonProperty("x")int x, @JsonProperty("y")int y, @JsonProperty("oldContent")String oldContent, @JsonProperty("newContent")String newContent) {
		super("changeCellContent", x, y);
		this.oldContent = oldContent;
		this.newContent = newContent;
	}

	public String getOldContent() {
		return oldContent;
	}

	public String getNewContent() {
		return newContent;
	}
}