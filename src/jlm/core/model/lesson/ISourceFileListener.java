package jlm.core.model.lesson;

public interface ISourceFileListener {

	public void sourceFileContentHasChanged() ;

	public void clear(); /* before removing it */
	
}
