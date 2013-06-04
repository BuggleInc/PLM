package jlm.universe.bat;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;

public class BatTest {
	Object[] parameters;
	
	protected Object result;
	protected Object expected;
	
	private boolean visible;
	private boolean correct,answered;
	public boolean objectiveTest=false; // ExoTest messes with it, sorry
	private String funName;
	
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
		return res;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	@Override
	public boolean equals(Object o) {  
		if (!(o instanceof BatTest)) 
			return false;
		BatTest other = (BatTest) o;
		if (other.parameters.length != parameters.length) {
			//System.out.println("While comparing a Bat test, the amount of parameters differs: "+parameters.length+" != "+other.parameters.length);
			return false;
		}
		for (int i=0;i<parameters.length;i++)
			if (!parameters[i].equals(other.parameters[i])) {
				//System.out.println("While comparing a Bat test, the parameter "+i+" differs: "+parameters[i]+" != "+other.parameters[i]);
				return false;
			}
		if (isObjective() && !other.isObjective()) {
			/* We seem to be called as answer.equals(current) from the check() method. 
			 * Act accordingly by comparing our expected to their result
			 */
			if (expected == null && other.result != null) {
				return false;			
			}
			if (expected !=null && !expected.equals(other.result)) {
				return false;
			}
		} else if (!isObjective() && other.isObjective()) {
			/* We seem to be called as current.equals(answer). Weird I thought it was impossible. Anyway. */
			if (result == null && other.expected != null) {
				return false;			
			}
			if (result !=null && !result.equals(other.expected)) {
				return false;
			}
		} else {
			/* Act as an usual equal method as we don't seem to be called from check(). From the UI maybe? */
			if (result == null && other.result != null) {
				//System.out.println("While comparing a Bat test, the result differs: null != "+other.result);
				return false;			
			}
			if (result !=null && !result.equals(other.result)) {
				//System.out.println("While comparing a Bat test, the result differs: "+result+" != "+other.result);
				return false;
			}
			if (expected == null && other.result != null) {
				return false;			
			}
			if (expected != null && !expected.equals(other.expected)) {
				//System.out.println("While comparing a Bat test, the expected value differs: "+expected+" != "+other.expected);
				return false;
			}
		}
		return true;
	}

	public Object getParameter(int i) {
		return parameters[i];
	}

	public boolean isAnswered() {
		return answered;
	}
	public boolean isCorrect() {
		return correct;
	}
	
	private String name = null;

	private void displayParameter(Object o, StringBuffer sb, ProgrammingLanguage pl) {
		if (o == null) {
			sb.append("null");
		} else if (o instanceof String[]) {
			sb.append("{");
			String[]a = (String[]) o;
			for (String i:a) {
				sb.append(i+",");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("}");					
		} else if (o.getClass().isArray()){
			if (pl.equals(Game.JAVA)) {
				sb.append("{");
			} else { // Python
				sb.append("[");
			}
			if (o.getClass().getComponentType().equals(Integer.TYPE)) {
				int[]a = (int[]) o;
				for (int i:a) 
					sb.append(i+",");
				
				if (a.length > 0) // Don't kill the last comma if there is none
					sb.deleteCharAt(sb.length()-1);
			} else {
				throw new RuntimeException("Unhandled internal type (only integer arrays are handled so far)");
			}
			if (pl.equals(Game.JAVA)) {
				sb.append("}");
			} else { // Python
				sb.append("]");
			}
		} else if (o instanceof Boolean && pl.equals(Game.PYTHON)){
			Boolean b = (Boolean) o;
			if (b) {
				sb.append("True");
			} else {
				sb.append("False");
			}
		} else if (o instanceof String && pl.equals(Game.PYTHON)) {
			sb.append("\""+o+"\"");
		} else {
			sb.append(o.toString());
		}		
	}
	public String getName() {
		ProgrammingLanguage pl = Game.getProgrammingLanguage();
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
	
	public String toString() {
		ProgrammingLanguage pl = Game.getProgrammingLanguage();
		StringBuffer res = new StringBuffer(getName());
		res.append("=");
		displayParameter(result, res, pl);
		res.append(" (expected: ");
		displayParameter(expected, res, pl);
		res.append("; isObjective: "+isObjective()+")");
		return res.toString();
	}
	public String getResult() {
		Object o = result;
		if (isObjective())
			o = expected;
		
		if (o != null) {
			StringBuffer sb = new StringBuffer();
			displayParameter(o, sb, Game.getProgrammingLanguage());
			return sb.toString();
		} else {
			return "(null)";
		}
	}
	public void setResult(Object r) {
		result = r;
		if (expected == null) {
			expected = r; // The first time we're set, that's an answer which comes in
		} else {
			if (expected != null)
				correct = expected.equals(result);
			answered = true;
		}
	}
}
