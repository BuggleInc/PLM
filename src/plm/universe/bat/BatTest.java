package plm.universe.bat;

import java.util.Vector;

import org.python.core.PyInstance;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;

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
		res.expectedHasValue = expectedHasValue;
		return res;
	}
	
	public boolean isVisible() {
		return visible;
	}

        @Override public boolean equals(Object o)
        {
          if (!(o instanceof BatTest))
            return false;
          BatTest other = (BatTest)o;
          if (other.parameters.length != parameters.length) {
            // System.out.println("While comparing a Bat test, the amount of parameters differs: "+parameters.length+"
            // != "+other.parameters.length);
            return false;
          }
          for (int i = 0; i < parameters.length; i++) {
            if (parameters[i] != null && !parameters[i].equals(other.parameters[i])) {
              // System.out.println("While comparing a Bat test, the parameter "+i+" differs: "+parameters[i]+" !=
              // "+other.parameters[i]);
              return false;
            }
            if (parameters[i] == null && other.parameters[i] != null)
              return false;
          }

          if (isObjective() && !other.isObjective()) {
            /* We seem to be called as answer.equals(current) from the check() method.
             * Act accordingly by comparing our expected to their result
             */
            return ValueFormatter.equals(expected, other.result);
          } else if (!isObjective() && other.isObjective()) {
            /* We seem to be called as current.equals(answer). Weird I thought it was impossible. Anyway. */
            return ValueFormatter.equals(result, other.expected);
          } else {
            /* Act as an usual equal method as we don't seem to be called from check(). From the UI maybe? */
            if (!ValueFormatter.equals(result, other.result))
              // System.out.println("While comparing a Bat test, the result differs: null !=
              // "+other.result);
              return false;
            // System.out.println("While comparing a Bat test, the expected value differs: "+expected+" !=
            // "+other.expected);
            return ValueFormatter.equals(expected, other.expected);
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

        public String stringParameter(Object o)
        {
          return ValueFormatter.format(o, Game.getInstance().getProgrammingLanguage());
        }
        public String getName()
        {
          ProgrammingLanguage pl = Game.getInstance().getProgrammingLanguage();
          if (name == null) {
            StringBuffer sb = new StringBuffer(funName + "(");

            for (Object o : parameters) {
              sb.append(ValueFormatter.format(o, pl));
              sb.append(",");
            }

            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");
            name = sb.toString();

		}
		return name;
        }
        public boolean isObjective() {
		return objectiveTest;
	}
	
	public String toString() {
          ProgrammingLanguage pl = Game.getInstance().getProgrammingLanguage();
          StringBuffer res       = new StringBuffer(getName());
          res.append("=");
          res.append(ValueFormatter.format(result, pl));
          res.append(" (expected: ");
          res.append(ValueFormatter.format(expected, pl));
          res.append("; isObjective: " + isObjective() + ")");
          return res.toString();
	}
	public String getResult() {
		Object o = result;
		if (isObjective())
			o = expected;
		
		if (o != null) {
                  return ValueFormatter.format(o, Game.getInstance().getProgrammingLanguage());
                } else {
                  if (Game.getInstance().getProgrammingLanguage().isScala())
                    return "Nil";
                  if (Game.getInstance().getProgrammingLanguage().isPython())
                    return "None";
                  return "null";
                }
        }
	private boolean expectedHasValue = false;
	public void setResult(Object r) {
		result = r;
		if (!expectedHasValue) {
			expected = r; // The first time we're set, that's an answer which comes in
			expectedHasValue = true;
		} else {
			if (expectedHasValue)
                          correct = ValueFormatter.equals(expected, result);
                        answered = true;
                }
	}
}
