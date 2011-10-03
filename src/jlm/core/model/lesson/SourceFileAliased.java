package jlm.core.model.lesson;

import java.util.Map;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;

/* This class uses lazy initialization because we cannot retrieve and traverse the list 
 * of lessons while initializing the exercise because that would let the Game.getInstance()
 * mechanism loop.
 */

public class SourceFileAliased extends SourceFile {

	private SourceFile aliased = null;
	private String lesson;
	private String exercise;
	private String file;
	private ProgrammingLanguage lang;

	public SourceFileAliased(ProgrammingLanguage lang, String lesson, String exercise, String file) {
		super(file, "", null, null);
		this.lesson = lesson;
		this.exercise = exercise;
		this.file = file;
		this.lang = lang;
	}

	private void searchAliased() { /* Check the headers to see why we lazy initialize this way */
		for (Lesson l : Game.getInstance().getLessons()) {
			if (l.getClass().getName().equals(lesson)) {
				for (int eCount=0; eCount<l.getExerciseCount();eCount++) {
					Lecture lect = l.getExercise(eCount);
					if (lect instanceof Exercise) {
						Exercise e = (Exercise) lect;
						if (e.getClass().getName().equals(exercise)) {
							for (SourceFile sf : e.getSourceFiles(lang)) {
								System.out.println("Seen file "+sf.getName() +" (searching for "+file+")");
								if (sf.getName().equals(file)) {
									/* Found it, cool */
									aliased=sf;
									name=sf.name;
									return;
								}
							}
							throw new RuntimeException("No file "+file+" found in exercise "+exercise+" of lesson "+lesson+". Cannot find the aliased file");				
						}
					} else {
						throw new RuntimeException("Exercise "+exercise+" in lesson "+lesson+" is a lecture. Cannot find the aliased file");										
					}
				}
				throw new RuntimeException("No exercise "+exercise+" found in lesson "+lesson+". Cannot find the aliased file");				
			}
		}
		throw new RuntimeException("No lesson "+lesson+" found. Cannot find the aliased file");		
	}
	
	/* Methods of SourceFile */
	@Override
	public String getName() {
		if (aliased==null)
			searchAliased();
		return aliased.getName();
	}

	@Override
	public String getBody() {
		if (aliased==null)
			searchAliased();
		return aliased.getBody();
	}

	@Override
	public void setBody(String text) {
		if (aliased==null)
			searchAliased();
		aliased.setBody(text);
	}

	@Override
	public String getCompilableContent() {
		if (aliased==null)
			searchAliased();
		return aliased.getCompilableContent();
	}

	@Override
	public String getCompilableContent(Map<String, String> runtimePatterns) {
		if (aliased==null)
			searchAliased();
		return aliased.getCompilableContent(runtimePatterns);
	}
	
	@Override
	public int hashCode() {
		if (aliased==null)
			searchAliased();
		return aliased.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (aliased==null)
			searchAliased();
		return aliased.equals(obj);
	}
}
