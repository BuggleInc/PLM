package lessons.recursion.cons.universe;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.ImageIcon;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.ui.ResourcesCache;
import plm.universe.bat.BatWorld;

public class ConsWorld extends BatWorld {
	public ConsWorld(ConsWorld other) {
		super(other);
	}
	public ConsWorld(String funName) {
		super(funName);
	}
	@Override
	public ImageIcon getIcon() {
		return ResourcesCache.getIcon("img/world_cons.png");
	}

	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine e) {
		super.setupBindings(lang, e);
		if (lang == Game.PYTHON) {
			try {
				e.eval(
						"class RecList:\n"+
						"  def __init__(self, head, tail):\n"+
						"    self.head = head\n"+
						"    self.tail = tail\n"+
						"\n"+
						"def RecListFromArray(a,rank=0):\n"+
						"  if len(a) <= rank:\n"+
						"     return None\n"+
						"  return RecList(a.get(rank),RecListFromArray(a,rank+1))\n");
			} catch (ScriptException e1) {
				e1.printStackTrace();
			}
		}
	}


}
