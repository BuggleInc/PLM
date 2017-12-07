package plm.universe.bat;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.script.ScriptEngine;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import plm.core.lang.LangBlockly;
import plm.core.lang.LangPython;
import plm.core.lang.ProgrammingLanguage;
import plm.core.utils.FileUtils;
import plm.universe.SVGOperation;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorldView;

public class BatWorld extends World {

	@JsonProperty("batTests")
	public List<BatTest> tests = new Vector<BatTest>();

	public BatWorld(FileUtils fileUtils, String funName) {
		super(fileUtils, funName);
		
		addEntity(new BatEntity(funName));
	}
	public BatWorld(BatWorld w2) {
		super(w2);
		this.tests = new Vector<BatTest>();
		for (BatTest t:w2.tests) 
			tests.add(t.copy());
	}

	@JsonCreator
	public BatWorld(FileUtils fileUtils, @JsonProperty("name")String name, @JsonProperty("batTests")Vector<BatTest> tests) {
		super(fileUtils, name);
		this.tests = tests;
	}

	@Override
	public void reset(World w) {
		BatWorld anotherWorld = (BatWorld) w;
		this.tests = new Vector<BatTest>();
		for (BatTest t:anotherWorld.tests) 
			tests.add(t.copy());
		super.reset(anotherWorld);		
	}


	protected List<SVGOperation> draw() {


			String svg = BatWorldView.draw(this,200,200);
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(this.getName()+".svg", "UTF-8");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			writer.write(svg);
			writer.close();
			List<SVGOperation> list = new ArrayList<SVGOperation>();
			SVGOperation operation = new SVGOperation(svg);
			list.add(operation);

			return list;
		}


	@Override
	public boolean equals(Object o){
		if (!(o instanceof BatWorld)) {
			return false;
		}
		BatWorld other = (BatWorld) o;
		if (other.tests.size() != tests.size()) {
			//Logger.log("Amount of tests differ between worlds: "+tests.size()+" != "+other.tests.size());
			return false;
		}
		for (int i=0;i<tests.size();i++)
			if (!tests.get(i).equals(other.tests.get(i))) {
				//throw new RuntimeException("Test "+i+" differs: "+tests.get(i)+" != "+other.tests.get(i));
				return false;
			}
		return true;
	}

	/* So that the view can display them */
	public List<BatTest> getTests() {
		return tests;
	}

	/* World logic */
	public void addTest(boolean visible, Object...params) {
		tests.add(new BatTest(getName(),visible, params));
	}
	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine e) {
		if (lang instanceof LangPython || lang instanceof LangBlockly) {
			e.put("batTests", tests);
		}
	}

	@Override
	public String diffTo(World w) {
		BatWorld other = (BatWorld) w;
		StringBuffer sb = new StringBuffer();
		boolean foundError = false;
		for (int i=0;i<tests.size();i++) {
			if (foundError && !tests.get(i).isVisible()) 
				return sb.toString();
					
			if (!tests.get(i).equals(other.tests.get(i))) { 
				sb.append(other.tests.get(i).getName(getProgLang())+" returned "+other.tests.get(i).getResult(getProgLang())+
						                               " while "+         tests.get(i).getResult(getProgLang())+" were expected.\n");
				foundError = true;
			}
		}
		return sb.toString();
	}
}
