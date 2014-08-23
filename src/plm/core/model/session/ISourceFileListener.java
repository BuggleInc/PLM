package plm.core.model.session;

public interface ISourceFileListener {

	public void sourceFileContentHasChanged() ;

	public void clear(); /* before removing it */
	
}
