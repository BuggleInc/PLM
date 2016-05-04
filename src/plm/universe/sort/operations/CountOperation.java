package plm.universe.sort.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty; ;

public class CountOperation extends SortOperation
{
	private int read;
	private int write;
	private int oldRead;
	private int oldWrite;

	@JsonCreator
	public CountOperation(@JsonProperty("read")int read, @JsonProperty("write")int write)
	{
		super("countOperation");
		this.read = read;
		this.write = write;
	}

	public int getRead() {
		return read;
	}

	public void setRead(int read) {
		this.read = read;
	}

	public int getWrite() {
		return write;
	}

	public void setWrite(int write) {
		this.write = write;
	}

	public int getOldRead() {
		return oldRead;
	}

	public void setOldRead(int oldRead) {
		this.oldRead = oldRead;
	}

	public int getOldWrite() {
		return oldWrite;
	}

	public void setOldWrite(int oldWrite) {
		this.oldWrite = oldWrite;
	}

}
