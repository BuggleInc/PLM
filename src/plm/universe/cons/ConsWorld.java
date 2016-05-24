package plm.universe.cons;

import java.util.Vector;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import plm.core.lang.LangPython;
import plm.core.lang.ProgrammingLanguage;
import plm.core.log.Logger;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class ConsWorld extends BatWorld {
	public ConsWorld(ConsWorld other) {
		super(other);
	}
	public ConsWorld(String funName) {
		super(funName);
	}

	@JsonCreator
	public ConsWorld(@JsonProperty("name")String name, @JsonProperty("batTests")Vector<BatTest> tests) {
		super(name, tests);
	}

	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine e) {
		super.setupBindings(lang, e);
		if (lang instanceof LangPython) {
			try {
				e.put("RecList", RecList.class);
				String script = 
						/*
						"class RecList:\n"+
						"  def __init__(self, head, tail):\n"+
						"    self.head = head\n"+
						"    self.tail = tail\n"+
						"  def __str__(self):\n"+
						"    res = '['\n"+
						"    ptr = self\n"+
						"    first = True\n"+
						"    while ptr != None:\n"+
						"      if first:\n"+
						"         first = False\n"+
						"      else:\n"+
						"         res += ', '\n"+
						"      res += str(ptr.head)\n"+
						"      ptr = ptr.tail\n"+
						"    res += ']'\n"+
						"    return res\n"+
						"\n"+
						"def RecListToArray(l):\n"+
						"  res = java.util.Vector()\n"+
						"  ptr = l\n"+
						"  while ptr != None:\n"+
						"    res.add(ptr.head)\n"+
						"    ptr = ptr.tail\n"+
						"  return res.toArray(ExampleArrayInt)\n"+
						"def RecListFromArray(a,rank=0):\n"+
						"  if len(a) <= rank:\n"+
						"     return None\n"+
						"  return RecList(a.get(rank),RecListFromArray(a,rank+1))\n"+
						"\n"+*/
						"def cons(a,b):\n"+
						"  return RecList(a,b)\n";
				Logger.log("Extra script chunk added by "+getClass()+":\n"+script);
				e.eval(script);
			} catch (ScriptException e1) {
				e1.printStackTrace();
			}
		}
	}


}
