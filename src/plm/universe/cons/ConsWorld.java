package plm.universe.cons;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.universe.bat.BatWorld;

public class ConsWorld extends BatWorld {
	public ConsWorld(ConsWorld other) {
		super(other);
	}
	public ConsWorld(Game game, String funName) {
		super(game, funName);
	}

	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine e) {
		super.setupBindings(lang, e);
		if (lang == Game.PYTHON) {
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
				if (getGame().isDebugEnabled()) 
					getGame().getLogger().log("Extra script chunk added by "+getClass()+":\n"+script);
				e.eval(script);
			} catch (ScriptException e1) {
				e1.printStackTrace();
			}
		}
	}


}
