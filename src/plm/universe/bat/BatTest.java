package plm.universe.bat;

import java.util.Vector;

import org.python.core.PyInstance;

import plm.core.lang.LangJava;
import plm.core.lang.LangPython;
import plm.core.lang.LangScala;
import plm.core.lang.ProgrammingLanguage;

public class BatTest {
	Object[] parameters;
	
	protected Object result;
	protected Object expected;
	
	private boolean visible;
	private boolean correct,answered;
	public boolean objectiveTest=false; // ExoTest messes with it, sorry
	private String funName;

	public static final boolean INVISIBLE = false;
	public static final boolean VISIBLE = true;
	
	public BatTest(String funName, boolean visible,Object parameters) {
		this.funName = funName;
		this.visible = visible;
		this.correct = false;
		this.answered = false;
		
		/* Cast parameters into an array on need */
		if (parameters.getClass().isArray()) {
			this.parameters = (Object[]) parameters;
		} else {
			this.parameters = new Object[] {parameters};
		}
	}

	public BatTest copy() {
		BatTest res = new BatTest(funName,visible,parameters.clone());
		res.result = result;
		res.expected = expected;
		res.expectedHasValue = expectedHasValue;
		return res;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	@SuppressWarnings({ "rawtypes" })
	private Object changeToPrimitiveArray(Object input) {
		if (input == null) 
			return null;
		if (input instanceof java.util.List) {
			java.util.List l = (java.util.List)input;
			int[] res = new int[l.size()];
			for (int i=0;i<l.size();i++) 
				res[i] = (Integer)l.get(i);
			
			return res;
		} if (input instanceof plm.universe.cons.RecList) {
			input = ((plm.universe.cons.RecList)input).toArray();
		} 
		if (input.getClass().isArray() && input.getClass().getComponentType().equals(Integer.class)) {
			Integer[] orig = (Integer[])input;
			if (orig.length==0 || (orig.length == 1 && orig[0]==null))
				return new int[]{};
			int[] res = new int[orig.length];
 			for (int i=0;i<res.length;i++)
				res[i] = orig[i];
			return res;
		} 
		return input;
	}
	
	@Override
	public boolean equals(Object o) {  
		if (!(o instanceof BatTest)) 
			return false;
		BatTest other = (BatTest) o;
		if (other.parameters.length != parameters.length) {
			//getGame().getLogger().log("While comparing a Bat test, the amount of parameters differs: "+parameters.length+" != "+other.parameters.length);
			return false;
		}
		for (int i=0;i<parameters.length;i++) {
			if (parameters[i] != null && !parameters[i].equals(other.parameters[i])) {
				//getGame().getLogger().log("While comparing a Bat test, the parameter "+i+" differs: "+parameters[i]+" != "+other.parameters[i]);
				return false;
			}
			if (parameters[i] == null && other.parameters[i]!=null)
				return false;
		}
		
		
		if (isObjective() && !other.isObjective()) {
			/* We seem to be called as answer.equals(current) from the check() method. 
			 * Act accordingly by comparing our expected to their result
			 */
			return equalParameter(expected, other.result);
		} else if (!isObjective() && other.isObjective()) {
			/* We seem to be called as current.equals(answer). Weird I thought it was impossible. Anyway. */
			return equalParameter(result, other.expected);
		} else {
			/* Act as an usual equal method as we don't seem to be called from check(). From the UI maybe? */
			if (! equalParameter(result, other.result)) 
				//getGame().getLogger().log("While comparing a Bat test, the result differs: null != "+other.result);
				return false;
			//getGame().getLogger().log("While comparing a Bat test, the expected value differs: "+expected+" != "+other.expected);
			return equalParameter(expected, other.expected);
		}
	}

	public Object getParameter(int i) {
		if (parameters[i]!=null && parameters[i].getClass().isArray()) {
			if (parameters[i].getClass().getComponentType().equals(Integer.TYPE)) {
				int[] orig = (int[]) parameters[i];
				int[] res = new int[orig.length];
				for (int cpt=0;cpt<orig.length;cpt++)
					res[cpt] = orig[cpt];
				return res;
			} else if (parameters[i].getClass().getComponentType().equals(Integer.class)) {
				Integer[] orig = (Integer[]) parameters[i];
				Integer[] res = new Integer[orig.length];
				for (int cpt=0;cpt<orig.length;cpt++)
					res[cpt] = orig[cpt];
				return res;
			} else {
				throw new RuntimeException("Unhandled internal type (only Array<int> and Array<Integer> are handled so far)");
			}
		}
		return parameters[i];
	}

	public boolean isAnswered() {
		return answered;
	}
	public boolean isCorrect() {
		return correct;
	}
	
	private String name = null;

	private boolean equalParameter(Object o1, Object o2) {
		if (o1==null && o2==null)
			return true;
		if (o1==null || o2==null)
			return false;
		o1 = changeToPrimitiveArray(o1);
		o2 = changeToPrimitiveArray(o2);
		if (o1.getClass().isArray() && o2.getClass().isArray()) {
			int[] tab1 = (int[])o1;
			int[] tab2 = (int[])o2;
			if (tab1.length != tab2.length)
				return false;
			for (int i=0; i<tab1.length;i++) 
				if (tab1[i]!=tab2[i])
					return false;
			return true;
		} 
		if (o1.getClass().isArray() || o2.getClass().isArray())
			return false; // The other cannot be an array because of previous test
		return o1.equals(o2);
	}
	public String stringParameter(ProgrammingLanguage progLang, Object o) {
		StringBuffer res = new StringBuffer();
		displayParameter(o, res, progLang);
		return res.toString();
	}
	private void displayParameter(Object o, StringBuffer sb, ProgrammingLanguage pl) {
		if (o == null) {
			if (pl instanceof LangScala)
				sb.append("Nil");
			else if (pl instanceof LangPython)
				sb.append("None");
			else
				sb.append("null");
			
		} else if (o instanceof String[]) {
			if (pl instanceof LangJava) {
				sb.append("{");
			} else if (pl instanceof LangScala) {
				sb.append("Array(");
			} else if (pl instanceof LangPython) { 
				sb.append("[");
			} else {
				throw new RuntimeException("Please port me to "+pl.getLang());
			}
			
			String[]a = (String[]) o;
			for (String i:a) {
				sb.append(i+",");
			}
			
			sb.deleteCharAt(sb.length()-1);
			if (pl instanceof LangJava) {
				sb.append("}");
			} else if (pl instanceof LangScala) {
				sb.append(")");
			} else if (pl instanceof LangPython) { 
				sb.append("]");
			} else {
				throw new RuntimeException("Please port me to "+pl.getLang());
			}
		} else if (o.getClass().equals(Vector.class) || o.getClass().isArray()){
			if (o.getClass().equals(Vector.class))
				o = changeToPrimitiveArray(o);
			
			if (pl instanceof LangJava) {
				sb.append("{");
			} else if (pl instanceof LangScala) {
				sb.append("Array(");
			} else if (pl instanceof LangPython) { // Python
				sb.append("[");
			} else {
				throw new RuntimeException("Please port me to "+pl.getLang());
			}
			if (o.getClass().getComponentType().equals(Integer.TYPE)) {
				int[]a = (int[]) o;
				for (int i:a) 
					sb.append(i+",");
				
				if (a.length > 0) // Don't kill the last comma if there is none
					sb.deleteCharAt(sb.length()-1);
			} else if (o.getClass().getComponentType().equals(Integer.class)) {
				Integer[]a = (Integer[]) o;
				for (Integer i:a) 
					sb.append(i+",");
				
				if (a.length > 0) // Don't kill the last comma if there is none
					sb.deleteCharAt(sb.length()-1);
				
			} else {
				throw new RuntimeException("Unhandled internal type (only Array<int> and Array<Integer> are handled so far)");
			}
			if (pl instanceof LangJava) {
				sb.append("}");
			} else if (pl instanceof LangScala) {
				sb.append(")");
			} else if (pl instanceof LangPython) { 
				sb.append("]");
			} else {
				throw new RuntimeException("Please port me to "+pl.getLang());
			}
		} else if (o instanceof Boolean) {
			Boolean b = (Boolean) o;
			if (pl instanceof LangJava || pl instanceof LangScala) {
				sb.append(b ? "true":"false");
			} else if (pl instanceof LangPython) { 
				sb.append(b ? "True" : "False");
			} else {
				throw new RuntimeException("Please port me to "+pl.getLang());
			}
		} else if (o instanceof String && pl instanceof LangPython) {
			sb.append("\""+o+"\"");
		} else if (o instanceof PyInstance) {
			sb.append( ((PyInstance)o).__str__());
		} else {
			sb.append(o.toString());
		}		
	}
	public String getName(ProgrammingLanguage pl) {
		if (name == null) {
			StringBuffer sb=new StringBuffer(funName+"(");
			
			for (Object o:parameters) {
				displayParameter(o, sb, pl);
				sb.append(",");
			}
			
			sb.deleteCharAt(sb.length()-1);
			sb.append(")");					
			name=sb.toString();

		}
		return name;
	}
	public boolean isObjective() {
		return objectiveTest;
	}
	
	public String formatAsString(ProgrammingLanguage progLang) {
		return getName(progLang)+"="+getResult(progLang)
		+(isAnswered() && !isCorrect() ?" (expected: "+stringParameter(progLang, expected)+")":"");
	}
	
	public String toString(ProgrammingLanguage pl) {
		StringBuffer res = new StringBuffer(getName(pl));
		res.append("=");
		displayParameter(result, res, pl);
		res.append(" (expected: ");
		displayParameter(expected, res, pl);
		res.append("; isObjective: "+isObjective()+")");
		return res.toString();
	}
	public String getResult(ProgrammingLanguage progLang) {
		Object o = result;
		if (isObjective())
			o = expected;
		
		if (o != null) {
			StringBuffer sb = new StringBuffer();
			displayParameter(o, sb, progLang);
			return sb.toString();
		} else {
			if (progLang instanceof LangScala)
				return "Nil";
			if (progLang instanceof LangPython)
				return "None";
			return "null";
		}
	}
	private boolean expectedHasValue = false;
	public void setResult(Object r) {
		result = r;
		answered = true;
	}
}
